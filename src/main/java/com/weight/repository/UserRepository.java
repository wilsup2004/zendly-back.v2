package com.weight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weight.model.Users;

public interface UserRepository extends JpaRepository<Users,String> {

	List<Users> findByNomLike(String nom);
	Users findByMailAndPassword(String mail,String password);
	
}
