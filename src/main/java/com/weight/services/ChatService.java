// src/main/java/com/weight/services/ChatService.java
package com.weight.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.weight.model.Messages;
import com.weight.model.MessagesId;
import com.weight.repository.MessagesRepository;
import com.weight.views.MessageView;

@Service
public class ChatService {

    @Autowired
    private MessagesRepository messagesRepository;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Récupère l'historique des messages pour une prise en charge spécifique
     * 
     * @param idPrise ID de la prise en charge
     * @return Liste des messages
     */
    public List<MessageView> getChatHistory(int idPrise) {
        List<Messages> messages = messagesRepository.getLstMessageByIdPrise(idPrise);
        if (messages != null && !messages.isEmpty()) {
            return messages.stream()
                    .map(MessageView::new)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * Envoie un message dans une conversation
     * 
     * @param messageView Message à envoyer
     * @return L'ID du message créé
     */
    @Transactional
    public void sendMessage(MessageView messageView) {
        // Vérification des données requises
        if (messageView.getIdPrise() <= 0 || messageView.getIdUserPrise() == null ||
            messageView.getIdUserColis() == null || messageView.getSender() == null ||
            messageView.getMessage() == null) {
            throw new IllegalArgumentException("Informations de message incomplètes");
        }
        
        // Insertion du message en base de données
        messagesRepository.insertMessage(
            messageView.getIdPrise(),
            messageView.getIdUserPrise(),
            messageView.getIdUserColis(),
            messageView.getSender(),
            messageView.getHorodatage() != null ? messageView.getHorodatage() : new Date(),
            messageView.getMessage(),
            false
        );
        
        // Envoi du message via WebSocket
        messagingTemplate.convertAndSend("/topic/chat/" + messageView.getIdPrise(), messageView);
    }
    
    /**
     * Marque un message comme lu
     * 
     * @param idPrise ID de la prise en charge
     * @param messageId ID du message
     * @param userId ID de l'utilisateur qui a lu le message
     * @return true si le message a été marqué comme lu
     */
    @Transactional
    public boolean markMessageAsRead(int idPrise, Date messageDate, String userId) { 
       // List<Messages> messages = messagesRepository.getLstMessageByIdPriseAndDate(idPrise, messageDate);
    List<Messages> messages = messagesRepository.getLstMessageByIdPrise(idPrise);
    if (messages != null && !messages.isEmpty()) {
        	 boolean anyUpdated = false;
        	 
             /*
             // Parcourir tous les messages trouvés
             for (Messages message : messages) {
                 // Vérifier si le message n'est pas du destinataire
                 if (!message.getUsersBySender().equals(userId)) {
                     // Marquer le message comme lu
                     message.getId().setRead(true);
                     message = messagesRepository.saveAndFlush(message);
                     anyUpdated = true;
                 }
             }
             */
        	 messagesRepository.markAllMessageAsRead(idPrise, userId);
             return anyUpdated;
        }
        return false;
    }
    
    /**
     * Marque tous les messages d'une conversation comme lus pour un utilisateur spécifique
     * 
     * @param idPrise ID de la prise en charge
     * @param userId ID de l'utilisateur qui a lu les messages
     * @return Nombre de messages marqués comme lus
     */
    @Transactional
    public int markAllMessagesAsReadInRoom(int idPrise, String userId) {
        // Récupérer tous les messages non lus de cette conversation qui ne sont pas de l'utilisateur
        List<Messages> unreadMessages = messagesRepository.getLstUnreadMessagesByPriseAndUser(idPrise, userId);
        
        if (unreadMessages != null && !unreadMessages.isEmpty()) {
            int count = 0;
            
            // Parcourir tous les messages et les marquer comme lus
            for (Messages message : unreadMessages) {
                if (!message.getUsersBySender().equals(userId)) {
                	message.getId().setRead(true);
                    messagesRepository.save(message);
                    count++;
                }
            }
            
            return count;
        }
        
        return 0;
    }
    
    /**
     * Récupère les messages non lus pour un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des messages non lus
     */
    public List<MessageView> getUnreadMessages(String userId) {
        List<Messages> messages = messagesRepository.getLstUnreadMessagesByUser(userId);
        if (messages != null && !messages.isEmpty()) {
            return messages.stream()
                    .map(MessageView::new)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * Récupère le nombre de messages non lus pour un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Nombre de messages non lus
     */
    public int countUnreadMessages(String userId) {
        return messagesRepository.countUnreadMessagesByUser(userId);
    }
    
    /**
     * Récupère les conversations actives d'un utilisateur
     * 
     * @param userId ID de l'utilisateur
     * @return Liste des ID de prises en charge avec des conversations actives
     */
    public List<Integer> getActiveConversations(String userId) {
        return messagesRepository.findActiveConversationsByUser(userId);
    }
    
    /**
     * Envoie une notification pour un nouveau message
     * 
     * @param messageView Message pour lequel envoyer une notification
     */
    public void sendNotification(MessageView messageView) {
        // Si le message est pour le propriétaire du colis
        if (!messageView.getSender().equals(messageView.getIdUserColis())) {
            messagingTemplate.convertAndSendToUser(
                messageView.getIdUserColis(),
                "/queue/notifications",
                messageView
            );
        }
        
        // Si le message est pour la personne qui prend en charge
        if (!messageView.getSender().equals(messageView.getIdUserPrise())) {
            messagingTemplate.convertAndSendToUser(
                messageView.getIdUserPrise(),
                "/queue/notifications",
                messageView
            );
        }
    }
}