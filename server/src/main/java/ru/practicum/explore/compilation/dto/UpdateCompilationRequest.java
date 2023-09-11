package ru.practicum.explore.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.error.ErrorMessages;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {

    private List<Long> events;
    private Boolean pinned;

    @Size(min = 1, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 50, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String title;

}
