// src/main/java/com/weight/repository/AdminLogRepository.java
package com.weight.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.AdminLog;
import com.weight.model.AdminUser;

import java.util.Date;
import java.util.List;

public interface AdminLogRepository extends JpaRepository<AdminLog, Integer> {
    
    List<AdminLog> findByAdminUser(AdminUser adminUser);
    
    List<AdminLog> findByActionType(String actionType);
    
    @Query("SELECT l FROM AdminLog l WHERE l.actionDate BETWEEN :startDate AND :endDate")
    List<AdminLog> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT l FROM AdminLog l WHERE l.adminUser.idAdmin = :adminId")
    List<AdminLog> findByAdminId(@Param("adminId") int adminId);
    
    Page<AdminLog> findAll(Pageable pageable);
}