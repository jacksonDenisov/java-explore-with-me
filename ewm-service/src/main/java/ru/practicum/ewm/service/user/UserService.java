package ru.practicum.ewm.service.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.model.user.UserDtoFull;
import ru.practicum.ewm.model.user.UserDtoNew;

import java.util.List;

public interface UserService {

    UserDtoFull create(UserDtoNew userDtoNew);

    void delete(Long id);

    List<UserDtoFull> findUsers(List<Long> ids, Pageable pageable);
}
