package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Object> addOrder(@RequestBody Order order) {
        return orderService.addOrder(order);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserOrders(@PathVariable int userId) {
        return orderService.getOrdersByUser(userId);
    }

    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Object> checkout(@PathVariable int userId, @RequestParam int addressId,
                                           @RequestParam(required = false) String couponCode) {
        return orderService.checkout(userId, addressId, couponCode);
    }

    @PostMapping("/{orderId}/paypal")
    public ResponseEntity<Object> savePaypalOrderId(@PathVariable int orderId, @RequestParam String paypalOrderId) {
        return orderService.savePaypalOrderId(orderId, paypalOrderId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable int id) {
        return orderService.deleteOrder(id);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @PutMapping("/{id}/tracking")
    public ResponseEntity<Object> updateTrackingNumber(@PathVariable int id, @RequestParam String trackingNumber) {
        return orderService.updateTrackingNumber(id, trackingNumber);
    }
}