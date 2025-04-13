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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.weight.model.Payment;
import com.weight.model.PaymentMethod;
import com.weight.services.PaymentService;
import com.weight.views.PaymentRequestView;
import com.weight.views.PaymentResponseView;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private PaymentService paymentService;
    
    
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentById(@PathVariable int paymentId) {
        logger.info("Récupération des détails du paiement ID: {}", paymentId);
        
        try {
        	
            Payment payment = paymentService.getPaymentDetails(paymentId);
            PaymentResponseView response = new PaymentResponseView(payment);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            logger.error("Paiement non trouvé: {}", paymentId);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Paiement non trouvé: " + paymentId);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du paiement: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/check-needed")
    public ResponseEntity<?> isPaymentNeeded(@RequestParam int idPrise,@RequestParam int idColis) {
    	boolean isNeed = true;
        logger.info("Récupération de l'existence d'un payement entre un colis et une prise en charge");
        Payment pay = paymentService.getPaymentByIdPriseAndIdColis(idPrise, idColis);
        if(pay != null)
        	isNeed = false;
        return new ResponseEntity<>(isNeed, HttpStatus.OK);
    }
    
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(@RequestBody Map<String, Object> request) {
        logger.info("Initialisation d'un paiement");
        
        try {
            // Récupérer les paramètres
            int colisId = Integer.parseInt(request.get("colisId").toString());
            int priseId = Integer.parseInt(request.get("priseId").toString());
            
            // Vérifier si un paiement existe déjà
            Payment existingPayment = paymentService.getPaymentByIdPriseAndIdColis(priseId, colisId);
            if (existingPayment != null) {
                // Si le paiement existe déjà, renvoyer ses détails
                PaymentResponseView response = new PaymentResponseView();
                response.setPaymentId(existingPayment.getIdPayment());
                response.setTransactionId(existingPayment.getTransactionId());
                response.setStatus(existingPayment.getPaymentStatus());
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
            
            // Créer un paiement temporaire sans méthode de paiement
            Payment newPayment = paymentService.createTemporaryPayment(colisId, priseId);
            
            PaymentResponseView response = new PaymentResponseView();
            response.setPaymentId(newPayment.getIdPayment());
            response.setTransactionId(newPayment.getTransactionId());
            response.setStatus(newPayment.getPaymentStatus());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de l'initialisation du paiement: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    
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
        	 Payment payment = paymentService.processPaypalPayment(
                request.getUserId(),
                request.getAmount().doubleValue(),
                request.getColisId(),
                request.getPriseId(),
                request.getReturnUrl(),
                request.getCancelUrl(),
                null
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
    
 // Dans PaymentController.java, ajouter cette méthode
    @PostMapping("/paypal/capture")
    public ResponseEntity<?> capturePaypalPayment(@RequestBody Map<String, Object> requestData) {
        logger.info("Capture d'un paiement PayPal");
        String methodeName = "PayPal";
        
        try {
            // Extraire les données de la requête
            String userId = (String) requestData.get("userId");
            Integer colisId = (Integer) requestData.get("colisId");
            Integer priseId = (Integer) requestData.get("priseId");
            Integer paymentId = (Integer) requestData.get("paymentId"); 
            String transactionId = (String) requestData.get("transactionId");
            Double amount = Double.valueOf(requestData.get("amount").toString());
            
            // Vérifier les paramètres essentiels
            if (colisId == null || priseId == null) {
                return new ResponseEntity<>("Paramètres manquants", HttpStatus.BAD_REQUEST);
            }
            
            // Utiliser les méthodes existantes pour traiter le paiement
            Payment payment;
            
            // Si un paiement existant est spécifié, le mettre à jour
            if (paymentId != null) {
                payment = paymentService.getPaymentDetails(paymentId);
                payment.setTransactionId(transactionId);
                payment.setPaymentStatus("COMPLETED");
                payment = paymentService.updatePaymentStatus(paymentId, "COMPLETED");
                
                // Compléter le paiement (met à jour aussi les statuts du colis et de la prise en charge)
                paymentService.completePayment(paymentId,methodeName);
            } else {
                // Sinon, créer un nouveau paiement PayPal en utilisant la méthode existante
                // Note: on utilise un try/catch spécifique car la méthode peut lancer une exception PayPalRESTException
                try {
                    payment = paymentService.processPaypalPayment(
                        userId, 
                        amount, 
                        colisId, 
                        priseId,
                        (String) requestData.get("returnUrl"),
                        (String) requestData.get("cancelUrl"),
                        null // Pas d'ID de paiement existant
                    );
                    
                    // Mettre à jour le statut à COMPLETED
                    payment = paymentService.updatePaymentStatus(payment.getIdPayment(), "COMPLETED");
                    
                    // Compléter le paiement
                    paymentService.completePayment(payment.getIdPayment(),methodeName);
                } catch (PayPalRESTException e) {
                    logger.error("Erreur PayPal: " + e.getMessage());
                    Map<String, String> error = new HashMap<>();
                    error.put("error", e.getMessage());
                    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
            
            // Préparer la réponse
            PaymentResponseView response = new PaymentResponseView();
            response.setPaymentId(payment.getIdPayment());
            response.setTransactionId(payment.getTransactionId());
            response.setStatus(payment.getPaymentStatus());
            
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la capture du paiement PayPal: " + e.getMessage(), e);
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/stripe/create")
    public ResponseEntity<?> createStripePayment(@RequestBody PaymentRequestView request) {
        logger.info("Création d'un paiement Stripe");
        
        try {
            Payment payment = paymentService.processStripePayment(
                request.getUserId(),
                request.getToken(),
                request.getAmount().doubleValue(),
                request.getColisId(),
                request.getPriseId(),
                request.getDescription(),
                null
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
            Payment payment = paymentService.processOrangeMoneyPayment(
                request.getUserId(),
                request.getPhoneNumber(),
                request.getAmount().doubleValue(),
                request.getColisId(),
                request.getPriseId(),
                null
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
        
        Payment payment = paymentService.getPaymentDetails(paymentId);
        PaymentResponseView response = new PaymentResponseView();
        response.setPaymentId(payment.getIdPayment());
        response.setTransactionId(payment.getTransactionId());
        response.setStatus(payment.getPaymentStatus());
        
     // Ajouter les informations de frais
        response.setBaseAmount(payment.getBaseAmount());
        response.setServiceFees(payment.getServiceFees());
        response.setPaymentAmount(payment.getPaymentAmount());
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PatchMapping("/status/{paymentId}")
    public ResponseEntity<?> updatePaymentStatus(@PathVariable int paymentId, @RequestParam String status) {
        logger.info("Mise à jour du statut de paiement");
        
        Payment payment = paymentService.updatePaymentStatus(paymentId, status);
        PaymentResponseView response = new PaymentResponseView(payment);
        /*
        response.setPaymentId(payment.getIdPayment());
        response.setTransactionId(payment.getTransactionId());
        response.setStatus(payment.getPaymentStatus());
        */
        
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserPayments(@PathVariable String userId) {
        logger.info("Récupération des paiements d'un utilisateur");
        
        List<Payment> payments = paymentService.getUserPayments(userId);
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
    
    @GetMapping("/statistics/revenue")
    public ResponseEntity<?> getRevenueStatistics() {
        logger.info("Récupération des statistiques de revenus");
        
        Map<String, Object> statistics = paymentService.getPaymentStatistics();
        
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}