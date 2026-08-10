package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.ProductImage;
import com.kpop.kpopbackend.services.ProductImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product-images")
@CrossOrigin("*")
public class ProductImageController {

    private final ProductImageService service;

    public ProductImageController(ProductImageService service) {
        this.service = service;
    }

    @GetMapping("/product/{productId}")
    public List<ProductImage> getImages(@PathVariable int productId) {
        return service.getImagesByProduct(productId);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<?> addImage(@PathVariable int productId, @RequestBody ProductImage productImage) {
        ProductImage created = service.addImage(productId, productImage);

        if (created == null) return ResponseEntity.badRequest().body("Product not found");

        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteImage(@PathVariable int id) {
        if (!service.deleteImage(id)) return ResponseEntity.badRequest().body("Image not found");

        return ResponseEntity.ok("Image deleted successfully");
    }
}