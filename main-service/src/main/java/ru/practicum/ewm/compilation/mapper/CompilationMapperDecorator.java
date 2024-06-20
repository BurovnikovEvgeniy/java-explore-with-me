package ru.practicum.ewm.compilation.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.ewm.compilation.dto.CompilationDTO;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;

public abstract class CompilationMapperDecorator implements CompilationMapper {
    @Autowired
    private CompilationMapper compilationMapper;
    @Autowired
    private EventMapper eventMapper;

    public CompilationDTO compilationDTO(Compilation compilation) {
        CompilationDTO compilationDTO = compilationMapper.toCompilationDTO(compilation);
        compilationDTO.setEvents(eventMapper.toShortEventDTO(compilation.getEvents()));
        return compilationDTO;
    }
}
