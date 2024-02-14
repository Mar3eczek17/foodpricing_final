package eu.senlainc.course.foodpricing.dto;

import eu.senlainc.course.foodpricing.entities.ProductCategory;
import eu.senlainc.course.foodpricing.service.ProductService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductDto {

    private Integer productId;

    @NotBlank(message = "ProductName cannot be blank")
    private String productName;

    @NotNull(message = "CategoryId cannot be null")
    private ProductCategory categoryId;

    public void addProductToDatabase(ProductService productService) {
        productService.addProduct(this);
    }
}