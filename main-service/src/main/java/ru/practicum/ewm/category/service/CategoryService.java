package ru.practicum.ewm.category.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto addCategory(NewCategoryDto newCategoryDTO);

    void deleteCategory(Long catId);

    CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDTO);

    CategoryDto getCategory(Long catId);

    List<CategoryDto> getAllCategories(PageRequest pageRequest);
}