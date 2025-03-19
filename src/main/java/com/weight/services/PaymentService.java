// src/main/java/com/weight/services/PaymentService.java
package com.weight.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.weight.model.Colis;
import com.weight.model.Payment;
import com.weight.model.PaymentMethod;
import com.weight.model.PriseEnCharge;
import com.weight.model.Users;
import com.weight.repository.PaymentMethodRepository;
import com.weight.repository.PaymentRepository;
import com.weight.repository.UserRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PaypalService paypalService;
    
    @Autowired
    private StripeService stripeService;
    
    @Autowired
    private OrangeMoneyService orangeMoneyService;
    
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }
    
    public List<PaymentMethod> getActivePaymentMethods() {
        return paymentMethodRepository.findByIsActiveTrue();
    }
    
    @Transactional
    public Payment processPaypalPayment(String userId, BigDecimal amount, Integer colisId, Integer priseId, String returnUrl, String cancelUrl) throws PayPalRESTException {
        // Créer le paiement PayPal
        com.paypal.api.payments.Payment paypalPayment = paypalService.createPayment(
            amount.doubleValue(), 
            "USD", 
            "paypal", 
            "sale", 
            "Paiement Zendly", 
            cancelUrl, 
            returnUrl
        );
        
        // Créer un enregistrement de paiement dans notre système
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName("PayPal");
        
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentAmount(amount);
        payment.setPaymentStatus("PENDING");
        payment.setPaymentDate(new Date());
        payment.setTransactionId(paypalPayment.getId());
        
        // Associer avec le colis et la prise en charge si fournis
        if (colisId != null) {
            Colis colis = new Colis();
            colis.setIdColis(colisId);
            payment.setColis(colis);
        }
        
        if (priseId != null) {
            PriseEnCharge prise = new PriseEnCharge();
            prise.setIdPrise(priseId);
            payment.setPriseEnCharge(prise);
        }
        
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public Payment processStripePayment(String userId, String token, BigDecimal amount, Integer colisId, Integer priseId, String description) throws StripeException {
        // Créer la charge Stripe
        com.stripe.model.Charge charge = stripeService.createCharge(token, amount, "EUR", description);
        
        // Créer un enregistrement de paiement dans notre système
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName("Stripe");
        
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentAmount(amount);
        payment.setPaymentStatus(charge.getStatus().toUpperCase());
        payment.setPaymentDate(new Date());
        payment.setTransactionId(charge.getId());
        
        // Associer avec le colis et la prise en charge si fournis
        if (colisId != null) {
            Colis colis = new Colis();
            colis.setIdColis(colisId);
            payment.setColis(colis);
        }
        
        if (priseId != null) {
            PriseEnCharge prise = new PriseEnCharge();
            prise.setIdPrise(priseId);
            payment.setPriseEnCharge(prise);
        }
        
        return paymentRepository.save(payment);
    }
    
    @Transactional
    public Payment processOrangeMoneyPayment(String userId, String phoneNumber, BigDecimal amount, Integer colisId, Integer priseId) {
        // Générer une référence unique
        String reference = "ZDL-" + System.currentTimeMillis();
        
        // Initier le paiement Orange Money
        Map<String, Object> response = orangeMoneyService.initiatePayment(phoneNumber, amount, reference);
        
        // Créer un enregistrement de paiement dans notre système
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        PaymentMethod paymentMethod = paymentMethodRepository.findByMethodName("Orange Money");
        
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentAmount(amount);
        payment.setPaymentStatus("PENDING");
        payment.setPaymentDate(new Date());
        payment.setTransactionId((String) response.get("transactionId"));
        
        // Associer avec le colis et la prise en charge si fournis
        if (colisId != null) {
            Colis colis = new Colis();
            colis.setIdColis(colisId);
            payment.setColis(colis);
        }
        
        if (priseId != null) {
            PriseEnCharge prise = new PriseEnCharge();
            prise.setIdPrise(priseId);
            payment.setPriseEnCharge(prise);
        }
        
        return paymentRepository.save(payment);
    }
    
    public Payment updatePaymentStatus(int paymentId, String status) {
        Optional<Payment> optionalPayment = paymentRepository.findById(paymentId);
        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setPaymentStatus(status);
            return paymentRepository.save(payment);
        } else {
            throw new RuntimeException("Paiement non trouvé");
        }
    }
    
    public List<Payment> getUserPayments(String userId) {
        Users user = new Users();
        user.setIdUser(userId);
        return paymentRepository.findByUser(user);
    }
    
    public Payment getPaymentDetails(int paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
    }
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    public List<Payment> getPaymentsByStatus(String status) {
        return paymentRepository.findByStatus(status);
    }
    
    public List<Payment> getPaymentsByDateRange(Date startDate, Date endDate) {
        return paymentRepository.findByDateRange(startDate, endDate);
    }
    
    public Long countTotalPayments() {
        return paymentRepository.countTotalPayments();
    }
    
    public Double getTotalRevenue() {
        return paymentRepository.sumCompletedPayments();
    }
}