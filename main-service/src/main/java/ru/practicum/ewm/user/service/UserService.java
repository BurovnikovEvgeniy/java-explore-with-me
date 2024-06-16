package ru.practicum.ewm.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto newUser);

    List<UserDto> getUsers(List<Long> usersIds, PageRequest pageRequest);

    List<UserDto> getUsers(PageRequest pageRequest);

    void deleteUser(long userId);
}
