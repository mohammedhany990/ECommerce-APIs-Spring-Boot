package com.ECom.ECom.controller;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ECom.ECom.dto.order.CreateOrderRequestDto;
import com.ECom.ECom.dto.order.DeliveryMethodDto;
import com.ECom.ECom.dto.order.OrderDto;
import com.ECom.ECom.entity.order.Order;
import com.ECom.ECom.entity.order.UserOrderAddress;
import com.ECom.ECom.helper.ApiResponse;
import com.ECom.ECom.helper.OrderMapper;
import com.ECom.ECom.service.OrderService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDto>> createOrder(@Valid @RequestBody CreateOrderRequestDto request) {
        try {
           UserOrderAddress shippingAddress= orderMapper.toUserOrderAddress(request.getShippingAddress());

            Order order = orderService.createOrder(
                    request.getBuyerEmail(),
                    request.getBasketId(),
                    request.getDeliveryMethodId(),
                    shippingAddress);
           
            OrderDto orderDto = orderMapper.toOrderDto(order);
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(true)
                    .message("Order created successfully")
                    .data(orderDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderDto>>> getOrdersForUser(@RequestParam @Valid @Email String buyerEmail) {
        try {
            List<OrderDto> orders = orderService.getOrdersForUser(buyerEmail)
                    .stream()
                    .map(orderMapper::toOrderDto)
                    .collect(Collectors.toList());
            ApiResponse<List<OrderDto>> response = ApiResponse.<List<OrderDto>>builder()
                    .success(true)
                    .message("Orders retrieved successfully")
                    .data(orders)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ApiResponse<List<OrderDto>> response = ApiResponse.<List<OrderDto>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDto>> getOrderByIdForUser(@PathVariable Long id, @RequestParam @Valid @Email String buyerEmail) {
        try {
            Order order = orderService.getOrderByIdForUser(buyerEmail, id);
            OrderDto orderDto = orderMapper.toOrderDto(order);
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(true)
                    .message("Order retrieved successfully")
                    .data(orderDto)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            ApiResponse<OrderDto> response = ApiResponse.<OrderDto>builder()
                    .success(false)
                    .message(e.getMessage())
                    .data(null)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delivery-methods")
    public ResponseEntity<ApiResponse<List<DeliveryMethodDto>>> getDeliveryMethods() {
        List<DeliveryMethodDto> deliveryMethods = orderService.getDeliveryMethods()
                .stream()
                .map(orderMapper::toDeliveryMethodDto)
                .collect(Collectors.toList());
        ApiResponse<List<DeliveryMethodDto>> response = ApiResponse.<List<DeliveryMethodDto>>builder()
                .success(true)
                .message("Delivery methods retrieved successfully")
                .data(deliveryMethods)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}