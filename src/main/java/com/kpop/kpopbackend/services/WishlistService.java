package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.WishlistResponse;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.Wishlist;
import com.kpop.kpopbackend.repository.ProductRepository;
import com.kpop.kpopbackend.repository.WishlistRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.ObjectError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;

    public WishlistService(
            WishlistRepository wishlistRepository,
            ProductRepository productRepository
    ){

        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;

    }

    public ResponseEntity<Object> addWishlist(int userId,int productId){

        Product product = productRepository.findById(productId).orElse(null);

        if (product == null){
            return ResponseEntity.badRequest().body("Product not found");

        }

        if(wishlistRepository.existsByUserIdAndProductId(userId,productId)){

        return ResponseEntity.badRequest().body("Product is already inside wishlist");

        }

        Wishlist wishlist = new Wishlist();

        wishlist.setUserId(userId);
        wishlist.setProductId(productId);
        wishlist.setCreatedDate(LocalDate.now().toString());

        wishlistRepository.save(wishlist);

        return ResponseEntity.ok(wishlist);

    }

    public ResponseEntity<Object> getWishlist(int userId){

        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);

        List<WishlistResponse> wishlistResponseList = new ArrayList<>();

        for (Wishlist wishlist : wishlists){

            Product product = productRepository.findById(wishlist.getProductId()).orElse(null);

            if(product == null){
                continue;
            }

            WishlistResponse dto = new WishlistResponse();

            dto.setWishlistId(wishlist.getId());
            dto.setProductId(product.getId());
            dto.setName(product.getName());
            dto.setPrice(product.getPrice());
            dto.setCategory(product.getCategory());
            dto.setImage(product.getImage());

            wishlistResponseList.add(dto);



        }

        return ResponseEntity.ok(wishlistResponseList);
    }

    public ResponseEntity<Object> deleteWishlist(int userId, int productId){

        Wishlist wishlist = wishlistRepository.findByUserIdAndProductId(userId,productId).orElse(null);

        if(wishlist == null){
            return ResponseEntity.badRequest().body("Wishlist not found");
        }

        wishlistRepository.delete(wishlist);

        return ResponseEntity.ok("Product removed from wishlist successfully");
    }

}




