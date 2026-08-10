package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin("*")
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

        if (product == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(product);
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody Product product) {
        Product created = service.addProduct(product);

        if (created == null) return ResponseEntity.badRequest().body("Invalid product, category, group or sale price");

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Product product) {
        Product updated = service.updateProduct(id, product);

        if (updated == null) return ResponseEntity.badRequest().body("Product not found or invalid data");

        return ResponseEntity.ok(updated);
    }

    @GetMapping("/sales")
    public List<Product> getSaleProducts() {
        return service.getSaleProducts();
    }

    @GetMapping("/search")
    public List<Product> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer categoryId,
            @RequestParam(required = false) Integer groupId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) String sort) {

        return service.searchProducts(keyword, categoryId, groupId, minPrice, maxPrice, sort);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable int id) {
        if (!service.deleteProduct(id)) return ResponseEntity.badRequest().body("Product not found");

        return ResponseEntity.ok("Product deleted successfully");
    }
}