package application.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import application.bookstore.dto.category.CategoryDto;
import application.bookstore.dto.category.CreateCategoryRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired WebApplicationContext applicationContext
    ) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/categories/remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Create a new category")
    void createCategory_ValidRequestDto_Success() throws Exception {
        //Given
        CategoryDto expected = new CategoryDto(
                1L,
                "Comedy",
                "Makes people laugh"
        );

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Comedy",
                "Makes people laugh"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                post("/api/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get all categories")
    void getAll_GivenCategoriesInCatalog_ShouldReturnAllCategories() throws Exception {
        //Given
        List<CategoryDto> expected = getCategoryDtoListMock();

        //When
        MvcResult result = mockMvc.perform(
                        get("/api/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryDto[] actual = objectMapper.readValue(result.getResponse()
                .getContentAsByteArray(), CategoryDto[].class);
        Assertions.assertEquals(expected.size(), actual.length);
        Assertions.assertEquals(expected, Arrays.stream(actual).toList());

    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Get category by id")
    void getCategoryById_GivenCategory_ShouldReturnCorrectCategory() throws Exception {
        //Given
        CategoryDto expected = new CategoryDto(
                2L,
                "Horror",
                "Genre with scary plots"
        );

        //When
        MvcResult result = mockMvc.perform(
                get("/api/categories/2")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    @Sql(scripts = "classpath:database/categories/add-mock-categories-to-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/categories/remove-mock-categories-from-categories-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @DisplayName("Update category by id")
    void updateCategory_GivenValidDtoAndId_ShouldReturnUpdatedCategory() throws Exception {
        //Given
        CreateCategoryRequestDto expected = new CreateCategoryRequestDto(
                "Horror",
                "Updated description for horror genre"
        );

        CreateCategoryRequestDto requestDto = new CreateCategoryRequestDto(
                "Horror",
                "Updated description for horror genre"
        );

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        //When
        MvcResult result = mockMvc.perform(
                        put("/api/categories/2")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        //Then
        CategoryDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryDto.class);
        Assertions.assertNotNull(actual.id());
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    private List<CategoryDto> getCategoryDtoListMock() {
        CategoryDto categoryDto1 = new CategoryDto(
                1L,
                "Crime",
                "Genre about criminals"
        );
        CategoryDto categoryDto2 = new CategoryDto(
                2L,
                "Horror",
                "Genre with scary plots"
        );
        CategoryDto categoryDto3 = new CategoryDto(
                3L,
                "Comedy",
                "Genre to laugh"
        );

        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryDtoList.add(categoryDto1);
        categoryDtoList.add(categoryDto2);
        categoryDtoList.add(categoryDto3);

        return categoryDtoList;
    }
}
