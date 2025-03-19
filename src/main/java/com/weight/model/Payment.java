// src/main/java/com/weight/model/Payment.java
package com.weight.model;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "payments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Payment implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private int idPayment;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Users user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_colis")
    private Colis colis;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_prise")
    private PriseEnCharge priseEnCharge;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_method")
    private PaymentMethod paymentMethod;
    
    @Column(name = "payment_amount")
    private BigDecimal paymentAmount;
    
    @Column(name = "payment_status")
    private String paymentStatus;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date")
    private Date paymentDate;
    
    @Column(name = "transaction_id")
    private String transactionId;
    
    @Column(name = "payment_details")
    private String paymentDetails;
    
    public Payment() {
    }
    
    // Constructor with minimal required fields
    public Payment(Users user, PaymentMethod paymentMethod, BigDecimal paymentAmount, String paymentStatus, Date paymentDate) {
        this.user = user;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
    }
    
    // Full constructor
    public Payment(int idPayment, Users user, Colis colis, PriseEnCharge priseEnCharge, PaymentMethod paymentMethod,
                  BigDecimal paymentAmount, String paymentStatus, Date paymentDate, String transactionId, String paymentDetails) {
        this.idPayment = idPayment;
        this.user = user;
        this.colis = colis;
        this.priseEnCharge = priseEnCharge;
        this.paymentMethod = paymentMethod;
        this.paymentAmount = paymentAmount;
        this.paymentStatus = paymentStatus;
        this.paymentDate = paymentDate;
        this.transactionId = transactionId;
        this.paymentDetails = paymentDetails;
    }
    
    // Getters and setters
    public int getIdPayment() {
        return this.idPayment;
    }
    
    public void setIdPayment(int idPayment) {
        this.idPayment = idPayment;
    }
    
    public Users getUser() {
        return this.user;
    }
    
    public void setUser(Users user) {
        this.user = user;
    }
    
    public Colis getColis() {
        return this.colis;
    }
    
    public void setColis(Colis colis) {
        this.colis = colis;
    }
    
    public PriseEnCharge getPriseEnCharge() {
        return this.priseEnCharge;
    }
    
    public void setPriseEnCharge(PriseEnCharge priseEnCharge) {
        this.priseEnCharge = priseEnCharge;
    }
    
    public PaymentMethod getPaymentMethod() {
        return this.paymentMethod;
    }
    
    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    
    public BigDecimal getPaymentAmount() {
        return this.paymentAmount;
    }
    
    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    
    public String getPaymentStatus() {
        return this.paymentStatus;
    }
    
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
    
    public Date getPaymentDate() {
        return this.paymentDate;
    }
    
    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
    
    public String getTransactionId() {
        return this.transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getPaymentDetails() {
        return this.paymentDetails;
    }
    
    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
}