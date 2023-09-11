package ru.practicum.explore.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.category.Category;
import ru.practicum.explore.error.ErrorMessages;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewCategoryDto {
    @NotBlank
    @NotNull
    @Size(min = 1, message = ErrorMessages.STRING_TOO_SHORT_MESSAGE)
    @Size(max = 50, message = ErrorMessages.STRING_TOO_LONG_MESSAGE)
    private String name;

    public Category toEntity() {
        var category = new Category();
        category.setName(name);
        return category;
    }
}
