package com.kpop.kpopbackend.services;


import com.kpop.kpopbackend.dto.CartResponse;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.repository.CartRepository;
import com.kpop.kpopbackend.repository.ProductRepository;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {


    private final CartRepository cartRepository;

    private final ProductRepository productRepository;


    public CartService(
            CartRepository cartRepository,
            ProductRepository productRepository
    ){
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }



    // Add item to cart
    // Add item to cart
    public Cart addCart(Cart cart){

        Product product = productRepository
                .findById(cart.getProductId())
                .orElse(null);

        if(product == null){
            return null;
        }

        if(cart.getQuantity() <= 0){
            return null;
        }

        Cart existing = cartRepository.findByUserIdAndProductId(
                cart.getUserId(),
                cart.getProductId()
        );

        if(existing != null){

            int newQuantity = existing.getQuantity() + cart.getQuantity();

            if(newQuantity > product.getQuantity()){
                return null;
            }

            existing.setQuantity(newQuantity);

            return cartRepository.save(existing);

        }

        if(cart.getQuantity() > product.getQuantity()){
            return null;
        }

        return cartRepository.save(cart);

    }



    // Get user's cart with product details
    public List<CartResponse> getCartByUser(int userId){


        List<Cart> carts =
                cartRepository.findByUserId(userId);


        return carts.stream()
                .map(cart -> {


                    Product product =
                            productRepository
                                    .findById(cart.getProductId())
                                    .orElse(null);


                    CartResponse response =
                            new CartResponse();


                    response.setId(cart.getId());

                    response.setUserId(cart.getUserId());

                    response.setProductId(cart.getProductId());

                    response.setQuantity(cart.getQuantity());



                    if(product != null){

                        response.setName(product.getName());

                        response.setCategory(product.getCategory());

                        response.setPrice(product.getPrice());

                        response.setRating(product.getRating());

                        response.setImage(product.getImage());

                    }


                    return response;


                })
                .toList();

    }



    // Update quantity
    public Cart updateCart(
            int id,
            Cart cart
    ){

        Cart existing =
                cartRepository
                        .findById(id)
                        .orElse(null);


        if(existing == null){
            return null;
        }


        Product product =
                productRepository
                        .findById(existing.getProductId())
                        .orElse(null);


        if(product == null){
            return null;
        }


        if(cart.getQuantity() > product.getQuantity()){

            return null;

        }

        if(cart.getQuantity() <= 0){

            return null;

        }


        existing.setQuantity(
                cart.getQuantity()
        );


        return cartRepository.save(existing);

    }



    // Delete cart item
    public void deleteCart(int id){

        cartRepository.deleteById(id);

    }

}