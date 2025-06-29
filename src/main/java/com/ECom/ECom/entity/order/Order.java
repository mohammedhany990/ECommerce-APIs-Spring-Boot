package com.ECom.ECom.entity.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "orders", indexes = {
        @Index(name = "idx_buyer_email", columnList = "buyerEmail"),
        @Index(name = "idx_order_date", columnList = "orderDate")
})
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String buyerEmail;

    @Embedded
    private UserOrderAddress shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_method_id")
    private DeliveryMethod deliveryMethod;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @ToString.Exclude // Exclude from toString
    @EqualsAndHashCode.Exclude // Exclude from equals and hashCode
    private Set<OrderItem> items = new HashSet<>();

    @Column(nullable = false)
    private OffsetDateTime orderDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false)
    private BigDecimal subTotal;

    @Column(nullable = false, name = "payment_intent_id")
    private String paymentIntentId;

    @PrePersist
    protected void onCreate() {
        if (orderDate == null) {
            orderDate = OffsetDateTime.now();
        }
    }
}