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

    public CartController(CartService service) {
        this.service = service;
    }

    @PostMapping
    public Cart addCart(@RequestBody Cart cart) {
        return service.addCart(cart);
    }

    @GetMapping("/user/{userId}")
    public List<CartResponse> getCart(@PathVariable int userId) {
        return service.getCartByUser(userId);
    }

    @PutMapping("/{id}")
    public Cart updateCart(@PathVariable int id, @RequestBody Cart cart) {
        return service.updateCart(id, cart);
    }

    @DeleteMapping("/{id}")
    public String deleteCart(@PathVariable int id) {
        service.deleteCart(id);
        return "Cart item deleted";
    }
}