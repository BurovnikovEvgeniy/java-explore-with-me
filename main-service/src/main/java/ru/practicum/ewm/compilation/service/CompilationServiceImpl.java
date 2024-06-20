package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.mapper.CompilationMapper;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.error.NotFoundException;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;
import ru.practicum.ewm.event.service.EventService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationMapper compilationMapper;
    private final EventService eventService;

    @Override
    @Transactional
    public CompilationDto addCompilation(NewCompilationDto newCompilationDTO) {
        Compilation compilation = compilationMapper.toCompilation(newCompilationDTO);
        List<Event> events = newCompilationDTO.getEvents() == null ? new ArrayList<>()
                : eventRepository.findAllByIdIn(newCompilationDTO.getEvents());
        events = eventService.fillWithConfirmedRequests(events);
        events = eventService.fillWithEventViews(events);
        compilation.setEvents(new HashSet<>(events));
        CompilationDto compilationDTO = compilationMapper.toCompilationDto(compilationRepository.save(compilation));
        return compilationDTO;
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        getCompilationIfExist(compId);
        compilationRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(UpdateCompilationDto updateCompilationDto, Long compId) {
        Compilation compilation = getCompilationIfExist(compId);
        if (updateCompilationDto.getEvents() != null) {
            HashSet<Event> events = new HashSet<>(eventRepository.findAllByIdIn(updateCompilationDto.getEvents()));
            compilation.setEvents(events);
        }
        if (updateCompilationDto.getPinned() != null) {
            compilation.setPinned(updateCompilationDto.getPinned());
        }
        if (updateCompilationDto.getTitle() != null) {
            compilation.setTitle(updateCompilationDto.getTitle());
        }

        return compilationMapper.toCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    @Transactional(readOnly = true)
    public CompilationDto getCompilation(Long compId) {
        return compilationMapper.toCompilationDto(getCompilationIfExist(compId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompilationDto> getCompilations(Boolean pined, PageRequest pageRequest) {
        return (pined == null) ? compilationMapper.toCompilationDto(compilationRepository.findAll(pageRequest).toList())
                : compilationMapper.toCompilationDto(compilationRepository.findAllByPinned(pined, pageRequest).toList());

    }

    private Compilation getCompilationIfExist(Long compId) {
        return compilationRepository.findById(compId).orElseThrow(() ->
                new NotFoundException("Подборка с id=" + compId + " не найдена"));
    }
}