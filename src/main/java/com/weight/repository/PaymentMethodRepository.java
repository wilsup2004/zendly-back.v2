// src/main/java/com/weight/repository/PaymentMethodRepository.java
package com.weight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.weight.model.PaymentMethod;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    List<PaymentMethod> findByIsActiveTrue();
    PaymentMethod findByMethodName(String methodName);
}