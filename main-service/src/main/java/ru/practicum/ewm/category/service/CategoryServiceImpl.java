package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exceptions.EntityNotFoundException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategory) {
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategory));
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        getCategoryIfExist(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDTO) {
        Category category = getCategoryIfExist(catId);
        category.setName(newCategoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long catId) {
        Category category = getCategoryIfExist(catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(PageRequest pageRequest) {
        return categoryMapper.toCategoryDto(categoryRepository.findAll(pageRequest).toList());
    }

    private Category getCategoryIfExist(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() ->
                new EntityNotFoundException("Категория с id=" + categoryId + " не найдена"));
    }
}