package ru.practicum.explore.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.user.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @Email
    private String email;

    @NotBlank
    private String name;

    public User toEntity() {
        return new User();
    }

    public static UserDto toDto(User entity) {
        return new UserDto();
    }
}
