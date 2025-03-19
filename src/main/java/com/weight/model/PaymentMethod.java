// src/main/java/com/weight/model/PaymentMethod.java
package com.weight.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "payment_methods")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PaymentMethod implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_method")
    private int idMethod;
    
    @Column(name = "method_name", nullable = false)
    private String methodName;
    
    @Column(name = "method_description")
    private String methodDescription;
    
    @Column(name = "is_active")
    private Boolean isActive;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "paymentMethod")
    private Set<Payment> payments = new HashSet<Payment>(0);
    
    public PaymentMethod() {
    }
    
    public PaymentMethod(int idMethod) {
        this.idMethod = idMethod;
    }
    
    public PaymentMethod(int idMethod, String methodName, String methodDescription, Boolean isActive) {
        this.idMethod = idMethod;
        this.methodName = methodName;
        this.methodDescription = methodDescription;
        this.isActive = isActive;
    }
    
    // Getters and setters
    public int getIdMethod() {
        return this.idMethod;
    }
    
    public void setIdMethod(int idMethod) {
        this.idMethod = idMethod;
    }
    
    public String getMethodName() {
        return this.methodName;
    }
    
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
    
    public String getMethodDescription() {
        return this.methodDescription;
    }
    
    public void setMethodDescription(String methodDescription) {
        this.methodDescription = methodDescription;
    }
    
    public Boolean getIsActive() {
        return this.isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Set<Payment> getPayments() {
        return this.payments;
    }
    
    public void setPayments(Set<Payment> payments) {
        this.payments = payments;
    }
}