package com.weight.services;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ResSearchFlight {
	
	@JsonProperty("pagination")
	private Pagination pagination;
	
	@JsonProperty("data")
	private List<Vol> data ;
	
	
	public ResSearchFlight() {
		super();
	}
	
	
	public ResSearchFlight(Pagination pagination,List<Vol>data) {
		super();
		this.pagination = pagination;
		this.data = data;
	}


	public Pagination getPagination() {
		return pagination;
	}
	public void setPagination(Pagination pagination) {
		this.pagination = pagination;
	}
	public List<Vol>getData() {
		return data;
	}
	public void setData(List<Vol> data) {
		this.data = data;
	}
	
	

}
