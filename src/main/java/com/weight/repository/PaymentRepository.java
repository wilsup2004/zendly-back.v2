// src/main/java/com/weight/repository/PaymentRepository.java (mise à jour avec des méthodes supplémentaires)
package com.weight.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.Payment;
import com.weight.model.Users;
import com.weight.model.Colis;
import com.weight.model.PriseEnCharge;

import java.util.Date;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    
    List<Payment> findByUser(Users user);
    List<Payment> findByColis(Colis colis);
    List<Payment> findByPriseEnCharge(PriseEnCharge priseEnCharge);
    List<Payment> findByPaymentStatus(String status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentStatus = :status")
    List<Payment> findByStatus(@Param("status") String status);
    
    @Query("SELECT p FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    List<Payment> findByDateRange(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p")
    Long countTotalPayments();
    
    @Query("SELECT SUM(p.paymentAmount) FROM Payment p WHERE p.paymentStatus = 'COMPLETED'")
    Double sumCompletedPayments();
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentDate BETWEEN :startDate AND :endDate")
    Long countPaymentsInPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentMethod.idMethod = :methodId")
    Long countPaymentsByMethod(@Param("methodId") int methodId);
    
    @Query("SELECT COUNT(p) FROM Payment p WHERE p.paymentStatus = :status")
    Long countByPaymentStatus(@Param("status") String status);
    
    @Query("SELECT MONTH(p.paymentDate), SUM(p.paymentAmount) FROM Payment p " +
           "WHERE YEAR(p.paymentDate) = :year AND p.paymentStatus = 'COMPLETED' " +
           "GROUP BY MONTH(p.paymentDate) ORDER BY MONTH(p.paymentDate)")
    List<Object[]> monthlyRevenue(@Param("year") int year);
    
    @Query("SELECT p.paymentMethod.methodName, COUNT(p) FROM Payment p " +
           "GROUP BY p.paymentMethod.methodName")
    List<Object[]> countByPaymentMethod();
    
    @Query("SELECT p.paymentStatus, COUNT(p) FROM Payment p " +
           "GROUP BY p.paymentStatus")
    List<Object[]> countByStatus();
}
