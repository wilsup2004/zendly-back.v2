package com.weight.views;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.weight.model.Colis;
import com.weight.model.Statuts;
import com.weight.model.Users;
import com.weight.repository.ColisRepository;
import com.weight.repository.StatutsRepository;
import com.weight.repository.UserRepository;

public class ColisView implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer idColis;
	private Integer idStatut;
	private String idUser;
	private BigDecimal longueur;
	private BigDecimal largeur;
	private BigDecimal hauteur;
	private BigDecimal nbKilo;
	private BigDecimal tarif;
	private Date horodatage;
	private String villeDepart;
	private String villeArrivee;
	private String description;
	private String photoPath;
	private MultipartFile file;
	
	


	public ColisView() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public ColisView(Integer idColis, Integer statuts, String users, BigDecimal longueur, BigDecimal largeur,
			BigDecimal hauteur, BigDecimal nbKilo, BigDecimal tarif, Date horodatage, String villeDepart, String villeArrivee,
			String description, String photoPath, MultipartFile file) {
		super();
		this.idColis = idColis;
		this.idStatut = statuts;
		this.idUser = users;
		this.longueur = longueur;
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.nbKilo = nbKilo;
		this.tarif = tarif;
		this.horodatage = horodatage;
		this.villeDepart = villeDepart;
		this.villeArrivee = villeArrivee;
		this.description = description;
		this.photoPath = photoPath;
		this.file = file;
	}
	
	
	public ColisView (Colis colis) {
		super();
		this.idColis = colis.getIdColis();
		this.idStatut = colis.getStatuts().getIdStatut();
		this.idUser = colis.getUsers().getIdUser();
		this.longueur = colis.getLongueur();
		this.largeur = colis.getLargeur();
		this.hauteur = colis.getHauteur();
		this.nbKilo = colis.getNbKilo();
		this.horodatage = colis.getHorodatage();
		this.villeDepart = colis.getVilleDepart();
		this.villeArrivee = colis.getVilleArrivee();
		this.description = colis.getDescription();
		this.photoPath = colis.getPhotoPath();
	}
	
	public Colis toColis (ColisRepository colisrepos,StatutsRepository statutsrepos,UserRepository userrepos) {
		Colis colis =null;
		Users user = null;
		Statuts statuts = null;
		
		//Recuperations du user
		user = userrepos.findById(this.idUser).get();
		
		//recuperation du statut
		statuts = statutsrepos.findById(this.idStatut).get();
		
		if(this.idColis!= null) {
			
			Optional<Colis> data = colisrepos.findById(this.idColis);
			if(data.isPresent())
				colis = data.get();
			else {
				colis = new Colis();
				colis.setIdColis(this.idColis);
			}
			
			colis.setColis(statuts,user, this.longueur, this.largeur, this.hauteur,this.nbKilo,
					this.tarif,this.horodatage,this.villeDepart,this.villeArrivee, this.description,
					this.photoPath);
			
		}else {
			colis= new Colis(this.idColis,statuts,user, this.longueur, this.largeur, this.hauteur,
					this.nbKilo,this.tarif,this.horodatage,this.villeDepart,this.villeArrivee, this.description,
					this.photoPath);
		}
		
		return colis;
		
	}

	
	public Colis toUpdate(Colis colis, ColisRepository colisrepos,StatutsRepository statutsrepos,UserRepository userrepos) {

		//recuperation du statut
		Statuts statuts = null;
		if(this.idStatut != null)
			statuts = statutsrepos.findById(this.idStatut).get();
		else
			statuts = colis.getStatuts();

		colis.setColis(statuts,this.longueur, this.largeur, this.hauteur,
				this.nbKilo,this.tarif,this.villeDepart,this.villeArrivee, this.description);

		return colis;

	}
	

	public Integer getIdColis() {
		return idColis;
	}


	public void setIdColis(Integer idColis) {
		this.idColis = idColis;
	}




	public Integer getIdStatut() {
		return idStatut;
	}


	public void setIdStatut(Integer idStatut) {
		this.idStatut = idStatut;
	}


	public String getIdUser() {
		return idUser;
	}


	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}


	public BigDecimal getLongueur() {
		return longueur;
	}


	public void setLongueur(BigDecimal longueur) {
		this.longueur = longueur;
	}


	public BigDecimal getLargeur() {
		return largeur;
	}


	public void setLargeur(BigDecimal largeur) {
		this.largeur = largeur;
	}


	public BigDecimal getHauteur() {
		return hauteur;
	}


	public void setHauteur(BigDecimal hauteur) {
		this.hauteur = hauteur;
	}


	public BigDecimal getNbKilo() {
		return nbKilo;
	}


	public void setNbKilo(BigDecimal nbKilo) {
		this.nbKilo = nbKilo;
	}


	public Date getHorodatage() {
		return horodatage;
	}


	public void setHorodatage(Date horodatage) {
		this.horodatage = horodatage;
	}


	public String getVilleDepart() {
		return villeDepart;
	}


	public void setVilleDepart(String villeDepart) {
		this.villeDepart = villeDepart;
	}


	public String getVilleArrivee() {
		return villeArrivee;
	}


	public void setVilleArrivee(String villeArrivee) {
		this.villeArrivee = villeArrivee;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getPhotoPath() {
		return photoPath;
	}


	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}


	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}
	
	
	
}
