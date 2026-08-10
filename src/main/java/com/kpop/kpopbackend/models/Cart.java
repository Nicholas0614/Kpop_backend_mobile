package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="cart")
public class Cart {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name="user_id")
    private int userId;


    @Column(name="product_id")
    private int productId;


    private int quantity;

    private Integer variantId;

}