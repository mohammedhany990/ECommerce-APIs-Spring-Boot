package com.ECom.ECom.service;

import com.ECom.ECom.entity.Product;
import com.ECom.ECom.entity.order.*;
import com.ECom.ECom.exception.ResourceNotFoundException;
import com.ECom.ECom.repository.DeliveryMethodRepo;
import com.ECom.ECom.repository.OrderRepo;
import com.ECom.ECom.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final BasketService basketService;
    private final ProductRepo productRepository;
    private final DeliveryMethodRepo deliveryMethodRepository;
    private final OrderRepo orderRepository;
    private final PaymentService paymentService;

    @Transactional
    public Order createOrder(String buyerEmail, String basketId, Long deliveryMethodId, UserOrderAddress shippingAddress) {
        // 1. Get Basket
        var basket = basketService.getBasket(buyerEmail);
        if (basket == null) {
            throw new ResourceNotFoundException("Basket not found for email: " + buyerEmail);
        }

        // 2. Build Order Items
        Set<OrderItem> orderItems = basket.getItems()
                .stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));

                    ProductItemOrdered productItemOrdered = ProductItemOrdered.builder()
                            .productId(product.getId())
                            .productName(product.getProductName())
                            .pictureUrl(product.getProductImageUrl())
                            .build();

                    return OrderItem.builder()
                            .product(productItemOrdered)
                            .quantity(item.getQuantity())
                            .price(product.getProductPrice())
                            .build();
                }).collect(Collectors.toSet());

        // 3. Calculate Subtotal
        BigDecimal subtotal = orderItems.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 4. Get Delivery Method
        DeliveryMethod deliveryMethod = deliveryMethodRepository.findById(deliveryMethodId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery method not found: " + deliveryMethodId));

        // 5. Remove existing order with same PaymentIntentId
        orderRepository.findByPaymentIntentId(basket.getPaymentIntentId())
                .ifPresent(orderRepository::delete);

        // 6. Create / Update PaymentIntent
        paymentService.createOrUpdatePaymentIntent(basketId);

        // 7. Create new Order
        Order order = Order.builder()
                .buyerEmail(buyerEmail)
                .shippingAddress(shippingAddress)
                .deliveryMethod(deliveryMethod)
                .items(orderItems) // Set items here
                .subTotal(subtotal)
                .status(OrderStatus.PENDING)
                .paymentIntentId(basket.getPaymentIntentId())
                .build();

        // 8. Set the bidirectional relationship
        orderItems.forEach(item -> item.setOrder(order));

        // 9. Save to DB and return
        return orderRepository.save(order);
    }

    public List<Order> getOrdersForUser(String buyerEmail) {
        return orderRepository.findByBuyerEmailOrderByOrderDateDesc(buyerEmail);
    }

    public Order getOrderByIdForUser(String buyerEmail, Long orderId) {
        return orderRepository.findByIdAndBuyerEmail(orderId, buyerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));
    }

    public List<DeliveryMethod> getDeliveryMethods() {
        return deliveryMethodRepository.findAll();
    }
}