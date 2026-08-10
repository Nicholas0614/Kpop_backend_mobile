package com.kpop.kpopbackend.dto;

import lombok.Data;

@Data
public class ReviewResponse {

    private int id;

    private int userId;

    private String userName;

    private int productId;

    private float rating;

    private String comment;

    private String date;

}