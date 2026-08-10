package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.CartResponse;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.ProductVariant;
import com.kpop.kpopbackend.repository.CartRepository;
import com.kpop.kpopbackend.repository.ProductRepository;
import com.kpop.kpopbackend.repository.ProductVariantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, ProductVariantRepository variantRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
    }

    public Cart addCart(Cart cart) {
        Product product = productRepository.findById(cart.getProductId()).orElse(null);

        if (product == null || cart.getQuantity() <= 0) return null;

        int availableStock = product.getQuantity();

        if (cart.getVariantId() != null) {
            ProductVariant variant = variantRepository.findById(cart.getVariantId()).orElse(null);

            if (variant == null || variant.getProduct().getId() != product.getId()) return null;

            availableStock = variant.getQuantity();
        }

        Cart existing = cartRepository.findByUserIdAndProductIdAndVariantId(
                cart.getUserId(), cart.getProductId(), cart.getVariantId());

        if (existing != null) {
            int newQuantity = existing.getQuantity() + cart.getQuantity();

            if (newQuantity > availableStock) return null;

            existing.setQuantity(newQuantity);

            return cartRepository.save(existing);
        }

        if (cart.getQuantity() > availableStock) return null;

        return cartRepository.save(cart);
    }

    public List<CartResponse> getCartByUser(int userId) {
        List<Cart> carts = cartRepository.findByUserId(userId);

        return carts.stream().map(cart -> {
            Product product = productRepository.findById(cart.getProductId()).orElse(null);
            ProductVariant variant = cart.getVariantId() != null ? variantRepository.findById(cart.getVariantId()).orElse(null) : null;

            CartResponse response = new CartResponse();

            response.setId(cart.getId());
            response.setUserId(cart.getUserId());
            response.setProductId(cart.getProductId());
            response.setVariantId(cart.getVariantId());
            response.setQuantity(cart.getQuantity());

            if (product != null) {
                double originalPrice = variant != null ? variant.getPrice() : product.getPrice();
                double finalPrice = originalPrice;
                boolean onSale = false;

                if (variant != null && variant.isOnSale() && variant.getSalePrice() != null) {
                    finalPrice = variant.getSalePrice();
                    onSale = true;
                } else if (product.isOnSale() && product.getSalePrice() != null) {
                    finalPrice = product.getSalePrice();
                    onSale = true;
                }

                response.setName(product.getName());
                response.setCategory(product.getCategory() != null ? product.getCategory().getName() : null);
                response.setOriginalPrice(originalPrice);
                response.setPrice(finalPrice);
                response.setOnSale(onSale);
                response.setRating(product.getRating());
                response.setImage(variant != null && variant.getImage() != null ? variant.getImage() : product.getImage());
            }

            if (variant != null) response.setVariantName(variant.getName());

            return response;
        }).toList();
    }

    public Cart updateCart(int id, Cart cart) {
        Cart existing = cartRepository.findById(id).orElse(null);

        if (existing == null || cart.getQuantity() <= 0) return null;

        Product product = productRepository.findById(existing.getProductId()).orElse(null);

        if (product == null) return null;

        int availableStock = product.getQuantity();

        if (existing.getVariantId() != null) {
            ProductVariant variant = variantRepository.findById(existing.getVariantId()).orElse(null);

            if (variant == null) return null;

            availableStock = variant.getQuantity();
        }

        if (cart.getQuantity() > availableStock) return null;

        existing.setQuantity(cart.getQuantity());

        return cartRepository.save(existing);
    }

    public void deleteCart(int id) {
        cartRepository.deleteById(id);
    }
}