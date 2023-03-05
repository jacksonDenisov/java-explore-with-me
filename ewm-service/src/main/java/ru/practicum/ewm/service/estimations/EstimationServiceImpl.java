package ru.practicum.ewm.service.estimations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.BusinessLogicConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.estimations.*;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.event.EventState;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.model.user.UserDtoWithRating;
import ru.practicum.ewm.model.user.UserMapper;
import ru.practicum.ewm.storage.estimation.EstimationRepository;
import ru.practicum.ewm.storage.event.EventRepository;
import ru.practicum.ewm.storage.user.UserRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstimationServiceImpl implements EstimationService {

    private final EstimationRepository estimationRepository;

    private final UserRepository userRepository;

    private final EventRepository eventRepository;

    @Override
    @Transactional
    public EstimationDtoFull estimateEvent(Long userId, Long eventId, EstimationDto estimationDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new BusinessLogicConflictException("Не удалось поставить оценку событию",
                    "Статус события должен быть PUBLISHED",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя",
                        "Пользователя с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (event.getInitiator().getId().equals(user.getId())) {
            throw new BusinessLogicConflictException("Не удалось поставить оценку событию",
                    "Организатор события не может ставить оценку своему мероприятию",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        Estimation estimation = estimationRepository.save(EstimationMapper.toEstimation(estimationDto, user, event));
        event.setRating(calculateEventRating(estimationRepository.findAllByEventId(eventId)));
        eventRepository.save(event);
        return EstimationMapper.toEstimationDto(estimation);
    }

    @Override
    @Transactional
    public EstimationDtoFull updateEventEstimation(Long userId, Long eventId, EstimationDto estimationDto) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти событие",
                        "События с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        Estimation estimation = estimationRepository.findByEventIdAndUserId(eventId, userId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти оценку события",
                        "Оценки события с указанными параметрами в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        if (estimation.getEstimationType().equals(estimationDto.getEstimationType())) {
            throw new BusinessLogicConflictException("Не удалось обновить оценку события",
                    "Такая оценка уже проставлена",
                    new ArrayList<>(Collections.singletonList("BusinessLogicConflictException")));
        }
        estimation.setEstimationType(estimationDto.getEstimationType());
        estimation = estimationRepository.save(estimation);

        event.setRating(calculateEventRating(estimationRepository.findAllByEventId(eventId)));
        eventRepository.save(event);

        return EstimationMapper.toEstimationDto(estimation);
    }

    @Override
    @Transactional
    public UserDtoWithRating getInitiatorRatingInfo(Long initiatorId) {
        User user = userRepository.findById(initiatorId)
                .orElseThrow(() -> new NotFoundException("Не удалось найти пользователя",
                        "Пользователя с запрошенным id в системе не существует",
                        new ArrayList<>(Collections.singletonList("NotFoundException"))));
        Double rating = eventRepository.findAvgInitiatorRating(initiatorId);
        return UserMapper.toUserDtoWithRating(user, rating);
    }

    private double calculateEventRating(List<Estimation> estimations) {
        long likes = 0L;
        for (Estimation estimation : estimations) {
            if (estimation.getEstimationType().equals(EstimationType.LIKE)) {
                likes++;
            }
        }
        BigDecimal result = BigDecimal.valueOf(
                (double) likes / (double) estimations.size() * 10);
        return result.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
