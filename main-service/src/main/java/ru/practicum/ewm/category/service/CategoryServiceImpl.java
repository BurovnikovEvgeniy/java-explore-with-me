package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.mapper.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.error.NotFoundException;

import java.util.List;

import static ru.practicum.ewm.utils.Constants.CATEGORY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional
    public CategoryDto addCategory(NewCategoryDto newCategoryDTO) {
        log.debug("Adding category {}", newCategoryDTO);
        Category category = categoryRepository.save(categoryMapper.toCategory(newCategoryDTO));
        log.debug("Category is added {}", category);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.debug("Deleting category ID{}", catId);
        getCategoryIfExist(catId);
        categoryRepository.deleteById(catId);
        log.debug("Category ID{} is deleted", catId);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto newCategoryDTO) {
        log.debug("Updating category ID{}", catId);
        Category category = getCategoryIfExist(catId);
        category.setName(newCategoryDTO.getName());
        Category updatedCategory = categoryRepository.save(category);
        log.debug("Category is updated {}", category);
        return categoryMapper.toCategoryDto(updatedCategory);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDto getCategory(Long catId) {
        log.debug("Getting category ID{}", catId);
        Category category = getCategoryIfExist(catId);
        return categoryMapper.toCategoryDto(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDto> getAllCategories(PageRequest pageRequest) {
        log.debug("Getting list of categories");
        return categoryMapper.toCategoryDto(categoryRepository.findAll(pageRequest).toList());
    }

    public Category getCategoryIfExist(Long catId) {
        return categoryRepository.findById(catId).orElseThrow(() ->
                new NotFoundException(String.format(CATEGORY_NOT_FOUND, catId)));
    }
}