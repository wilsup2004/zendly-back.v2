// src/main/java/com/weight/views/PaymentRequestView.java
package com.weight.views;

import java.math.BigDecimal;

public class PaymentRequestView {
    private String userId;
    private BigDecimal amount;
    private Integer colisId;
    private Integer priseId;
    private String description;
    private String returnUrl;
    private String cancelUrl;
    private String token; // Pour Stripe
    private String phoneNumber; // Pour Orange Money
    
    // Constructeur par défaut
    public PaymentRequestView() {
    }
    
    // Getters et Setters
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Integer getColisId() {
        return colisId;
    }
    
    public void setColisId(Integer colisId) {
        this.colisId = colisId;
    }
    
    public Integer getPriseId() {
        return priseId;
    }
    
    public void setPriseId(Integer priseId) {
        this.priseId = priseId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getReturnUrl() {
        return returnUrl;
    }
    
    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
    
    public String getCancelUrl() {
        return cancelUrl;
    }
    
    public void setCancelUrl(String cancelUrl) {
        this.cancelUrl = cancelUrl;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
