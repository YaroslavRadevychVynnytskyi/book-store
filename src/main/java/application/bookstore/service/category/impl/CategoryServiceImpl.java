package application.bookstore.service.category.impl;

import application.bookstore.dto.category.CategoryDto;
import application.bookstore.dto.category.CreateCategoryRequestDto;
import application.bookstore.exception.EntityNotFoundException;
import application.bookstore.mapper.CategoryMapper;
import application.bookstore.model.Category;
import application.bookstore.repository.category.CategoryRepository;
import application.bookstore.service.category.CategoryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll(Pageable pageable) {
        Page<Category> allCategoryEntities = categoryRepository.findAll(pageable);
        return allCategoryEntities.stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        Category categoryById = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Can't find category by id: " + id));
        return categoryMapper.toDto(categoryById);
    }

    @Override
    public CategoryDto save(CreateCategoryRequestDto categoryDto) {
        Category entity = categoryMapper.toEntity(categoryDto);
        return categoryMapper.toDto(categoryRepository.save(entity));
    }

    @Override
    public CategoryDto update(Long id, CreateCategoryRequestDto categoryDto) {
        if (!categoryRepository.existsById(id)) {
            throw new EntityNotFoundException("Category with id: " + id + " does not exist");
        }
        Category categoryEntity = categoryMapper.toEntity(categoryDto);
        categoryEntity.setId(id);
        return categoryMapper.toDto(categoryRepository.save(categoryEntity));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
