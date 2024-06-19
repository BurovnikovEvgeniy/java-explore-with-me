package ru.practicum.ewm.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUser(NewUserDto newUserDTO);

    UserDto toUserDto(User user);

    User toUser(UserDto user);
}
