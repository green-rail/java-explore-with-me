package ru.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.dto.NewCategoryDto;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.event.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public CategoryDto addCategory(NewCategoryDto categoryDto) {
        try {
            return CategoryDto.toDto(categoryRepository.save(categoryDto.toEntity()));
        } catch (Exception e) {
            throw new DataConflictException(e.getMessage());
        }
    }

    @Override
    public void deleteCategory(long id) {
        var cat = categoryRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getCategoryNotFoundMessage(id)));
        if (eventRepository.existsByCategory(cat)) {
            throw new DataConflictException("The category is not empty");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto changeCategory(long id, CategoryDto categoryDto) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getCategoryNotFoundMessage(id)));

        if (categoryDto.getName().equals(category.getName())) {
            return CategoryDto.toDto(category);
        }

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new DataConflictException(String.format("Category with the name [%s] already exists.", categoryDto.getName()));
        }

        category.setName(categoryDto.getName());

        return CategoryDto.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getCategories(int from, int size) {
        PageRequest page = PageRequest.of(from / size, size);

        return categoryRepository.findAll(page)
                .stream()
                .map(CategoryDto::toDto)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(long id) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getCategoryNotFoundMessage(id)));

        return CategoryDto.toDto(category);
    }
}
