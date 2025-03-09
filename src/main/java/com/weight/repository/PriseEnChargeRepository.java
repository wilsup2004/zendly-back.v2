package com.weight.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.Colis;
import com.weight.model.PriseEnCharge;
import com.weight.model.Users;

public interface PriseEnChargeRepository extends JpaRepository<PriseEnCharge,Integer>   {
	
	List<PriseEnCharge> findByUsers(Users user);
	
	PriseEnCharge findByColis(Colis colis);
	
	String reqLstByStatut = "SELECT * FROM prise_en_charge "
			+ "WHERE ID_STATUT = :statut ";
	@Query(value= reqLstByStatut, nativeQuery = true)
	List<PriseEnCharge> findLstByStatut(@Param("statut") Integer statut);
	
	String reqLstEnCoursByUser = "SELECT * FROM prise_en_charge "
			+ "WHERE ID_USER_PRENEUR = :idUser "
			+ "AND ID_STATUT <= 8 ";
	@Query(value=reqLstEnCoursByUser, nativeQuery = true)
	List<PriseEnCharge> getLstEnCoursByUser(@Param("idUser") String idUser);

	String reqLstByUserAndStatut = "SELECT * FROM prise_en_charge "
			+ "WHERE ID_USER_PRENEUR = :idUser "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByUserAndStatut, nativeQuery = true)
	List<PriseEnCharge> getLstByUserAndStatut(@Param("idUser") String idUser,@Param("statut") Integer statut);

}
