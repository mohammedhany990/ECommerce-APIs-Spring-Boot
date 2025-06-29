package com.ECom.ECom.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_brand")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBrand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Brand name cannot be blank")
    @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    private String brandName;

    @Size(max = 500, message = "Brand description cannot exceed 500 characters")
    private String brandDescription;
}