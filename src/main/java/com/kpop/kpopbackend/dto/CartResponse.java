package com.kpop.kpopbackend.dto;

import lombok.Data;

@Data
public class CartResponse {

    private int id;

    private int userId;

    private int productId;

    private String name;

    private String category;

    private double price;

    private double rating;

    private String image;

    private int quantity;
}