package com.weight.repository;


import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.UsersDispo;

public interface UsersDispoRepository  extends JpaRepository<UsersDispo,Integer>{

	String reqNewTransac= "INSERT INTO USERS_DISPO "
			+"( id_user_dispo,id_vol,ville_depart,aeronom_depart,date_depart,"
			+ "ville_arrivee,aeronom_arrivee,date_arrivee,nb_kilo_dispo,id_statut) " 
			+"VALUES "
			+"( :idUserDispo,:idVol,:villeDepart,:aeronomDepart,:dateDepart,"
			+ ":villeArrivee,:aeronomArrivee,:dateArrivee,:nbKiloDispo,1) ";
	
	@Modifying
	@Query(value=reqNewTransac, nativeQuery = true)
	void createTransaction(@Param("idUserDispo") String idUserDispo,@Param("idVol") String idVol,
			@Param("villeDepart") String villeDepart,@Param("aeronomDepart") String aeronomDepart,@Param("dateDepart") Date dateDepart,
			@Param("villeArrivee") String villeArrivee,@Param("aeronomArrivee") String aeronomArrivee,@Param("dateArrivee") Date dateArrivee,
			@Param("nbKiloDispo") int nbKiloDispo);

	
	
	String reqModifTransac= "UPDATE USERS_DISPO SET nb_kilo_dispo = :nbKiloDispo,"
			+"id_statut = :idStatut "
			+ "WHERE id_dispo = :idDispo ";
	@Modifying
	@Query(value=reqModifTransac, nativeQuery = true)
	void updateTransaction(@Param("idDispo") int idDispo,@Param("nbKiloDispo") int nbKiloDispo,@Param("idStatut") int idStatut);
	
	
	
	String reqLstTransacByUser= "SELECT * FROM USERS_DISPO "
			+ "WHERE ID_USER_DISPO  = :userNom ";
	@Query(value=reqLstTransacByUser, nativeQuery = true)
	List<UsersDispo> findByIdUserDispo(@Param("userNom") String userNom);
	
	
	String reqLstTransacByUserAndStatut= "SELECT * FROM USERS_DISPO "
			+ "WHERE ID_USER_DISPO  = :userNom "
			+ "AND ID_STATUT  = :idStatut ";
	@Query(value=reqLstTransacByUserAndStatut, nativeQuery = true)
	List<UsersDispo> findLstTransacByUserAndStatut(@Param("userNom") String userNom,@Param("idStatut") int idStatut);
	
	
	String reqLstTransacByVolrAndStatut= "SELECT * FROM USERS_DISPO "
			+ "WHERE VILLE_DEPART like :villeDepart "
			+ "AND VILLE_ARRIVEE like :villeArrivee "
			+ "AND ID_STATUT  <> 3";
	@Query(value=reqLstTransacByVolrAndStatut, nativeQuery = true)
	List<UsersDispo> getLstByVilleDepartAndVilleArriveeEncours(String villeDepart,String villeArrivee);
	
	String reqLstAllUsersDispoEncours= "SELECT * FROM USERS_DISPO "
			+ "WHERE ID_STATUT  <> 3 ";
	@Query(value=reqLstAllUsersDispoEncours, nativeQuery = true)
	List<UsersDispo> getLstAllUsersDispoEncours();
	
	String reqLstAllDispoHorsUserEncours= "SELECT * FROM USERS_DISPO "
			+ "WHERE ID_STATUT  <> 3 "
			+ "AND ID_USER_DISPO  <> :userNom ";
	@Query(value=reqLstAllDispoHorsUserEncours, nativeQuery = true)
	List<UsersDispo> getLstAllDispoHorsUserEncours(@Param("userNom") String userNom);
	
	
	
	
}
