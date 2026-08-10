package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Coupon;
import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.services.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    private final AdminService service;

    public AdminController(AdminService service) {
        this.service = service;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> getDashboard() {
        return service.getDashboard();
    }

    @GetMapping("/users")
    public List<User> getUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/products")
    public List<Product> getProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/orders")
    public List<Order> getOrders() {
        return service.getAllOrders();
    }

    @GetMapping("/coupons")
    public List<Coupon> getCoupons() {
        return service.getAllCoupons();
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<Object> updateOrderStatus(@PathVariable int id, @RequestParam String status) {
        return service.updateOrderStatus(id, status);
    }

    @PutMapping("/orders/{id}/tracking")
    public ResponseEntity<Object> updateTrackingNumber(@PathVariable int id, @RequestParam String trackingNumber) {
        return service.updateTrackingNumber(id, trackingNumber);
    }

    @PutMapping("/products/{id}/sale")
    public ResponseEntity<Object> updateProductSale(@PathVariable int id, @RequestParam boolean onSale,
                                                    @RequestParam(required = false) Double salePrice) {
        return service.updateProductSale(id, onSale, salePrice);
    }

    @PutMapping("/variants/{id}/sale")
    public ResponseEntity<Object> updateVariantSale(@PathVariable int id, @RequestParam boolean onSale,
                                                    @RequestParam(required = false) Double salePrice) {
        return service.updateVariantSale(id, onSale, salePrice);
    }
}