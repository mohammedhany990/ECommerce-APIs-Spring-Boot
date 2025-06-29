package com.ECom.ECom.dto.order;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    @NotNull
    private Long id;

    @NotNull
    private String buyerEmail;

    @NotNull
    private UserOrderAddressDto shippingAddress;

    @NotNull
    private DeliveryMethodDto deliveryMethod;

    @NotNull
    private Set<OrderItemDto> items;

    @NotNull
    private OffsetDateTime orderDate;

    @NotNull
    private String status;

    @NotNull
    private BigDecimal subTotal;
}