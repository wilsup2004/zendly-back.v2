package com.weight.views;

import java.util.Date;

import com.weight.model.UsersDispo;

public class PropositionInitialView {

private int idDispo;
	
	private String idUserInitiateur;	
	private String idVol;
	private String villeDepart;
	private Date dateDepart;
	private String villeArrivee;
	private Date dateArrivee;
	private Integer nbKiloAchete;
	private String libelStatut;
	
	public PropositionInitialView() {
		
	}

	public PropositionInitialView(int idDispo, String idUserInitiateur, String idVol, String villeDepart,
			Date dateDepart, String villeArrivee,  Date dateArrivee,Integer nbKiloAchete,String libelStatut) {
		super();
		this.idDispo = idDispo;
		this.idUserInitiateur = idUserInitiateur;
		this.idVol = idVol;
		this.villeDepart = villeDepart;
		this.dateDepart = dateDepart;
		this.villeArrivee = villeArrivee;
		this.dateArrivee = dateArrivee;
		this.nbKiloAchete = nbKiloAchete;
		this.libelStatut = libelStatut;
	}

	
	public PropositionInitialView(UsersDispo userDispo) {
		super();
		this.idDispo = userDispo.getIdDispo();
		this.idUserInitiateur = userDispo.getUsers().getIdUser();
		this.idVol = userDispo.getIdVol();
		this.villeDepart = userDispo.getVilleDepart();
		this.dateDepart = userDispo.getDateDepart();
		this.villeArrivee = userDispo.getVilleArrivee();
		this.dateArrivee = userDispo.getDateArrivee();
		this.nbKiloAchete = userDispo.getNbKiloDispo();
		this.libelStatut = userDispo.getStatuts().getLibelStatut();
	}

	public PropositionInitialView(PropositionView userDispo) {
		super();
		this.idDispo = userDispo.getIdDispo();
		this.idUserInitiateur = userDispo.getIdUserInitiateur();
		this.idVol = userDispo.getIdVol();
		this.villeDepart = userDispo.getVilleDepart();
		this.dateDepart = userDispo.getDateDepart();
		this.villeArrivee = userDispo.getVilleArrivee();
		this.dateArrivee = userDispo.getDateArrivee();
		this.nbKiloAchete = userDispo.getNbKiloAchete();
		this.libelStatut = userDispo.getLibelStatut();
	}
	
	
	public int getIdDispo() {
		return idDispo;
	}

	public void setIdDispo(int idDispo) {
		this.idDispo = idDispo;
	}

	public String getIdUserInitiateur() {
		return idUserInitiateur;
	}

	public void setIdUserInitiateur(String idUserInitiateur) {
		this.idUserInitiateur = idUserInitiateur;
	}

	public String getIdVol() {
		return idVol;
	}

	public void setIdVol(String idVol) {
		this.idVol = idVol;
	}

	public String getVilleDepart() {
		return villeDepart;
	}

	public void setVilleDepart(String villeDepart) {
		this.villeDepart = villeDepart;
	}


	public Date getDateDepart() {
		return dateDepart;
	}

	public void setDateDepart(Date dateDepart) {
		this.dateDepart = dateDepart;
	}

	public String getVilleArrivee() {
		return villeArrivee;
	}

	public void setVilleArrivee(String villeArrivee) {
		this.villeArrivee = villeArrivee;
	}

	public Date getDateArrivee() {
		return dateArrivee;
	}

	public void setDateArrivee(Date dateArrivee) {
		this.dateArrivee = dateArrivee;
	}

	public Integer getNbKiloAchete() {
		return nbKiloAchete;
	}

	public void setNbKiloAchete(Integer nbKiloAchete) {
		this.nbKiloAchete = nbKiloAchete;
	}
	
	public String getLibelStatut() {
		return libelStatut;
	}

	public void setLibelStatut(String libelStatut) {
		this.libelStatut = libelStatut;
	}
	
	
	
	
}
