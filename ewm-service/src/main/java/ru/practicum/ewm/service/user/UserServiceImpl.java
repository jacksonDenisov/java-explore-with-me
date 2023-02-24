package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.DataBaseConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.user.User;
import ru.practicum.ewm.model.user.UserDtoFull;
import ru.practicum.ewm.model.user.UserDtoNew;
import ru.practicum.ewm.model.user.UserMapper;
import ru.practicum.ewm.storage.user.UserRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDtoFull create(UserDtoNew userDtoNew) {
        try {
            User user = userRepository.save(UserMapper.toUser(userDtoNew));
            return UserMapper.toUserDtoFull(user);
        } catch (DataIntegrityViolationException e) {
            throw new DataBaseConflictException("Не удалось добавить нового пользователя",
                    "Нарушение целостности данных",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Не удалось удалить пользователя",
                    "Такого пользователя в системе не существует",
                    new ArrayList<>(Collections.singletonList(e.getMessage())));
        }
    }

    @Override
    public List<UserDtoFull> findUsers(List<Long> ids, Pageable pageable) {
        Page<User> usersPage = userRepository.findAllByIdIn(ids, pageable);
        return UserMapper.toUserDtoFull(usersPage.toList());
    }
}
