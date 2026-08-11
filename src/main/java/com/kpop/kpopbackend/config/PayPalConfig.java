package com.kpop.kpopbackend.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalConfig {

    private final String clientId;
    private final String clientSecret;
    private final String mode;

    public PayPalConfig(@Value("${paypal.client.id}") String clientId,
                        @Value("${paypal.client.secret}") String clientSecret,
                        @Value("${paypal.mode}") String mode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.mode = mode;
    }

    @Bean
    public PayPalHttpClient paypalClient() {
        PayPalEnvironment environment;

        if ("live".equalsIgnoreCase(mode)) {
            environment = new PayPalEnvironment.Live(clientId, clientSecret);
        } else {
            environment = new PayPalEnvironment.Sandbox(clientId, clientSecret);
        }

        return new PayPalHttpClient(environment);
    }
}