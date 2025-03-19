package com.weight.views;

import java.util.Date;

import com.weight.model.Messages;

public class MessageView {
    private int idPrise;
    private String idUserPrise;
    private String idUserColis;
    private String sender;
    private Date horodatage;
    private String message;
    private boolean isRead;
    private boolean notificationSent;

    public MessageView() {
    }

    public MessageView(int idPrise, String idUserPrise, String idUserColis, String sender, Date horodatage,
            String message) {
        this.idPrise = idPrise;
        this.idUserPrise = idUserPrise;
        this.idUserColis = idUserColis;
        this.sender = sender;
        this.horodatage = horodatage;
        this.message = message;
        this.isRead = false;
        this.notificationSent = false;
    }
    
    public MessageView(int idPrise, String idUserPrise, String idUserColis, String sender, Date horodatage,
            String message, boolean isRead, boolean notificationSent) {
        this.idPrise = idPrise;
        this.idUserPrise = idUserPrise;
        this.idUserColis = idUserColis;
        this.sender = sender;
        this.horodatage = horodatage;
        this.message = message;
        this.isRead = isRead;
        this.notificationSent = notificationSent;
    }

    public MessageView(Messages mes) {
        this.idPrise = mes.getId().getIdPrise();
        this.idUserPrise = mes.getId().getIdUserPrise();
        this.idUserColis = mes.getId().getIdUserColis();
        this.sender = mes.getId().getSender();
        this.horodatage = mes.getId().getHorodatage();
        this.message = mes.getId().getMessage();
        this.isRead = mes.getId().isRead();
        this.notificationSent = mes.getId().isNotificationSent();
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
    
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isNotificationSent() {
        return notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }
}