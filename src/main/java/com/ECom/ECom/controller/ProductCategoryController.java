package com.ECom.ECom.controller;


import com.ECom.ECom.dto.CreateOrUpdateProductCategoryDto;
import com.ECom.ECom.entity.ProductCategory;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllProductCategories() {
        List<ProductCategory> categories = productCategoryService.getAllProductCategories();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched all product categories successfully", categories)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCategory>> getProductCategoryById(@PathVariable Long id) {
        ProductCategory category = productCategoryService.getProductCategoryById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched product category successfully", category)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductCategory>> createProductCategory(@RequestBody CreateOrUpdateProductCategoryDto dto) {
        ProductCategory createdCategory = productCategoryService.createProductCategory(dto);
        return new ResponseEntity<>(
                new ApiResponse<>(true, "Product category created successfully", createdCategory),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductCategory>> updateProductCategory(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateProductCategoryDto dto
    ) {
        ProductCategory updatedCategory = productCategoryService.updateProductCategory(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product category updated successfully", updatedCategory)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductCategory(@PathVariable Long id) {
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product category deleted successfully", null)
        );
    }
}

