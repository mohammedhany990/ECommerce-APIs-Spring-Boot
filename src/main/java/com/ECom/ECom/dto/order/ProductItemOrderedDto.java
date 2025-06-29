package com.ECom.ECom.dto.order;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductItemOrderedDto {
    @NotNull
    @Positive
    private Long productId;

    @NotBlank
    private String productName;

    private String pictureUrl;
}