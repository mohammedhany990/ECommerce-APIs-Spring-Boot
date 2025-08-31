package com.ECom.ECom.service;

import com.ECom.ECom.dto.*;
import com.ECom.ECom.entity.Product;
import com.ECom.ECom.entity.ProductBrand;
import com.ECom.ECom.entity.ProductCategory;
import com.ECom.ECom.exception.ResourceNotFoundException;
import com.ECom.ECom.helper.FileUploadUtil;
import com.ECom.ECom.helper.ProductMapper;
import com.ECom.ECom.helper.ProductSpecification;
import com.ECom.ECom.repository.ProductBrandRepo;
import com.ECom.ECom.repository.ProductCategoryRepo;
import com.ECom.ECom.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductCategoryRepo productCategoryRepo;
    private final ProductBrandRepo productBrandRepo;
    private final ProductMapper productMapper;


    @Cacheable(value = "productSearch", key = "#filter.hashCode()")
    public PageResponse<ProductReturningDto> getAllProducts(ProductFilterDTO filter) {
        int page = filter.getPage() < 0 ? 0 : filter.getPage();
        int size = filter.getSize() <= 0 ? 10 : filter.getSize();


        BigDecimal minPrice = (filter.getMinPrice() == null || filter.getMinPrice().compareTo(BigDecimal.ZERO) < 0)
                ? BigDecimal.ZERO
                : filter.getMinPrice();

        BigDecimal maxPrice = (filter.getMaxPrice() == null || filter.getMaxPrice().compareTo(BigDecimal.ZERO) <= 0)
                ? BigDecimal.valueOf(Long.MAX_VALUE)
                : filter.getMaxPrice();


        String sortBy = (filter.getSortBy() == null || filter.getSortBy().isEmpty()) ? "id" : filter.getSortBy();
        if ("price".equalsIgnoreCase(sortBy)) {
            sortBy = "productPrice";
        }

        Sort.Direction direction =
                (filter.getSortDir() != null && filter.getSortDir().equalsIgnoreCase("DESC"))
                        ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Specification<Product> spec = Specification.allOf(
                ProductSpecification.hasName(filter.getName()),
                ProductSpecification.hasProductCategory(filter.getCategory()),
                ProductSpecification.hasProductBrand(filter.getBrand()),
                ProductSpecification.priceBetween(minPrice, maxPrice)
        );


        Page<ProductReturningDto> productsPage = productRepo
                .findAll(spec, pageable)
                .map(productMapper::toProductReturningDto);

        return new PageResponse<>(
                productsPage.getContent(),
                productsPage.getNumber(),
                productsPage.getSize(),
                productsPage.getTotalElements(),
                productsPage.getTotalPages()
        );
    }

    @Cacheable(value = "products", key = "#id")
    public ProductReturningDto getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return productMapper.toProductReturningDto(product);
    }

    public ProductReturningDto createProduct(CreateProductDto dto, MultipartFile pictureFile) throws IOException {
        ProductCategory category = productCategoryRepo.findById(dto.getProductCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        ProductBrand brand = productBrandRepo.findById(dto.getProductBrandId())
                .orElseThrow(() -> new RuntimeException("Brand not found"));

        String picturePath = FileUploadUtil.saveFile("products", pictureFile);

        Product product = productMapper.toProduct(dto, category, brand, picturePath);
        product = productRepo.save(product);

        return productMapper.toProductReturningDto(product);
    }

//    @CacheEvict(value = "products", key = "#id")
//    public void deleteProduct(Long id) {
//        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
//        productRepo.delete(product);
//    }

    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        String picturePath = product.getProductImageUrl();
        if (picturePath != null && !picturePath.isEmpty()) {
            String imageName = picturePath.substring(picturePath.lastIndexOf("/") + 1);
            if (!imageName.isEmpty()) {
                FileUploadUtil.deleteFile("products", imageName);
            }
        }

        productRepo.delete(product);
    }


    @CachePut(value = "products", key = "#id")
    public ProductReturningDto updateProduct(Long id, UpdateProductDto dto) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        if (dto.getProductName() != null) {
            product.setProductName(dto.getProductName());
        }
        if (dto.getProductDescription() != null) {
            product.setProductDescription(dto.getProductDescription());
        }

        if (dto.getProductPrice() != null) {
            product.setProductPrice(dto.getProductPrice());
        }

        if (dto.getProductCategoryId() != null) {
            ProductCategory category = productCategoryRepo.findById(dto.getProductCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getProductCategoryId()));
            product.setProductCategory(category);
        }

        if (dto.getProductBrandId() != null) {
            ProductBrand brand = productBrandRepo.findById(dto.getProductBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found with id: " + dto.getProductBrandId()));
            product.setProductBrand(brand);
        }
        if (dto.getPictureFile() != null && !dto.getPictureFile().isEmpty()) {
            try {
                String oldPicturePath = product.getProductImageUrl();
                if (oldPicturePath != null && !oldPicturePath.isEmpty()) {
                    // Assuming your productImageUrl is like "images/products/imagename.png"
                    String imageName = oldPicturePath.substring(oldPicturePath.lastIndexOf("/") + 1);

                    if (!imageName.isEmpty()) {
                        FileUploadUtil.deleteFile("products", imageName);
                    }
                }

                // Save new image and set path
                String newPicturePath = FileUploadUtil.saveFile("products", dto.getPictureFile());
                product.setProductImageUrl(newPicturePath);

            } catch (IOException e) {
                throw new RuntimeException("Failed to update product image", e);
            }
        }

        Product updatedProduct = productRepo.save(product);

        return productMapper.toProductReturningDto(updatedProduct);
    }
}
