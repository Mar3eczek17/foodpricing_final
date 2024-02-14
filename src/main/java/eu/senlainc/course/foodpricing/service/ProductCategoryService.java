package eu.senlainc.course.foodpricing.service;

import eu.senlainc.course.foodpricing.dao.ProductCategoryDao;
import eu.senlainc.course.foodpricing.dto.ProductCategoryDto;
import eu.senlainc.course.foodpricing.entities.ProductCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    @Autowired
    private ProductCategoryDao productCategoryDao;
    private static final Logger logger = LoggerFactory.getLogger(ProductCategoryService.class);
    private static final String FETCHED_PRODUCT_CATEGORIES_MESSAGE = "Fetched {} product categories";

    public List<ProductCategoryDto> getAllCategories() {
        List<ProductCategory> productCategories = productCategoryDao.findAll();

        logger.info(FETCHED_PRODUCT_CATEGORIES_MESSAGE, productCategories.size());

        return productCategories.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private ProductCategoryDto mapToDto(ProductCategory productCategory) {
        return new ProductCategoryDto(
                productCategory.getCategoryId(),
                productCategory.getCategoryName()
        );
    }
}