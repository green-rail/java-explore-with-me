package ru.practicum.explore.compilation.service;

import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.dto.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getCompilationsPublic(Boolean pinned, int from, int size);

    CompilationDto getCompilationPublic(long compId);

    CompilationDto addCompilationAdmin(NewCompilationDto compilationDto);

    void deleteCompilationAdmin(long compId);

    CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest compilationUpdate);
}
