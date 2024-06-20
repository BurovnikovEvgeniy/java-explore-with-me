package ru.practicum.ewm.compilation.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.compilation.dto.CompilationDTO;
import ru.practicum.ewm.compilation.dto.NewCompilationDTO;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDTO;

import java.util.List;

public interface CompilationService {
    CompilationDTO addCompilation(NewCompilationDTO newCompilationDTO);

    void deleteCompilation(Long compId);

    CompilationDTO updateCompilation(UpdateCompilationDTO updateCompilationDTO, Long compId);

    CompilationDTO getCompilation(Long compId);

    List<CompilationDTO> getCompilations(Boolean pined, PageRequest pageRequest);
}