package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.DataBaseConflictException;
import ru.practicum.ewm.exceptions.NotFoundException;
import ru.practicum.ewm.model.user.UserDto;
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
    public UserDto create(UserDtoNew userDtoNew) {
        try {
            return UserMapper.toUserDtoFull(
                    userRepository.save(UserMapper.toUser(userDtoNew)));
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
    @Transactional
    public List<UserDto> findAllByIds(List<Long> ids, Pageable pageable) {
        return UserMapper.toUserDtoFull(
                userRepository.findAllByIdIn(ids, pageable).toList());
    }
}
