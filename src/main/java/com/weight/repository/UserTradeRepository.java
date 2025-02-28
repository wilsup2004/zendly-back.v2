package com.weight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.UserTrade;
import com.weight.model.UserTradeId;
import com.weight.views.PropositionView;

public interface UserTradeRepository  extends JpaRepository<UserTrade,UserTradeId>{
	
	String reqLstTradeByTransacAndUserAndStatut= "SELECT * FROM USER_TRADE "
			+ "WHERE ID_DISPO  = :idDispo "
			+ "AND ID_USER_CANDIDAT  = :userNom "
			+ "AND ID_STATUT  = :idStatut ";
	@Query(value=reqLstTradeByTransacAndUserAndStatut, nativeQuery = true)
	List<UserTrade> getLstTradeByTransacAndCandidatAndStatut(@Param("idDispo") int idDipso,@Param("userNom") String userNom,@Param("idStatut") int idStatut);

	String reqLstTradeByTransacAndUser= "SELECT * FROM USER_TRADE "
			+ "WHERE ID_DISPO  = :idDispo "
			+ "AND ID_USER_CANDIDAT  = :userNom ";
	@Query(value=reqLstTradeByTransacAndUser, nativeQuery = true)
	List<UserTrade> getLstTradeByTransacAndCandidat(@Param("idDispo") int idDipso,@Param("userNom") String userNom);

	
	String reqLstTradeByTransac= "SELECT * FROM USER_TRADE "
			+ "WHERE ID_DISPO  = :idDispo ";
	@Query(value=reqLstTradeByTransac, nativeQuery = true)
	List<UserTrade> getLstTradeByTransac(@Param("idDispo") int idDipso);

	
	String reqLstTradeByUser= "SELECT * FROM USER_TRADE "
			+ "WHERE ID_USER_CANDIDAT  = :userNom ";
	@Query(value=reqLstTradeByUser, nativeQuery = true)
	List<UserTrade> getLstTradeByCandidat(@Param("userNom") String userNom);

	
	String reqNewProposition= "INSERT INTO USER_TRADE "
			+"( id_dispo,id_user_candidat,nb_kilo_achete,id_statut) " 
			+"VALUES "
			+"( :idDispo,:idUserCandidat,:nbKiloAchete,4) ";
	
	@Modifying
	@Query(value=reqNewProposition, nativeQuery = true)
	void createProposition(@Param("idDispo") int idDispo,@Param("idUserCandidat") String idUserCandidat,@Param("nbKiloAchete") int nbKiloAchete);

	
	
	String reqModifProposition= "UPDATE USER_TRADE SET id_statut = :idStatut "
			+ "WHERE ID_DISPO  = :idDispo "
			+ "AND ID_USER_CANDIDAT  = :idUserCandidat ";
	@Modifying
	@Query(value=reqModifProposition, nativeQuery = true)
	void updateStatutProposition(@Param("idDispo") int idDispo,@Param("idUserCandidat") String idUserCandidat,@Param("idStatut") int idStatut);
	
	String reqModifPropositionKiloAndStatus= "UPDATE USER_TRADE SET "
			+ "id_statut = :idStatut, "
			+ "nb_kilo_achete = :nbKilo "
			+ "WHERE ID_DISPO  = :idDispo "
			+ "AND ID_USER_CANDIDAT  = :idUserCandidat ";
	@Modifying
	@Query(value=reqModifPropositionKiloAndStatus, nativeQuery = true)
	void updatePropositionKiloAndStatus(@Param("idDispo") int idDispo,@Param("idUserCandidat") String idUserCandidat,@Param("idStatut") int idStatut,@Param("nbKilo") int nbKilo);
	
	@Query(nativeQuery = true, name = "reqLstPropositionByTransac")
	List<PropositionView> getLstPropositionByTransac(@Param("idDispo") int idDispo);
	
	@Query(nativeQuery = true, name = "reqLstPropositionAcceptedByTransac")
	List<PropositionView> getLstPropositionAcceptedByTransac(@Param("idDispo") int idDispo);
	
	
	@Query(nativeQuery = true, name = "reqLstPropositionByCandidat")
	List<PropositionView> getLstPropositionByCandidat(@Param("idCandidat") String idCandidat);
	
	@Query(nativeQuery = true, name = "reqLstPropositionByCandidatAndStatut")
	List<PropositionView> getLstPropositionByCandidatAndStatut(@Param("idCandidat") String idCandidat,@Param("idStatut") int idStatut);
	
	@Query(nativeQuery = true, name = "reqLstPropositionByCandidatAndId")
	PropositionView getPropositionByCandidatAndId(@Param("idCandidat") String idCandidat,@Param("idDispo") int idDispo);
	
	
	@Query(nativeQuery = true, name = "reqLstPropositionByInitiateur")
	List<PropositionView> getLstPropositionByInitiateur(@Param("idInitiateur") String idInitiateur);

	@Query(nativeQuery = true, name = "reqLstPropositionByInitiateurAndStatut")
	List<PropositionView> getLstPropositionByInitiateurAndStatut(@Param("idInitiateur") String idInitiateur,@Param("idStatut") int idStatut);
	
}
