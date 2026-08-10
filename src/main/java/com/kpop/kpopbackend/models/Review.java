package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name="user_id")
    private int userId;


    @Column(name="product_id")
    private int productId;

    private float rating;

    private String comment;

    @Column(name = "review_date")
    private String date;
}
