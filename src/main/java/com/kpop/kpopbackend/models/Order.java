package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Entity
@Table(name = "orders")
@Data
public class Order {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "user_id")
    private int userId;


    private String paymentStatus;


    private String paypalOrderId;


    @Column(name = "total_price")
    private double totalPrice;


    @Column(name = "coupon_code")
    private String couponCode;


    @Column(name = "discount_amount")
    private double discountAmount;


    @Column(name = "final_price")
    private double finalPrice;


    @Column(name = "order_date")
    private String date;



    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL
    )
    private List<OrderItem> items;

    private Integer addressId;
    private String recipientName;
    private String phone;
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String postcode;
    private String country;

    private String orderStatus;
    private String trackingNumber;

}