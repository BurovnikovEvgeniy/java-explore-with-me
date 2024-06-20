package ru.practicum.ewm.compilation.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;

public abstract class CompilationMapperDecorator implements CompilationMapper {
    @Autowired
    private CompilationMapper compilationMapper;
    @Autowired
    private EventMapper eventMapper;

    public CompilationDto compilationDto(Compilation compilation) {
        CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);
        compilationDto.setEvents(eventMapper.toShortEventDto(compilation.getEvents()));
        return compilationDto;
    }
}
