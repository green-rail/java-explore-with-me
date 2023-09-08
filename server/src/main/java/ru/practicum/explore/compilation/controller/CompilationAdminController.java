package ru.practicum.explore.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto compilationDto,
                                         HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return compilationService.addCompilationAdmin(compilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable @Positive long compId,
                                            HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        compilationService.deleteCompilationAdmin(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationDto updateCompilation(@PathVariable @Positive long compId,
                                            @RequestBody @Valid UpdateCompilationRequest compilationUpdate,
                                            HttpServletRequest request) {

        log.debug("On URL [{}] used method [{}]", request.getRequestURL(), request.getMethod());
        return compilationService.updateCompilationAdmin(compId, compilationUpdate);
    }

}
