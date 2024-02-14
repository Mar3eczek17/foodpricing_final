package eu.senlainc.course.foodpricing.dto;

import eu.senlainc.course.foodpricing.enums.ProductCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductCategoryDto {

    private Integer categoryId;

    private ProductCategoryEnum categoryName;
}