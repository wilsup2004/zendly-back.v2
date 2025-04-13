package com.weight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.Colis;
import com.weight.model.Statuts;

public interface ColisRepository extends JpaRepository<Colis,Integer>  {

	String reqLstByStatut = "SELECT * FROM colis "
			+ "WHERE ID_STATUT = :statut ";
	@Query(value= reqLstByStatut, nativeQuery = true)
	List<Colis> findLstByStatut(@Param("statut") Integer statut);
	
	String reqLstEnCoursByUser = "SELECT * FROM colis "
			+ "WHERE ID_USER = :idUser "
			+ "AND ID_STATUT <= 8 ";
	@Query(value=reqLstEnCoursByUser, nativeQuery = true)
	List<Colis> getLstEnCoursByUser(@Param("idUser") String idUser);
	
	String reqLstDispoByTrajet = "SELECT * FROM colis "
			+ "WHERE VILLE_DEPART = :origine "
			+ "AND VILLE_ARRIVEE = :destination "
			+ "AND ID_STATUT =1 "
			+ "UNION "
			+"SELECT * FROM colis "
			+" where id_colis in "
			+"(select id_colis from prise_en_charge where id_prise =:idPrise) ";
	@Query(value=reqLstDispoByTrajet, nativeQuery = true)
	List<Colis> getLstDispoByTrajet(@Param("origine") String origine,@Param("destination") String destination,@Param("idPrise") Integer idPrise);
	
	
	String reqLstByUserAndStatut = "SELECT * FROM colis "
			+ "WHERE ID_USER = :idUser "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByUserAndStatut, nativeQuery = true)
	List<Colis> getLstByUserAndStatut(@Param("idUser") String idUser,@Param("statut") Integer statut);
	
	String reqLstByTrajetAndStatut = "SELECT * FROM colis "
			+ "WHERE VILLE_DEPART = :origine "
			+ "AND VILLE_ARRIVEE = :destination "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByTrajetAndStatut, nativeQuery = true)
	List<Colis> getLstByTrajetAndStatut(@Param("origine") String origine,@Param("destination") String destination,@Param("statut") Integer statut);
	
	String reqLstByTrajetAndStatutForUser = "SELECT * FROM colis "
			+ "WHERE VILLE_DEPART = :origine "
			+ "AND VILLE_ARRIVEE = :destination "
			+ "AND ID_USER <> :idUser "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByTrajetAndStatutForUser, nativeQuery = true)
	List<Colis> getLstByTrajetAndStatutForUser(@Param("origine") String origine,@Param("destination") String destination,@Param("idUser") String idUser,@Param("statut") Integer statut);
	
	
	String reqLstByStatutHorsUser = "SELECT * FROM colis "
			+ "WHERE ID_USER <> :idUser "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByStatutHorsUser, nativeQuery = true)
	List<Colis> getLstLstByStatutHorsUser(@Param("idUser") String idUser,@Param("statut") Integer statut);
	
	String reqLstByDestinationAndStatutHorsUser = "SELECT * FROM colis "
			+ "WHERE VILLE_ARRIVEE = :destination "
			+ "AND ID_USER <> :idUser "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByDestinationAndStatutHorsUser, nativeQuery = true)
	List<Colis> getLstByDestinationAndStatutHorsUser(@Param("destination") String destination,@Param("idUser") String idUser,@Param("statut") Integer statut);
	
	String reqLstByOrigineAndStatutHorsUser = "SELECT * FROM colis "
			+ "WHERE VILLE_DEPART = :origine "
			+ "AND ID_USER <> :idUser "
			+ "AND ID_STATUT = :statut ";
	@Query(value=reqLstByOrigineAndStatutHorsUser, nativeQuery = true)
	List<Colis> getLstByOrigineAndStatutHorsUser(@Param("origine") String origine,@Param("idUser") String idUser,@Param("statut") Integer statut);
	

    // Nouvelles méthodes pour les statistiques
    @Query("SELECT COUNT(c) FROM Colis c WHERE c.statuts = :statut")
    Long countByStatuts(@Param("statut") Statuts statut);
    
    @Query("SELECT COUNT(c) FROM Colis c WHERE c.statuts.idStatut = :statutId")
    Long countByStatutsId(@Param("statutId") Integer statutId);
    
    @Query("SELECT COUNT(c) FROM Colis c WHERE MONTH(c.horodatage) = :month AND YEAR(c.horodatage) = :year")
    Long countByMonth(@Param("month") int month, @Param("year") int year);
    
    @Query("SELECT c.villeDepart, COUNT(c) FROM Colis c GROUP BY c.villeDepart ORDER BY COUNT(c) DESC")
    List<Object[]> countByDepartureCity();
    
    @Query("SELECT c.villeArrivee, COUNT(c) FROM Colis c GROUP BY c.villeArrivee ORDER BY COUNT(c) DESC")
    List<Object[]> countByArrivalCity();
    
}
