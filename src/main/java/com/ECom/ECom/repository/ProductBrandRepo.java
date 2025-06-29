package com.ECom.ECom.repository;
import com.ECom.ECom.entity.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductBrandRepo extends JpaRepository<ProductBrand, Long> {

}
