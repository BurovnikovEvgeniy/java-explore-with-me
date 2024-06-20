package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.dto.NewUserDTO;
import ru.practicum.ewm.user.dto.UserDTO;
import ru.practicum.ewm.user.model.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(NewUserDTO newUserDTO);

    UserDTO toUserDTO(User user);

    List<UserDTO> toUserDTO(List<User> users);
}