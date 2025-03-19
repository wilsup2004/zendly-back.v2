// src/main/java/com/weight/services/AdminService.java
package com.weight.services;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weight.model.AdminLog;
import com.weight.model.AdminUser;
import com.weight.model.Colis;
import com.weight.model.Payment;
import com.weight.model.PaymentMethod;
import com.weight.model.Statuts;
import com.weight.model.Users;
import com.weight.repository.AdminLogRepository;
import com.weight.repository.AdminUserRepository;
import com.weight.repository.ColisRepository;
import com.weight.repository.PaymentMethodRepository;
import com.weight.repository.PaymentRepository;
import com.weight.repository.PriseEnChargeRepository;
import com.weight.repository.StatutsRepository;
import com.weight.repository.UserRepository;

@Service
public class AdminService {
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    @Autowired
    private AdminLogRepository adminLogRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ColisRepository colisRepository;
    
    @Autowired
    private PriseEnChargeRepository priseEnChargeRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    
    @Autowired
    private StatutsRepository statutsRepository;
    
    // ======= Gestion des utilisateurs admin =======
    
    public List<AdminUser> getAllAdmins() {
        return adminUserRepository.findAll();
    }
    
    public Optional<AdminUser> getAdminById(int adminId) {
        return adminUserRepository.findById(adminId);
    }
    
    public Optional<AdminUser> getAdminByUserId(String userId) {
        return adminUserRepository.findByUserId(userId);
    }
    
    @Transactional
    public AdminUser createAdmin(String userId, int adminLevel) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Vérifier si l'utilisateur est déjà admin
        Optional<AdminUser> existingAdmin = adminUserRepository.findByUser(user);
        if (existingAdmin.isPresent()) {
            throw new RuntimeException("L'utilisateur est déjà administrateur");
        }
        
        AdminUser adminUser = new AdminUser();
        adminUser.setUser(user);
        adminUser.setAdminLevel(adminLevel);
        adminUser.setCreationDate(new Date());
        
        return adminUserRepository.save(adminUser);
    }
    
    @Transactional
    public AdminUser updateAdminLevel(int adminId, int newLevel) {
        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé"));
        
        adminUser.setAdminLevel(newLevel);
        return adminUserRepository.save(adminUser);
    }
    
    @Transactional
    public void removeAdmin(int adminId) {
        adminUserRepository.deleteById(adminId);
    }
    
    @Transactional
    public void updateLastLogin(int adminId) {
        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé"));
        
        adminUser.setLastLogin(new Date());
        adminUserRepository.save(adminUser);
    }
    
    // ======= Logging des actions administratives =======
    
    @Transactional
    public AdminLog logAdminAction(int adminId, String actionType, String actionDetails) {
        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Administrateur non trouvé"));
        
        AdminLog log = new AdminLog();
        log.setAdminUser(adminUser);
        log.setActionType(actionType);
        log.setActionDetails(actionDetails);
        log.setActionDate(new Date());
        
        return adminLogRepository.save(log);
    }
    
    public List<AdminLog> getAdminLogs(int adminId) {
        return adminLogRepository.findByAdminId(adminId);
    }
    
    public Page<AdminLog> getAdminLogsPaginated(Pageable pageable) {
        return adminLogRepository.findAll(pageable);
    }
    
    public List<AdminLog> getLogsByActionType(String actionType) {
        return adminLogRepository.findByActionType(actionType);
    }
    
    public List<AdminLog> getLogsByDateRange(Date startDate, Date endDate) {
        return adminLogRepository.findByDateRange(startDate, endDate);
    }
    
    // ======= Statistiques pour le Dashboard =======
    
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Statistiques des utilisateurs
        stats.put("totalUsers", userRepository.count());
        
        // Statistiques des colis
        stats.put("totalColis", colisRepository.count());
        
        // Statistiques des prises en charge
        stats.put("totalPriseEnCharge", priseEnChargeRepository.count());
        
        // Statistiques des paiements
        stats.put("totalPayments", paymentRepository.countTotalPayments());
        stats.put("totalRevenue", paymentRepository.sumCompletedPayments());
        
        return stats;
    }
    
    public Map<String, Object> getDetailedStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Stats utilisateurs
        stats.put("totalUsers", userRepository.count());
        
        // Stats colis
        stats.put("totalColis", colisRepository.count());
        
        // Colis par statut
        Map<String, Long> colisByStatus = new HashMap<>();
        List<Statuts> statuts = statutsRepository.findAll();
        for (Statuts statut : statuts) {
            Long count = colisRepository.countByStatuts(statut);
            colisByStatus.put(statut.getLibelStatut(), count);
        }
        stats.put("colisByStatus", colisByStatus);
        
        // Stats paiements
        stats.put("totalPayments", paymentRepository.countTotalPayments());
        stats.put("completedPayments", paymentRepository.countByPaymentStatus("COMPLETED"));
        stats.put("pendingPayments", paymentRepository.countByPaymentStatus("PENDING"));
        stats.put("failedPayments", paymentRepository.countByPaymentStatus("FAILED"));
        stats.put("totalRevenue", paymentRepository.sumCompletedPayments());
        
        // Paiements par méthode
        Map<String, Long> paymentsByMethod = new HashMap<>();
        List<PaymentMethod> methods = paymentMethodRepository.findAll();
        for (PaymentMethod method : methods) {
            Long count = paymentRepository.countPaymentsByMethod(method.getIdMethod());
            paymentsByMethod.put(method.getMethodName(), count);
        }
        stats.put("paymentsByMethod", paymentsByMethod);
        
        return stats;
    }
    
    // ======= Gestion des utilisateurs =======
    
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Transactional
    public void disableUser(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // Ajoutez un champ 'isActive' dans la classe Users pour gérer l'état
        // user.setIsActive(false);
        userRepository.save(user);
    }
    
    @Transactional
    public void enableUser(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        // user.setIsActive(true);
        userRepository.save(user);
    }
    
    // ======= Gestion des colis =======
    
    public List<Colis> getAllColis() {
        return colisRepository.findAll();
    }
    
    @Transactional
    public Colis updateColisStatus(Integer colisId, Integer statutId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        
        Statuts statut = new Statuts();
        statut.setIdStatut(statutId);
        
        colis.setStatuts(statut);
        return colisRepository.save(colis);
    }
    
    // ======= Gestion des paiements =======
    
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }
    
    @Transactional
    public Payment updatePaymentStatus(Integer paymentId, String status) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé"));
        
        payment.setPaymentStatus(status);
        return paymentRepository.save(payment);
    }
}
