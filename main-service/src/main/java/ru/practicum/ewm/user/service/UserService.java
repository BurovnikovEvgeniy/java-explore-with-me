package ru.practicum.ewm.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(NewUserDto newUserDTO);

    void deleteUser(Long userId);

    List<UserDto> getUsers(List<Long> ids, PageRequest pageRequest);
}