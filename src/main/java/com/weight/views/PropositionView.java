package com.weight.views;

import java.util.Date;
import java.util.Optional;

import com.weight.model.Statuts;
import com.weight.model.UserTrade;
import com.weight.model.UserTradeId;
import com.weight.model.UsersDispo;
import com.weight.repository.StatutsRepository;
import com.weight.repository.UserTradeRepository;

public class PropositionView  implements Comparable<PropositionView>{
	
	
	private int idDispo;
	
	private String idUserInitiateur;	
	private String idVol;
	private String villeDepart;
	private String aeronomDepart;
	private Date dateDepart;
	private String villeArrivee;
	private String aeronomArrivee;
	private Date dateArrivee;
	private String idUserCandidat;
	private Integer nbKiloAchete;
	private int idStatut;
	private String libelStatut;
	
	
	public PropositionView() {
		super();
	}



	public PropositionView(int idDispo, String idUserInitiateur, String idVol, String villeDepart, String aeronomDepart,
			Date dateDepart, String villeArrivee, String aeronomArrivee, Date dateArrivee, String idUserCandidat,
			Integer nbKiloAchete, int idStatut, String libelStatut) {
		super();
		this.idDispo = idDispo;
		this.idUserInitiateur = idUserInitiateur;
		this.idVol = idVol;
		this.villeDepart = villeDepart;
		this.aeronomDepart = aeronomDepart;
		this.dateDepart = dateDepart;
		this.villeArrivee = villeArrivee;
		this.aeronomArrivee = aeronomArrivee;
		this.dateArrivee = dateArrivee;
		this.idUserCandidat = idUserCandidat;
		this.nbKiloAchete = nbKiloAchete;
		this.idStatut = idStatut;
		this.libelStatut = libelStatut;
	}
	
	public PropositionView(UsersDispo userDispo) {
		super();
		this.idDispo = userDispo.getIdDispo();
		this.idUserInitiateur = userDispo.getUsers().getIdUser();
		this.idVol = userDispo.getIdVol();
		this.villeDepart = userDispo.getVilleDepart();
		this.aeronomDepart = userDispo.getAeronomDepart();
		this.dateDepart = userDispo.getDateDepart();
		this.villeArrivee = userDispo.getVilleArrivee();
		this.aeronomArrivee = userDispo.getAeronomArrivee();
		this.dateArrivee = userDispo.getDateArrivee();
		this.nbKiloAchete = userDispo.getNbKiloDispo();
		this.idStatut = userDispo.getStatuts().getIdStatut();
		this.libelStatut = userDispo.getStatuts().getLibelStatut();
	}

	public UserTrade toUsertrade(UserTradeRepository utraderepo,StatutsRepository statusrepo) {
		UserTrade utrade = null;
		Statuts status = null;
		UserTradeId id = new UserTradeId(this.idDispo, this.idUserCandidat);
	
		Optional<UserTrade> oUtrade = utraderepo.findById(id);
		if(oUtrade.isPresent())
			utrade = oUtrade.get();
		else {
			utrade = new UserTrade();
			utrade.setId(id);
		}
		
		status = statusrepo.findById(this.idStatut).get();
		utrade.setStatuts(status);
		utrade.setNbKiloAchete(this.nbKiloAchete);
		return utrade;
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


	public String getIdUserCandidat() {
		return idUserCandidat;
	}


	public void setIdUserCandidat(String idUserCandidat) {
		this.idUserCandidat = idUserCandidat;
	}

	public Integer getNbKiloAchete() {
		return nbKiloAchete;
	}


	public void setNbKiloAchete(Integer nbKiloAchete) {
		this.nbKiloAchete = nbKiloAchete;
	}


	public int getIdStatut() {
		return idStatut;
	}


	public void setIdStatut(int idStatut) {
		this.idStatut = idStatut;
	}


	public String getLibelStatut() {
		return libelStatut;
	}


	public void setLibelStatut(String libelStatut) {
		this.libelStatut = libelStatut;
	}



	public String getAeronomDepart() {
		return aeronomDepart;
	}



	public void setAeronomDepart(String aeronomDepart) {
		this.aeronomDepart = aeronomDepart;
	}



	public String getAeronomArrivee() {
		return aeronomArrivee;
	}



	public void setAeronomArrivee(String aeronomArrivee) {
		this.aeronomArrivee = aeronomArrivee;
	}



	@Override
	public int compareTo(PropositionView o) {
		return Integer.compare(this.getIdDispo(), o.getIdDispo());
	}
	
	
}
