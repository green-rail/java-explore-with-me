package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explore.compilation.service.CompilationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "admin/compilations")
@Slf4j
@RequiredArgsConstructor
@Validated
public class CompilationAdminController {

    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto compilationDto,
                                         HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return compilationService.addCompilation(compilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable @Positive long compId,
                                            HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    public void updateCompilation(@PathVariable @Positive long compId,
                                  @RequestBody @Valid UpdateCompilationRequest compilationUpdate,
                                  HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        compilationService.updateCompilation(compId, compilationUpdate);
    }

}
