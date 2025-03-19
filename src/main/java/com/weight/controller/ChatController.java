package com.weight.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.weight.model.Messages;
import com.weight.repository.MessagesRepository;
import com.weight.views.MessageView;

//@CrossOrigin(origins = {"http://192.168.1.21:4200","http://localhost:4200"})
@CrossOrigin(origins = "*")
@RestController
//@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private MessagesRepository messagesrepository;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
/*
	@Transactional
	@MessageMapping("/chat/{roomId}")
	public void sendMessage(@DestinationVariable String roomId, @Payload MessageView message) {
		logger.info("Insertion d'un message");
		String msg ="Opération réalisée avec succès";
		// Enregistrer le message en base
		try {
			int idPrise = message.getIdPrise();
			String idUserPrise = message.getIdUserPrise();
			String idUserColis = message.getIdUserColis();
			String sender = message.getSender();
			Date horodatage = message.getHorodatage();
			String text = message.getMessage();

			messagesrepository.insertMessage(idPrise, idUserPrise, idUserColis, sender, horodatage, text);

		}catch (Exception e) {
			msg ="Message non inséré:" + e.getMessage();
			logger.error(msg);

		}

		messagingTemplate.convertAndSend("/topic/chat/" + roomId, message);
	}
*/

	@GetMapping("/history/{roomId}")
	@ResponseBody
	public List<MessageView> getChatHistory(@PathVariable int roomId) {
		
		logger.info("Récupération des messages d'une prise en charge");
		String msg ="Opération réalisée avec succès";
		
		List<MessageView> lstViews = null;
		List<Messages> lst = messagesrepository.getLstMessageByIdPrise(roomId);
		if(lst != null && lst.size()>0) {
			lstViews = new ArrayList<>();
			for(Messages message:lst)
				lstViews.add(new MessageView(message));
			
		}else {
			msg ="Aucun message trouvé";
			logger.warn(msg);
		}
		
		logger.info(msg);
		return lstViews;
	}


	/*
	@GetMapping("/messages")
	public ResponseEntity<?> getAllMessagesByIdAndUserColis (int idPrise,String idUserColis) {

		logger.info("Récupération des messages d'une prise en charge");
		String msg ="Opération réalisée avec succès";
		HttpStatus httpRes = HttpStatus.OK;
		ResponseEntity<?> res = null;

		List<Messages> lst = messagesrepository.getLstMessageByIdAndUserColis(idPrise,idUserColis);

		if(lst != null && lst.size()>0) {
			List<MessageView> lstViews = new ArrayList<>();
			for(Messages message:lst)
				lstViews.add(new MessageView(message));
			res = new ResponseEntity<>(lstViews,httpRes);
		}else {
			msg ="Aucun message trouvé";
			logger.warn(msg);
			httpRes = HttpStatus.NOT_FOUND;
			res = new ResponseEntity<>(msg,httpRes);
		}

		logger.info(msg);
		return  res;
	}

	@Transactional
	@MessageMapping("/chat/{idPrise}")
	@SendTo("/topic/chat/{idPrise}")
	public MessageView insertMessage(@RequestBody MessageView view) {
		logger.info("Insertion d'un message");
		String msg ="Opération réalisée avec succès";
		String message = view.getMessage();

		try {
			int idPrise = view.getIdPrise();
			String idUserPrise = view.getIdUserPrise();
			String idUserColis = view.getIdUserColis();
			String sender = view.getSender();
			Date horodatage = view.getHorodatage();


			messagesrepository.insertMessage(idPrise, idUserPrise, idUserColis, sender, horodatage, message);

		}catch (Exception e) {
			msg ="Message non inséré:" + e.getMessage();
			logger.error(msg);

		}

		logger.info(msg);
		return  view;

	}
	*/
}
