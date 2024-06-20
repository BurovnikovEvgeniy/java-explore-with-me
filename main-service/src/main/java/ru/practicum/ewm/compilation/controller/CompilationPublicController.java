package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.COMPILATIONS_PUBLIC_URI;
import static ru.practicum.ewm.utils.Constants.COMPILATION_ID_URI;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(COMPILATIONS_PUBLIC_URI)
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping(COMPILATION_ID_URI)
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(required = false) Boolean pined,
                                                @RequestParam(defaultValue = "0") int from,
                                                @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilations(pined, PageRequest.of(from / size, size));
    }
}