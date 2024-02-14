package eu.senlainc.course.foodpricing.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import eu.senlainc.course.foodpricing.dao.ProductDao;
import eu.senlainc.course.foodpricing.dto.ProductCategoryDto;
import eu.senlainc.course.foodpricing.dto.ProductDto;
import eu.senlainc.course.foodpricing.entities.Product;
import eu.senlainc.course.foodpricing.entities.ProductCategory;
import eu.senlainc.course.foodpricing.expectations.BatchUploadException;
import eu.senlainc.course.foodpricing.expectations.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductDao productDao;
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private static final String BATCH_UPLOAD_SUCCESS_MESSAGE = "Batch upload of product data completed successfully";
    private static final String BATCH_UPLOAD_ERROR_MESSAGE = "Error during batch upload";
    private static final String ADDED_PRODUCT_SUCCESS_MESSAGE = "Added product successfully. ProductId: {}";
    private static final String UPDATED_PRODUCT_SUCCESS_MESSAGE = "Updated product successfully. ProductId: {}";
    private static final String DELETED_PRODUCT_SUCCESS_MESSAGE = "Deleted product successfully. ProductId: {}";
    private static final String FETCHED_PRODUCTS_MESSAGE = "Fetched {} products for categoryId: {}";
    private static final String ERROR_READING_CSV_FILE = "Error reading CSV file";
    private static final String PRODUCT_NOT_FOUND_WITH_ID = "Product not found with ID: ";
    private static final String PRODUCT_WITH_ID_NOT_FOUND = "Product with ID ";
    private static final String PRODUCT_NOT_FOUND = " not found";

    @Transactional
    public void batchUploadProductData(MultipartFile file) {
        try {
            List<ProductDto> productDtos = readProductDataFromCsv(file);
            for (ProductDto productDto : productDtos) {
                productDto.addProductToDatabase(this);
            }

            logger.info(BATCH_UPLOAD_SUCCESS_MESSAGE);
        } catch (IOException e) {
            logger.error(BATCH_UPLOAD_ERROR_MESSAGE, e);
            throw new BatchUploadException(BATCH_UPLOAD_ERROR_MESSAGE, e);
        }
    }

    private List<ProductDto> readProductDataFromCsv(MultipartFile file) throws IOException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            List<String[]> records = csvReader.readAll();
            return mapCsvToProductDtoList(records);
        } catch (CsvException e) {
            throw new IOException(ERROR_READING_CSV_FILE, e);
        }
    }

    private List<ProductDto> mapCsvToProductDtoList(List<String[]> records) {
        List<ProductDto> productDtos = new ArrayList<>();

        for (int i = 1; i < records.size(); i++) {
            String[] record = records.get(i);

            Integer productId = Integer.parseInt(record[0]);
            String productName = record[1];
            Integer categoryId = Integer.parseInt(record[2]);

            ProductDto productDto = new ProductDto(productId, productName, new ProductCategory(categoryId, null));
            productDtos.add(productDto);
        }

        return productDtos;
    }

    @Transactional
    public ProductDto addProduct(ProductDto productDto) {
        Product product = mapDtoToProduct(productDto);
        productDao.save(product);

        logger.info(ADDED_PRODUCT_SUCCESS_MESSAGE, product.getProductId());

        return mapProductToDto(product);
    }

    private Product mapDtoToProduct(ProductDto productDto) {
        return new Product(
                productDto.getProductId(),
                productDto.getProductName(),
                productDto.getCategoryId()
        );
    }

    private ProductDto mapProductToDto(Product product) {
        return new ProductDto(
                product.getProductId(),
                product.getProductName(),
                product.getCategoryId()
        );
    }

    public Product getProductById(Integer productId) {
        return productDao.findById(productId).orElse(null);
    }

    public List<ProductDto> getAllProductsByCategoryId(Integer categoryId) {
        ProductCategoryDto categoryDto = new ProductCategoryDto(categoryId, null);
        return getAllProductsByCategory(categoryDto);
    }

    public List<ProductDto> getAllProductsByCategory(ProductCategoryDto categoryDto) {
        ProductCategory category = convertDtoToEntity(categoryDto);
        List<Product> productsByCategory = productDao.getProductsByCategory(category);

        logger.info(FETCHED_PRODUCTS_MESSAGE, productsByCategory.size(), category.getCategoryId());

        return productsByCategory.stream()
                .map(this::mapProductToDto)
                .collect(Collectors.toList());
    }

    private ProductCategory convertDtoToEntity(ProductCategoryDto categoryDto) {
        return new ProductCategory(
                categoryDto.getCategoryId(),
                categoryDto.getCategoryName()
        );
    }

    @Transactional
    public ProductDto updateProduct(Integer productId, ProductDto productDto) {
        Product existingProduct = getProductById(productId);

        if (existingProduct == null) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND_WITH_ID + productId);
        }

        existingProduct.setProductName(productDto.getProductName());
        existingProduct.setCategoryId(productDto.getCategoryId());

        productDao.save(existingProduct);

        logger.info(UPDATED_PRODUCT_SUCCESS_MESSAGE, existingProduct.getProductId());

        return mapProductToDto(existingProduct);
    }

    @Transactional
    public ProductDto deleteProduct(Integer productId) {
        Product existingProduct = getProductById(productId);

        if (existingProduct != null) {
            productDao.delete(existingProduct);

            logger.info(DELETED_PRODUCT_SUCCESS_MESSAGE, existingProduct.getProductId());

            return mapProductToDto(existingProduct);
        } else {
            throw new ProductNotFoundException(PRODUCT_WITH_ID_NOT_FOUND + productId + PRODUCT_NOT_FOUND);
        }
    }
}