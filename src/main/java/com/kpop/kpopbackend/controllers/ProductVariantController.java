package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.ProductVariant;
import com.kpop.kpopbackend.services.ProductVariantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/variants")
@CrossOrigin("*")
public class ProductVariantController {

    private final ProductVariantService service;

    public ProductVariantController(ProductVariantService service) {
        this.service = service;
    }

    @GetMapping("/product/{productId}")
    public List<ProductVariant> getVariantsByProduct(@PathVariable int productId) {
        return service.getVariantsByProduct(productId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVariant(@PathVariable int id) {
        ProductVariant variant = service.getVariantById(id);

        if (variant == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(variant);
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<?> addVariant(@PathVariable int productId, @RequestBody ProductVariant variant) {
        ProductVariant created = service.addVariant(productId, variant);

        if (created == null) return ResponseEntity.badRequest().body("Invalid product or variant");

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateVariant(@PathVariable int id, @RequestBody ProductVariant variant) {
        ProductVariant updated = service.updateVariant(id, variant);

        if (updated == null) return ResponseEntity.badRequest().body("Variant not found or invalid");

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteVariant(@PathVariable int id) {
        if (!service.deleteVariant(id)) return ResponseEntity.badRequest().body("Variant not found");

        return ResponseEntity.ok("Variant deleted successfully");
    }
}