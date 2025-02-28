package com.weight.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.weight.model.Aeroport;

public interface AeroportRepository extends JpaRepository<Aeroport,String> {

	List<Aeroport> findByAeroNomLike(String nom);
	
}
