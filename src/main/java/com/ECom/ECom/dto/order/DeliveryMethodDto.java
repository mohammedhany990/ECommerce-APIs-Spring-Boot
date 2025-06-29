package com.ECom.ECom.dto.order;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryMethodDto {
    @NotNull
    private Long id;

    @NotBlank
    private String shortName;

    private String description;

    private String deliveryTime;

    @NotNull
    @PositiveOrZero
    private BigDecimal cost;
}