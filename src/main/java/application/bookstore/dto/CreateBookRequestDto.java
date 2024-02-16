package application.bookstore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateBookRequestDto {
    private static final long MIN_VALUE = 0;
    private static final int MIN_DESCRIPTION_LENGTH = 25;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String isbn;
    @NotNull
    @Min(MIN_VALUE)
    private BigDecimal price;
    @Size(min = MIN_DESCRIPTION_LENGTH)
    private String description;
    private String coverImage;
}
