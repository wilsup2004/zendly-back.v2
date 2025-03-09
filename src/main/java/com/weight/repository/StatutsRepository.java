package com.weight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.weight.model.Statuts;

public interface StatutsRepository  extends JpaRepository<Statuts,Integer>{
	
	String reqLstStatutsForTransaction= "SELECT * FROM statuts "
			+ "WHERE LIBEL_STATUT IN ('CREE','EN COURS','CLOTUREE')";
	@Query(value=reqLstStatutsForTransaction, nativeQuery = true)
	List<Statuts> getLstStatutsForTransaction();

	
	String reqLstStatutsForProposition= "SELECT * FROM statuts "
			+ "WHERE LIBEL_STATUT NOT IN ('CREE','EN COURS','CLOTUREE')";
	@Query(value=reqLstStatutsForProposition, nativeQuery = true)
	List<Statuts> getLstStatutsForProposition();


}
