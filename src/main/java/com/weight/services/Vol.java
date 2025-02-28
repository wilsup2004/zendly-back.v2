package com.weight.services;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({ 
"aircraft",
"live"
})
public class Vol {

	@JsonProperty("flight_date")
	private Date flight_date;
	
	@JsonProperty("flight_status")
	private String flight_status;
	
	@JsonProperty("departure")
	private AeroportVol departure;
	
	@JsonProperty("arrival")
	private AeroportVol arrival;
	
	@JsonProperty("airline")
	private Airline airline;
	
	@JsonProperty("flight")
	private Flight flight;
	
	
	private String aircompagnieline ;
	
	private String numVol;
	
	
	public Vol() {
		super();
	}
	

	public Vol(Date flight_date, String flight_status, AeroportVol departure, AeroportVol arrival, Airline airline,
			Flight flight, String aircompagnieline, String numVol) {
		super();
		this.flight_date = flight_date;
		this.flight_status = flight_status;
		this.departure = departure;
		this.arrival = arrival;
		this.airline = airline;
		this.flight = flight;
		this.aircompagnieline = aircompagnieline;
		this.numVol = numVol;
	}

	public Date getFlight_date() {
		return flight_date;
	}
	public void setFlight_date(Date flight_date) {
		this.flight_date = flight_date;
	}
	public String getFlight_status() {
		return flight_status;
	}
	public void setFlight_status(String flight_status) {
		this.flight_status = flight_status;
	}
	public AeroportVol getDeparture() {
		return departure;
	}
	public void setDeparture(AeroportVol departure) {
		this.departure = departure;
	}
	public AeroportVol getArrival() {
		return arrival;
	}
	public void setArrival(AeroportVol arrival) {
		this.arrival = arrival;
	}
	
	public Airline getAirline() {
		return airline;
	}

	public void setAirline(Airline airline) {
		this.airline = airline;
	}

	public Flight getFlight() {
		return flight;
	}

	public void setFlight(Flight flight) {
		this.flight = flight;
	}

	public String getAircompagnieline() {
		if(this.airline!=null && this.airline.getName()!=null)
			aircompagnieline = this.airline.getName();
		return aircompagnieline;
	}
	public void setAircompagnieline(String aircompagnieline) {
		this.aircompagnieline = aircompagnieline;
	}
	public String getNumVol() {
		if(this.flight!=null && this.flight.getIcao()!=null)
			numVol = this.flight.getIcao();
		return numVol;
	}
	public void setNumVol(String numVol) {
		this.numVol = numVol;
	}
	
	
	
}
