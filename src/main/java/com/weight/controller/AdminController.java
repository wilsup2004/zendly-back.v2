// src/main/java/com/weight/controller/AdminController.java
package com.weight.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weight.model.AdminLog;
import com.weight.model.AdminUser;
import com.weight.model.Colis;
import com.weight.model.Payment;
import com.weight.model.Users;
import com.weight.services.AdminService;
import com.weight.views.AdminUserView;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
public class AdminController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private AdminService adminService;
    
    // ======== Dashboard et statistiques ========
    
    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardStats() {
        logger.info("Récupération des statistiques pour le dashboard administrateur");
        
        Map<String, Object> stats = adminService.getDashboardStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    @GetMapping("/stats/detailed")
    public ResponseEntity<?> getDetailedStats() {
        logger.info("Récupération des statistiques détaillées");
        
        Map<String, Object> stats = adminService.getDetailedStats();
        return new ResponseEntity<>(stats, HttpStatus.OK);
    }
    
    // ======== Gestion des administrateurs ========
    
    @GetMapping("/users")
    public ResponseEntity<?> getAllAdmins() {
        logger.info("Récupération de tous les administrateurs");
        
        List<AdminUser> admins = adminService.getAllAdmins();
        return new ResponseEntity<>(admins, HttpStatus.OK);
    }
    
    @GetMapping("/users/{adminId}")
    public ResponseEntity<?> getAdminById(@PathVariable int adminId) {
        logger.info("Récupération d'un administrateur par ID: " + adminId);
        
        Optional<AdminUser> admin = adminService.getAdminById(adminId);
        if (admin.isPresent()) {
            return new ResponseEntity<>(admin.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Administrateur non trouvé", HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/users")
    public ResponseEntity<?> createAdmin(@RequestBody AdminUserView adminUserView) {
        logger.info("Création d'un nouvel administrateur");
        
        try {
            AdminUser admin = adminService.createAdmin(adminUserView.getUserId(), adminUserView.getAdminLevel());
            
            // Logging de l'action
            adminService.logAdminAction(
                adminUserView.getAdminId(),
                "CREATE_ADMIN",
                "Création d'un nouvel administrateur: " + adminUserView.getUserId()
            );
            
            return new ResponseEntity<>(admin, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de la création d'un administrateur: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/users/{adminId}")
    public ResponseEntity<?> updateAdminLevel(
            @PathVariable int adminId,
            @RequestParam int newLevel,
            @RequestParam int adminActionId) {
        
        logger.info("Mise à jour du niveau d'un administrateur: " + adminId);
        
        try {
            AdminUser admin = adminService.updateAdminLevel(adminId, newLevel);
            
            // Logging de l'action
            adminService.logAdminAction(
                adminActionId,
                "UPDATE_ADMIN_LEVEL",
                "Mise à jour du niveau d'administrateur: " + adminId + " => " + newLevel
            );
            
            return new ResponseEntity<>(admin, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du niveau d'administrateur: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/users/{adminId}")
    public ResponseEntity<?> removeAdmin(
            @PathVariable int adminId,
            @RequestParam int adminActionId) {
        
        logger.info("Suppression d'un administrateur: " + adminId);
        
        try {
            // Logging de l'action
            adminService.logAdminAction(
                adminActionId,
                "REMOVE_ADMIN",
                "Suppression de l'administrateur: " + adminId
            );
            
            adminService.removeAdmin(adminId);
            return new ResponseEntity<>("Administrateur supprimé avec succès", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la suppression d'un administrateur: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/users/{adminId}/login")
    public ResponseEntity<?> updateLastLogin(@PathVariable int adminId) {
        logger.info("Mise à jour de la dernière connexion d'un administrateur: " + adminId);
        
        try {
            adminService.updateLastLogin(adminId);
            return new ResponseEntity<>("Dernière connexion mise à jour", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour de la dernière connexion: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // ======== Logs administratifs ========
    
    @PostMapping("/logs")
    public ResponseEntity<?> logAdminAction(
            @RequestParam int adminId,
            @RequestParam String actionType,
            @RequestParam String actionDetails) {
        
        logger.info("Enregistrement d'une action administrative: " + actionType);
        
        try {
            AdminLog log = adminService.logAdminAction(adminId, actionType, actionDetails);
            return new ResponseEntity<>(log, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Erreur lors de l'enregistrement d'une action administrative: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @GetMapping("/logs")
    public ResponseEntity<?> getAdminLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        logger.info("Récupération des logs administratifs");
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("actionDate").descending());
        Page<AdminLog> logs = adminService.getAdminLogsPaginated(pageable);
        
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
    
    @GetMapping("/logs/admin/{adminId}")
    public ResponseEntity<?> getAdminLogsByAdmin(@PathVariable int adminId) {
        logger.info("Récupération des logs pour l'administrateur: " + adminId);
        
        List<AdminLog> logs = adminService.getAdminLogs(adminId);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
    
    @GetMapping("/logs/type/{actionType}")
    public ResponseEntity<?> getLogsByActionType(@PathVariable String actionType) {
        logger.info("Récupération des logs par type d'action: " + actionType);
        
        List<AdminLog> logs = adminService.getLogsByActionType(actionType);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
    
    @GetMapping("/logs/date")
    public ResponseEntity<?> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        
        logger.info("Récupération des logs par plage de dates");
        
        List<AdminLog> logs = adminService.getLogsByDateRange(startDate, endDate);
        return new ResponseEntity<>(logs, HttpStatus.OK);
    }
    
    // ======== Gestion utilisateurs ========
    
    @GetMapping("/manage/users")
    public ResponseEntity<?> getAllUsers() {
        logger.info("Récupération de tous les utilisateurs");
        
        List<Users> users = adminService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    @PutMapping("/manage/users/{userId}/disable")
    public ResponseEntity<?> disableUser(
            @PathVariable String userId,
            @RequestParam int adminId) {
        
        logger.info("Désactivation de l'utilisateur: " + userId);
        
        try {
            adminService.disableUser(userId);
            
            // Logging de l'action
            adminService.logAdminAction(
                adminId,
                "DISABLE_USER",
                "Désactivation de l'utilisateur: " + userId
            );
            
            return new ResponseEntity<>("Utilisateur désactivé", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la désactivation de l'utilisateur: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PutMapping("/manage/users/{userId}/enable")
    public ResponseEntity<?> enableUser(
            @PathVariable String userId,
            @RequestParam int adminId) {
        
        logger.info("Activation de l'utilisateur: " + userId);
        
        try {
            adminService.enableUser(userId);
            
            // Logging de l'action
            adminService.logAdminAction(
                adminId,
                "ENABLE_USER",
                "Activation de l'utilisateur: " + userId
            );
            
            return new ResponseEntity<>("Utilisateur activé", HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de l'activation de l'utilisateur: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // ======== Gestion colis ========
    
    @GetMapping("/manage/colis")
    public ResponseEntity<?> getAllColis() {
        logger.info("Récupération de tous les colis");
        
        List<Colis> colis = adminService.getAllColis();
        return new ResponseEntity<>(colis, HttpStatus.OK);
    }
    
    @PutMapping("/manage/colis/{colisId}/status")
    public ResponseEntity<?> updateColisStatus(
            @PathVariable Integer colisId,
            @RequestParam Integer statutId,
            @RequestParam int adminId) {
        
        logger.info("Mise à jour du statut du colis: " + colisId);
        
        try {
            Colis colis = adminService.updateColisStatus(colisId, statutId);
            
            // Logging de l'action
            adminService.logAdminAction(
                adminId,
                "UPDATE_COLIS_STATUS",
                "Mise à jour du statut du colis: " + colisId + " => " + statutId
            );
            
            return new ResponseEntity<>(colis, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du statut du colis: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // ======== Gestion paiements ========
    
    @GetMapping("/manage/payments")
    public ResponseEntity<?> getAllPayments() {
        logger.info("Récupération de tous les paiements");
        
        List<Payment> payments = adminService.getAllPayments();
        return new ResponseEntity<>(payments, HttpStatus.OK);
    }
    
    @PutMapping("/manage/payments/{paymentId}/status")
    public ResponseEntity<?> updatePaymentStatus(
            @PathVariable Integer paymentId,
            @RequestParam String status,
            @RequestParam int adminId) {
        
        logger.info("Mise à jour du statut du paiement: " + paymentId);
        
        try {
            Payment payment = adminService.updatePaymentStatus(paymentId, status);
            
            // Logging de l'action
            adminService.logAdminAction(
                adminId,
                "UPDATE_PAYMENT_STATUS",
                "Mise à jour du statut du paiement: " + paymentId + " => " + status
            );
            
            return new ResponseEntity<>(payment, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du statut du paiement: " + e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}