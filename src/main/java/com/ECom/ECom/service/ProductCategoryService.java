package com.ECom.ECom.service;


import com.ECom.ECom.dto.CreateOrUpdateProductCategoryDto;
import com.ECom.ECom.entity.ProductCategory;
import com.ECom.ECom.exception.ResourceNotFoundException;
import com.ECom.ECom.repository.ProductCategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryRepo productCategoryRepo;

    public List<ProductCategory> getAllProductCategories() {
        return productCategoryRepo.findAll();
    }

    public ProductCategory getProductCategoryById(Long id) {
        return productCategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Category not found with id:" + id));
    }

    public ProductCategory createProductCategory(CreateOrUpdateProductCategoryDto dto) {
        ProductCategory productCategory = ProductCategory.builder()
                .categoryName(dto.getCategoryName())
                .categoryDescription(dto.getCategoryDescription())
                .build();
        return productCategoryRepo.save(productCategory);
    }

    public ProductCategory updateProductCategory(Long id, CreateOrUpdateProductCategoryDto dto) {
        ProductCategory productCategory = productCategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Category not found with id:" + id));
        if (!dto.getCategoryName().isEmpty()) {
            productCategory.setCategoryName(dto.getCategoryName());
        }
        if (!dto.getCategoryDescription().isEmpty()) {
            productCategory.setCategoryDescription(dto.getCategoryDescription());
        }
        return productCategoryRepo.save(productCategory);
    }

    public void deleteProductCategory(Long id) {
        ProductCategory productCategory = productCategoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Category not found with id:" + id));
        productCategoryRepo.delete(productCategory);
    }
}