package com.ECom.ECom.helper;

import com.ECom.ECom.entity.Product;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasName(String productName) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(productName)) return null;
            return cb.like(cb.lower(root.get("productName")),
                    "%" + productName.toLowerCase() + "%");
        };
    }

    public static Specification<Product> hasProductCategory(String productCategory) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(productCategory)) return null;
            return cb.equal(cb.lower(root.get("productCategory").get("categoryName")),
                    productCategory.toLowerCase());
        };
    }

    public static Specification<Product> hasProductBrand(String productBrand) {
        return (root, query, cb) -> {
            if (!StringUtils.hasText(productBrand)) return null;
            return cb.equal(cb.lower(root.get("productBrand").get("brandName")),
                    productBrand.toLowerCase());
        };
    }

    public static Specification<Product> priceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();
            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("productPrice"), minPrice));
            }
            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("productPrice"), maxPrice));
            }
            return predicate;
        };
    }
}
