// src/main/java/com/weight/services/StripeService.java
package com.weight.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class StripeService {
    
    @Value("${stripe.api.key}")
    private String stripeApiKey;
    
    public StripeService(@Value("${stripe.api.key}") String stripeApiKey) {
        this.stripeApiKey = stripeApiKey;
        Stripe.apiKey = this.stripeApiKey;
    }
    
    public PaymentIntent createPaymentIntent(BigDecimal amount, String currency, String description, String customerId) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amount.multiply(new BigDecimal(100)).longValue()) // Le montant est en centimes
                .setCurrency(currency)
                .setDescription(description)
                .setCustomer(customerId)
                .build();
        
        return PaymentIntent.create(params);
    }
    
    public Charge createCharge(String token, BigDecimal amount, String currency, String description) throws StripeException {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", amount.multiply(new BigDecimal(100)).intValue());
        chargeParams.put("currency", currency);
        chargeParams.put("description", description);
        chargeParams.put("source", token);
        
        return Charge.create(chargeParams);
    }
    
    public Customer createCustomer(String email, String token) throws StripeException {
        Map<String, Object> customerParams = new HashMap<>();
        customerParams.put("email", email);
        customerParams.put("source", token);
        
        return Customer.create(customerParams);
    }
    
    public Customer getCustomer(String customerId) throws StripeException {
        return Customer.retrieve(customerId);
    }
}
