package com.kpop.kpopbackend.services;

import com.kpop.kpopbackend.dto.PaymentResponse;
import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class PayPalService {

    private final PayPalHttpClient client;
    private final String currency;
    private final String returnUrl;
    private final String cancelUrl;

    public PayPalService(PayPalHttpClient client,
                         @Value("${paypal.currency}") String currency,
                         @Value("${paypal.return-url}") String returnUrl,
                         @Value("${paypal.cancel-url}") String cancelUrl) {
        this.client = client;
        this.currency = currency;
        this.returnUrl = returnUrl;
        this.cancelUrl = cancelUrl;
    }

    public PaymentResponse createPayment(double amount) throws IOException {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");

        ApplicationContext applicationContext = new ApplicationContext()
                .returnUrl(returnUrl)
                .cancelUrl(cancelUrl);

        orderRequest.applicationContext(applicationContext);

        String formattedAmount = BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .toPlainString();

        AmountWithBreakdown amountObject = new AmountWithBreakdown()
                .currencyCode(currency)
                .value(formattedAmount);

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
            if ("approve".equals(link.rel())) paymentResponse.setApprovalUrl(link.href());
        }

        return paymentResponse;
    }

    public String capturePayment(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        HttpResponse<Order> response = client.execute(request);
        return response.result().status();
    }
}