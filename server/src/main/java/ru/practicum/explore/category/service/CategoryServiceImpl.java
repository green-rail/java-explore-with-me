package ru.practicum.explore.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.category.CategoryDto;
import ru.practicum.explore.category.CategoryRepository;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        return CategoryDto.toDto(categoryRepository.save(categoryDto.toEntity()));
    }

    @Override
    public void deleteCategory(long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryDto changeCategory(long id, CategoryDto categoryDto) {

        var category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        if (categoryDto.getName().equals(category.getName())) {
            return CategoryDto.toDto(category);
        }

        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new DataConflictException("Категория с таким имененем уже существует");
        }

        category.setName(category.getName());

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
                .orElseThrow(() -> new EntityNotFoundException("Категория не найдена"));

        return CategoryDto.toDto(category);
    }
}
