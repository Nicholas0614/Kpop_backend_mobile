package com.kpop.kpopbackend.dto;

import lombok.Data;

@Data
public class OrderResponse {

    private int id;


    private int userId;


    private int productId;


    private String name;


    private String category;


    private double price;


    private String image;


    private int quantity;


    private double totalPrice;


    private String date;

}