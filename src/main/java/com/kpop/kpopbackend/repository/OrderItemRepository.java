package com.kpop.kpopbackend.repository;


import com.kpop.kpopbackend.models.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderItemRepository
        extends JpaRepository<OrderItem,Integer> {


}