// src/main/java/com/weight/model/MessagesId.java 
package com.weight.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class MessagesId implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    private int idPrise;
    private String idUserPrise;
    private String idUserColis;
    private String sender;
    private Date horodatage;
    private String message;
    
    // Nouveaux champs pour la lecture et les notifications
    private boolean isRead;
    private boolean notificationSent;

    public MessagesId() {
    }

    public MessagesId(int idPrise, String idUserPrise, String idUserColis, String sender, Date horodatage,
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
    
    public MessagesId(int idPrise, String idUserPrise, String idUserColis, String sender, Date horodatage,
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

    @Column(name = "id_prise", nullable = false)
    public int getIdPrise() {
        return this.idPrise;
    }

    public void setIdPrise(int idPrise) {
        this.idPrise = idPrise;
    }

    @Column(name = "id_user_prise", nullable = false, length = 50)
    public String getIdUserPrise() {
        return this.idUserPrise;
    }

    public void setIdUserPrise(String idUserPrise) {
        this.idUserPrise = idUserPrise;
    }

    @Column(name = "id_user_colis", nullable = false, length = 50)
    public String getIdUserColis() {
        return this.idUserColis;
    }

    public void setIdUserColis(String idUserColis) {
        this.idUserColis = idUserColis;
    }

    @Column(name = "sender", nullable = false, length = 50)
    public String getSender() {
        return this.sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Column(name = "horodatage", nullable = false, length = 19)
    public Date getHorodatage() {
        return this.horodatage;
    }

    public void setHorodatage(Date horodatage) {
        this.horodatage = horodatage;
    }

    @Column(name = "message", nullable = false, length = 500)
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Column(name = "is_read", nullable = false)
    public boolean isRead() {
        return this.isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    @Column(name = "notification_sent", nullable = false)
    public boolean isNotificationSent() {
        return this.notificationSent;
    }

    public void setNotificationSent(boolean notificationSent) {
        this.notificationSent = notificationSent;
    }

    public boolean equals(Object other) {
        if ((this == other))
            return true;
        if ((other == null))
            return false;
        if (!(other instanceof MessagesId))
            return false;
        MessagesId castOther = (MessagesId) other;

        return (this.getIdPrise() == castOther.getIdPrise())
                && ((this.getIdUserPrise() == castOther.getIdUserPrise())
                        || (this.getIdUserPrise() != null && castOther.getIdUserPrise() != null
                                && this.getIdUserPrise().equals(castOther.getIdUserPrise())))
                && ((this.getIdUserColis() == castOther.getIdUserColis())
                        || (this.getIdUserColis() != null && castOther.getIdUserColis() != null
                                && this.getIdUserColis().equals(castOther.getIdUserColis())))
                && ((this.getSender() == castOther.getSender()) || (this.getSender() != null
                        && castOther.getSender() != null && this.getSender().equals(castOther.getSender())))
                && ((this.getHorodatage() == castOther.getHorodatage()) || (this.getHorodatage() != null
                        && castOther.getHorodatage() != null && this.getHorodatage().equals(castOther.getHorodatage())))
                && ((this.getMessage() == castOther.getMessage()) || (this.getMessage() != null
                        && castOther.getMessage() != null && this.getMessage().equals(castOther.getMessage())));
    }

    public int hashCode() {
        int result = 17;

        result = 37 * result + this.getIdPrise();
        result = 37 * result + (getIdUserPrise() == null ? 0 : this.getIdUserPrise().hashCode());
        result = 37 * result + (getIdUserColis() == null ? 0 : this.getIdUserColis().hashCode());
        result = 37 * result + (getSender() == null ? 0 : this.getSender().hashCode());
        result = 37 * result + (getHorodatage() == null ? 0 : this.getHorodatage().hashCode());
        result = 37 * result + (getMessage() == null ? 0 : this.getMessage().hashCode());
        return result;
    }
}
