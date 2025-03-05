
package com.weight.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.weight.model.Aeroport;
import com.weight.model.Colis;
import com.weight.model.PriseEnCharge;
import com.weight.model.Statuts;
import com.weight.model.UserTrade;
import com.weight.model.Users;
import com.weight.model.UsersDispo;
import com.weight.model.UsersProfil;
import com.weight.repository.AeroportRepository;
import com.weight.repository.ColisRepository;
import com.weight.repository.PriseEnChargeRepository;
import com.weight.repository.StatutsRepository;
import com.weight.repository.UserRepository;
import com.weight.repository.UserTradeRepository;
import com.weight.repository.UsersDispoRepository;
import com.weight.repository.UsersProfilRepository;
import com.weight.services.ImageService;
import com.weight.services.Vol;
import com.weight.services.VolServices;
import com.weight.views.ColisView;
import com.weight.views.PropositionInitialView;
import com.weight.views.PropositionView;
import com.weight.views.UserTradeView;
import com.weight.views.UsersView;


//@CrossOrigin(origins = {"http://192.168.1.21:4200","http://localhost:4200"})
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/trade")
public class Controller {

	@Autowired
	private AeroportRepository aerorepository;

	@Autowired
	private UserRepository userrepository;

	@Autowired
	private UsersDispoRepository userdiporepository;

	@Autowired
	private UserTradeRepository usertraderepository;

	@Autowired
	private UsersProfilRepository userprofilrepository;

	@Autowired
	private StatutsRepository statutsrepository;

	@Autowired
	private ColisRepository colisrepository;
	
	@Autowired
	private PriseEnChargeRepository priserepository;
	
