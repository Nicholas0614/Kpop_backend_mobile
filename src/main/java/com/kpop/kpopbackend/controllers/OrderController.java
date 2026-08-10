package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.services.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // Add single order
    @PostMapping
    public ResponseEntity<Object> addOrder(@RequestBody Order order){

        return orderService.addOrder(order);

    }

    // Get order history by user
    @GetMapping("/user/{userId}")
    public ResponseEntity<Object> getUserOrders(@PathVariable int userId){

        return orderService.getOrdersByUser(userId);

    }

    // Checkout cart -> create pending order
    @PostMapping("/checkout/{userId}")
    public ResponseEntity<Object> checkout(
            @PathVariable int userId,
            @RequestParam(required = false) String couponCode
    ){

        return orderService.checkout(userId, couponCode);

    }

    // SAVE PAYPAL ORDER ID
    @PostMapping("/{orderId}/paypal")
    public ResponseEntity<Object> savePaypalOrderId(
            @PathVariable int orderId,
            @RequestParam String paypalOrderId
    ){

        return orderService.savePaypalOrderId(orderId, paypalOrderId);

    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOrder(@PathVariable int id){

        return orderService.deleteOrder(id);

    }

}