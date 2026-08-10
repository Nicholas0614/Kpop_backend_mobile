package com.kpop.kpopbackend.repository;


import com.kpop.kpopbackend.models.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CouponRepository
        extends JpaRepository<Coupon, Integer> {


    Coupon findByCode(String code);

}