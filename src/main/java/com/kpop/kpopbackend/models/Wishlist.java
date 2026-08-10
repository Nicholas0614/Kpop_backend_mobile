package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Wishlists")
@Data
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id")
    private int userId;


    @Column(name = "product_id")
    private int productId;

    private String createdDate;

}
