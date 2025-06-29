package com.ECom.ECom.controller;

import com.ECom.ECom.dto.CreateOrUpdateProductBrandDto;
import com.ECom.ECom.entity.ProductBrand;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.service.ProductBrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-brands")
@RequiredArgsConstructor
public class ProductBrandController {
    private final ProductBrandService productBrandService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductBrand>>> getAllProductBrands() {
        List<ProductBrand> brands = productBrandService.getAllProductBrands();
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched all product brands successfully", brands)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductBrand>> getProductBrandById(@PathVariable Long id) {
        ProductBrand brand = productBrandService.getProductBrandById(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Fetched product brand successfully", brand)
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProductBrand>> createProductBrand(@RequestBody CreateOrUpdateProductBrandDto dto) {
        ProductBrand createdBrand = productBrandService.createProductBrand(dto);
        return new ResponseEntity<>(
                new ApiResponse<>(true, "Product brand created successfully", createdBrand),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductBrand>> updateProductBrand(
            @PathVariable Long id,
            @RequestBody CreateOrUpdateProductBrandDto dto
    ) {
        ProductBrand updatedBrand = productBrandService.updateProductBrand(id, dto);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product brand updated successfully", updatedBrand)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProductBrand(@PathVariable Long id) {
        productBrandService.deleteProductBrand(id);
        return ResponseEntity.ok(
                new ApiResponse<>(true, "Product brand deleted successfully", null)
        );
    }
}
