package eu.senlainc.course.foodpricing.controllers;

import eu.senlainc.course.foodpricing.dto.ApiResponse;
import eu.senlainc.course.foodpricing.dto.ProductDto;
import eu.senlainc.course.foodpricing.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Integer categoryId) {
        List<ProductDto> products = productService.getAllProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<ProductDto> addProduct(@RequestBody @Valid ProductDto productDto) {
        ProductDto addedProduct = productService.addProduct(productDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid ProductDto productDto) {
        Integer productId = productDto.getProductId();
        ProductDto updatedProduct = productService.updateProduct(productId, productDto);
        return ResponseEntity.ok(updatedProduct);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/batch-upload")
    public ResponseEntity<ApiResponse> batchUploadProductData(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            ApiResponse response = new ApiResponse("Please select a file.");
            return ResponseEntity.badRequest().body(response);
        }

        productService.batchUploadProductData(file);
        ApiResponse response = new ApiResponse("Batch upload successful");
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ProductDto> deleteProduct(@PathVariable Integer productId) {
        ProductDto deletedProduct = productService.deleteProduct(productId);
        return ResponseEntity.ok(deletedProduct);
    }
}