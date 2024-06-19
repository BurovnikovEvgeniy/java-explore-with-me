package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.EntityNotFoundException;
import ru.practicum.ewm.user.dto.NewUserDto;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDto addUser(NewUserDto newUser) {
        return mapper.toUserDto(repository.save(mapper.toUser(newUser)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> usersIds, PageRequest pageRequest) {
        return (usersIds.isEmpty()) ? mapper.toUserDto(repository.findAll(pageRequest).toList())
                : mapper.toUserDto(repository.findAllByIdIn(usersIds, pageRequest));
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        if (repository.existsById(userId)) {
            throw new EntityNotFoundException("Не найден пользователь с id=" + userId + ", он не найден");
        }
        repository.deleteById(userId);
    }
}
