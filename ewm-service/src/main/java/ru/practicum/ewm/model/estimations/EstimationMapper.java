package ru.practicum.ewm.model.estimations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.event.Event;
import ru.practicum.ewm.model.user.User;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EstimationMapper {

    public static Estimation toEstimation(EstimationDto estimationDto, User user, Event event) {
        Estimation estimation = new Estimation();
        estimation.setUser(user);
        estimation.setEvent(event);
        estimation.setEstimationType(estimationDto.getEstimationType());
        return estimation;
    }

    public static EstimationDtoFull toEstimationDto(Estimation estimation) {
        EstimationDtoFull estimationDtoFull = new EstimationDtoFull();
        estimationDtoFull.setId(estimation.getId());
        estimationDtoFull.setUserId(estimation.getUser().getId());
        estimationDtoFull.setEvent_id(estimation.getEvent().getId());
        estimationDtoFull.setEstimationType(estimation.getEstimationType());
        return estimationDtoFull;
    }
}
