// src/main/java/com/weight/views/PaymentResponseView.java
package com.weight.views;

import java.math.BigDecimal;
import java.util.Date;

import com.weight.model.Colis;
import com.weight.model.Payment;
import com.weight.model.PaymentMethod;
import com.weight.model.PriseEnCharge;
import com.weight.model.Users;

public class PaymentResponseView {
    private int paymentId;
    private String transactionId;
    private String status;
    private String paymentUrl;
    
    private BigDecimal paymentAmount;
    private Double baseAmount;
    private Double serviceFees;
    
    private String idUser;
    private int colisId;
    private int priseId;
    private Date paymentDate;
    private int idMethod;
    
    private Users user;
    private Colis colis;
    private PaymentMethod paymentMethod;
    private PriseEnCharge priseEnCharge;
    
    // Constructeur par défaut
    public PaymentResponseView() {
    }
    
    
    public PaymentResponseView(int paymentId, String transactionId, String status, String paymentUrl,
			BigDecimal paymentAmount, Double baseAmount, Double serviceFees, String idUser, int colisId, int priseId,
			Date paymentDate, int idMethod, Users user, Colis colis, PaymentMethod paymentMethod,
			PriseEnCharge priseEnCharge) {
		super();
		this.paymentId = paymentId;
		this.transactionId = transactionId;
		this.status = status;
		this.paymentUrl = paymentUrl;
		this.paymentAmount = paymentAmount;
		this.baseAmount = baseAmount;
		this.serviceFees = serviceFees;
		this.idUser = idUser;
		this.colisId = colisId;
		this.priseId = priseId;
		this.paymentDate = paymentDate;
		this.idMethod = idMethod;
		this.user = user;
		this.colis = colis;
		this.paymentMethod = paymentMethod;
		this.priseEnCharge = priseEnCharge;
	}

    
    public PaymentResponseView(Payment payment) {
		super();
		this.paymentId = payment.getIdPayment();
		this.transactionId = payment.getTransactionId();
		this.status = payment.getPaymentStatus();
		//this.paymentUrl = payment.;
		this.paymentAmount = payment.getPaymentAmount();
		this.baseAmount = payment.getBaseAmount();
		this.serviceFees = payment.getServiceFees();
		this.idUser = payment.getUser().getIdUser();
		this.colisId = payment.getColis().getIdColis();
		this.priseId = payment.getPriseEnCharge().getIdPrise();
		this.paymentDate = payment.getPaymentDate();
		this.user = payment.getUser();
		this.colis = payment.getColis();
		if(payment.getPaymentMethod() != null) {
			this.paymentMethod = payment.getPaymentMethod();
			this.idMethod = payment.getPaymentMethod().getIdMethod();
		}
		this.priseEnCharge = payment.getPriseEnCharge();
	}
    

	// Getters et Setters
    public int getPaymentId() {
        return paymentId;
    }
    
    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getPaymentUrl() {
        return paymentUrl;
    }
    
    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public Double getBaseAmount() {
		return baseAmount;
	}

	public void setBaseAmount(Double baseAmount) {
		this.baseAmount = baseAmount;
	}

	public Double getServiceFees() {
		return serviceFees;
	}

	public void setServiceFees(Double serviceFees) {
		this.serviceFees = serviceFees;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public int getColisId() {
		return colisId;
	}

	public void setColisId(int colisId) {
		this.colisId = colisId;
	}

	public int getPriseId() {
		return priseId;
	}

	public void setPriseId(int priseId) {
		this.priseId = priseId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public int getIdMethod() {
		return idMethod;
	}

	public void setIdMethod(int idMethod) {
		this.idMethod = idMethod;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	public Colis getColis() {
		return colis;
	}

	public void setColis(Colis colis) {
		this.colis = colis;
	}

	public PaymentMethod getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(PaymentMethod paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public PriseEnCharge getPriseEnCharge() {
		return priseEnCharge;
	}

	public void setPriseEnCharge(PriseEnCharge priseEnCharge) {
		this.priseEnCharge = priseEnCharge;
	}
	
	
    
    
}