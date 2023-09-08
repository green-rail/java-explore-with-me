package ru.practicum.explore.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.category.dto.CategoryDto;
import ru.practicum.explore.category.service.CategoryService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryDto> getCategories(@RequestParam(defaultValue = "0")  @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size,
                                           HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return categoryService.getCategories(from, size);
    }

    @GetMapping("/{catId}")
    public CategoryDto getCategory(@PathVariable long catId, HttpServletRequest request) {
        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return categoryService.getCategory(catId);
    }
}
