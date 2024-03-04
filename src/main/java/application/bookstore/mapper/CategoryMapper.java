package application.bookstore.mapper;

import application.bookstore.config.MapperConfig;
import application.bookstore.dto.category.CategoryDto;
import application.bookstore.dto.category.CreateCategoryRequestDto;
import application.bookstore.model.Category;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequestDto categoryDto);
}
