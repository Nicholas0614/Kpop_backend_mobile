package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.PaymentResponse;
import com.kpop.kpopbackend.models.Cart;
import com.kpop.kpopbackend.models.Coupon;
import com.kpop.kpopbackend.models.Order;
import com.kpop.kpopbackend.models.Product;
import com.kpop.kpopbackend.models.OrderItem;
import com.kpop.kpopbackend.repository.OrderItemRepository;

import com.kpop.kpopbackend.repository.CartRepository;
import com.kpop.kpopbackend.repository.OrderRepository;
import com.kpop.kpopbackend.repository.ProductRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;





@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final PayPalService paypalService;
    private final CouponService couponService;

    private final OrderItemRepository orderItemRepository;

    public OrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            CartRepository cartRepository,
            PayPalService paypalService,
            CouponService couponService,
            OrderItemRepository orderItemRepository
    ){

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.paypalService = paypalService;
        this.couponService = couponService;
        this.orderItemRepository = orderItemRepository;

    }

    // CREATE ORDER
    public ResponseEntity<Object> addOrder(Order order){

        order.setPaymentStatus("PENDING");

        Order savedOrder = orderRepository.save(order);

        return ResponseEntity.ok(savedOrder);

    }

    // GET ORDER BY USER
    public ResponseEntity<Object> getOrdersByUser(int userId){

        List<Order> orders = orderRepository.findByUserId(userId);

        return ResponseEntity.ok(orders);

    }

    // CHECKOUT CART
    // CHECKOUT CART
    public ResponseEntity<Object> checkout(int userId, String couponCode){

        List<Cart> carts = cartRepository.findByUserId(userId);

        if(carts.isEmpty()){

            return ResponseEntity
                    .badRequest()
                    .body("Cart is empty");

        }


        double total = 0;

        List<OrderItem> items = new ArrayList<>();


        for(Cart cart : carts){

            Product product = productRepository
                    .findById(cart.getProductId())
                    .orElse(null);


            if(product == null){
                continue;
            }


            if(product.getQuantity() < cart.getQuantity()){

                return ResponseEntity
                        .badRequest()
                        .body("Not enough stock for " + product.getName());

            }


            double itemTotal = product.getPrice() * cart.getQuantity();


            total += itemTotal;


            OrderItem item = new OrderItem();

            item.setProductId(product.getId());
            item.setProductName(product.getName());
            item.setQuantity(cart.getQuantity());
            item.setPrice(product.getPrice());


            items.add(item);

        }


        double discountAmount = 0;
        double finalPrice = total;



        if(couponCode != null){

            Coupon coupon = couponService.getCouponByCode(couponCode);


            if(coupon == null){

                return ResponseEntity
                        .badRequest()
                        .body("Invalid coupon");

            }


            if(total < coupon.getMinimumPurchase()){

                return ResponseEntity
                        .badRequest()
                        .body("Minimum purchase amount not reached");

            }


            discountAmount = Math.round(
                    total * (coupon.getDiscountPercentage() / 100) * 100
            ) / 100.0;


            finalPrice = Math.round(
                    (total - discountAmount) * 100
            ) / 100.0;

        }



        PaymentResponse paymentResponse;


        try {

            paymentResponse = paypalService.createPayment(finalPrice);

        }catch(Exception e){

            return ResponseEntity
                    .badRequest()
                    .body("Paypal creation failed: " + e.getMessage());

        }



        Order order = new Order();


        order.setUserId(userId);

        order.setTotalPrice(total);

        order.setCouponCode(couponCode);

        order.setDiscountAmount(discountAmount);

        order.setFinalPrice(finalPrice);

        order.setDate(LocalDate.now().toString());

        order.setPaymentStatus("PENDING");

        order.setPaypalOrderId(paymentResponse.getOrderId());



        Order savedOrder = orderRepository.save(order);



        for(OrderItem item : items){

            item.setOrder(savedOrder);

            orderItemRepository.save(item);

        }



        Map<String,Object> response = new HashMap<>();

        response.put("order", savedOrder);

        response.put("items", items);

        response.put(
                "paypalApprovalUrl",
                paymentResponse.getApprovalUrl()
        );


        return ResponseEntity.ok(response);

    }

    // SAVE PAYPAL ORDER ID
    public ResponseEntity<Object> savePaypalOrderId(
            int orderId,
            String paypalOrderId
    ){

        Order order = orderRepository.findById(orderId).orElse(null);

        if(order == null){

            return ResponseEntity
                    .badRequest()
                    .body("Order not found");

        }

        order.setPaypalOrderId(paypalOrderId);

        orderRepository.save(order);

        return ResponseEntity.ok(order);

    }

    // AFTER PAYPAL PAYMENT SUCCESS
    public ResponseEntity<Object> paymentSuccess(String paypalOrderId){

        Order order = orderRepository
                .findByPaypalOrderId(paypalOrderId)
                .orElse(null);


        if(order == null){

            return ResponseEntity
                    .badRequest()
                    .body("Order not found");

        }


        order.setPaymentStatus("PAID");


        for(OrderItem item : order.getItems()){


            Product product = productRepository
                    .findById(item.getProductId())
                    .orElse(null);


            if(product != null){

                product.setQuantity(
                        product.getQuantity() - item.getQuantity()
                );

                productRepository.save(product);

            }

        }


        List<Cart> carts =
                cartRepository.findByUserId(order.getUserId());


        cartRepository.deleteAll(carts);


        orderRepository.save(order);


        return ResponseEntity.ok("Payment completed");

    }


    // DELETE ORDER
    public ResponseEntity<Object> deleteOrder(int id){

        Order order = orderRepository.findById(id).orElse(null);


        if(order == null){

            return ResponseEntity
                    .badRequest()
                    .body("Order not found");

        }


        orderRepository.delete(order);

        return ResponseEntity.ok("Order deleted");

    }

}