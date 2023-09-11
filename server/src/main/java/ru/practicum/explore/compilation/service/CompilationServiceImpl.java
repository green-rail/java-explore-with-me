package ru.practicum.explore.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.compilation.Compilation;
import ru.practicum.explore.compilation.CompilationRepository;
import ru.practicum.explore.compilation.dto.CompilationDto;
import ru.practicum.explore.compilation.dto.NewCompilationDto;
import ru.practicum.explore.compilation.dto.UpdateCompilationRequest;
import ru.practicum.explore.error.ErrorMessages;
import ru.practicum.explore.error.exception.DataConflictException;
import ru.practicum.explore.error.exception.EntityNotFoundException;
import ru.practicum.explore.event.EventRepository;
import ru.practicum.explore.event.model.Event;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilationsPublic(Boolean pinned, int from, int size) {

        PageRequest page = PageRequest.of(from / size, size);

        List<Compilation> compilations = pinned == null ?
                compilationRepository.findAll(page).getContent() :
                compilationRepository.findAllByPinned(pinned, page).getContent();

        return compilations.stream()
                .map(CompilationDto::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilationPublic(long compId) {
        var compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getCompilationNotFoundMessage(compId)));
        return CompilationDto.toDto(compilation);
    }

    @Override
    public CompilationDto addCompilationAdmin(NewCompilationDto compilationDto) {
        if (compilationRepository.existsByTitle(compilationDto.getTitle())) {
            throw new DataConflictException("Compilation with this name already exists.");
        }
        var compilation = compilationDto.toEntity();
        if (compilationDto.getEvents() != null && !compilationDto.getEvents().isEmpty()) {
            List<Event> events = eventRepository.findAllById(new HashSet<>(compilationDto.getEvents()));
            compilation.setEvents(new HashSet<>(events));
        }
        return CompilationDto.toDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilationAdmin(long compId) {
        if (!compilationRepository.existsById(compId)) {
            throw new EntityNotFoundException(ErrorMessages.getCompilationNotFoundMessage(compId));
        }
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationDto updateCompilationAdmin(long compId, UpdateCompilationRequest compilationUpdate) {
        var compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.getCompilationNotFoundMessage(compId)));

        if (compilationUpdate.getPinned() != null) {
            compilation.setPinned(compilationUpdate.getPinned());
        }

        if (compilationUpdate.getTitle() != null) {
            if (compilationUpdate.getTitle().isBlank()) {
                throw new IllegalArgumentException("Compilation title can't be blank.");
            }
            compilation.setTitle(compilationUpdate.getTitle());
        }

        if (compilationUpdate.getEvents() != null) {
            if (compilationUpdate.getEvents().isEmpty()) {
                compilation.setEvents(new HashSet<>());
            } else {
                List<Event> events = eventRepository.findAllById(new HashSet<>(compilationUpdate.getEvents()));
                compilation.setEvents(new HashSet<>(events));
            }
        }
        return CompilationDto.toDto(compilationRepository.save(compilation));
    }
}
