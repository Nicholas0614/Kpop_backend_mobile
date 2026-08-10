package com.kpop.kpopbackend.repository;

import com.kpop.kpopbackend.models.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart,Integer> {

    List<Cart> findByUserId(int userId);

    Cart findByUserIdAndProductId(int userId,int productId);

}