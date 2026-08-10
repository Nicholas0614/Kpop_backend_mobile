package com.kpop.kpopbackend.controllers;


import com.kpop.kpopbackend.dto.CartResponse;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.services.CartService;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/cart")
public class CartController {



    private final CartService service;



    public CartController(
            CartService service
    ){

        this.service = service;

    }




    // Add product to cart
    @PostMapping
    public Cart addCart(
            @RequestBody Cart cart
    ){

        return service.addCart(cart);

    }




    // Get cart by user
    @GetMapping("/user/{userId}")
    public List<CartResponse> getCart(
            @PathVariable int userId
    ){

        return service.getCartByUser(userId);

    }





    // Update quantity
    @PutMapping("/{id}")
    public Cart updateCart(
            @PathVariable int id,
            @RequestBody Cart cart
    ){

        return service.updateCart(id, cart);

    }





    // Delete cart item
    @DeleteMapping("/{id}")
    public String deleteCart(
            @PathVariable int id
    ){

        service.deleteCart(id);

        return "Cart item deleted";

    }


}