package com.kpop.kpopbackend.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String code;

    private Double discountPercentage;

    private Double minimumPurchase;

    private LocalDateTime expiryDate;

    private boolean active;

}