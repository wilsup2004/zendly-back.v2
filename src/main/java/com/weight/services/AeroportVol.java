package com.weight.services;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ 
"baggage",
"delay",
"estimated",
"actual",
"estimated_runway",
"actual_runway"
})
public class AeroportVol {

	@JsonProperty("airport")
	private String airport ;
	
	@JsonProperty("timezone")
	private String timezone;
	
	@JsonProperty("iata")
	private String iata;
	
	@JsonProperty("icao")
	private String icao;
	
	@JsonProperty("terminal")
	private String terminal;
	
	@JsonProperty("gate")
	private String gate;
	
	@JsonProperty("scheduled")
	private Date scheduled;
	
	
	
	public AeroportVol() {
		super();
	}
	
	
	public AeroportVol(String airport, String timezone, String iata, String icao, String terminal, String gate,
			Date scheduled) {
		super();
		this.airport = airport;
		this.timezone = timezone;
		this.iata = iata;
		this.icao = icao;
		this.terminal = terminal;
		this.gate = gate;
		this.scheduled = scheduled;
	}


	public String getAirport() {
		return airport;
	}
	public void setAirport(String airport) {
		this.airport = airport;
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
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
	public String getTerminal() {
		return terminal;
	}
	public void setTerminal(String terminal) {
		this.terminal = terminal;
	}
	public String getGate() {
		return gate;
	}
	public void setGate(String gate) {
		this.gate = gate;
	}
	public Date getScheduled() {
		return scheduled;
	}
	public void setScheduled(Date scheduled) {
		this.scheduled = scheduled;
	}
	
	
}
