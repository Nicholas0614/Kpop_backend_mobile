package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.PaymentResponse;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PayPalService {

    private final PayPalHttpClient client;

    public PayPalService(PayPalHttpClient client) {
        this.client = client;
    }

    public PaymentResponse createPayment(double amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();

        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl("http://localhost:3000/payment-success")
                .cancelUrl("http://localhost:3000/payment-cancel");

        orderRequest.applicationContext(applicationContext);

        AmountWithBreakdown amountObject = new AmountWithBreakdown()
                .currencyCode("USD")
                .value(String.valueOf(amount));

        PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
                .amountWithBreakdown(amountObject);

        orderRequest.purchaseUnits(List.of(purchaseUnit));

        OrdersCreateRequest request = new OrdersCreateRequest();
        request.requestBody(orderRequest);

        HttpResponse<Order> response = client.execute(request);
        Order order = response.result();

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setOrderId(order.id());

        for (LinkDescription link : order.links()) {
            if (link.rel().equals("approve")) paymentResponse.setApprovalUrl(link.href());
        }

        return paymentResponse;
    }

    public String capturePayment(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        HttpResponse<Order> response = client.execute(request);

        return response.result().status();
    }
}