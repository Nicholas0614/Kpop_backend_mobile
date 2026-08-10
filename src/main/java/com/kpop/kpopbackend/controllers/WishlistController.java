package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.services.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    public WishlistController(WishlistService wishlistService) {
        this.wishlistService = wishlistService;
    }

    @PostMapping
    public ResponseEntity<Object> addWishlist(@RequestParam int userId, @RequestParam int productId) {
        return wishlistService.addWishlist(userId, productId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getWishlist(@PathVariable int userId) {
        return wishlistService.getWishlist(userId);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeWishlist(@RequestParam int userId, @RequestParam int productId) {
        return wishlistService.deleteWishlist(userId, productId);
    }
}