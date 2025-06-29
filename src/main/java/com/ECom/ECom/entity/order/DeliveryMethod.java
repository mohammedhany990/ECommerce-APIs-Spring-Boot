package com.ECom.ECom.entity.order;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "delivery_methods", indexes = {
        @Index(name = "idx_short_name", columnList = "shortName")
})
public class DeliveryMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String shortName;

    @Column
    private String description;

    @Column
    private String deliveryTime;

    @Column(nullable = false)
    private BigDecimal cost;
}