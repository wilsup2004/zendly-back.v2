// src/main/java/com/weight/controller/PaymentController.java
package com.weight.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.weight.model.PaymentMethod;
import com.weight.services.PaymentService;
import com.weight.views.PaymentRequestView;
import com.weight.views.PaymentResponseView;

@RestController
@RequestMapping("/payment")
@CrossOrigin(origins = "*")
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private PaymentService paymentService;
    
    @GetMapping("/methods")
    public ResponseEntity<?> getPaymentMethods() {
        logger.info("Récupération des méthodes de paiement disponibles");
        List<PaymentMethod> methods = paymentService.getActivePaymentMethods();
        return new ResponseEntity<>(methods, HttpStatus.OK);
    }
    
    @PostMapping("/paypal/create")
    public ResponseEntity<?> createPaypalPayment(@RequestBody PaymentRequestView request) {
        logger.info("Création d'un paiement PayPal");
        
        try {
            com.weight.model.Payment payment = paymentService.processPaypalPayment(
                request.getUserId(),
                request.getAmount(),
                request.getColisId(),
                request.getPriseId(),
                request.getReturnUrl(),
                request.getCancelUrl()
            );
            
            PaymentResponseView response = new PaymentResponseView();
            response.setPaymentId(payment.getIdPayment());
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getPaymentStatus());
            response.setPaymentUrl(request.getReturnUrl() + "?paymentId=" + payment.getTransactionId());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (PayPalRESTException e) {
            logger.error("Erreur PayPal: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/stripe/create")
    public ResponseEntity<?> createStripePayment(@RequestBody PaymentRequestView request) {
        logger.info("Création d'un paiement Stripe");
        
        try {
            com.weight.model.Payment payment = paymentService.processStripePayment(
                request.getUserId(),
                request.getToken(),
                request.getAmount(),
                request.getColisId(),
                request.getPriseId(),
                request.getDescription()
            );
            
            PaymentResponseView response = new PaymentResponseView();
            response.setPaymentId(payment.getIdPayment());
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getPaymentStatus());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (StripeException e) {
            logger.error("Erreur Stripe: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/orangemoney/create")
    public ResponseEntity<?> createOrangeMoneyPayment(@RequestBody PaymentRequestView request) {
        logger.info("Création d'un paiement Orange Money");
        
        try {
            com.weight.model.Payment payment = paymentService.processOrangeMoneyPayment(
                request.getUserId(),
                request.getPhoneNumber(),
                request.getAmount(),
                request.getColisId(),
                request.getPriseId()
            );
            
            PaymentResponseView response = new PaymentResponseView();
            response.setPaymentId(payment.getIdPayment());
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getPaymentStatus());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur Orange Money: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/status/{paymentId}")
    public ResponseEntity<?> getPaymentStatus(@PathVariable int paymentId) {
        logger.info("Vérification du statut de paiement");
        
        com.weight.model.Payment payment = paymentService.getPaymentDetails(paymentId);
        PaymentResponseView response = new PaymentResponseView();
        response.setPaymentId(payment.getIdPayment());
        response.setTransactionId(payment.getTransactionId());
        response.setStatus(payment.getPaymentStatus());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/status/{paymentId}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable int paymentId, @RequestParam String status) {
        logger.info("Mise à jour du statut de paiement");
        
        com.weight.model.Payment payment = paymentService.updatePaymentStatus(paymentId, status);
        PaymentResponseView response = new PaymentResponseView();
        response.setPaymentId(payment.getIdPayment());
        response.setTransactionId(payment.getTransactionId());
        response.setStatus(payment.getPaymentStatus());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPayments(@PathVariable String userId) {
        logger.info("Récupération des paiements d'un utilisateur");
        
        List<com.weight.model.Payment> payments = paymentService.getUserPayments(userId);
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @GetMapping("/statistics")
    public ResponseEntity<?> getPaymentStatistics() {
        logger.info("Récupération des statistiques de paiement");
        
        Map<String, Object> statistics = new HashMap<>();
        statistics.put("totalPayments", paymentService.countTotalPayments());
        statistics.put("totalRevenue", paymentService.getTotalRevenue());
        
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}