package ru.practicum.ewm.user.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.user.dto.NewUserDTO;
import ru.practicum.ewm.user.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO addUser(NewUserDTO newUserDTO);

    void deleteUser(Long userId);

    List<UserDTO> getUsers(List<Long> ids, PageRequest pageRequest);
}