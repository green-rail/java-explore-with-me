package ru.practicum.explore.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank
    @NotNull
    private String name;

    public Category toEntity() {
        return new Category(null, name);
    }

    public static CategoryDto toDto(Category entity) {
        return new CategoryDto(entity.getId(), entity.getName());
    }
}
