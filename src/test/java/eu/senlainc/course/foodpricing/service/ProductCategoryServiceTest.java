package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.ProductCategoryDao;
import eu.senlainc.course.foodpricing.dto.ProductCategoryDto;
import eu.senlainc.course.foodpricing.entities.ProductCategory;
import eu.senlainc.course.foodpricing.enums.ProductCategoryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryDao productCategoryDao;
    @InjectMocks
    private ProductCategoryService productCategoryService;

    private static final ProductCategory CATEGORY_FRUITS = createProductCategory(1, ProductCategoryEnum.FRUITS);
    private static final ProductCategory CATEGORY_VEGETABLES = createProductCategory(2, ProductCategoryEnum.VEGETABLES);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories_ReturnsExpectedListSize() {
        List<ProductCategory> expectedCategories = Arrays.asList(CATEGORY_FRUITS, CATEGORY_VEGETABLES);
        when(productCategoryDao.findAll()).thenReturn(expectedCategories);

        List<ProductCategoryDto> actualCategories = productCategoryService.getAllCategories();

        verify(productCategoryDao, times(1)).findAll();

        assertEquals(expectedCategories.size(), actualCategories.size());
    }

    private static ProductCategory createProductCategory(int categoryId, ProductCategoryEnum categoryName) {
        return new ProductCategory(categoryId, categoryName);
    }
}