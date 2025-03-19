// src/main/java/com/weight/model/AdminUser.java
package com.weight.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "admin_users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AdminUser implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_admin")
    private int idAdmin;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private Users user;
    
    @Column(name = "admin_level")
    private Integer adminLevel;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation_date")
    private Date creationDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Date lastLogin;
    
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adminUser")
    private Set<AdminLog> adminLogs = new HashSet<AdminLog>(0);
    
    public AdminUser() {
    }
    
    public AdminUser(int idAdmin, Users user, Integer adminLevel, Date creationDate) {
        this.idAdmin = idAdmin;
        this.user = user;
        this.adminLevel = adminLevel;
        this.creationDate = creationDate;
    }
    
    public AdminUser(int idAdmin, Users user, Integer adminLevel, Date creationDate, Date lastLogin, Set<AdminLog> adminLogs) {
        this.idAdmin = idAdmin;
        this.user = user;
        this.adminLevel = adminLevel;
        this.creationDate = creationDate;
        this.lastLogin = lastLogin;
        this.adminLogs = adminLogs;
    }
    
    // Getters and setters
    public int getIdAdmin() {
        return this.idAdmin;
    }
    
    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }
    
    public Users getUser() {
        return this.user;
    }
    
    public void setUser(Users user) {
        this.user = user;
    }
    
    public Integer getAdminLevel() {
        return this.adminLevel;
    }
    
    public void setAdminLevel(Integer adminLevel) {
        this.adminLevel = adminLevel;
    }
    
    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
    public Date getLastLogin() {
        return this.lastLogin;
    }
    
    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    public Set<AdminLog> getAdminLogs() {
        return this.adminLogs;
    }
    
    public void setAdminLogs(Set<AdminLog> adminLogs) {
        this.adminLogs = adminLogs;
    }
}