package ru.practicum.ewm.model.location;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.ewm.model.event.EventDtoNew;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LocationMapper {

    public static Location toLocationNew(EventDtoNew.Location locationNew) {
        Location location = new Location();
        location.setLon(locationNew.getLon());
        location.setLat(locationNew.getLat());
        return location;
    }
}
