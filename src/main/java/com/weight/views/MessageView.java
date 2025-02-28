package com.weight.views;

import java.util.Date;

import com.weight.model.Messages;

public class MessageView{

	private int idPrise;
	private String idUserPrise;
	private String idUserColis;
	private String sender;
	private Date horodatage;
	private String message;
	
	public MessageView() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MessageView(int idPrise, String idUserPrise, String idUserColis, String sender, Date horodatage,
			String message) {
		super();
		this.idPrise = idPrise;
		this.idUserPrise = idUserPrise;
		this.idUserColis = idUserColis;
		this.sender = sender;
		this.horodatage = horodatage;
		this.message = message;
	}

	
	public MessageView(Messages mes) {
		super();
		this.idPrise = mes.getId().getIdPrise();
		this.idUserPrise = mes.getId().getIdUserPrise();
		this.idUserColis = mes.getId().getIdUserColis();
		this.sender = mes.getId().getSender();
		this.horodatage = mes.getId().getHorodatage();
		this.message = mes.getId().getMessage();
	}
	
	
	

	public int getIdPrise() {
		return idPrise;
	}

	public void setIdPrise(int idPrise) {
		this.idPrise = idPrise;
	}

	public String getIdUserPrise() {
		return idUserPrise;
	}

	public void setIdUserPrise(String idUserPrise) {
		this.idUserPrise = idUserPrise;
	}

	public String getIdUserColis() {
		return idUserColis;
	}

	public void setIdUserColis(String idUserColis) {
		this.idUserColis = idUserColis;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public Date getHorodatage() {
		return horodatage;
	}

	public void setHorodatage(Date horodatage) {
		this.horodatage = horodatage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
}
