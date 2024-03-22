package application.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import application.bookstore.dto.category.CategoryDto;
import application.bookstore.dto.category.CreateCategoryRequestDto;
import application.bookstore.exception.EntityNotFoundException;
import application.bookstore.mapper.CategoryMapper;
import application.bookstore.model.Category;
import application.bookstore.repository.category.CategoryRepository;
import application.bookstore.service.category.impl.CategoryServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @Mock
    private Pageable pageable;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Verify that findAll() return correct dto list")
    public void findAll_AllOk_ShouldReturnCategoryDtoList() {
        //Given
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Mock category 1");
        category1.setDescription("Mock description 1");

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Mock category 2");
        category2.setDescription("Mock description 2");

        Page<Category> categories;
        categories = new PageImpl<>(List.of(category1, category2));

        CategoryDto categoryDto1 = new CategoryDto(1L, "Mock category 1", "Mock description 1");
        CategoryDto categoryDto2 = new CategoryDto(2L, "Mock category 2", "Mock description 2");

        final List<CategoryDto> categoryDtoList = List.of(categoryDto1, categoryDto2);

        when(categoryRepository.findAll(pageable)).thenReturn(categories);
        when(categoryMapper.toDto(category1)).thenReturn(categoryDto1);
        when(categoryMapper.toDto(category2)).thenReturn(categoryDto2);

        //When
        List<CategoryDto> actual = categoryService.findAll(pageable);

        //Then
        assertEquals(categoryDtoList, actual);

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(category1);
        verify(categoryMapper, times(1)).toDto(category2);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Verify that EntityNotFoundException was thrown "
            + "in getById() when passed non existing category id")
    public void getById_WithNonExistingId_ShouldThrowEntityNotFoundException() {
        //Given
        Long nonExistingId = 999L;

        when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        //When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.getById(nonExistingId));

        //Then
        String expected = "Can't find category by id: " + nonExistingId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

        verify(categoryRepository, times(1)).findById(nonExistingId);
        verifyNoMoreInteractions(categoryRepository);
    }

    @Test
    @DisplayName("Verify that correct dto was returned"
            + " after save() call with valid request passed")
    public void save_WithValidRequestDto_ShouldReturnCategoryDto() {
        //Given
        final CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Crime",
                "Movie genre about crimes"
        );

        Category category = new Category();
        category.setId(1L);
        category.setName("Crime");
        category.setDescription("Movie genre about crimes");

        CategoryDto categoryDto = new CategoryDto(
                1L,
                "Crime",
                "Movie genre about crimes"
        );

        when(categoryMapper.toEntity(requestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(categoryDto);

        //When
        CategoryDto actual = categoryService.save(requestDto);

        //Then
        assertEquals(categoryDto, actual);

        verify(categoryMapper, times(1)).toEntity(requestDto);
        verify(categoryRepository, times(1)).save(category);
        verify(categoryMapper, times(1)).toDto(category);
        verifyNoMoreInteractions(categoryMapper, categoryRepository);
    }
}
