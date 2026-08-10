package com.kpop.kpopbackend.dto;

import lombok.Data;

@Data
public class CartResponse {

    private int id;
    private int userId;
    private int productId;
    private Integer variantId;
    private String variantName;

    private String name;
    private String category;

    private Double originalPrice;
    private Double price;
    private boolean onSale;

    private Double rating;
    private String image;
    private int quantity;
}