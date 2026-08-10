package com.kpop.kpopbackend.dto;


import lombok.Data;


@Data
public class PaymentResponse {

    private String orderId;

    private String approvalUrl;


}