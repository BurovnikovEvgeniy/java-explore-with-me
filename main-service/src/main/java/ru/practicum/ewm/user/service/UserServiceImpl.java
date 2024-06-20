package ru.practicum.ewm.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.exceptions.EntityNotFoundException;
import ru.practicum.ewm.user.dto.NewUserDTO;
import ru.practicum.ewm.user.dto.UserDTO;
import ru.practicum.ewm.user.mapper.UserMapper;
import ru.practicum.ewm.user.model.User;
import ru.practicum.ewm.user.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDTO addUser(NewUserDTO newUserDTO) {
        User user = userRepository.save(userMapper.toUser(newUserDTO));
        return userMapper.toUserDTO(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("Юзер не найден");
        }
        userRepository.deleteById(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsers(List<Long> ids, PageRequest pageRequest) {
        return (ids.isEmpty()) ? userMapper.toUserDTO(userRepository.findAll(pageRequest).toList())
                : userMapper.toUserDTO(userRepository.findAllByIdIn(ids, pageRequest));
    }
}