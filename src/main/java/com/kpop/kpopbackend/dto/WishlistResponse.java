package com.kpop.kpopbackend.dto;

import lombok.Data;

@Data
public class WishlistResponse {

    private int productId;

    private int wishlistId;

    private String name;

    private String category;

    private Double price;

    private String image;

}
