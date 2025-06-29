package com.ECom.ECom.helper;

import com.ECom.ECom.dto.CreateProductDto;
import com.ECom.ECom.dto.ProductReturningDto;
import com.ECom.ECom.entity.Product;
import com.ECom.ECom.entity.ProductBrand;
import com.ECom.ECom.entity.ProductCategory;
import org.springframework.stereotype.Component;

//@Mapper(componentModel = "spring")
//public interface ProductMapper {
//
//    @Mapping(source = "name", target = "productName")
//    @Mapping(source = "description", target = "productDescription")
//    @Mapping(source = "price", target = "productPrice")
//    @Mapping(source = "productStock", target = "productStock")
//    @Mapping(source = "productCategoryId", target = "productCategory.id")
//    @Mapping(source = "productBrandId", target = "productBrand.id")
//    Product toProduct(CreateProductDto dto);
//
//    @Mapping(source = "id", target = "id")
//    @Mapping(source = "productName", target = "name")
//    @Mapping(source = "productDescription", target = "description")
//    @Mapping(source = "productPrice", target = "price")
//    @Mapping(source = "productStock", target = "productStock")
//    @Mapping(source = "productCategory.id", target = "productCategoryId")
//    @Mapping(source = "productCategory.categoryName", target = "category")
//    @Mapping(source = "productBrand.id", target = "productBrandId")
//    @Mapping(source = "productBrand.brandName", target = "brand")
//    @Mapping(target = "pictureUrl", expression = "java(\"http://localhost:8080/\" + product.getProductImageUrl())")
//    ProductReturningDto toProductReturningDto(Product product);
//}

@Component
public class ProductMapper {

    public Product toProduct(CreateProductDto dto, ProductCategory category, ProductBrand brand, String picturePath) {
        return Product.builder()
                .productName(dto.getName())
                .productDescription(dto.getDescription())
                .productPrice(dto.getPrice())
                .productStock(dto.getProductStock())
                .productImageUrl(picturePath)
                .productCategory(category)
                .productBrand(brand)
                .build();
    }

    public ProductReturningDto toProductReturningDto(Product product) {
        return ProductReturningDto.builder()
                .id(product.getId())
                .name(product.getProductName())
                .description(product.getProductDescription())
                .price(product.getProductPrice())
                .productStock(product.getProductStock())
                .productCategoryId(product.getProductCategory().getId())
                .category(product.getProductCategory().getCategoryName())
                .productBrandId(product.getProductBrand().getId())
                .brand(product.getProductBrand().getBrandName())
                .pictureUrl("http://localhost:8080/" + product.getProductImageUrl())
                .build();
    }
}
