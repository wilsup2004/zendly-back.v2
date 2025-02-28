package com.weight.model;
// Generated 1 oct. 2023, 20:27:00 by Hibernate Tools 5.4.33.Final

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * HistAvis generated by hbm2java
 */
@Entity
@Table(name = "HIST_AVIS")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class HistAvis implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID_AVIS")
	private int idAvis;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PROFIL",nullable = false, insertable = false, updatable = false)
	private Profil profil;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USER", nullable = false, insertable = false, updatable = false)
	private Users users;
	
	@Column(name = "LIBEL_AVIS")
	private String libelAvis;
	
	@Column(name = "NOTE")
	private Integer note;

	public HistAvis() {
	}

	public HistAvis(int idAvis, Profil profil, Users users) {
		this.idAvis = idAvis;
		this.profil = profil;
		this.users = users;
	}

	public HistAvis(int idAvis, Profil profil, Users users, String libelAvis, Integer note) {
		this.idAvis = idAvis;
		this.profil = profil;
		this.users = users;
		this.libelAvis = libelAvis;
		this.note = note;
	}

	public int getIdAvis() {
		return this.idAvis;
	}

	public void setIdAvis(int idAvis) {
		this.idAvis = idAvis;
	}

	public Profil getProfil() {
		return this.profil;
	}

	public void setProfil(Profil profil) {
		this.profil = profil;
	}

	public Users getUsers() {
		return this.users;
	}

	public void setUsers(Users users) {
		this.users = users;
	}

	public String getLibelAvis() {
		return this.libelAvis;
	}

	public void setLibelAvis(String libelAvis) {
		this.libelAvis = libelAvis;
	}

	public Integer getNote() {
		return this.note;
	}

	public void setNote(Integer note) {
		this.note = note;
	}

}
