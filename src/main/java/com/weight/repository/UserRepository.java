package com.weight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.weight.model.Users;

public interface UserRepository extends JpaRepository<Users,String> {

	List<Users> findByNomLike(String nom);
	
	String reqAuthByMailAndPassword = "SELECT * FROM users "
			+ "WHERE mail  = :mail "
			+ "and password  = :password ";
	@Query(value=reqAuthByMailAndPassword, nativeQuery = true)
	Users findByMailAndPassword(String mail,String password);
	
}
