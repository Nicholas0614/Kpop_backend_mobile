package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return service.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {

        Product product = service.getProductById(id);

        if (product == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {

        return ResponseEntity.ok(service.addProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int id,
            @RequestBody Product product) {

        Product updated = service.updateProduct(id, product);

        if (updated == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {

        if (!service.deleteProduct(id))
            return ResponseEntity.badRequest().body("Product not found");

        return ResponseEntity.ok("Product deleted successfully");
    }

}