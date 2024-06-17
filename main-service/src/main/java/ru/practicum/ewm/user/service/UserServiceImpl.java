package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.EntityNotFoundException;
import ru.practicum.ewm.user.dto.UserDto;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    @Transactional
    public UserDto addUser(UserDto newUser) {
        return mapper.toUserDto(repository.save(mapper.toUser(newUser)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> usersIds, PageRequest pageRequest) {
        return repository.findAll(pageRequest)
                .stream().filter(el -> usersIds.contains(el.getId()))
                .map(el -> mapper.toUserDto(el))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(PageRequest pageRequest) {
        return repository.findAll(pageRequest)
                .map(el -> mapper.toUserDto(el))
                .toList();
    }

    @Override
    @Transactional
    public void deleteUser(long userId) {
        if (repository.findById(userId).isEmpty()) {
            throw new EntityNotFoundException("Не найден пользователь с id=" + userId + ", он не найден");
        }
        repository.deleteById(userId);
    }
}
