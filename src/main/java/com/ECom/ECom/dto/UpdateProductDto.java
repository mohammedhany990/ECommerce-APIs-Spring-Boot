package com.ECom.ECom.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProductDto {

    private String productName;
    private String productDescription;

    private MultipartFile pictureFile;
    private BigDecimal productPrice;

    private Long productBrandId;

    private Long productCategoryId;
}
