package com.ECom.ECom.dto.order;

import jakarta.validation.constraints.Email;
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
public class CreateOrderRequestDto {
    @NotBlank
    @Email
    private String buyerEmail;

    @NotBlank
    private String basketId;

    @NotNull
    @Positive
    private Long deliveryMethodId;

    @NotNull
    private UserOrderAddressDto shippingAddress;
}