package com.ECom.ECom.dto.order;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDto {
    @NotNull
    private ProductItemOrderedDto product;

    @NotNull
    @Positive
    private Long quantity;

    @NotNull
    @Positive
    private BigDecimal price;
}