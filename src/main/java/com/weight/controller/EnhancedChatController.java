// src/main/java/com/weight/controller/EnhancedChatController.java
package com.weight.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weight.services.ChatService;
import com.weight.views.MessageView;

//@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/chat")
public class EnhancedChatController {

	@Autowired
	private ChatService chatService;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * Envoie un message WebSocket avec enregistrement en base de données
	 * 
	 * @param roomId ID de la salle de discussion (correspond à l'ID de prise en charge)
	 * @param message Le message à envoyer
	 */
	@MessageMapping("/chat/{roomId}")
	public void sendMessage(@DestinationVariable String roomId, @Payload MessageView message) {
		logger.info("Envoi d'un message dans la salle: " + roomId);
		try {
			// Ajout de l'horodatage si non fourni
			if (message.getHorodatage() == null) {
				message.setHorodatage(new Date());
			}

			// Enregistrement et diffusion du message
			chatService.sendMessage(message);

			// Envoi des notifications push si nécessaire
			chatService.sendNotification(message);
		} catch (Exception e) {
			logger.error("Erreur lors de l'envoi du message: " + e.getMessage(), e);
		}
	}

	/**
	 * Récupère l'historique des messages pour une prise en charge
	 * 
	 * @param roomId ID de la prise en charge
	 * @return Liste des messages
	 */
	@GetMapping("/history/{roomId}")
	@ResponseBody
	public ResponseEntity<?> getChatHistory(@PathVariable int roomId) {
		logger.info("Récupération de l'historique des messages pour la prise en charge: " + roomId);

		List<MessageView> messages = chatService.getChatHistory(roomId);
		if (messages != null && !messages.isEmpty()) {
			return new ResponseEntity<>(messages, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Aucun message trouvé", HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Marque un message comme lu
	 * 
	 * @param roomId ID de la prise en charge
	 * @param messageDate Date du message
	 * @param userId ID de l'utilisateur qui a lu le message
	 * @return Statut de l'opération
	 */
	@PutMapping("/read/{roomId}")
	public ResponseEntity<?> markMessageAsRead(
			@PathVariable int roomId,
			@RequestParam String messageDate,  // Accepter une chaîne au lieu d'un Date
			@RequestParam String userId) {	

		try {
			// Convertir la chaîne en Date dans le contrôleur
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			Date date = formatter.parse(messageDate);

			// Appeler votre service avec la date convertie
			chatService.markMessageAsRead(roomId, date, userId);

			return ResponseEntity.ok().build();
		} catch (ParseException e) {
			return ResponseEntity.badRequest().body("Format de date invalide: " + messageDate);
		}
	}
	
	/**
	 * Marque tous les messages d'une conversation comme lus
	 * 
	 * @param roomId ID de la prise en charge
	 * @param userId ID de l'utilisateur qui a lu les messages
	 * @return Statut de l'opération
	 */
	@PutMapping("/read-all/{roomId}")
	public ResponseEntity<?> markAllMessagesAsReadInRoom(
	        @PathVariable int roomId,
	        @RequestParam String userId) {
	    
	    logger.info("Marquage de tous les messages comme lus pour la conversation " + roomId + " par l'utilisateur " + userId);
	    
	    int count = chatService.markAllMessagesAsReadInRoom(roomId, userId);
	    
	    Map<String, Object> response = new HashMap<>();
	    response.put("status", "success");
	    response.put("messagesMarkedAsRead", count);
	    
	    return ResponseEntity.ok(response);
	}
	

	/**
	 * Récupère les messages non lus pour un utilisateur
	 * 
	 * @param userId ID de l'utilisateur
	 * @return Liste des messages non lus
	 */
	@GetMapping("/unread/{userId}")
	public ResponseEntity<?> getUnreadMessages(@PathVariable String userId) {
		logger.info("Récupération des messages non lus pour l'utilisateur: " + userId);

		List<MessageView> messages = chatService.getUnreadMessages(userId);
		return new ResponseEntity<>(messages, HttpStatus.OK);
	}

	/**
	 * Compte les messages non lus pour un utilisateur
	 * 
	 * @param userId ID de l'utilisateur
	 * @return Nombre de messages non lus
	 */
	@GetMapping("/unread/count/{userId}")
	public ResponseEntity<?> countUnreadMessages(@PathVariable String userId) {
		logger.info("Comptage des messages non lus pour l'utilisateur: " + userId);

		int count = chatService.countUnreadMessages(userId);
		Map<String, Integer> response = new HashMap<>();
		response.put("unreadCount", count);

		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	/**
	 * Récupère les conversations actives d'un utilisateur
	 * 
	 * @param userId ID de l'utilisateur
	 * @return Liste des IDs de conversation
	 */
	@GetMapping("/active/{userId}")
	public ResponseEntity<?> getActiveConversations(@PathVariable String userId) {
		logger.info("Récupération des conversations actives pour l'utilisateur: " + userId);

		List<Integer> conversations = chatService.getActiveConversations(userId);
		return new ResponseEntity<>(conversations, HttpStatus.OK);
	}

	/**
	 * Envoie une notification à un utilisateur spécifique
	 * 
	 * @param userId ID de l'utilisateur destinataire
	 * @param message Message à envoyer
	 * @return Statut de l'opération
	 */
	@PostMapping("/notify/{userId}")
	public ResponseEntity<?> sendDirectNotification(
			@PathVariable String userId,
			@RequestBody MessageView message) {

		logger.info("Envoi d'une notification directe à l'utilisateur: " + userId);

		messagingTemplate.convertAndSendToUser(
				userId,
				"/queue/notifications",
				message
				);

		return new ResponseEntity<>("Notification envoyée", HttpStatus.OK);
	}
}
