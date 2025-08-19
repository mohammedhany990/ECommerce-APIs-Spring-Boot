package com.ECom.ECom.controller;

import com.ECom.ECom.dto.CreateProductDto;
import com.ECom.ECom.dto.ProductFilterDTO;
import com.ECom.ECom.dto.ProductReturningDto;
import com.ECom.ECom.dto.UpdateProductDto;
import com.ECom.ECom.exception.FileProcessingException;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ProductReturningDto>>> getAllProducts() {
//        List<ProductReturningDto> products = productService.getAllProducts();
//        return ResponseEntity.ok(
//                new ApiResponse<>(
//                        true,
//                        "Products fetched successfully",
//                        products
//                )
//        );
//    }

    @PostMapping("products")
    public ResponseEntity<ApiResponse<Page<ProductReturningDto>>> searchProducts(@RequestBody ProductFilterDTO filter) {
        Page<ProductReturningDto> products = productService.getAllProducts(filter);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Products fetched successfully",
                        products
                )
        );
    }



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductReturningDto>> createProduct(
            @Valid @RequestPart("product") CreateProductDto dto,
            @RequestPart("pictureFile") MultipartFile pictureFile) {

        if (pictureFile.isEmpty()) {
            throw new IllegalArgumentException("Picture file cannot be empty");
        }

        try {
            ProductReturningDto createdProduct = productService.createProduct(dto, pictureFile);
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    new ApiResponse<>(
                            true,
                            "Product created successfully",
                            createdProduct
                    )
            );
        } catch (IOException e) {
            throw new FileProcessingException("Failed to process image file: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductReturningDto>> getProductById(@PathVariable Long id) {
        ProductReturningDto product = productService.getProductById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(
                        true,
                        "Product fetched successfully",
                        product
                )
        );
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductReturningDto>> updateProduct(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateProductDto dto) {

        // Validate pictureFile Content-Type if present
        if (dto.getPictureFile() != null && !dto.getPictureFile().isEmpty()) {
            String contentType = dto.getPictureFile().getContentType();
            if (contentType != null && !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are supported (e.g., image/jpeg, image/png)");
            }
        }

        ProductReturningDto updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product updated successfully", updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Product deleted successfully", null));
    }
}