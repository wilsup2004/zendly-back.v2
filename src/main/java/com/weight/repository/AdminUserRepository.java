// src/main/java/com/weight/repository/AdminUserRepository.java
package com.weight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.AdminUser;
import com.weight.model.Users;

import java.util.List;
import java.util.Optional;

public interface AdminUserRepository extends JpaRepository<AdminUser, Integer> {
    
    Optional<AdminUser> findByUser(Users user);
    
    @Query("SELECT a FROM AdminUser a WHERE a.user.idUser = :userId")
    Optional<AdminUser> findByUserId(@Param("userId") String userId);
    
    List<AdminUser> findByAdminLevel(Integer adminLevel);
}
