package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.ProductImage;
import com.kpop.kpopbackend.repository.ProductImageRepository;
import com.kpop.kpopbackend.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductImageService {

    private final ProductImageRepository imageRepository;
    private final ProductRepository productRepository;

    public ProductImageService(ProductImageRepository imageRepository, ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    public List<ProductImage> getImagesByProduct(int productId) {
        return imageRepository.findByProductId(productId);
    }

    public ProductImage addImage(int productId, ProductImage productImage) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) return null;

        productImage.setProduct(product);

        return imageRepository.save(productImage);
    }

    public boolean deleteImage(int id) {
        if (!imageRepository.existsById(id)) return false;

        imageRepository.deleteById(id);
        return true;
    }
}