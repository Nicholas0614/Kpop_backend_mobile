package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Integer> {

    List<ProductVariant> findByProductId(int productId);
}