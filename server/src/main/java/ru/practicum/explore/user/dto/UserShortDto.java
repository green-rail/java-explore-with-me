package ru.practicum.explore.user.dto;

import lombok.Value;
import ru.practicum.explore.user.User;

@Value
public class UserShortDto {

    Long id;
    String name;

    public static UserShortDto toDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}
