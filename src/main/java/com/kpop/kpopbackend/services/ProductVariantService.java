package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.ProductVariant;
import com.kpop.kpopbackend.repository.ProductRepository;
import com.kpop.kpopbackend.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;

    public ProductVariantService(ProductVariantRepository variantRepository, ProductRepository productRepository) {
        this.variantRepository = variantRepository;
        this.productRepository = productRepository;
    }

    public List<ProductVariant> getVariantsByProduct(int productId) {
        return variantRepository.findByProductId(productId);
    }

    public ProductVariant getVariantById(int id) {
        return variantRepository.findById(id).orElse(null);
    }

    public ProductVariant addVariant(int productId, ProductVariant variant) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) return null;
        if (variant.getPrice() == null || variant.getPrice() < 0 || variant.getQuantity() < 0) return null;

        if (variant.isOnSale() && (variant.getSalePrice() == null || variant.getSalePrice() < 0 ||
                variant.getSalePrice() >= variant.getPrice())) return null;

        variant.setProduct(product);

        return variantRepository.save(variant);
    }

    public ProductVariant updateVariant(int id, ProductVariant variant) {
        ProductVariant existing = variantRepository.findById(id).orElse(null);

        if (existing == null) return null;
        if (variant.getPrice() == null || variant.getPrice() < 0 || variant.getQuantity() < 0) return null;

        if (variant.isOnSale() && (variant.getSalePrice() == null || variant.getSalePrice() < 0 ||
                variant.getSalePrice() >= variant.getPrice())) return null;

        existing.setName(variant.getName());
        existing.setPrice(variant.getPrice());
        existing.setOnSale(variant.isOnSale());
        existing.setSalePrice(variant.getSalePrice());
        existing.setQuantity(variant.getQuantity());
        existing.setImage(variant.getImage());

        return variantRepository.save(existing);
    }

    public boolean deleteVariant(int id) {
        if (!variantRepository.existsById(id)) return false;

        variantRepository.deleteById(id);
        return true;
    }
}