package com.kpop.kpopbackend.controllers;


import com.kpop.kpopbackend.services.PayPalService;
import com.kpop.kpopbackend.services.OrderService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/payment")
public class PaymentController {


    private final PayPalService payPalService;

    private final OrderService orderService;



    public PaymentController(
            PayPalService payPalService,
            OrderService orderService
    ){

        this.payPalService = payPalService;
        this.orderService = orderService;

    }




    @PostMapping("/paypal/capture/{id}")
    public ResponseEntity<Object> capture(
            @PathVariable String id
    ) throws Exception {


        String result =
                payPalService.capturePayment(id);



        if(result.equals("COMPLETED")){


            return orderService.paymentSuccess(id);


        }



        return ResponseEntity
                .badRequest()
                .body(
                        "Payment failed: " + result
                );

    }

}