package ru.practicum.ewm.category.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "id", ignore = true)
    Category toCategory(NewCategoryDto newCategoryDTO);

    CategoryDto toCategoryDto(Category category);

    List<CategoryDto> toCategoryDto(List<Category> categories);
}