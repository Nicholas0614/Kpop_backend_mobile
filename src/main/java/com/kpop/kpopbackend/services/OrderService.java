package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.PaymentResponse;
import com.kpop.kpopbackend.models.Address;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.models.Coupon;
import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.models.OrderItem;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.ProductVariant;
import com.kpop.kpopbackend.repository.AddressRepository;
import com.kpop.kpopbackend.repository.CartRepository;
import com.kpop.kpopbackend.repository.OrderItemRepository;
import com.kpop.kpopbackend.repository.OrderRepository;
import com.kpop.kpopbackend.repository.ProductRepository;
import com.kpop.kpopbackend.repository.ProductVariantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PayPalService paypalService;
    private final CouponService couponService;
    private final OrderItemRepository orderItemRepository;
    private final ProductVariantRepository variantRepository;
    private final AddressRepository addressRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository,
                        CartRepository cartRepository, PayPalService paypalService,
                        CouponService couponService, OrderItemRepository orderItemRepository,
                        ProductVariantRepository variantRepository, AddressRepository addressRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.paypalService = paypalService;
        this.couponService = couponService;
        this.orderItemRepository = orderItemRepository;
        this.variantRepository = variantRepository;
        this.addressRepository = addressRepository;
    }

    public ResponseEntity<Object> addOrder(Order order) {
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("PENDING");

        return ResponseEntity.ok(orderRepository.save(order));
    }

    public ResponseEntity<Object> getAllOrders() {
        return ResponseEntity.ok(orderRepository.findAll());
    }

    public ResponseEntity<Object> getOrdersByUser(int userId) {
        return ResponseEntity.ok(orderRepository.findByUserId(userId));
    }

    @Transactional
    public ResponseEntity<Object> checkout(int userId, int addressId, String couponCode) {
        Address address = addressRepository.findById(addressId).orElse(null);

        if (address == null) return ResponseEntity.badRequest().body("Address not found");
        if (address.getUserId() != userId) return ResponseEntity.badRequest().body("Address does not belong to user");

        List<Cart> carts = cartRepository.findByUserId(userId);

        if (carts.isEmpty()) return ResponseEntity.badRequest().body("Cart is empty");

        double total = 0;
        List<OrderItem> items = new ArrayList<>();

        for (Cart cart : carts) {
            Product product = productRepository.findById(cart.getProductId()).orElse(null);

            if (product == null) continue;

            ProductVariant variant = null;
            double price = product.isOnSale() && product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
            int stock = product.getQuantity();

            if (cart.getVariantId() != null) {
                variant = variantRepository.findById(cart.getVariantId()).orElse(null);

                if (variant == null || variant.getProduct().getId() != product.getId()) {
                    return ResponseEntity.badRequest().body("Invalid product variant");
                }

                if (variant.isOnSale() && variant.getSalePrice() != null) {
                    price = variant.getSalePrice();
                } else if (product.isOnSale() && product.getSalePrice() != null) {
                    price = product.getSalePrice();
                } else {
                    price = variant.getPrice();
                }

                stock = variant.getQuantity();
            }

            if (stock < cart.getQuantity()) {
                String name = variant != null ? product.getName() + " - " + variant.getName() : product.getName();

                return ResponseEntity.badRequest().body("Not enough stock for " + name);
            }

            total += price * cart.getQuantity();

            OrderItem item = new OrderItem();
            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setVariantId(variant != null ? variant.getId() : null);
            item.setVariantName(variant != null ? variant.getName() : null);
            item.setQuantity(cart.getQuantity());
            item.setPrice(price);

            items.add(item);
        }

        if (items.isEmpty()) return ResponseEntity.badRequest().body("No valid products in cart");

        double discountAmount = 0;
        double finalPrice = total;

        if (couponCode != null && !couponCode.isBlank()) {
            Coupon coupon = couponService.getCouponByCode(couponCode);

            if (coupon == null) return ResponseEntity.badRequest().body("Invalid coupon");

            if (total < coupon.getMinimumPurchase()) {
                return ResponseEntity.badRequest().body("Minimum purchase amount not reached");
            }

            discountAmount = Math.round(total * (coupon.getDiscountPercentage() / 100) * 100) / 100.0;
            finalPrice = Math.round((total - discountAmount) * 100) / 100.0;
        }

        PaymentResponse paymentResponse;

        try {
            paymentResponse = paypalService.createPayment(finalPrice);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Paypal creation failed: " + e.getMessage());
        }

        Order order = new Order();

        order.setUserId(userId);
        order.setTotalPrice(total);
        order.setCouponCode(couponCode);
        order.setDiscountAmount(discountAmount);
        order.setFinalPrice(finalPrice);
        order.setDate(LocalDate.now().toString());
        order.setPaymentStatus("PENDING");
        order.setOrderStatus("PENDING");
        order.setPaypalOrderId(paymentResponse.getOrderId());

        order.setAddressId(address.getId());
        order.setRecipientName(address.getRecipientName());
        order.setPhone(address.getPhone());
        order.setAddressLine1(address.getAddressLine1());
        order.setAddressLine2(address.getAddressLine2());
        order.setCity(address.getCity());
        order.setState(address.getState());
        order.setPostcode(address.getPostcode());
        order.setCountry(address.getCountry());

        Order savedOrder = orderRepository.save(order);

        for (OrderItem item : items) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("order", savedOrder);
        response.put("items", items);
        response.put("paypalApprovalUrl", paymentResponse.getApprovalUrl());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Object> savePaypalOrderId(int orderId, String paypalOrderId) {
        Order order = orderRepository.findById(orderId).orElse(null);

        if (order == null) return ResponseEntity.badRequest().body("Order not found");

        order.setPaypalOrderId(paypalOrderId);
        orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    @Transactional
    public ResponseEntity<Object> paymentSuccess(String paypalOrderId) {
        Order order = orderRepository.findByPaypalOrderId(paypalOrderId).orElse(null);

        if (order == null) return ResponseEntity.badRequest().body("Order not found");
        if ("PAID".equals(order.getPaymentStatus())) return ResponseEntity.ok("Payment already completed");

        for (OrderItem item : order.getItems()) {

            if (item.getVariantId() != null) {
                ProductVariant variant = variantRepository.findById(item.getVariantId()).orElse(null);

                if (variant == null) return ResponseEntity.badRequest().body("Variant not found");
                if (variant.getQuantity() < item.getQuantity()) return ResponseEntity.badRequest().body("Not enough variant stock");

                variant.setQuantity(variant.getQuantity() - item.getQuantity());
                variantRepository.save(variant);

            } else {
                Product product = productRepository.findById(item.getProductId()).orElse(null);

                if (product == null) return ResponseEntity.badRequest().body("Product not found");
                if (product.getQuantity() < item.getQuantity()) return ResponseEntity.badRequest().body("Not enough product stock");

                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepository.save(product);
            }
        }

        order.setPaymentStatus("PAID");
        order.setOrderStatus("PROCESSING");

        cartRepository.deleteAll(cartRepository.findByUserId(order.getUserId()));

        orderRepository.save(order);

        return ResponseEntity.ok("Payment completed");
    }

    public ResponseEntity<Object> updateOrderStatus(int id, String status) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order == null) return ResponseEntity.badRequest().body("Order not found");

        List<String> validStatuses = List.of("PENDING", "PROCESSING", "PACKED", "SHIPPED", "DELIVERED", "CANCELLED");

        status = status.toUpperCase();

        if (!validStatuses.contains(status)) return ResponseEntity.badRequest().body("Invalid order status");

        order.setOrderStatus(status);
        orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    public ResponseEntity<Object> updateTrackingNumber(int id, String trackingNumber) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order == null) return ResponseEntity.badRequest().body("Order not found");

        order.setTrackingNumber(trackingNumber);
        orderRepository.save(order);

        return ResponseEntity.ok(order);
    }

    public ResponseEntity<Object> deleteOrder(int id) {
        Order order = orderRepository.findById(id).orElse(null);

        if (order == null) return ResponseEntity.badRequest().body("Order not found");

        orderRepository.delete(order);

        return ResponseEntity.ok("Order deleted");
    }
}