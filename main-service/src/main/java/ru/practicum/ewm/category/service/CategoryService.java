package ru.practicum.ewm.category.service;

import org.springframework.data.domain.PageRequest;
import ru.practicum.ewm.category.dto.CategoryDTO;
import ru.practicum.ewm.category.dto.NewCategoryDTO;

import java.util.List;

public interface CategoryService {
    CategoryDTO addCategory(NewCategoryDTO newCategoryDTO);

    void deleteCategory(Long catId);

    CategoryDTO updateCategory(Long catId, NewCategoryDTO newCategoryDTO);

    CategoryDTO getCategory(Long catId);

    List<CategoryDTO> getAllCategories(PageRequest pageRequest);
}