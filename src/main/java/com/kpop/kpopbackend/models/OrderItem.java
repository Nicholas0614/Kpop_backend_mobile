package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "order_items")
public class OrderItem {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "product_id")
    private int productId;


    private String productName;


    private int quantity;


    private double price;


    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}