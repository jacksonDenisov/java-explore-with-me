package ru.practicum.ewm.service.participation_request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.BusinessLogicConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.participation_request.*;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.participation_request.ParticipationRequestRepository;
import ru.practicum.ewm.storage.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.practicum.ewm.model.event.EventState.PUBLISHED;
import static ru.practicum.ewm.model.participation_request.ParticipationRequestState.*;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final ParticipationRequestRepository participationRequestRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public ParticipationRequestDto createParticipationRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя",
                        "Пользователя с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (event.getInitiator().getId().equals(userId) || !event.getState().equals(PUBLISHED)) {
            throw new BusinessLogicConflictException("Не удалось создать запрос на участие в событии",
                    "Инициатор события не может добавить запрос на участие в своём событии." +
                            " Нельзя участвовать в неопубликованном событии",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        if (event.getParticipantLimit() != 0 && event.getConfirmedRequests() >= event.getParticipantLimit()) {
            throw new BusinessLogicConflictException("Не удалось создать запрос на участие в событии",
                    "Для данного события достигнут лимит участников",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        ParticipationRequestState status;
        if (event.getRequestModeration()) {
            status = PENDING;
        } else {
            status = CONFIRMED;
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }
        ParticipationRequest participationRequest = participationRequestRepository.save(
                ParticipationRequestMapper.toParticipationRequest(event, requester, status, LocalDateTime.now()));
        return ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> findParticipationRequestByRequesterId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя",
                        "Такого пользователя в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        return ParticipationRequestMapper.toParticipationRequestDto(
                participationRequestRepository.findAllByRequesterId(userId));
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelParticipationRequest(Long userId, Long requestId) {
        ParticipationRequest participationRequest = participationRequestRepository
                .findParticipationRequestByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти запрос на участие",
                        "Запроса на участие с запрошенным id для данного пользователя в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (participationRequest.getStatus().equals(CANCELED)) {
            throw new BusinessLogicConflictException("Не удалось отменить запрос",
                    "Данный запрос уже отменен",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        participationRequest.setStatus(CANCELED);
        return ParticipationRequestMapper.toParticipationRequestDto(
                participationRequestRepository.save(participationRequest));
    }

    @Override
    @Transactional
    public List<ParticipationRequestDto> findParticipationRequestsForEventInitiator(Long initiatorId, Long eventId) {
        return ParticipationRequestMapper.toParticipationRequestDto(
                participationRequestRepository.findAllByEventId(eventId));
    }

    @Override
    @Transactional
    public ParticipationRequestDtoUpdated updateParticipationRequestStatusByEventInitiator(
            Long userId, Long eventId, ParticipationRequestDtoStatusUpdate participationRequestDtoStatusUpdate) {

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                "События с запрошенным id в системе не существует",
                new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
            throw new BusinessLogicConflictException("Не удалось выполнить запрос",
                    "Для события лимит заявок равен 0 или отключена пре-модерация, подтверждение заявок не требуется",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByIdIn(
                participationRequestDtoStatusUpdate.getRequestIds());
        ParticipationRequestDtoUpdated participationRequestDtoUpdated = new ParticipationRequestDtoUpdated(
                new ArrayList<>(), new ArrayList<>());
        if (participationRequestDtoStatusUpdate.getStatus().equals(CONFIRMED)) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new BusinessLogicConflictException("Не удалось выполнить запрос",
                        "Для события исчерпан лимит заявок",
                        new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
            }
            for (ParticipationRequest participationRequest : participationRequests) {
                if (!participationRequest.getStatus().equals(PENDING)) {
                    throw new BusinessLogicConflictException("Не удалось выполнить запрос",
                            "Статус можно изменить только у заявок, находящихся в состоянии ожидания",
                            new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
                }
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    participationRequest.setStatus(CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    participationRequestDtoUpdated.getConfirmedRequests()
                            .add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
                } else {
                    participationRequest.setStatus(REJECTED);
                    participationRequestDtoUpdated.getRejectedRequests()
                            .add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
                }
            }
            participationRequestRepository.saveAll(participationRequests);
            eventRepository.save(event);
            return participationRequestDtoUpdated;
        } else if (participationRequestDtoStatusUpdate.getStatus().equals(REJECTED)) {
            for (ParticipationRequest participationRequest : participationRequests) {
                if (!participationRequest.getStatus().equals(PENDING)) {
                    throw new BusinessLogicConflictException("Не удалось выполнить запрос",
                            "Статус можно изменить только у заявок, находящихся в состоянии ожидания",
                            new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
                }
                participationRequest.setStatus(REJECTED);
                participationRequestDtoUpdated.getRejectedRequests()
                        .add(ParticipationRequestMapper.toParticipationRequestDto(participationRequest));
            }
            participationRequestRepository.saveAll(participationRequests);
        }
        return participationRequestDtoUpdated;
    }
}