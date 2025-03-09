package com.weight.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
/**
 * Aeroport 
 */
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aeroport")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Aeroport implements java.io.Serializable,Comparable<Aeroport>  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id_aero")
	private String idAero;
	
	@Column(name = "aero_nom", length = 100)
	private String aeroNom;
	
	@Column(name = "aero_ville", length = 100)
	private String aeroVille;
	
	@Column(name = "aero_pays", length = 100)
	private String aeroPays;
	
	@Column(name = "aero_latitude")
	private Float aeroLatitude;
	
	@Column(name = "aero_longitude")
	private Float aeroLongitude;
	

	public Aeroport() {
	}

	public Aeroport(String idAero, String aeroNom) {
		this.idAero = idAero;
		this.aeroNom = aeroNom;
	}

	public Aeroport(String idAero, String aeroNom, String aeroVille, String aeroPays, Float aeroLatitude,
			Float aeroLongitude) {
		this.idAero = idAero;
		this.aeroNom = aeroNom;
		this.aeroVille = aeroVille;
		this.aeroPays = aeroPays;
		this.aeroLatitude = aeroLatitude;
		this.aeroLongitude = aeroLongitude;
	}

	
	public String getIdAero() {
		return this.idAero;
	}

	public void setIdAero(String idAero) {
		this.idAero = idAero;
	}

	
	public String getAeroNom() {
		return this.aeroNom;
	}

	public void setAeroNom(String aeroNom) {
		this.aeroNom = aeroNom;
	}

	
	public String getAeroVille() {
		return this.aeroVille;
	}

	public void setAeroVille(String aeroVille) {
		this.aeroVille = aeroVille;
	}

	
	public String getAeroPays() {
		return this.aeroPays;
	}

	public void setAeroPays(String aeroPays) {
		this.aeroPays = aeroPays;
	}

	
	public Float getAeroLatitude() {
		return this.aeroLatitude;
	}

	public void setAeroLatitude(Float aeroLatitude) {
		this.aeroLatitude = aeroLatitude;
	}


	public Float getAeroLongitude() {
		return this.aeroLongitude;
	}

	public void setAeroLongitude(Float aeroLongitude) {
		this.aeroLongitude = aeroLongitude;
	}

	@Override
	public int compareTo(Aeroport o) {
		return this.getAeroNom().compareTo(o.getAeroNom());
	}

}
