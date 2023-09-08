package ru.practicum.explore.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.compilation.Compilation;
import ru.practicum.explore.error.ErrorMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned;

    @NotEmpty
    @NotNull
    @NotBlank
    @Size(min = 1, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 50, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String title;

    public Compilation toEntity() {
        var compilation = new Compilation();
        if (pinned != null) {
            compilation.setPinned(pinned);
        }
        compilation.setTitle(title);
        return compilation;
    }
}
