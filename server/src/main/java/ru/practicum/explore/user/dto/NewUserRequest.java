package ru.practicum.explore.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.user.User;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {


    @NotEmpty
    @NotNull
    @Email
    @Size(min = 6, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 254, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String email;

    @NotEmpty
    @NotBlank
    @NotNull
    @Size(min = 2, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 250, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String name;

    public User toEntity() {
        var user = new User();
        user.setEmail(email);
        user.setName(name);
        return user;
    }
}
