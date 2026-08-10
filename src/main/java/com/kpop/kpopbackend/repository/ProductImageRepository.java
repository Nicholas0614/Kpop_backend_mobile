package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    List<ProductImage> findByProductId(int productId);
}