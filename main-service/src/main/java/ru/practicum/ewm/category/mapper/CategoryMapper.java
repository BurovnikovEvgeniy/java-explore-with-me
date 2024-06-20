package ru.practicum.ewm.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.dto.CategoryDTO;
import ru.practicum.ewm.category.dto.NewCategoryDTO;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDTO newCategoryDTO);

    CategoryDTO toCategoryDTO(Category category);

    List<CategoryDTO> toCategoryDTO(List<Category> categories);
}