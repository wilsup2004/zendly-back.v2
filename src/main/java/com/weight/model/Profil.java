package com.weight.model;
// Generated 1 oct. 2023, 21:30:36 by Hibernate Tools 5.4.33.Final

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;



@Entity
@Table(name = "PROFIL")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Profil implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name = "ID_PROFIL")
	private int idProfil;
	
	@Column(name = "LIBEL_PROFIL")
	private String libelProfil;
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "profil")
	private Set<UsersProfil> usersProfils = new HashSet<UsersProfil>(0);

	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "profil")
	private Set <HistAvis>histAvises = new HashSet<HistAvis>(0);
	
	public Profil() {
	}

	public Profil(int idProfil) {
		this.idProfil = idProfil;
	}



	
	
	public Profil(int idProfil, String libelProfil, Set<UsersProfil> usersProfils) {
		super();
		this.idProfil = idProfil;
		this.libelProfil = libelProfil;
		this.usersProfils = usersProfils;
	}


	

	public Profil(int idProfil, String libelProfil, Set<UsersProfil> usersProfils, Set<HistAvis> histAvises) {
		super();
		this.idProfil = idProfil;
		this.libelProfil = libelProfil;
		this.usersProfils = usersProfils;
		this.histAvises = histAvises;
	}

	public int getIdProfil() {
		return this.idProfil;
	}

	public void setIdProfil(int idProfil) {
		this.idProfil = idProfil;
	}

	public String getLibelProfil() {
		return this.libelProfil;
	}

	public void setLibelProfil(String libelProfil) {
		this.libelProfil = libelProfil;
	}


	public Set<UsersProfil> getUsersProfils() {
		return usersProfils;
	}

	public void setUsersProfils(Set<UsersProfil> usersProfils) {
		this.usersProfils = usersProfils;
	}

	public Set<HistAvis> getHistAvises() {
		return this.histAvises;
	}
	
	public void setHistAvises(Set<HistAvis> histAvises) {
		this.histAvises = histAvises;
	}

	
	
	

}
