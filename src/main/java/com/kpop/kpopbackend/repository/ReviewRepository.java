package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Integer> {

    List<Review> findByProductId(int productId);

}