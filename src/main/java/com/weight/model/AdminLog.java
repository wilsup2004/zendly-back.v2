// src/main/java/com/weight/model/AdminLog.java
package com.weight.model;

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
@Table(name = "admin_logs")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdminLog implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private int idLog;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_admin")
    private AdminUser adminUser;
    
    @Column(name = "action_type", nullable = false)
    private String actionType;
    
    @Column(name = "action_details")
    private String actionDetails;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "action_date", nullable = false)
    private Date actionDate;
    
    public AdminLog() {
    }
    
    public AdminLog(int idLog, AdminUser adminUser, String actionType, Date actionDate) {
        this.idLog = idLog;
        this.adminUser = adminUser;
        this.actionType = actionType;
        this.actionDate = actionDate;
    }
    
    public AdminLog(int idLog, AdminUser adminUser, String actionType, String actionDetails, Date actionDate) {
        this.idLog = idLog;
        this.adminUser = adminUser;
        this.actionType = actionType;
        this.actionDetails = actionDetails;
        this.actionDate = actionDate;
    }
    
    // Getters and setters
    public int getIdLog() {
        return this.idLog;
    }
    
    public void setIdLog(int idLog) {
        this.idLog = idLog;
    }
    
    public AdminUser getAdminUser() {
        return this.adminUser;
    }
    
    public void setAdminUser(AdminUser adminUser) {
        this.adminUser = adminUser;
    }
    
    public String getActionType() {
        return this.actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getActionDetails() {
        return this.actionDetails;
    }
    
    public void setActionDetails(String actionDetails) {
        this.actionDetails = actionDetails;
    }
    
    public Date getActionDate() {
        return this.actionDate;
    }
    
    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }
}