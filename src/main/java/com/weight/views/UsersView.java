package com.weight.views;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import com.weight.model.Profil;
import com.weight.model.Users;
import com.weight.model.UsersProfil;
import com.weight.model.UsersProfilId;
import com.weight.repository.UserRepository;

public class UsersView {

	private String idUser;
	private String nom;
	private String prenom;
	private String mail;
	private String password;
	private int idProfil;
	private String libelProfil;
	private Date dateInit;
	private Long note;
	private UsersProfil[] usersProfils ;
	
	
	public UsersView() {
		super();
	}
	

	public UsersView(String idUser, String nom, String prenom, String mail, String password, int idProfil,
			String libelProfil, Date dateInit, Long note) {
		super();
		this.idUser = idUser;
		this.nom = nom;
		this.prenom = prenom;
		this.mail = mail;
		this.password = password;
		this.idProfil = idProfil;
		this.libelProfil = libelProfil;
		this.dateInit = dateInit;
		this.note = note;
	}


	public UsersView(Users user,UsersProfil usprofil) {
		super();
		this.idUser = user.getIdUser();
		this.nom = user.getNom();
		this.prenom = user.getPrenom();
		this.mail = user.getMail();
		this.password = user.getPassword();
		this.idProfil = usprofil.getProfil().getIdProfil();
		this.libelProfil = usprofil.getProfil().getLibelProfil();
		this.dateInit = usprofil.getDateInit();
		this.note = usprofil.getNote();
	}
	
	public UsersView(Users user) {
		super();
		this.idUser = user.getIdUser();
		this.nom = user.getNom();
		this.prenom = user.getPrenom();
		this.mail = user.getMail();
		this.password = user.getPassword();
		if( user.getUsersProfils()!= null && user.getUsersProfils().size()>0) {
			for(UsersProfil userPro: user.getUsersProfils()) {
				
				this.idProfil = userPro.getProfil().getIdProfil();
				this.libelProfil = userPro.getProfil().getLibelProfil();
				this.dateInit = userPro.getDateInit();
				this.note = userPro.getNote();
				break;
			}
		}
	}
	
	
	
	public Users   toUser (UserRepository userrepository) {
		Users user =null;
		
		Optional<Users> ouser = userrepository.findById(this.idUser);
		if(ouser.isPresent())
			user = ouser.get();
		else
			user = new Users(idUser);
		user.setNom(this.nom);
		user.setPrenom(this.prenom);
		user.setPassword(this.password);
		
		if(this.usersProfils !=null && this.usersProfils.length>0) {
			this.idProfil = this.usersProfils[0].getId().getIdProfil();
			this.libelProfil = this.usersProfils[0].getProfil().getLibelProfil();
		}
		
		UsersProfilId id = new UsersProfilId(this.idUser, this.idProfil);
		java.util.Set<UsersProfil> hsetUserProfils = new HashSet<>();
		
		if(user.getUsersProfils()!=null && user.getUsersProfils().size()>0) {
			hsetUserProfils = user.getUsersProfils();
			boolean NonExist = true;
			
			for(UsersProfil proTest :hsetUserProfils) {
				UsersProfilId idTest = proTest.getId();
				if(proTest.equals(id)) {
					NonExist = false;
					break;
				}
			}
			
			if(NonExist)
				hsetUserProfils.add(new UsersProfil(id, new Profil(this.idProfil), new Users(this.idUser), this.dateInit, this.note));
			
		}else 
			hsetUserProfils.add(new UsersProfil(id, new Profil(this.idProfil), new Users(this.idUser), this.dateInit, this.note));
		
		user.setUsersProfils(hsetUserProfils);
		
		return user;
	}


	

	public Date getDateInit() {
		return dateInit;
	}



	public void setDateInit(Date dateInit) {
		this.dateInit = dateInit;
	}



	public Long getNote() {
		return note;
	}



	public void setNote(Long note) {
		this.note = note;
	}



	public String getIdUser() {
		return idUser;
	}
	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getIdProfil() {
		return idProfil;
	}
	public void setIdProfil(int idProfil) {
		this.idProfil = idProfil;
	}
	public String getLibelProfil() {
		return libelProfil;
	}
	public void setLibelProfil(String libelProfil) {
		this.libelProfil = libelProfil;
	}


	public UsersProfil[] getUsersProfils() {
		return usersProfils;
	}


	public void setUsersProfils(UsersProfil[] usersProfils) {
		this.usersProfils = usersProfils;
	}



}
