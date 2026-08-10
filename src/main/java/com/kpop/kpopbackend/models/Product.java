package com.kpop.kpopbackend.models;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "Product")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    private String name ;

    private String category;

    private Double price ;

    private String description ;

    private Double rating ;

    private int quantity;

    private String image;
}
