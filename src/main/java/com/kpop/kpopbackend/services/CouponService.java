package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.models.Coupon;
import com.kpop.kpopbackend.repository.CouponRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CouponService {

    private final CouponRepository repository;

    public CouponService(CouponRepository repository) {
        this.repository = repository;
    }

    public List<Coupon> getAllCoupons() {
        return repository.findAll();
    }

    public Coupon getCouponById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Coupon addCoupon(Coupon coupon) {
        return repository.save(coupon);
    }

    public Coupon updateCoupon(int id, Coupon coupon) {
        Coupon existing = repository.findById(id).orElse(null);

        if (existing == null) return null;

        existing.setCode(coupon.getCode());
        existing.setDiscountPercentage(coupon.getDiscountPercentage());
        existing.setMinimumPurchase(coupon.getMinimumPurchase());
        existing.setExpiryDate(coupon.getExpiryDate());
        existing.setActive(coupon.isActive());

        return repository.save(existing);
    }

    public Coupon getCouponByCode(String code) {
        Coupon coupon = repository.findByCode(code);

        if (coupon == null) return null;
        if (!coupon.isActive()) return null;
        if (coupon.getExpiryDate().isBefore(LocalDateTime.now())) return null;

        return coupon;
    }

    public boolean deleteCoupon(int id) {
        if (!repository.existsById(id)) return false;

        repository.deleteById(id);
        return true;
    }
}