package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Category;
import com.kpop.kpopbackend.models.KpopGroup;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.repository.CategoryRepository;
import com.kpop.kpopbackend.repository.KpopGroupRepository;
import com.kpop.kpopbackend.repository.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;
    private final KpopGroupRepository groupRepository;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository,
                          KpopGroupRepository groupRepository) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.groupRepository = groupRepository;
    }

    public List<Product> getAllProducts() {
        return repository.findAll();
    }

    public Product getProductById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Product addProduct(Product product) {
        if (product.getPrice() == null || product.getPrice() < 0) return null;

        if (product.isOnSale() && (product.getSalePrice() == null || product.getSalePrice() < 0 ||
                product.getSalePrice() >= product.getPrice())) return null;

        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId()).orElse(null);

            if (category == null) return null;

            product.setCategory(category);
        }

        if (product.getGroup() != null) {
            KpopGroup group = groupRepository.findById(product.getGroup().getId()).orElse(null);

            if (group == null) return null;

            product.setGroup(group);
        }

        return repository.save(product);
    }

    public Product updateProduct(int id, Product product) {
        Product existing = repository.findById(id).orElse(null);

        if (existing == null) return null;
        if (product.getPrice() == null || product.getPrice() < 0) return null;

        if (product.isOnSale() && (product.getSalePrice() == null || product.getSalePrice() < 0 ||
                product.getSalePrice() >= product.getPrice())) return null;

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setOnSale(product.isOnSale());
        existing.setSalePrice(product.getSalePrice());
        existing.setRating(product.getRating());
        existing.setQuantity(product.getQuantity());
        existing.setImage(product.getImage());

        if (product.getCategory() != null) {
            Category category = categoryRepository.findById(product.getCategory().getId()).orElse(null);

            if (category == null) return null;

            existing.setCategory(category);
        }

        if (product.getGroup() != null) {
            KpopGroup group = groupRepository.findById(product.getGroup().getId()).orElse(null);

            if (group == null) return null;

            existing.setGroup(group);
        }

        return repository.save(existing);
    }

    public List<Product> getSaleProducts() {
        return repository.findByOnSaleTrue();
    }

    public List<Product> searchProducts(String keyword, Integer categoryId, Integer groupId,
                                        Double minPrice, Double maxPrice, String sort) {
        Sort sortOrder = Sort.unsorted();

        if ("priceAsc".equals(sort)) sortOrder = Sort.by("price").ascending();
        if ("priceDesc".equals(sort)) sortOrder = Sort.by("price").descending();
        if ("ratingDesc".equals(sort)) sortOrder = Sort.by("rating").descending();
        if ("nameAsc".equals(sort)) sortOrder = Sort.by("name").ascending();

        if (keyword != null && keyword.trim().isEmpty()) keyword = null;

        return repository.searchProducts(keyword, categoryId, groupId, minPrice, maxPrice, sortOrder);
    }

    public boolean deleteProduct(int id) {
        if (!repository.existsById(id)) return false;

        repository.deleteById(id);
        return true;
    }
}