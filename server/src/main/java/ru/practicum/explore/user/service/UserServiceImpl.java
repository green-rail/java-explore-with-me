package ru.practicum.explore.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.user.User;
import ru.practicum.explore.user.UserRepository;
import ru.practicum.explore.user.dto.NewUserRequest;
import ru.practicum.explore.user.dto.UserDto;
import ru.practicum.explore.user.exception.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        List<User> users;
        if (ids.isEmpty()) {
            PageRequest page = PageRequest.of(from / size, size);
            users = userRepository.findAll(page).getContent();
        } else {
            users = userRepository.findAllById(ids);
        }
        return users.stream()
                .map(UserDto::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public UserDto addUser(NewUserRequest userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new DataConflictException("Email already exists.");
        }
        return UserDto.toDto(userRepository.save(userDto.toEntity()));
    }

    @Override
    public void deleteUser(long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
    }
}
