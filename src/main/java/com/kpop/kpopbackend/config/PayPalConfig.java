package com.kpop.kpopbackend.config;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class PayPalConfig {


    private String clientId = "BAAZoqSiSYSpfor_0EKMS5HcdPTbGWyPh7cmxdjG2minkKeOdIHcj5iilufkBO1EZCGgLUfr2ErJ9E8GbQ";

    private String clientSecret = "EJylhvgVROGFp5RIMSMQPzQbIDi_cRQieL8UvLf0F3HawVSfmETbfzEZXTq44-uyka_AzwlbaglzMMLz";


    @Bean
    public PayPalHttpClient paypalClient(){

        PayPalEnvironment environment =
                new PayPalEnvironment.Sandbox(
                        clientId,
                        clientSecret
                );


        return new PayPalHttpClient(environment);

    }

}