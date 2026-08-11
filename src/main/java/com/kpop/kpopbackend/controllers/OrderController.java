package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.services.CurrentUserService;
import com.kpop.kpopbackend.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final CurrentUserService currentUserService;

    public OrderController(OrderService orderService, CurrentUserService currentUserService) {
        this.orderService = orderService;
        this.currentUserService = currentUserService;
    }

    @PostMapping
    public ResponseEntity<Object> addOrder(@RequestBody Order order, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, order.getUserId())) {
            return ResponseEntity.status(403).body("You cannot create an order for another user");
        }

        return orderService.addOrder(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserOrders(@PathVariable int userId, Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot access another user's orders");
        }

        return orderService.getOrdersByUser(userId);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Object> checkout(@PathVariable int userId, @RequestParam int addressId,
                                           @RequestParam(required = false) String couponCode,
                                           Authentication authentication) {
        if (!currentUserService.canAccessUser(authentication, userId)) {
            return ResponseEntity.status(403).body("You cannot checkout for another user");
        }

        if (!currentUserService.canAccessAddress(authentication, addressId)) {
            return ResponseEntity.status(403).body("You cannot use this address");
        }

        return orderService.checkout(userId, addressId, couponCode);
    }

    @PostMapping("/{orderId}/paypal")
    public ResponseEntity<Object> savePaypalOrderId(@PathVariable int orderId, @RequestParam String paypalOrderId,
                                                    Authentication authentication) {
        if (!currentUserService.canAccessOrder(authentication, orderId)) {
            return ResponseEntity.status(403).body("You cannot modify this order");
        }

        return orderService.savePaypalOrderId(orderId, paypalOrderId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable int id, Authentication authentication) {
        if (!currentUserService.isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        return orderService.deleteOrder(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable int id, @RequestParam String status,
                                                    Authentication authentication) {
        if (!currentUserService.isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        return orderService.updateOrderStatus(id, status);
    }

    @PutMapping("/{id}/tracking")
    public ResponseEntity<Object> updateTrackingNumber(@PathVariable int id, @RequestParam String trackingNumber,
                                                       Authentication authentication) {
        if (!currentUserService.isAdmin(authentication)) {
            return ResponseEntity.status(403).body("Admin access required");
        }

        return orderService.updateTrackingNumber(id, trackingNumber);
    }
}