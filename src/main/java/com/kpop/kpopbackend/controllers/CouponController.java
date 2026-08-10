package com.kpop.kpopbackend.controllers;


import com.kpop.kpopbackend.models.Coupon;
import com.kpop.kpopbackend.services.CouponService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;


@RestController
@RequestMapping("/coupons")
public class CouponController {


    private final CouponService service;


    public CouponController(CouponService service){

        this.service = service;

    }



    @GetMapping
    public List<Coupon> getAllCoupons(){

        return service.getAllCoupons();

    }



    @GetMapping("/{id}")
    public ResponseEntity<?> getCoupon(
            @PathVariable int id
    ){

        Coupon coupon = service.getCouponById(id);


        if(coupon == null)

            return ResponseEntity.notFound().build();


        return ResponseEntity.ok(coupon);

    }



    @PostMapping
    public ResponseEntity<Coupon> addCoupon(
            @RequestBody Coupon coupon
    ){

        return ResponseEntity.ok(
                service.addCoupon(coupon)
        );

    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateCoupon(
            @PathVariable int id,
            @RequestBody Coupon coupon
    ){

        Coupon updated =
                service.updateCoupon(id, coupon);


        if(updated == null)

            return ResponseEntity.notFound().build();


        return ResponseEntity.ok(updated);

    }



    @GetMapping("/code/{code}")
    public ResponseEntity<?> checkCoupon(
            @PathVariable String code
    ){

        Coupon coupon =
                service.getCouponByCode(code);


        if(coupon == null)

            return ResponseEntity
                    .badRequest()
                    .body("Invalid coupon");


        return ResponseEntity.ok(coupon);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCoupon(
            @PathVariable int id
    ){


        if(!service.deleteCoupon(id))

            return ResponseEntity
                    .badRequest()
                    .body("Coupon not found");


        return ResponseEntity.ok(
                "Coupon deleted successfully"
        );

    }

}