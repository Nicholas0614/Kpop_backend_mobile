package com.kpop.kpopbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_images")
@Data
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
}