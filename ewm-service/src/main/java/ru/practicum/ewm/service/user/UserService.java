package ru.practicum.ewm.service.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.user.UserDto;
import ru.practicum.ewm.model.user.UserDtoNew;

import java.util.List;

public interface UserService {

    UserDto create(UserDtoNew userDtoNew);

    void delete(Long id);

    List<UserDto> findAllByIds(List<Long> ids, Pageable pageable);
}
