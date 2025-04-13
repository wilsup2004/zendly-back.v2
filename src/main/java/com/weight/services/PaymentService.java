// src/main/java/com/weight/services/PaymentService.java
package com.weight.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.weight.model.Colis;
import com.weight.model.Payment;
import com.weight.model.PaymentMethod;
import com.weight.model.PriseEnCharge;
import com.weight.model.Statuts;
import com.weight.model.Users;
import com.weight.repository.AppConfigRepository;
import com.weight.repository.ColisRepository;
import com.weight.repository.PaymentMethodRepository;
import com.weight.repository.PaymentRepository;
import com.weight.repository.PriseEnChargeRepository;
import com.weight.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PaymentService {
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private ColisRepository colisRepository;
    
    @Autowired
    private PriseEnChargeRepository priseEnChargeRepository;
    
    @Autowired
    private AppConfigRepository appConfigRepository;
    
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
    
    @Autowired
    private AppConfigService appConfigService;
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    
    
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }
    
    public List<PaymentMethod> getActivePaymentMethods() {
        return paymentMethodRepository.findByIsActiveTrue();
    }
    
    
    
    /**
     * Récupère tous les paiements
     */
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    /**
     * Récupère un paiement par son ID
     */
    public Payment getPaymentDetails(int paymentId) {
        Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
        if (!paymentOpt.isPresent()) {
            throw new EntityNotFoundException("Paiement non trouvé: " + paymentId);
        }
        return paymentOpt.get();
    }
    
    /**
     * Récupère les paiements d'un utilisateur
     */
    public List<Payment> getUserPayments(String userId) {
        Users user = new Users();
        user.setIdUser(userId);
        return paymentRepository.findByUser(user);
    }
    
    
    /**
     * Récupère un paiement pour une prise en charge et un colis
     */
    public Payment getPaymentByIdPriseAndIdColis(int idPrise, int idColis) {
        return paymentRepository.findByIdPriseAndIdColis(idPrise, idColis);
    }
    
    /**
     * Met à jour le statut d'un paiement
     */
    @Transactional
    public Payment updatePaymentStatus(int paymentId, String status) {
        Payment payment = getPaymentDetails(paymentId);
        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }
    
    /**
     * Calcule les statistiques de paiement
     */
    public Map<String, Object> getPaymentStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPayments", countTotalPayments());
        stats.put("totalRevenue", getTotalRevenue());
        // Ajouter d'autres statistiques au besoin
        return stats;
    }
    
    /**
     * Compte le nombre total de paiements
     */
    public long countTotalPayments() {
        return paymentRepository.count();
    }
    
    /**
     * Calcule le revenu total
     */
    public double getTotalRevenue() {
        List<Payment> completedPayments = paymentRepository.findByPaymentStatus("COMPLETED");
        return completedPayments.stream()
                .mapToDouble(p -> p.getPaymentAmount().doubleValue())
                .sum();
    }
    
    /**
     * Crée un paiement temporaire sans méthode définie
     */
    @Transactional
    public Payment createTemporaryPayment(int colisId, int priseId) {
        // Récupérer le colis et la prise en charge
        Optional<Colis> colisOpt = colisRepository.findById(colisId);
        if (!colisOpt.isPresent()) {
            throw new EntityNotFoundException("Colis non trouvé avec l'ID: " + colisId);
        }
        Colis colis = colisOpt.get();
        
        Optional<PriseEnCharge> priseOpt = priseEnChargeRepository.findById(priseId);
        if (!priseOpt.isPresent()) {
            throw new EntityNotFoundException("Prise en charge non trouvée avec l'ID: " + priseId);
        }
        PriseEnCharge prise = priseOpt.get();
        
        // Vérifier si un paiement existe déjà
        Payment existingPayment = getPaymentByIdPriseAndIdColis(priseId, colisId);
        if (existingPayment != null) {
            return existingPayment;
        }
        
        // Créer un nouveau paiement sans méthode pour l'instant
        Payment payment = new Payment();
        payment.setColis(colis);
        payment.setPriseEnCharge(prise);
        payment.setUser(colis.getUsers());
        payment.setPaymentStatus("PENDING_METHOD_SELECTION");
        payment.setPaymentDate(new Date());
        payment.setTransactionId("TEMP" + System.currentTimeMillis());
        
        // Définir les montants
        Double baseAmount = colis.getTarif().doubleValue();
        Double serviceFees = calculateServiceFees(baseAmount);
        payment.setBaseAmount(baseAmount);
        payment.setServiceFees(serviceFees);
        payment.setPaymentAmount(new BigDecimal(baseAmount + serviceFees));
        
        // Important: Ne pas définir de méthode de paiement ici
        
        return paymentRepository.save(payment);
    }
    
    /**
     * Calcule les frais de service
     */
    private Double calculateServiceFees(Double baseAmount) {
    	String key = "serviceFeesPercentage";
    	double frais = Double.valueOf(appConfigRepository.findByParamKey(key).get().getParamValue() );
        return (baseAmount * frais)/100;
    }
    
    /**
     * Traite un paiement PayPal
     */
    @Transactional
    public Payment processPaypalPayment(String userId, Double amount, Integer colisId, 
                                      Integer priseId, String returnUrl, String cancelUrl, 
                                      Integer paymentId) throws PayPalRESTException {
        try {
            Payment payment;
            
            if (paymentId != null) {
                // Récupérer et mettre à jour le paiement existant
                Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
                if (!paymentOpt.isPresent()) {
                    throw new EntityNotFoundException("Paiement non trouvé avec l'ID: " + paymentId);
                }
                payment = paymentOpt.get();
                
                // Mettre à jour avec la méthode PayPal
                PaymentMethod paypalMethod = paymentMethodRepository.findByMethodName("PayPal");
                if (paypalMethod == null) {
                    throw new EntityNotFoundException("Méthode PayPal non trouvée");
                }
                
                payment.setPaymentMethod(paypalMethod);
                
            } else {
                // Créer un nouveau paiement complet
                Users user = userRepository.findById(userId).orElse(null);
                if (user == null) {
                    throw new EntityNotFoundException("Utilisateur non trouvé: " + userId);
                }
                
                Optional<Colis> colisOpt = colisRepository.findById(colisId);
                if (!colisOpt.isPresent()) {
                    throw new EntityNotFoundException("Colis non trouvé: " + colisId);
                }
                Colis colis = colisOpt.get();
                
                Optional<PriseEnCharge> priseOpt = priseEnChargeRepository.findById(priseId);
                if (!priseOpt.isPresent()) {
                    throw new EntityNotFoundException("Prise en charge non trouvée: " + priseId);
                }
                PriseEnCharge prise = priseOpt.get();
                
                PaymentMethod paypalMethod = paymentMethodRepository.findByMethodName("PayPal");
                if (paypalMethod == null) {
                    throw new EntityNotFoundException("Méthode PayPal non trouvée");
                }
                
                payment = new Payment();
                payment.setUser(user);
                payment.setColis(colis);
                payment.setPriseEnCharge(prise);
                payment.setPaymentMethod(paypalMethod);
                
                // Définir les montants
                Double baseAmount = colis.getTarif().doubleValue();
                Double serviceFees = calculateServiceFees(baseAmount);
                payment.setBaseAmount(baseAmount);
                payment.setServiceFees(serviceFees);
                payment.setPaymentAmount(new BigDecimal(amount));
            }
            
            // Configuration spécifique à PayPal
            payment.setPaymentStatus("PENDING");
            payment.setTransactionId("PP" + System.currentTimeMillis());
            payment.setPaymentDate(new Date());
            payment.setPaymentDetails("returnUrl: " + returnUrl + ", cancelUrl: " + cancelUrl);
            
            // Ici, vous intégreration de l'API PayPal pour créer un paiement PayPal
             
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
            
            return paymentRepository.saveAndFlush(payment);
        } catch (Exception e) {
            logger.error("Erreur lors du traitement du paiement PayPal", e);
            throw new PayPalRESTException(e.getMessage());
        }
    }
    
    /**
     * Traite un paiement Stripe
     */
    @Transactional
    public Payment processStripePayment(String userId, String token, Double amount, 
                                      Integer colisId, Integer priseId, String description,
                                      Integer paymentId) throws StripeException {
        try {
            Payment payment;
            
            if (paymentId != null) {
                // Récupérer et mettre à jour le paiement existant
                Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
                if (!paymentOpt.isPresent()) {
                    throw new EntityNotFoundException("Paiement non trouvé avec l'ID: " + paymentId);
                }
                payment = paymentOpt.get();
                
                // Mettre à jour avec la méthode Stripe
                PaymentMethod stripeMethod = paymentMethodRepository.findByMethodName("Stripe");
                if (stripeMethod == null) {
                    throw new EntityNotFoundException("Méthode Stripe non trouvée");
                }
                
                payment.setPaymentMethod(stripeMethod);
            } else {
                // Créer un nouveau paiement complet
                Users user = userRepository.findById(userId).orElse(null);
                if (user == null) {
                    throw new EntityNotFoundException("Utilisateur non trouvé: " + userId);
                }
                
                Optional<Colis> colisOpt = colisRepository.findById(colisId);
                if (!colisOpt.isPresent()) {
                    throw new EntityNotFoundException("Colis non trouvé: " + colisId);
                }
                Colis colis = colisOpt.get();
                
                Optional<PriseEnCharge> priseOpt = priseEnChargeRepository.findById(priseId);
                if (!priseOpt.isPresent()) {
                    throw new EntityNotFoundException("Prise en charge non trouvée: " + priseId);
                }
                PriseEnCharge prise = priseOpt.get();
                
                PaymentMethod stripeMethod = paymentMethodRepository.findByMethodName("Stripe");
                if (stripeMethod == null) {
                    throw new EntityNotFoundException("Méthode Stripe non trouvée");
                }
                
                payment = new Payment();
                payment.setUser(user);
                payment.setColis(colis);
                payment.setPriseEnCharge(prise);
                payment.setPaymentMethod(stripeMethod);
                
                // Définir les montants
                Double baseAmount = colis.getTarif().doubleValue();
                Double serviceFees = calculateServiceFees(baseAmount);
                payment.setBaseAmount(baseAmount);
                payment.setServiceFees(serviceFees);
                payment.setPaymentAmount(new BigDecimal(amount));
            }
            
            // Configuration spécifique à Stripe
            payment.setPaymentStatus("PENDING");
            payment.setTransactionId("ST" + System.currentTimeMillis());
            payment.setPaymentDate(new Date());
            payment.setPaymentDetails("token: " + token + ", description: " + description);
            
            // Ici, vous intégreriez l'API Stripe pour créer un paiement Stripe
            // Ex: stripeClient.createCharge(...)
            
        	// Créer la charge Stripe
            Charge charge = stripeService.createCharge(token, new BigDecimal(amount), "EUR", description);
            
            
            return paymentRepository.save(payment);
            
        } catch (Exception e) {
            logger.error("Erreur lors du traitement du paiement Stripe", e);
            throw e ;
        }
    }
    
    /**
     * Traite un paiement Orange Money / Mobile Money
     */
    @Transactional
    public Payment processOrangeMoneyPayment(String userId, String phoneNumber, Double amount, 
                                           Integer colisId, Integer priseId, Integer paymentId) {
        try {
            Payment payment;
            
            if (paymentId != null) {
                // Récupérer et mettre à jour le paiement existant
                Optional<Payment> paymentOpt = paymentRepository.findById(paymentId);
                if (!paymentOpt.isPresent()) {
                    throw new EntityNotFoundException("Paiement non trouvé avec l'ID: " + paymentId);
                }
                payment = paymentOpt.get();
                
                // Mettre à jour avec la méthode Orange Money
                PaymentMethod orangeMethod = paymentMethodRepository.findByMethodName("Orange Money");
                if (orangeMethod == null) {
                    throw new EntityNotFoundException("Méthode Orange Money non trouvée");
                }
                
                payment.setPaymentMethod(orangeMethod);
            } else {
                // Créer un nouveau paiement complet
                Users user = userRepository.findById(userId).orElse(null);
                if (user == null) {
                    throw new EntityNotFoundException("Utilisateur non trouvé: " + userId);
                }
                
                Optional<Colis> colisOpt = colisRepository.findById(colisId);
                if (!colisOpt.isPresent()) {
                    throw new EntityNotFoundException("Colis non trouvé: " + colisId);
                }
                Colis colis = colisOpt.get();
                
                Optional<PriseEnCharge> priseOpt = priseEnChargeRepository.findById(priseId);
                if (!priseOpt.isPresent()) {
                    throw new EntityNotFoundException("Prise en charge non trouvée: " + priseId);
                }
                PriseEnCharge prise = priseOpt.get();
                
                PaymentMethod orangeMethod = paymentMethodRepository.findByMethodName("Orange Money");
                if (orangeMethod == null) {
                    throw new EntityNotFoundException("Méthode Orange Money non trouvée");
                }
                
                payment = new Payment();
                payment.setUser(user);
                payment.setColis(colis);
                payment.setPriseEnCharge(prise);
                payment.setPaymentMethod(orangeMethod);
                
                // Définir les montants
                Double baseAmount = colis.getTarif().doubleValue();
                Double serviceFees = calculateServiceFees(baseAmount);
                payment.setBaseAmount(baseAmount);
                payment.setServiceFees(serviceFees);
                payment.setPaymentAmount(new BigDecimal(amount));
            }
            
            // Configuration spécifique à Orange Money
            payment.setPaymentStatus("PENDING_CONFIRMATION");
            payment.setTransactionId("OM" + System.currentTimeMillis());
            payment.setPaymentDate(new Date());
            payment.setPaymentDetails("phoneNumber: " + phoneNumber);
            
            // Ici, vous intégreriez l'API Orange Money pour créer un paiement
            // Ex: orangeMoneyClient.requestPayment(...)
            
            // Générer une référence unique
            String reference = "ZDL-" + System.currentTimeMillis();
            
            // Initier le paiement Orange Money
            Map<String, Object> response = orangeMoneyService.initiatePayment(phoneNumber, new BigDecimal(amount), reference);
            
            
            return paymentRepository.save(payment);
        } catch (Exception e) {
            logger.error("Erreur lors du traitement du paiement Orange Money", e);
            throw new RuntimeException("Erreur lors du traitement du paiement Orange Money: " + e.getMessage());
        }
    }
    
    /**
     * Vérifie le statut d'un paiement mobile
     */
    public boolean checkMobilePaymentStatus(String transactionId) {
        // Intégration avec l'API Orange Money pour vérifier le statut de la transaction
        // Retourne true si le paiement est confirmé
        return false; // À implémenter avec l'API réelle
    }
    
    /**
     * Marque un paiement comme complété et met à jour les entités associées
     */
    @Transactional
    public void completePayment(int paymentId,String methodeName) {
    	
    	PaymentMethod method = paymentMethodRepository.findByMethodName(methodeName);
        
        Payment payment = getPaymentDetails(paymentId);
        payment.setPaymentMethod(method);
        payment.setPaymentStatus("COMPLETED");
        paymentRepository.save(payment);
        
        // Mettre à jour le statut du colis
        Colis colis = payment.getColis();
        if (colis != null) {
            Statuts enCoursStatus = new Statuts();
            enCoursStatus.setIdStatut(2); // ID du statut "En cours"
            enCoursStatus.setLibelStatut("En cours");
            colis.setStatuts(enCoursStatus);
            colisRepository.save(colis);
        }
        
        // Mettre à jour le statut de la prise en charge
        PriseEnCharge prise = payment.getPriseEnCharge();
        if (prise != null) {
            Statuts accepteStatus = new Statuts();
            accepteStatus.setIdStatut(5); // ID du statut "Accepté"
            accepteStatus.setLibelStatut("Accepté");
            prise.setStatuts(accepteStatus);
            priseEnChargeRepository.save(prise);
        }
    }

   
   
    
    /**
     * Crée un paiement initial pour une prise en charge de colis
     * @param colisId ID du colis
     * @param priseId ID de la prise en charge
     * @return Le paiement créé
     */
    public Payment createInitialPayment(int colisId, int priseId) {
        // Récupérer le colis et la prise en charge
        Colis colis = colisRepository.findById(colisId).orElseThrow();
        PriseEnCharge prise = priseEnChargeRepository.findById(priseId).orElseThrow();
        
        // Créer un nouveau paiement
        Payment payment = new Payment();
        payment.setColis(colis);
        payment.setPriseEnCharge(prise);
        payment.setUser(colis.getUsers()); // L'utilisateur qui paie est le propriétaire du colis
        payment.setPaymentStatus("CREATED");
        payment.setPaymentDate(new Date());
        payment.setTransactionId(generateTransactionId());
        
        // Définir les montants
        payment.setBaseAmount(colis.getTarif().doubleValue());
        payment.setServiceFees(calculateServiceFees(colis.getTarif().doubleValue()));
        payment.setPaymentAmount(new BigDecimal(colis.getTarif().doubleValue() + calculateServiceFees(colis.getTarif().doubleValue())));
        
        // Sauvegarder et retourner le paiement
        return paymentRepository.save(payment);
    }
    
    private double calculateServiceFees(double baseAmount) {
    	String key = "serviceFeesPercentage";
    	double frais = Double.valueOf(appConfigRepository.findByParamKey(key).get().getParamValue() );
        return baseAmount * frais;
    }

    private String generateTransactionId() {
        return "TX" + System.currentTimeMillis();
    }
    
}