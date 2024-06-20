package ru.practicum.ewm.category.controller;

import lombok.AllArgsConstructor;
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
import ru.practicum.ewm.category.dto.CategoryDTO;
import ru.practicum.ewm.category.dto.NewCategoryDTO;
import ru.practicum.ewm.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

import static ru.practicum.ewm.utils.Constants.CATEGORIES_ADMIN_URI;
import static ru.practicum.ewm.utils.Constants.USERS_ADMIN_URI;

@Slf4j
@Validated
@RestController
@AllArgsConstructor
@RequestMapping(CATEGORIES_ADMIN_URI)
public class CategoryAdminController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO addCategory(@RequestBody @Valid NewCategoryDTO newCategoryDTO) {
        log.info("Response from POST request on {}", CATEGORIES_ADMIN_URI);
        return categoryService.addCategory(newCategoryDTO);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable @Positive Long catId) {
        log.info("Response from DELETE request on {}/{}", CATEGORIES_ADMIN_URI, catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/{catId}")
    public CategoryDTO updateCategory(@RequestBody @Valid NewCategoryDTO newCategoryDTO,
                                      @PathVariable @Positive Long catId) {
        log.info("Response from PATCH request on {}", USERS_ADMIN_URI);
        return categoryService.updateCategory(catId, newCategoryDTO);
    }
}