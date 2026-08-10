package com.kpop.kpopbackend.repository;


import com.kpop.kpopbackend.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findByUserId(int userId);

    Optional<Order> findByPaypalOrderId(String paypalOrderId);


}