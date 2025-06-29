package com.ECom.ECom.repository;

import com.ECom.ECom.entity.order.DeliveryMethod;
import com.ECom.ECom.entity.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByBuyerEmailOrderByOrderDateDesc(String buyerEmail);

    Optional<Order> findByPaymentIntentId(String paymentIntentId);

    Optional<DeliveryMethod> findDeliveryMethodById(Long id);

    Optional<Order> findByItemsId(Long orderItemId);

    Optional<Order> findByIdAndBuyerEmail(Long id, String buyerEmail);
}
