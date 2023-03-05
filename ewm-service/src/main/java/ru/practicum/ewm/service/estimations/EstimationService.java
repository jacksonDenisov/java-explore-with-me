package ru.practicum.ewm.service.estimations;

import ru.practicum.ewm.model.estimations.EstimationDtoFull;
import ru.practicum.ewm.model.estimations.EstimationDto;
import ru.practicum.ewm.model.user.UserDtoWithRating;

public interface EstimationService {

    EstimationDtoFull estimateEvent(Long userId, Long eventId, EstimationDto estimationDto);

    EstimationDtoFull updateEventEstimation(Long userId, Long eventId, EstimationDto estimationDto);

    UserDtoWithRating getInitiatorRatingInfo(Long initiatorId);
}
