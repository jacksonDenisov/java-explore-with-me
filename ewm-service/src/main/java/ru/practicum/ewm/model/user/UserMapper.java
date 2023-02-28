package ru.practicum.ewm.model.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserMapper {

    public static User toUser(UserDtoNew userDtoNew) {
        User user = new User();
        user.setName(userDtoNew.getName());
        user.setEmail(userDtoNew.getEmail());
        return user;
    }

    public static UserDto toUserDtoFull(User user) {
        UserDto userDtoFull = new UserDto();
        userDtoFull.setId(user.getId());
        userDtoFull.setName(user.getName());
        userDtoFull.setEmail(user.getEmail());
        return userDtoFull;
    }

    public static List<UserDto> toUserDtoFull(List<User> users) {
        List<UserDto> usersDtoFull = new ArrayList<>();
        for (User user : users) {
            usersDtoFull.add(toUserDtoFull(user));
        }
        return usersDtoFull;
    }
}
