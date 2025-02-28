package com.weight.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ 
"codeshared"
})
public class Flight {
	
	@JsonProperty("number")
	private String number;
	
	@JsonProperty("iata")
	private String iata;
	
	@JsonProperty("icao")
	private String icao;
	
	
	public Flight() {
		super();
	}
	
	
	public Flight(String number, String iata, String icao) {
		super();
		this.number = number;
		this.iata = iata;
		this.icao = icao;
	}


	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getIata() {
		return iata;
	}
	public void setIata(String iata) {
		this.iata = iata;
	}
	public String getIcao() {
		return icao;
	}
	public void setIcao(String icao) {
		this.icao = icao;
	}

	
}
