package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Address;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.repository.AddressRepository;
import com.kpop.kpopbackend.repository.CartRepository;
import com.kpop.kpopbackend.repository.OrderRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;

    public CurrentUserService(CartRepository cartRepository, AddressRepository addressRepository, OrderRepository orderRepository) {
        this.cartRepository = cartRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
    }

    public int getUserId(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        assert jwt != null;
        Number userId = jwt.getClaim("userId");
        assert userId != null;
        return userId.intValue();
    }

    public boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    public boolean canAccessUser(Authentication authentication, int userId) {
        return isAdmin(authentication) || getUserId(authentication) == userId;
    }

    public boolean canAccessCart(Authentication authentication, int cartId) {
        if (isAdmin(authentication)) return true;

        Cart cart = cartRepository.findById(cartId).orElse(null);
        return cart != null && cart.getUserId() == getUserId(authentication);
    }

    public boolean canAccessAddress(Authentication authentication, int addressId) {
        if (isAdmin(authentication)) return true;

        Address address = addressRepository.findById(addressId).orElse(null);
        return address != null && address.getUserId() == getUserId(authentication);
    }

    public boolean canAccessOrder(Authentication authentication, int orderId) {
        if (isAdmin(authentication)) return true;

        Order order = orderRepository.findById(orderId).orElse(null);
        return order != null && order.getUserId() == getUserId(authentication);
    }

    public boolean canAccessPaypalOrder(Authentication authentication, String paypalOrderId) {
        if (isAdmin(authentication)) return true;

        Order order = orderRepository.findByPaypalOrderId(paypalOrderId).orElse(null);
        return order != null && order.getUserId() == getUserId(authentication);
    }
}