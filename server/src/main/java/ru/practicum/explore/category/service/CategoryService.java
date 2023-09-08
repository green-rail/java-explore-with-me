package ru.practicum.explore.category.service;

import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto categoryDto);

    void deleteCategory(long id);

    CategoryDto changeCategory(long id, CategoryDto categoryDto);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(long id);
}
