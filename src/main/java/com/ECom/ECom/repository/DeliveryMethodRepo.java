package com.ECom.ECom.repository;

import com.ECom.ECom.entity.order.DeliveryMethod;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryMethodRepo extends JpaRepository<DeliveryMethod, Long> {
}