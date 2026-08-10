package com.kpop.kpopbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product_variants")
@Data
public class ProductVariant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private Double price;

    private boolean onSale;
    private Double salePrice;

    private int quantity;
    private String image;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    @JsonIgnore
    private Product product;
}