
//
//@Component
//public class ProductMapper {
//
//    public Product toProduct(CreateProductDto dto, ProductCategory category, ProductBrand brand, String picturePath) {
//        return Product.builder()
//                .productName(dto.getName())
//                .productDescription(dto.getDescription())
//                .productPrice(dto.getPrice())
//                .productStock(dto.getProductStock())
//                .productImageUrl(picturePath)
//                .productCategory(category)
//                .productBrand(brand)
//                .build();
//    }
//
//    public ProductReturningDto toProductReturningDto(Product product) {
//        return ProductReturningDto.builder()
//                .id(product.getId())
//                .name(product.getProductName())
//                .description(product.getProductDescription())
//                .price(product.getProductPrice())
//                .productStock(product.getProductStock())
//                .productCategoryId(product.getProductCategory().getId())
//                .category(product.getProductCategory().getCategoryName())
//                .productBrandId(product.getProductBrand().getId())
//                .brand(product.getProductBrand().getBrandName())
//                .pictureUrl("http://localhost:8080/" + product.getProductImageUrl())
//                .build();
//    }
//}

package com.ECom.ECom.helper;

import com.ECom.ECom.dto.CreateProductDto;
import com.ECom.ECom.dto.ProductReturningDto;
import com.ECom.ECom.entity.Product;
import com.ECom.ECom.entity.ProductBrand;
import com.ECom.ECom.entity.ProductCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ProductMapper {


    @Mapping(target ="id", ignore = true)
    @Mapping(source = "dto.name", target = "productName")
    @Mapping(source = "dto.description", target = "productDescription")
    @Mapping(source = "dto.price", target = "productPrice")
    @Mapping(source = "dto.productStock", target = "productStock")
    @Mapping(source = "category", target = "productCategory")
    @Mapping(source = "brand", target = "productBrand")
    @Mapping(source = "picturePath", target = "productImageUrl")
    Product toProduct(CreateProductDto dto, ProductCategory category, ProductBrand brand, String picturePath);

    @Mapping(source = "product.id", target = "id")
    @Mapping(source = "product.productName", target = "name")
    @Mapping(source = "product.productDescription", target = "description")
    @Mapping(source = "product.productPrice", target = "price")
    @Mapping(source = "product.productStock", target = "productStock")
    @Mapping(source = "product.productCategory.id", target = "productCategoryId")
    @Mapping(source = "product.productCategory.categoryName", target = "category")
    @Mapping(source = "product.productBrand.id", target = "productBrandId")
    @Mapping(source = "product.productBrand.brandName", target = "brand")
    @Mapping(source = "product.productImageUrl", target = "pictureUrl", qualifiedByName = "buildPictureUrl")
    ProductReturningDto toProductReturningDto(Product product);

    @Named("buildPictureUrl")
    default String buildPictureUrl(String imageUrl) {
        return "http://localhost:8080/" + imageUrl;
    }
}
