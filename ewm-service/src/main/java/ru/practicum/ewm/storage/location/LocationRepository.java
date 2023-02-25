package ru.practicum.ewm.storage.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewm.model.location.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