	@Autowired
	private ImageService imageService;


	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	//***************************  GESTION DES USERS ***************************************//
	@GetMapping(value = "/users")
	public ResponseEntity<?>  getAllUser() {
		logger.info("Récupération de tous les utilisateurs");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		//List<UsersView> lstView = null;
		List<Users> lst = userrepository.findAll();

		if(lst != null && lst.size()>0) {

			res = new ResponseEntity<>(lst,httpRes);
		}else {
			msg ="Aucun utilisateur n'a été trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@GetMapping("/users/{id}")
	public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
		logger.info("Récupération d'un user en fonction de son code");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		Users user = userrepository.getReferenceById(id);

		if(user != null ) 
			res = new ResponseEntity<>(user,httpRes);
		//res = new ResponseEntity<>(new UsersView(user),httpRes);

		else {
			msg ="Aucun utilisateur trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/users/search")
	public ResponseEntity<?> getUserByNom(String nom) {
		logger.info("Récupération d'un user en fonction de son nom");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		String searchName = "%"+nom+"%";
		List<Users> lst = userrepository.findByNomLike(searchName);

		if(lst != null && lst.size()>0) {
			res = new ResponseEntity<>(lst,httpRes);
		}else {
			msg ="Aucun utilisateur trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/users/auth")
	public ResponseEntity<?> getUserAuth(String mail,String password) {
		logger.info("Authentification d'un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		Users user = userrepository.findByMailAndPassword(mail,password);
		if(user != null) {

			res = new ResponseEntity<>(user,httpRes);
		}else {
			msg ="Aucun utilisateur n'a été trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@PostMapping("/users")
	public ResponseEntity<?> createUser(@RequestBody Users user) {
		logger.info("Création d'un utilisateur ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {

			Users userCheck = new Users(
					user.getIdUser(), user.getNom(), user.getPrenom(), user.getMail(), user.getPassword(), null);
			userrepository.saveAndFlush(userCheck);

			if( user.getUsersProfils()!= null && user.getUsersProfils().size()>0) {
				for(UsersProfil userPro: user.getUsersProfils()) {
					userprofilrepository.saveAndFlush(userPro);
				}
				userCheck.setUsersProfils( user.getUsersProfils());
			}

			userrepository.saveAndFlush(userCheck);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="L'utilisateur n'a pas été créé:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable("id") String id, @RequestBody UsersView view) {
		logger.info("Modification d'un utilisateur ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			Users user = view.toUser(userrepository);
			userrepository.saveAndFlush(user);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="L'utilisateur n'a pas été modifié:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable("id") String id) {
		logger.info("Suppression d'un utilisateur ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			userrepository.deleteById(id);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="L'utilisateur n'a pas été supprimé:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	@DeleteMapping("/users")
	public ResponseEntity<?> deleteAllUsers() {
		logger.info("Suppression des utilisateurs ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			//TODO  Suppression TOTALE désactivé pour l'instant
			// userrepository.deleteAll();
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Une erreur est survenue lors de la suppression des utilisateurs:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);

		return  res;
	}

	//***************************  GESTION DES ECHANGES ***************************************//

	@Transactional
	@PostMapping("/usersDispo")
	public ResponseEntity<?> createUserDispo(@RequestBody UsersDispo userDispo) {
		logger.info("Création d'une transaction ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			String idUser = userDispo.getUsers().getIdUser();
			String idVol = userDispo.getIdVol();
			String villeDepart = userDispo.getVilleDepart();
			String aeronomDepart = userDispo.getAeronomDepart();
			Date datedepart = userDispo.getDateDepart();
			String villeArrivee= userDispo.getVilleArrivee();
			String aeronomArrivee= userDispo.getAeronomArrivee();
			Date dateArrivee = userDispo.getDateArrivee();
			int nbKiloDispo = userDispo.getNbKiloDispo();

			userdiporepository.createTransaction(idUser, idVol, villeDepart,aeronomDepart, datedepart, villeArrivee,aeronomArrivee, dateArrivee, nbKiloDispo);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Transaction non créée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}


	@GetMapping(value = "/usersDispo")
	public ResponseEntity<?>  getAllUsersDispoEncours() {
		logger.info("Récupération de toutes les transactions en cours");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<UsersDispo> lst = userdiporepository.getLstAllUsersDispoEncours();

		if(lst != null && lst.size()>0) {
			List<PropositionInitialView> lstProp = new ArrayList<>();
			Collections.sort(lst);
			for(UsersDispo user:lst) {
				PropositionInitialView view =  new PropositionInitialView(user);
				lstProp.add(view);
			}
			res = new ResponseEntity<>(lstProp,httpRes);
		}else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping(value = "/usersDispo/horsUser")
	public ResponseEntity<?>  getAllDispoHorsUserEncours(String userNom) {
		logger.info("Récupération de toutes les transactions en cours");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<UsersDispo> lst = userdiporepository.getLstAllDispoHorsUserEncours(userNom);

		if(lst != null && lst.size()>0) {
			List<PropositionInitialView> lstProp = new ArrayList<>();
			Collections.sort(lst);
			for(UsersDispo user:lst) {
				PropositionInitialView view =  new PropositionInitialView(user);
				lstProp.add(view);
			}
			res = new ResponseEntity<>(lstProp,httpRes);
		}else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@GetMapping("/usersDispo/{id}")
	public ResponseEntity<?> getUsersDispoById(@PathVariable("id") int id) {
		logger.info("Récupération d'une transaction en fonction de son code");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		UsersDispo userDispo = userdiporepository.getReferenceById(id);

		if(userDispo != null )
			res = new ResponseEntity<>(userDispo,httpRes);
		else {
			msg ="Aucune transactiont trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/usersDispo/search")
	public ResponseEntity<?> getUsersDispoByUser(String userNom) {
		logger.info("Récupération des transactions initiés par un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<UsersDispo> lst = userdiporepository.findByIdUserDispo(userNom);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@GetMapping("/usersDispo/searchAllBytrajet")
	public ResponseEntity<?> getListUsersDispoBytrajet(String villeDepart,String villeArrivee) {
		logger.info("Récupération des transactions en cours par trajets");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		String searchDepart = "%"+villeDepart+"%";
		String searchArrivee = "%"+villeArrivee+"%";

		List<UsersDispo> lst = userdiporepository.getLstByVilleDepartAndVilleArriveeEncours(searchDepart,searchArrivee);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@Transactional
	@PutMapping("/usersDispo/{id}")
	public ResponseEntity<?> updateUsersDispo (@PathVariable("id") int id, @RequestBody UsersDispo userDispo) {
		logger.info("Modification d'un utilisateur ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			userdiporepository.updateTransaction(id,userDispo.getNbKiloDispo(), userDispo.getStatuts().getIdStatut());
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="La transaction non modifiée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}


	//***************************  GESTION DES PROPOSITIONS ***************************************//

	@Transactional
	@PostMapping("/usersProposition")
	public ResponseEntity<?> createUserProposition(@RequestBody UserTradeView userTrade) {
		logger.info("Création d'une proposition ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			int idDispo = userTrade.getIdDispo();
			String idUserCandidat = userTrade.getIdUserCandidat();
			int nbKiloAchete = userTrade.getNbKiloAchete();

			usertraderepository.createProposition(idDispo, idUserCandidat, nbKiloAchete);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Transaction non créée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}


	@GetMapping(value = "/usersProposition")
	public ResponseEntity<?>  getAllUsersProposition() {
		logger.info("Récupération de toutes les propositions");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<UserTrade> lst = usertraderepository.findAll();

		if(lst != null && lst.size()>0) {
			Collections.sort(lst);
			res = new ResponseEntity<>(lst,httpRes);
		}else {
			msg ="Aucune proposition trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@GetMapping("/usersProposition/{id}")
	public ResponseEntity<?> getUsersPropositionById(@PathVariable("id") int id) {
		logger.info("Récupération des propositions par transaction");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<PropositionView> lst = usertraderepository.getLstPropositionByTransac(id);
		res = new ResponseEntity<>(lst,httpRes);

		/*
				if(lst != null && lst.size()>0) {
					Collections.sort(lst);
					res = new ResponseEntity<>(lst,httpRes);
				}else {
					msg ="Aucune proposition trouvée";
					logger.warn(msg);
					httpRes = HttpStatus.NOT_FOUND;
					res = new ResponseEntity<>(msg,httpRes);
				}
		 */
		logger.info(msg);
		return  res;
	}

	@GetMapping("/usersProposition/accept")
	public ResponseEntity<?> getUsersPropositionAcceptedById(int id) {
		logger.info("Récupération des propositions par transaction");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<PropositionView> lst = usertraderepository.getLstPropositionAcceptedByTransac(id);
		res = new ResponseEntity<>(lst,httpRes);


		if(lst != null && lst.size()>0) {
			Collections.sort(lst);
			res = new ResponseEntity<>(lst,httpRes);
		}else {
			msg ="Aucune proposition trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/usersProposition/search")
	public ResponseEntity<?> getUsersPropositionsByUser(String userNom) {
		logger.info("Récupération des propositions soumises par un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<UserTrade> lst = usertraderepository.getLstTradeByCandidat(userNom);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/usersProposition/candidat")
	public ResponseEntity<?> getUsersPropositionsForCandidat(Integer id,String userNom) {
		logger.info("Récupération de proposition soumise à une transaction par un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		PropositionView view = usertraderepository.getPropositionByCandidatAndId(userNom,id);

		res = new ResponseEntity<>(view,httpRes);

		logger.info(msg);
		return  res;
	}

	@GetMapping("/usersProposition/candidatPropal")
	public ResponseEntity<?> getPropositionsByTransacAndCandidat(String userNom,@Nullable Integer statut) {
		logger.info("Récupération des propositions soumises par un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		List<PropositionView> lst =  null;

		if(statut!= null)
			lst = usertraderepository.getLstPropositionByCandidatAndStatut(userNom,statut);
		else
			lst = usertraderepository.getLstPropositionByCandidat(userNom);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/usersProposition/initiateur")
	public ResponseEntity<?> getUsersPropositionsForinitiateur(String userNom,Integer statut) {
		logger.info("Récupération des propositions soumises par transaction pour un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		List<PropositionView> lst =  null;

		if(statut!= null)
			lst = usertraderepository.getLstPropositionByInitiateurAndStatut(userNom,statut);
		else
			lst = usertraderepository.getLstPropositionByInitiateur(userNom);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucune transaction trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@Transactional
	@PutMapping("/usersProposition/{id}")
	public ResponseEntity<?> updateUsersPropositions (@PathVariable("id") String id, @RequestBody PropositionView proposition) {
		logger.info("Modification d'une proposition");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {

			UserTrade utrade = proposition.toUsertrade(usertraderepository,statutsrepository);
			//usertraderepository.saveAndFlush(utrade);

			usertraderepository.updatePropositionKiloAndStatus(
					utrade.getId().getIdDispo(),
					utrade.getId().getIdUserCandidat(),
					utrade.getStatuts().getIdStatut(),
					utrade.getNbKiloAchete()
					);
			usertraderepository.flush();

			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="La transaction non modifiée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}


	//***************************  GESTION DES AEROPORTS ***********************************//
	@GetMapping(value = "/aeroports")
	public ResponseEntity<?>  getAllAeroport() {
		logger.info("Récupération de tous les aéroports");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<Aeroport> lst = aerorepository.findAll();

		if(lst != null && lst.size()>0) {
			Collections.sort(lst);
			res = new ResponseEntity<>(lst,httpRes);
		}else {
			msg ="Aucun aéroport trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@GetMapping("/aeroports/{id}")
	public ResponseEntity<?> getAeroportById(@PathVariable("id") String id) {
		logger.info("Récupération d'un aeroport en fonction de son code");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		Aeroport aero = aerorepository.getReferenceById(id);

		if(aero != null )
			res = new ResponseEntity<>(aero,httpRes);
		else {
			msg ="Aucun aéroport trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping("/aeroports/search")
	public ResponseEntity<?> getAeroportByNom(String aeroNom) {
		logger.info("Récupération d'un aeroport en fonction de son nom");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		String searchName = "%"+aeroNom+"%";
		List<Aeroport> lst = aerorepository.findByAeroNomLike(searchName);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucun aéroport trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@PostMapping("/aeroports")
	public ResponseEntity<?> createAeroport(@RequestBody Aeroport aeroport) {
		logger.info("Création d'un aeroport ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			aerorepository.saveAndFlush(aeroport);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="L'aeroport non créé:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}


		logger.info(msg);
		return  res;

	}

	@PutMapping("/aeroports/{id}")
	public ResponseEntity<?> updateAeroport(@PathVariable("id") String id, @RequestBody Aeroport aeroport) {
		logger.info("Modification d'un aeroport ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			aerorepository.saveAndFlush(aeroport);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="L'aeroport non modifié:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	@DeleteMapping("/aeroports/{id}")
	public ResponseEntity<?> deleteAeroport(@PathVariable("id") String id) {
		logger.info("Suppression d'un aeroport ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			aerorepository.deleteById(id);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="L'aeroport non supprimé:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	@DeleteMapping("/aeroports")
	public ResponseEntity<?> deleteAllAeroports() {
		logger.info("Suppression d'un aeroport ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			//TODO  Suppression TOTALE désactivé pour l'instant
			// aerorepository.deleteAll();
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Une erreur est survenue lors de la suppression des aeroports:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);

		return  res;
	}
	//***************************  GESTION DES VOLS ***********************************//

	@GetMapping("/flight/search")
	public ResponseEntity<?> getLstVols(String idOrigine,String idDestination) {

		logger.info("Récupération des vols planifiés entre 2 aeroports");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		VolServices vserv = new VolServices();

		List<Vol> lst = vserv.getLstVols(idOrigine, idDestination);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucun vol trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}
	
	@GetMapping("/flight/search/vol")
	public ResponseEntity<?> getVolByTrajetAndIdAndStatut(String idOrigine,String idDestination,String idVol,String statut) {

		logger.info("Récupération des vols planifiés entre 2 aeroports");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		VolServices vserv = new VolServices();

		Vol vol = vserv.getVolByTrajetAndIdAndStatut(idOrigine, idDestination,idVol, statut);

		if(vol != null)

			res = new ResponseEntity<>(vol,httpRes);
		else {
			msg ="Aucun vol trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	//***************************  GESTION DES STATUTS ***************************************//


	@GetMapping(value = "/statuts")
	public ResponseEntity<?>  getAllStatuts() {
		logger.info("Récupération de tous les statuts");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<Statuts> lst = statutsrepository.findAll();
		if(lst != null && lst.size()>0)
			Collections.sort(lst);

		res = new ResponseEntity<>(lst,httpRes);

		/*
			if(lst != null && lst.size()>0) {
				Collections.sort(lst);
				res = new ResponseEntity<>(lst,httpRes);
			}else {
				msg ="Aucun aéroport trouvé";
				logger.warn(msg);
				httpRes = HttpStatus.NOT_FOUND;
				res = new ResponseEntity<>(msg,httpRes);
			}
		 */

		logger.info(msg);
		return  res;
	}


	@GetMapping(value = "/statuts/transaction")
	public ResponseEntity<?>  getAllStatutsForTransaction() {
		logger.info("Récupération de tous les statuts");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<Statuts> lst = statutsrepository.getLstStatutsForTransaction();
		if(lst != null && lst.size()>0)
			Collections.sort(lst);

		res = new ResponseEntity<>(lst,httpRes);

		/*
			if(lst != null && lst.size()>0) {
				Collections.sort(lst);
				res = new ResponseEntity<>(lst,httpRes);
			}else {
				msg ="Aucun aéroport trouvé";
				logger.warn(msg);
				httpRes = HttpStatus.NOT_FOUND;
				res = new ResponseEntity<>(msg,httpRes);
			}
		 */

		logger.info(msg);
		return  res;
	}

	@GetMapping(value = "/statuts/proposition")
	public ResponseEntity<?>  getAllStatutsForProposition() {
		logger.info("Récupération de tous les statuts");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<Statuts> lst = statutsrepository.getLstStatutsForProposition();
		if(lst != null && lst.size()>0)
			Collections.sort(lst);

		res = new ResponseEntity<>(lst,httpRes);

		/*
			if(lst != null && lst.size()>0) {
				Collections.sort(lst);
				res = new ResponseEntity<>(lst,httpRes);
			}else {
				msg ="Aucun aéroport trouvé";
				logger.warn(msg);
				httpRes = HttpStatus.NOT_FOUND;
				res = new ResponseEntity<>(msg,httpRes);
			}
		 */
		logger.info(msg);
		return  res;
	}

	//***************************  GESTION DES COLIS ***************************************//

	@Transactional
	@PostMapping("/colis")
	public ResponseEntity<?> createColis(
			@RequestParam("idStatut") Integer idStatut,
			@RequestParam("idUser")  String idUser,
			@RequestParam("longueur")  BigDecimal longueur,
			@RequestParam("largeur")  BigDecimal largeur,
			@RequestParam("hauteur")  BigDecimal hauteur,
			@RequestParam("nbKilo")  BigDecimal nbKilo,
			@RequestParam("tarif")  BigDecimal tarif,
			@RequestParam("villeDepart")  String villeDepart,
			@RequestParam("villeArrivee")  String villeArrivee,
			@RequestParam("description")  String description,
			@RequestParam("file")  MultipartFile file
			) {
		logger.info("Création d'un colis ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			Date horodatage = new Date();

			ColisView view = new ColisView(null, idStatut, idUser, longueur, 
					largeur, hauteur, nbKilo,tarif, horodatage, villeDepart, 
					villeArrivee, description, null, file);

			Colis newColis = view.toColis(colisrepository, statutsrepository, userrepository);

			//Statuts statutCree = statutsrepository.findById(1).get();
			//newColis.setStatuts(statutCree);
			System.out.println( "nom du fichier : "+file.getName());
			//1- On copie la photo (si il y en a)
			if(file!=null && !file.getOriginalFilename().isEmpty()) {
				String filename = imageService.uploadImage(file,"colis");
				newColis.setPhotoPath(filename);
			}

			//2- on sauvegarde les infos du colis en base
			colisrepository.saveAndFlush(newColis);

			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Colis non créé:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}
	
	@Transactional
	@PutMapping("/colis")
	public ResponseEntity<?> updateColis(
			@RequestParam("idColis") Integer idColis,
			@Nullable @RequestParam("idStatut") Integer idStatut,
			@Nullable @RequestParam("idUser")  String idUser,
			@RequestParam("longueur")  BigDecimal longueur,
			@RequestParam("largeur")  BigDecimal largeur,
			@RequestParam("hauteur")  BigDecimal hauteur,
			@RequestParam("nbKilo")  BigDecimal nbKilo,
			@RequestParam("tarif")  BigDecimal tarif,
			@RequestParam("villeDepart")  String villeDepart,
			@RequestParam("villeArrivee")  String villeArrivee,
			@RequestParam("description")  String description,
			@Nullable @RequestParam("file")  MultipartFile file
			) {
		logger.info("Création d'un colis ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		boolean majPhoto = false;
		String filenameInit = null;

		try {
			Colis colisToUpdate = colisrepository.findById(idColis).get();
			
			if(file!=null) {
				if(colisToUpdate.getPhotoPath()!= null) {					
					String[] parts = colisToUpdate.getPhotoPath().split("_");
					filenameInit = parts[parts.length -1];
					if(!filenameInit.equalsIgnoreCase(file.getOriginalFilename()))
						majPhoto = true;
				}else
					majPhoto = true;
			} else {
				if(colisToUpdate.getPhotoPath()!= null) {
					majPhoto = true;
					filenameInit = "default";
				}
			}
			
			ColisView view = new ColisView(null, idStatut, idUser, longueur, 
					largeur, hauteur, nbKilo,tarif, null, villeDepart, 
					villeArrivee, description, null, file);

			Colis newColis = view.toUpdate(colisToUpdate,colisrepository, statutsrepository, userrepository);

			if(majPhoto) {
				//1- On dois supprimer l'ancienne photo (si il y en a)
				if(filenameInit!= null) {
					imageService.deleteImage(colisToUpdate.getPhotoPath(), "colis");
					newColis.setPhotoPath(null);
				}
				
				//2- On copie la nouvelle photo
				if(file!=null && !file.getOriginalFilename().isEmpty()) {
					String filename = imageService.uploadImage(file,"colis");
					newColis.setPhotoPath(filename);
				}
			}
		

			//2- on sauvegarde les infos du colis en base
			colisrepository.saveAndFlush(newColis);

			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Colis non créé:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	//Récupération de l'image pour un colis
	@GetMapping(value = "/colis/image/{id}")
	public ResponseEntity<?>  getImage(@PathVariable Integer id) {
		logger.info("Récupération des images d'un colis");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		Colis colis= colisrepository.findById(id).get();
		byte[] image;

		if(colis.getPhotoPath() != null && !colis.getPhotoPath().isBlank()) {
			
			try {
				image = imageService.downloadImage(colis.getPhotoPath() , "colis");

				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "image/jpeg"); // Ajuste le type MIME selon l'image
				res =  new ResponseEntity<>(image, headers, HttpStatus.OK);

			} catch (IOException e) {
				msg ="Une erreur est survenue lors de la récupération de l'image pour le colis [ID]= "+id+" :"+e;
				logger.error(msg);
				httpRes = HttpStatus.EXPECTATION_FAILED;
				res = new ResponseEntity<>(msg,httpRes);
			}

		}else {
			
			try {
				image = imageService.downloadImageDefault();

				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-Type", "image/jpeg"); // Ajuste le type MIME selon l'image
				res =  new ResponseEntity<>(image, headers, HttpStatus.OK);
				

			} catch (IOException e) {
				msg ="Une erreur est survenue lors de la récupération de l'image par defaut :"+e;
				logger.error(msg);
				httpRes = HttpStatus.EXPECTATION_FAILED;
				res = new ResponseEntity<>(msg,httpRes);
			}
		}

		logger.info(msg);
		return  res;
	}
	
	@Transactional
	@PutMapping("/colis/annule")
	public ResponseEntity<?> annulePriseEnCharge ( @RequestBody Colis colis) {
		logger.info("Annulation d'une prise en charge");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			//Récupération du colis en base pour recupérer la prise en charge
			Colis colisBase  = colisrepository.findById(colis.getIdColis()).get();
			PriseEnCharge priseEnCharge = colisBase.getPriseEnCharges().iterator().next();
			
			//On sauvegarde du changement de statut du colis
			colisBase.setStatuts(colis.getStatuts());
			colis.setPriseEnCharges(null);
			colis = colisrepository.saveAndFlush(colis);
			
			priseEnCharge.setColis(null);
			priseEnCharge.setStatuts(colis.getStatuts());
			//On sauvegarde maintenant la prise en charge
			priseEnCharge = priserepository.saveAndFlush(priseEnCharge);

			res = new ResponseEntity<>(priseEnCharge,httpRes);

		}catch (Exception e) {
			msg ="La transaction non modifiée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}


	//Récupération d'un colis par son Id
	@GetMapping(value = "/colis/{id}")
	public ResponseEntity<?>  getColisById(@PathVariable Integer id) {
		logger.info("Récupération d'un colis par son Id");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		Colis colis= colisrepository.findById(id).get();

		res = new ResponseEntity<>(colis,httpRes);

		logger.info(msg);
		return  res;
	}


	//Récupération de tous les colis
	@GetMapping(value = "/colis")
	public ResponseEntity<?>  getAllColis() {
		logger.info("Récupération de tous les colis");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<Colis> lst = colisrepository.findAll();

		if(lst != null && lst.size()>0)
			res = new ResponseEntity<>(lst,httpRes);

		else {
			msg ="Aucune proposition trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@GetMapping(value = "/colis/userstatut")
	public ResponseEntity<?> getColisByUserAndStatut(@Nullable Integer statut,@Nullable String userNom) {
		logger.info("Récupération des colis avec un statut précis soumis par un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		List<Colis> lst =  null;

		if(userNom!= null) {
			
			if(statut !=null)
				lst = colisrepository.getLstByUserAndStatut(userNom,statut);
			else
				lst = colisrepository.getLstEnCoursByUser(userNom);
		}else
			lst = colisrepository.findLstByStatut(statut);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucun colis trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}
	
	@GetMapping(value = "/colis/trajet")
	public ResponseEntity<?> getLstColisByTrajetAndStatut(String origine,String destination,String idUser,Integer statut) {
		logger.info("Récupération des colis avec un statut précis et en fonction d'un trajet");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		List<Colis> lst = null;
		
		if(idUser ==null)
			lst =  colisrepository.getLstByTrajetAndStatut(origine, destination, statut);
		else
			lst =  colisrepository.getLstByTrajetAndStatutForUser(origine, destination,idUser, statut);
		
		if(lst != null && lst.size()>0)
			res = new ResponseEntity<>(lst,httpRes);
		
		else {
			msg ="Aucun colis trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	//***************************  GESTION DES PRISES EN CHARGE ***************************************//

	@Transactional
	@PostMapping("/priseEnCharge")
	public ResponseEntity<?> createPriseEnCharge(@RequestBody PriseEnCharge priseCharge) {
		logger.info("Création d'une prise en charge");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			
			//Update du statut du colis
			Colis colis = priseCharge.getColis();
			colis = colisrepository.saveAndFlush(colis);
			
			//Insert en base de la prise en charge
			priseCharge.setColis(colis);
			priserepository.saveAndFlush(priseCharge);
			res = new ResponseEntity<>(msg,httpRes);

		}catch (Exception e) {
			msg ="Prise en charge non créée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}


	@GetMapping(value = "/priseEnCharge")
	public ResponseEntity<?>  getPriseEnCharge() {
		logger.info("Récupération de toutes les prises en charge");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<PriseEnCharge> lst = priserepository.findAll();

		if(lst != null && lst.size()>0) {
			//Collections.sort(lst);
			res = new ResponseEntity<>(lst,httpRes);
			
		}else {
			msg ="Aucune proposition trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}


	@GetMapping("/priseEnCharge/{id}")
	public ResponseEntity<?> getPriseEnChargeById(@PathVariable("id") int id) {
		logger.info("Récupération des prises en charge par id");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		
		 Optional<PriseEnCharge> opriseEnCharge = priserepository.findById(id);
		
				if(opriseEnCharge.isPresent()) {
					PriseEnCharge priseEnCharge = opriseEnCharge.get();
					res = new ResponseEntity<>(priseEnCharge,httpRes);
				}else {
					msg ="Aucune proposition trouvée";
					logger.warn(msg);
					httpRes = HttpStatus.NOT_FOUND;
					res = new ResponseEntity<>(msg,httpRes);
				}
		
		logger.info(msg);
		return  res;
	}
	

	@PostMapping("/priseEnCharge/user")
	public ResponseEntity<?> getAllPriseEnChargeByUser(@RequestBody Users user) {
		logger.info("Récupération des prises en charge d'un user");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<PriseEnCharge> lst = priserepository.findByUsers(user);
		
		if(lst != null && lst.size()>0) {
			//Collections.sort(lst);
			res = new ResponseEntity<>(lst,httpRes);
		}else {
			msg ="Aucune proposition trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}
	
	
	@GetMapping("/priseEnCharge/colis/{id}")
	public ResponseEntity<?> getPriseEnChargeByColis(@PathVariable("id") int id) {
		logger.info("Récupération des prises en charge d'un colis");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		Colis colis = colisrepository.findById(id).get();
		PriseEnCharge prise= priserepository.findByColis(colis);
		
		if(prise != null ) {
			//Collections.sort(lst);
			res = new ResponseEntity<>(prise,httpRes);
		}else {
			msg ="Aucune proposition trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}
	
	@GetMapping(value = "/priseEnCharge/userstatut")
	public ResponseEntity<?> getPriseEnChargeByUserAndStatut(@Nullable Integer statut,@Nullable String userNom) {
		logger.info("Récupération des prises en charge d'un user dont le statut est précis ");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;
		List<PriseEnCharge> lst =  null;
		
		if(userNom!= null) {
			
			if(statut !=null)
				lst = priserepository.getLstByUserAndStatut(userNom,statut);
			else
				lst = priserepository.getLstEnCoursByUser(userNom);
		}else
			lst = priserepository.findLstByStatut(statut);

		if(lst != null && lst.size()>0)

			res = new ResponseEntity<>(lst,httpRes);
		else {
			msg ="Aucune prise en charge trouvée";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
		
	}
	
	
	@Transactional
	@PutMapping("/priseEnCharge/{idPrise}")
	public ResponseEntity<?> updatePriseEnCharge (@PathVariable("idPrise") Integer id, @RequestBody PriseEnCharge priseEnCharge) {
		logger.info("Modification d'une prise en charge");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			if(priseEnCharge.getColis() != null) {
				//On sauvegarde les eventuels changements du colis
				Colis colis = priseEnCharge.getColis();
				colis = colisrepository.saveAndFlush(colis);
				priseEnCharge.setColis(colis);
			}
			
			//On sauvegarde maintenant la prise en charge
			priseEnCharge = priserepository.saveAndFlush(priseEnCharge);

			res = new ResponseEntity<>(priseEnCharge,httpRes);

		}catch (Exception e) {
			msg ="La transaction non modifiée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}
	
	@Transactional
	@PutMapping("/priseEnCharge/annule/{idPrise}")
	public ResponseEntity<?> annulePriseEnCharge (@PathVariable("idPrise") Integer id, @RequestBody PriseEnCharge priseEnCharge) {
		logger.info("Annulation d'une prise en charge");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		try {
			//On sauvegarde du changement de statut du colis
			Colis colis = priseEnCharge.getColis();
			colis.setPriseEnCharges(null);
			colis = colisrepository.saveAndFlush(colis);
			
			priseEnCharge.setColis(null);
			
			//On sauvegarde maintenant la prise en charge
			priseEnCharge = priserepository.saveAndFlush(priseEnCharge);

			res = new ResponseEntity<>(priseEnCharge,httpRes);

		}catch (Exception e) {
			msg ="La transaction non modifiée:" + e.getMessage();
			logger.error(msg);
			httpRes = HttpStatus.EXPECTATION_FAILED;
			res = new ResponseEntity<>(msg,httpRes);

		}

		logger.info(msg);
		return  res;

	}

	

}