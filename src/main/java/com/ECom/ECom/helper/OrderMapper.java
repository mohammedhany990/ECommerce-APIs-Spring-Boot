package com.ECom.ECom.helper;


import com.ECom.ECom.dto.order.*;
import com.ECom.ECom.entity.order.*;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    // Map Order to OrderDto
    public OrderDto toOrderDto(Order order) {
        if (order == null) {
            return null;
        }
        return OrderDto.builder()
                .id(order.getId())
                .buyerEmail(order.getBuyerEmail())
                .shippingAddress(toUserOrderAddressDto(order.getShippingAddress()))
                .deliveryMethod(toDeliveryMethodDto(order.getDeliveryMethod()))
                .items(order.getItems() != null
                        ? order.getItems().stream()
                        .map(this::toOrderItemDto)
                        .collect(Collectors.toSet())
                        : Collections.emptySet())
                .orderDate(order.getOrderDate())
                .status(order.getStatus() != null ? order.getStatus().name() : null)
                .subTotal(order.getSubTotal())
                .build();
    }

    // Map OrderItem to OrderItemDto
    public OrderItemDto toOrderItemDto(OrderItem orderItem) {
        if (orderItem == null) {
            return null;
        }
        return OrderItemDto.builder()
                .product(toProductItemOrderedDto(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .price(orderItem.getPrice())
                .build();
    }

    // Map ProductItemOrdered to ProductItemOrderedDto
    public ProductItemOrderedDto toProductItemOrderedDto(ProductItemOrdered productItemOrdered) {
        if (productItemOrdered == null) {
            return null;
        }
        return ProductItemOrderedDto.builder()
                .productId(productItemOrdered.getProductId())
                .productName(productItemOrdered.getProductName())
                .pictureUrl(productItemOrdered.getPictureUrl())
                .build();
    }

    // Map UserOrderAddress to UserOrderAddressDto
    public UserOrderAddressDto toUserOrderAddressDto(UserOrderAddress userOrderAddress) {
        if (userOrderAddress == null) {
            return null;
        }
        return UserOrderAddressDto.builder()
                .firstName(userOrderAddress.getFirstName())
                .lastName(userOrderAddress.getLastName())
                .street(userOrderAddress.getStreet())
                .city(userOrderAddress.getCity())
                .country(userOrderAddress.getCountry())
                .build();
    }

    // Map UserOrderAddressDto to UserOrderAddress
    public UserOrderAddress toUserOrderAddress(UserOrderAddressDto userOrderAddressDto) {
        if (userOrderAddressDto == null) {
            return null;
        }
        return UserOrderAddress.builder()
                .firstName(userOrderAddressDto.getFirstName())
                .lastName(userOrderAddressDto.getLastName())
                .street(userOrderAddressDto.getStreet())
                .city(userOrderAddressDto.getCity())
                .country(userOrderAddressDto.getCountry())
                .build();
    }

    // Map DeliveryMethod to DeliveryMethodDto
    public DeliveryMethodDto toDeliveryMethodDto(DeliveryMethod deliveryMethod) {
        if (deliveryMethod == null) {
            return null;
        }
        return DeliveryMethodDto.builder()
                .id(deliveryMethod.getId())
                .shortName(deliveryMethod.getShortName())
                .description(deliveryMethod.getDescription())
                .deliveryTime(deliveryMethod.getDeliveryTime())
                .cost(deliveryMethod.getCost())
                .build();
    }
}