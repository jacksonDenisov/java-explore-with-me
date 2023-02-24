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

    public static UserDtoFull toUserDtoFull(User user) {
        UserDtoFull userDtoFull = new UserDtoFull();
        userDtoFull.setId(user.getId());
        userDtoFull.setName(user.getName());
        userDtoFull.setEmail(user.getEmail());
        return userDtoFull;
    }

    public static List<UserDtoFull> toUserDtoFull(List<User> users) {
        List<UserDtoFull> usersDtoFull = new ArrayList<>();
        for (User user : users) {
            usersDtoFull.add(toUserDtoFull(user));
        }
        return usersDtoFull;
    }
}
