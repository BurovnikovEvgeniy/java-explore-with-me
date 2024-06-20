package ru.practicum.ewm.compilation.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.compilation.dto.CompilationDTO;
import ru.practicum.ewm.compilation.dto.NewCompilationDTO;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.mapper.EventMapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = EventMapper.class)
@DecoratedWith(CompilationMapperDecorator.class)
public interface CompilationMapper {
    @Mapping(target = "events", ignore = true)
    Compilation toCompilation(NewCompilationDTO newCompilationDTO);

    CompilationDTO toCompilationDTO(Compilation compilation);

    List<CompilationDTO> toCompilationDTO(List<Compilation> compilations);
}