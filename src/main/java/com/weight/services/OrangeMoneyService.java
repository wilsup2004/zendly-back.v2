// src/main/java/com/weight/services/OrangeMoneyService.java
package com.weight.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrangeMoneyService {
    
    @Value("${orangemoney.api.url}")
    private String apiUrl;
    
    @Value("${orangemoney.merchant.id}")
    private String merchantId;
    
    @Value("${orangemoney.api.key}")
    private String apiKey;
    
    private final RestTemplate restTemplate;
    
    public OrangeMoneyService() {
        this.restTemplate = new RestTemplate();
    }
    
    public Map<String, Object> initiatePayment(String phoneNumber, BigDecimal amount, String reference) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Auth-Token", apiKey);
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("merchantId", merchantId);
        requestBody.put("phoneNumber", phoneNumber);
        requestBody.put("amount", amount);
        requestBody.put("reference", reference);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        
        return restTemplate.postForObject(apiUrl + "/payments/initiate", request, Map.class);
    }
    
    public Map<String, Object> checkPaymentStatus(String transactionId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", apiKey);
        
        HttpEntity<?> request = new HttpEntity<>(headers);
        
        return restTemplate.getForObject(
            apiUrl + "/payments/status/" + transactionId, 
            Map.class, 
            request
        );
    }
}
