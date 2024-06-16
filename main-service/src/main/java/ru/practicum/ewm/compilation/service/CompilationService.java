package ru.practicum.ewm.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;

import java.util.List;

public interface CompilationService {
    CompilationDto addCompilation(NewCompilationDto newCompilationDTO);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(UpdateCompilationDto updateCompilationDTO, Long compId);

    CompilationDto getCompilation(Long compId);

    List<CompilationDto> getCompilations(Boolean pined, PageRequest pageRequest);
}