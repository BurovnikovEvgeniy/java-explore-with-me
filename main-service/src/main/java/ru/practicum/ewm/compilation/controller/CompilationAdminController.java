package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.dto.UpdateCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationService;

import javax.validation.Valid;


@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationAdminController {
    public final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CompilationDto addCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        if (newCompilationDto.getPinned() == null) newCompilationDto.setPinned(false);
        return compilationService.addCompilation(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping("/{compId}")
    CompilationDto updateCompilation(@RequestBody @Valid UpdateCompilationDto updateCompilationDto,
                                     @PathVariable Long compId) {
        return compilationService.updateCompilation(updateCompilationDto, compId);
    }
}