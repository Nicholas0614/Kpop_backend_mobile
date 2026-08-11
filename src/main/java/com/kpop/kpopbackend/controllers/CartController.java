package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.dto.CartResponse;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.services.CartService;
import com.kpop.kpopbackend.services.CurrentUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService service;
    private final CurrentUserService currentUserService;

    public CartController(CartService service, CurrentUserService currentUserService) {
        this.service = service;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<?> addCart(@RequestBody Cart cart, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, cart.getUserId())) {
            return ResponseEntity.status(403).body("You cannot modify another user's cart");
        }

        Cart result = service.addCart(cart);

        if (result == null) return ResponseEntity.badRequest().body("Invalid product, variant, quantity or stock");

        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getCart(@PathVariable int userId, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot access another user's cart");
        }

        List<CartResponse> cart = service.getCartByUser(userId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCart(@PathVariable int id, @RequestBody Cart cart, Authentication authentication) {
        if (!currentUserService.canAccessCart(authentication, id)) {
            return ResponseEntity.status(403).body("You cannot modify another user's cart");
        }

        Cart result = service.updateCart(id, cart);

        if (result == null) return ResponseEntity.badRequest().body("Invalid quantity or stock");

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable int id, Authentication authentication) {
        if (!currentUserService.canAccessCart(authentication, id)) {
            return ResponseEntity.status(403).body("You cannot delete another user's cart item");
        }

        service.deleteCart(id);
        return ResponseEntity.ok("Cart item deleted");
    }
}