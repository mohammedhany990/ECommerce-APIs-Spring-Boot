package com.ECom.ECom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ECom.ECom.entity.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {


}
