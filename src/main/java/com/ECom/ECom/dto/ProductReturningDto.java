package com.ECom.ECom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProductReturningDto {
    private Long id;
    private Long productStock;

    private String name;

    private String description;

    private String pictureUrl;


    private BigDecimal price;

    private Long productBrandId;

    private String brand;

    private Long productCategoryId;

    private String category;

}