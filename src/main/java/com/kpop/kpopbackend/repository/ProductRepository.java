package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
