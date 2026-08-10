package com.kpop.kpopbackend.controllers;

import com.kpop.kpopbackend.models.Category;
import com.kpop.kpopbackend.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @GetMapping
    public List<Category> getAllCategories() {
        return service.getAllCategories();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable int id) {
        Category category = service.getCategoryById(id);

        if (category == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(category);
    }

    @PostMapping
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        Category created = service.addCategory(category);

        if (created == null) return ResponseEntity.badRequest().body("Category already exists");

        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody Category category) {
        Category updated = service.updateCategory(id, category);

        if (updated == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        if (!service.deleteCategory(id)) return ResponseEntity.badRequest().body("Category not found");

        return ResponseEntity.ok("Category deleted successfully");
    }
}