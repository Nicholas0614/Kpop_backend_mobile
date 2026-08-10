package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Coupon;
import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.ProductVariant;
import com.kpop.kpopbackend.models.User;
import com.kpop.kpopbackend.repository.CouponRepository;
import com.kpop.kpopbackend.repository.OrderRepository;
import com.kpop.kpopbackend.repository.ProductRepository;
import com.kpop.kpopbackend.repository.ProductVariantRepository;
import com.kpop.kpopbackend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductVariantRepository variantRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository;
    private final OrderService orderService;

    public AdminService(UserRepository userRepository, ProductRepository productRepository,
                        ProductVariantRepository variantRepository, OrderRepository orderRepository,
                        CouponRepository couponRepository, OrderService orderService) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.variantRepository = variantRepository;
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
        this.orderService = orderService;
    }

    public Map<String, Object> getDashboard() {
        List<Order> orders = orderRepository.findAll();

        long paidOrders = orders.stream()
                .filter(order -> "PAID".equalsIgnoreCase(order.getPaymentStatus()))
                .count();

        long processingOrders = orders.stream()
                .filter(order -> "PROCESSING".equalsIgnoreCase(order.getOrderStatus()))
                .count();

        long shippedOrders = orders.stream()
                .filter(order -> "SHIPPED".equalsIgnoreCase(order.getOrderStatus()))
                .count();

        long deliveredOrders = orders.stream()
                .filter(order -> "DELIVERED".equalsIgnoreCase(order.getOrderStatus()))
                .count();

        double totalRevenue = orders.stream()
                .filter(order -> "PAID".equalsIgnoreCase(order.getPaymentStatus()))
                .mapToDouble(Order::getFinalPrice)
                .sum();

        Map<String, Object> dashboard = new LinkedHashMap<>();

        dashboard.put("totalUsers", userRepository.count());
        dashboard.put("totalProducts", productRepository.count());
        dashboard.put("productsOnSale", productRepository.findByOnSaleTrue().size());
        dashboard.put("totalOrders", orderRepository.count());
        dashboard.put("paidOrders", paidOrders);
        dashboard.put("processingOrders", processingOrders);
        dashboard.put("shippedOrders", shippedOrders);
        dashboard.put("deliveredOrders", deliveredOrders);
        dashboard.put("totalRevenue", Math.round(totalRevenue * 100) / 100.0);

        return dashboard;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

    public ResponseEntity<Object> updateOrderStatus(int orderId, String status) {
        return orderService.updateOrderStatus(orderId, status);
    }

    public ResponseEntity<Object> updateTrackingNumber(int orderId, String trackingNumber) {
        return orderService.updateTrackingNumber(orderId, trackingNumber);
    }

    public ResponseEntity<Object> updateProductSale(int productId, boolean onSale, Double salePrice) {
        Product product = productRepository.findById(productId).orElse(null);

        if (product == null) return ResponseEntity.badRequest().body("Product not found");

        if (onSale) {
            if (salePrice == null) return ResponseEntity.badRequest().body("Sale price is required");
            if (product.getPrice() == null || salePrice < 0 || salePrice >= product.getPrice()) {
                return ResponseEntity.badRequest().body("Sale price must be lower than normal price");
            }

            product.setSalePrice(salePrice);
        }

        product.setOnSale(onSale);
        productRepository.save(product);

        return ResponseEntity.ok(product);
    }

    public ResponseEntity<Object> updateVariantSale(int variantId, boolean onSale, Double salePrice) {
        ProductVariant variant = variantRepository.findById(variantId).orElse(null);

        if (variant == null) return ResponseEntity.badRequest().body("Variant not found");

        if (onSale) {
            if (salePrice == null) return ResponseEntity.badRequest().body("Sale price is required");
            if (variant.getPrice() == null || salePrice < 0 || salePrice >= variant.getPrice()) {
                return ResponseEntity.badRequest().body("Sale price must be lower than normal price");
            }

            variant.setSalePrice(salePrice);
        }

        variant.setOnSale(onSale);
        variantRepository.save(variant);

        return ResponseEntity.ok(variant);
    }
}