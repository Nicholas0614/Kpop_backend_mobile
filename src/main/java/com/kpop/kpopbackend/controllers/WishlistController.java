package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.services.CurrentUserService;
import com.kpop.kpopbackend.services.WishlistService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;
    private final CurrentUserService currentUserService;

    public WishlistController(WishlistService wishlistService, CurrentUserService currentUserService) {
        this.wishlistService = wishlistService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<Object> addWishlist(@RequestParam int userId, @RequestParam int productId,
                                              Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot modify another user's wishlist");
        }

        return wishlistService.addWishlist(userId, productId);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getWishlist(@PathVariable int userId, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot access another user's wishlist");
        }

        return wishlistService.getWishlist(userId);
    }

    @DeleteMapping
    public ResponseEntity<Object> removeWishlist(@RequestParam int userId, @RequestParam int productId,
                                                 Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot modify another user's wishlist");
        }

        return wishlistService.deleteWishlist(userId, productId);
    }
}