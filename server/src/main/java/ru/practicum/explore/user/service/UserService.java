package ru.practicum.explore.user.service;

import ru.practicum.explore.user.dto.UserDto;

import java.util.List;

public interface UserService {
    List<UserDto> getUsers(List<Long> ids, int from, int size);

    UserDto addUser(UserDto userDto);

    void deleteUser(long id);
}
