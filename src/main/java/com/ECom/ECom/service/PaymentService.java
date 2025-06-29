package com.ECom.ECom.service;

import java.math.BigDecimal;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ECom.ECom.entity.Basket;
import com.ECom.ECom.entity.BasketItem;
import com.ECom.ECom.entity.Product;
import com.ECom.ECom.entity.order.DeliveryMethod;
import com.ECom.ECom.entity.order.Order;
import com.ECom.ECom.entity.order.OrderStatus;
import com.ECom.ECom.repository.DeliveryMethodRepo;
import com.ECom.ECom.repository.OrderRepo;
import com.ECom.ECom.repository.ProductRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentUpdateParams;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final BasketService basketService;
    private final ProductRepo productRepository;
    private final OrderRepo orderRepository;
    private final DeliveryMethodRepo deliveryMethodRepository;

    @Value("${stripe.secret-key}")
    private String stripeSecretKey;

    @Transactional
    public Basket createOrUpdatePaymentIntent(String buyerEmail) {
        // 1. Set Stripe API Key
        Stripe.apiKey = stripeSecretKey;

        // 2. Get Basket
        Basket basket = basketService.getBasket(buyerEmail);
        if (basket == null) {
            return null;
        }

        // 3. Calculate Shipping Price
        BigDecimal shippingPrice = BigDecimal.ZERO;

        if (basket.getDeliveryMethodId() != null) {

            DeliveryMethod delivery = deliveryMethodRepository.findById(basket.getDeliveryMethodId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Delivery method not found: " + basket.getDeliveryMethodId()));

            shippingPrice = delivery.getCost();
            basket.setShippingPrice(shippingPrice);
        }

        // 4. Update Product Prices in Basket
        if (!basket.getItems().isEmpty()) {
            for (BasketItem item : basket.getItems()) {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new IllegalArgumentException("Product not found: " + item.getProductId()));
                if (item.getPrice().compareTo(product.getProductPrice()) != 0) {
                    item.setPrice(product.getProductPrice());
                }
            }
        }

        // 5. Calculate Subtotal
        BigDecimal subtotal = basket.getItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 6. Create or Update PaymentIntent
        PaymentIntent paymentIntent;
        try {
            if (basket.getPaymentIntentId() == null || basket.getPaymentIntentId().isEmpty()) {

                // Create new PaymentIntent
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(subtotal.add(shippingPrice).multiply(BigDecimal.valueOf(100)).longValue())
                        .setCurrency("usd")
                        .addAllPaymentMethodType(Collections.singletonList("card"))
                        .build();

                paymentIntent = PaymentIntent.create(params);

                basket.setPaymentIntentId(paymentIntent.getId());

                basket.setClientSecret(paymentIntent.getClientSecret());
            } else {

                // Update existing PaymentIntent
                PaymentIntentUpdateParams params = PaymentIntentUpdateParams.builder()
                        .setAmount(subtotal.add(shippingPrice).multiply(BigDecimal.valueOf(100)).longValue())
                        .build();

                paymentIntent = PaymentIntent.retrieve(basket.getPaymentIntentId());

                paymentIntent = paymentIntent.update(params);

                basket.setPaymentIntentId(paymentIntent.getId());

                basket.setClientSecret(paymentIntent.getClientSecret());
            }
        } catch (StripeException e) {
            throw new RuntimeException("Failed to create/update PaymentIntent: " + e.getMessage(), e);
        }

        // 7. Update Basket in Redis
        basketService.updateBasket(basket);

        return basket;
    }

    @Transactional
    public Basket confirmPayment(String buyerEmail, String paymentIntentId) {

        // 1. Set Stripe API Key
        Stripe.apiKey = stripeSecretKey;

        try {
            // 2. Verify PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);

            if (!"succeeded".equals(paymentIntent.getStatus())) {
                return null; // Payment not successful
            }

            // 3. Update order status
            Order order = updateOrderStatus(paymentIntentId, true);

            if (order == null) {
                return null; // Order not found
            }

            // 4. Get basket
            Basket basket = basketService.getBasket(buyerEmail);

            if (basket == null) {
                return null; // Basket not found
            }

            return basket;

        } catch (StripeException e) {
            throw new RuntimeException("Failed to confirm payment: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void handleWebhookEvent(Event event) {

        EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

        if (deserializer.getObject().isPresent()) {

            Object stripeObject = deserializer.getObject().get();

            if (stripeObject instanceof PaymentIntent paymentIntent) {

                String paymentIntentId = paymentIntent.getId();

                boolean isPaid = "succeeded".equals(paymentIntent.getStatus());

                updateOrderStatus(paymentIntentId, isPaid);
            }
        }
    }

    @Transactional
    public Order updateOrderStatus(String paymentIntentId, boolean isPaid) {

        Order order = orderRepository.findByPaymentIntentId(paymentIntentId)
                .orElse(null);

        if (order == null) {
            return null;
        }

        order.setStatus(isPaid ? OrderStatus.PAYMENT_RECEIVED : OrderStatus.PAYMENT_FAILED);
        
        orderRepository.save(order);

        return order;
    }
}