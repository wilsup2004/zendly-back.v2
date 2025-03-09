package com.weight.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.weight.model.Messages;
import com.weight.model.MessagesId;

public interface MessagesRepository  extends JpaRepository <Messages,MessagesId> {

	String reqInsertMessage= "INSERT INTO messages (id_prise,id_user_prise,id_user_colis,sender,horodatage,message) "
			+ "VALUES (:idPrise,:idUserPrise,:idUserColis,:sender,:date,:message ) ";
	@Modifying
	@Query(value=reqInsertMessage, nativeQuery = true)
	void insertMessage(@Param("idPrise") int idPrise,
			@Param("idUserPrise") String idUserPrise,
			@Param("idUserColis") String idUserColis,
			@Param("sender") String sender,
			@Param("date") Date horodatage,
			@Param("message") String message);
	
	String reqLstMessageByIdAndUserColis= "SELECT * FROM messages "
			+ "WHERE id_prise = :idPrise "
			+ "AND id_user_colis =:idUserColis "
			+ "ORDER BY HORODATAGE ";
	
	@Query(value = reqLstMessageByIdAndUserColis, nativeQuery = true)
	List<Messages> getLstMessageByIdAndUserColis(@Param("idPrise") int idPrise,@Param("idUserColis") String idUserColis);
	
	String reqLstMessageByIdPrise = "SELECT * FROM messages "
			+ "WHERE id_prise = :idPrise "
			+ "ORDER BY HORODATAGE ";
	
	@Query(value = reqLstMessageByIdPrise, nativeQuery = true)
	List<Messages> getLstMessageByIdPrise(@Param("idPrise") int idPrise);
	
}
