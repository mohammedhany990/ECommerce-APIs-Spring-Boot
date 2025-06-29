package com.ECom.ECom.service;

import com.ECom.ECom.dto.CreateOrUpdateProductBrandDto;
import com.ECom.ECom.entity.ProductBrand;
import com.ECom.ECom.exception.ResourceNotFoundException;
import com.ECom.ECom.repository.ProductBrandRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductBrandService {
    private final ProductBrandRepo productBrandRepo;

    public List<ProductBrand> getAllProductBrands() {
        return productBrandRepo.findAll();
    }

    public ProductBrand getProductBrandById(Long id) {
        return productBrandRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Brand not found with id:" + id));
    }

    public ProductBrand createProductBrand(CreateOrUpdateProductBrandDto dto) {
        ProductBrand productBrand = ProductBrand.builder()
                .brandName(dto.getBrandName())
                .brandDescription(dto.getBrandDescription())
                .build();
        return productBrandRepo.save(productBrand);
    }

    public ProductBrand updateProductBrand(Long id, CreateOrUpdateProductBrandDto dto) {
        ProductBrand productBrand = productBrandRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Brand not found with id:" + id));
        if (!dto.getBrandName().isEmpty()) {
            productBrand.setBrandName(dto.getBrandName());
        }
        if (!dto.getBrandDescription().isEmpty()) {
            productBrand.setBrandDescription(dto.getBrandDescription());
        }
        return productBrandRepo.save(productBrand);

    }

    public void deleteProductBrand(Long id) {
        ProductBrand productBrand = productBrandRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product Brand not found with id:" + id));
        productBrandRepo.delete(productBrand);
    }
}
