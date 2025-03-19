// src/main/java/com/weight/views/PaymentResponseView.java
package com.weight.views;

public class PaymentResponseView {
    private int paymentId;
    private String transactionId;
    private String status;
    private String paymentUrl;
    
    // Constructeur par défaut
    public PaymentResponseView() {
    }
    
    // Getters et Setters
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPaymentUrl() {
        return paymentUrl;
    }
    
    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}