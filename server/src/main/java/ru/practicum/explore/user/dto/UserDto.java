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

    public static UserDto toDto(User entity) {
        var dto = new UserDto();
        dto.setId(entity.getId());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        return dto;
    }
}
