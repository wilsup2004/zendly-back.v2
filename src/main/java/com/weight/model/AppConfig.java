package com.weight.model;
// Generated 10 f�vr. 2025, 20:27:41 by Hibernate Tools 4.3.6.Final

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;



@Entity
@Table(name = "app_config")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AppConfig implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
		
	@Column(name = "param_key")
	private String paramKey;
	
	@Column(name = "param_value")
	private String paramValue;
	
	@Column(name = "description")
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_at")
	private Date createdAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_at")
	private Date updatedAt;
	
	
	public AppConfig() {
	}


	public AppConfig(Integer id, String paramKey, String paramValue, String description, Date createdAt,
			Date updatedAt) {
		super();
		this.id = id;
		this.paramKey = paramKey;
		this.paramValue = paramValue;
		this.description = description;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getParamKey() {
		return paramKey;
	}


	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}


	public String getParamValue() {
		return paramValue;
	}


	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}


	public Date getUpdatedAt() {
		return updatedAt;
	}


	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}



}
