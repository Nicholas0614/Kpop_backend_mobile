package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Category;
import com.kpop.kpopbackend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAllCategories() {
        return repository.findAll();
    }

    public Category getCategoryById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Category addCategory(Category category) {
        if (repository.findByNameIgnoreCase(category.getName()).isPresent()) return null;

        return repository.save(category);
    }

    public Category updateCategory(int id, Category category) {
        Category existing = repository.findById(id).orElse(null);

        if (existing == null) return null;

        existing.setName(category.getName());

        return repository.save(existing);
    }

    public boolean deleteCategory(int id) {
        if (!repository.existsById(id)) return false;

        repository.deleteById(id);
        return true;
    }
}