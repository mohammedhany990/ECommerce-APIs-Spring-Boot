package com.ECom.ECom.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Basket {
    
    private String buyerEmail;
    private List<BasketItem> items;
    private Long deliveryMethodId;
    private String paymentIntentId;
    private String clientSecret;
    private BigDecimal shippingPrice;
}