package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.ProductDao;
import eu.senlainc.course.foodpricing.dto.ProductCategoryDto;
import eu.senlainc.course.foodpricing.dto.ProductDto;
import eu.senlainc.course.foodpricing.entities.Product;
import eu.senlainc.course.foodpricing.entities.ProductCategory;
import eu.senlainc.course.foodpricing.enums.ProductCategoryEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductDao productDao;
    @InjectMocks
    private ProductService productService;

    private static final ProductDto PRODUCT_DTO = new ProductDto(1, "TestProduct", new ProductCategory(1, ProductCategoryEnum.FRUITS));
    private static final String CSV_CONTENT = """
        productId,productName,categoryId
        1,TestProduct,1
        2,TestProduct,2
        """;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBatchUploadProductData_SuccessfulUpload() throws IOException {
        MultipartFile mockedFile = mock(MultipartFile.class);
        when(mockedFile.getInputStream()).thenReturn(new ByteArrayInputStream(CSV_CONTENT.getBytes()));

        productService.batchUploadProductData(mockedFile);

        verify(productDao, times(2)).save(any());
    }

    @Test
    void testAddProduct_SuccessfulAddition() {
        doNothing().when(productDao).save(any());

        ProductDto addedProduct = productService.addProduct(PRODUCT_DTO);

        verify(productDao, times(1)).save(any());

        assertNotNull(addedProduct);
    }

    @Test
    void testGetProductById_ReturnsProduct() {
        when(productDao.findById(anyInt())).thenReturn(Optional.of(new Product()));

        Product product = productService.getProductById(1);

        verify(productDao, times(1)).findById(anyInt());

        assertNotNull(product);
    }

    @Test
    void testGetAllProductsByCategoryId_ReturnsProducts() {
        when(productDao.getProductsByCategory(any())).thenReturn(Arrays.asList(new Product(), new Product()));

        List<ProductDto> products = productService.getAllProductsByCategoryId(1);

        verify(productDao, times(1)).getProductsByCategory(any());

        assertEquals(2, products.size());
    }

    @Test
    void testGetAllProductsByCategory_ReturnsProducts() {
        when(productDao.getProductsByCategory(any())).thenReturn(Arrays.asList(new Product(), new Product()));

        List<ProductDto> products = productService.getAllProductsByCategory(new ProductCategoryDto(1, ProductCategoryEnum.FRUITS));

        verify(productDao, times(1)).getProductsByCategory(any());

        assertEquals(2, products.size());
    }

    @Test
    void testUpdateProduct_SuccessfulUpdate() {
        Integer productId = PRODUCT_DTO.getProductId();
        when(productDao.findById(productId)).thenReturn(Optional.of(new Product()));

        ProductDto updatedProduct = productService.updateProduct(productId, PRODUCT_DTO);

        verify(productDao, times(1)).findById(productId);
        verify(productDao, times(1)).save(any());

        assertNotNull(updatedProduct);
    }

    @Test
    void testDeleteProduct_SuccessfulDeletion() {
        when(productDao.findById(anyInt())).thenReturn(Optional.of(new Product()));

        ProductDto deletedProduct = productService.deleteProduct(1);

        verify(productDao, times(1)).findById(anyInt());
        verify(productDao, times(1)).delete(any());

        assertNotNull(deletedProduct);
    }
}