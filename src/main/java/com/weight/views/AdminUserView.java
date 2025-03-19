// src/main/java/com/weight/views/AdminUserView.java
package com.weight.views;

import java.util.Date;

/**
 * Vue pour la gestion des utilisateurs administratifs
 */
public class AdminUserView {
    
    private int adminId;
    private String userId;
    private int adminLevel;
    private Date creationDate;
    private Date lastLogin;
    
    // Constructeur par défaut
    public AdminUserView() {
    }
    
    // Constructeur avec paramètres de base
    public AdminUserView(String userId, int adminLevel) {
        this.userId = userId;
        this.adminLevel = adminLevel;
    }
    
    // Constructeur complet
    public AdminUserView(int adminId, String userId, int adminLevel, Date creationDate, Date lastLogin) {
        this.adminId = adminId;
        this.userId = userId;
        this.adminLevel = adminLevel;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
    }
    
    // Getters et Setters
    public int getAdminId() {
        return adminId;
    }
    
    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public int getAdminLevel() {
        return adminLevel;
    }
    
    public void setAdminLevel(int adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Date getLastLogin() {
        return lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
}
