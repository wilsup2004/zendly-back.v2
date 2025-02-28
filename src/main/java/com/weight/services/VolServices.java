package com.weight.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

@Service
public class VolServices {
	
	private String accessKey = "26abfba6fe75c9f10f6ed4f4c185daa0";
	private String url = "http://api.aviationstack.com/v1/flights?";
	private String idDep = "dep_iata";
	private String idArr = "arr_iata";
	private String statut = "flight_status";
	private String statut_programme = "scheduled";
	private String statut_actif = "active";
	
	
	public String getIdDep() {
		return idDep;
	}
	public void setIdDep(String idDep) {
		this.idDep = idDep;
	}
	public String getIdArr() {
		return idArr;
	}
	public void setIdArr(String idArr) {
		this.idArr = idArr;
	}
	
	
	public List<Vol> getLstVols(String idOrigin,String idDestination){		
		List<Vol> lstVols =null;
		ResSearchFlight res = null;
		
	      try {
				//ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				

				OkHttpClient client = new OkHttpClient();
			      Request request = new Request.Builder()
			          .url( this.url
			          + "access_key=" 
			          + accessKey
			          + "&dep_iata="
			          + idOrigin
			          + "&arr_iata="
			          + idDestination)
			          .get().addHeader("Accept", "*/*")
				      .build();
			
			      Response response = client.newCall(request).execute();
			      String json = response.body().string();
			
			      System.out.println(json);
			    
				if(response !=null) {
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
					res = mapper.readValue(json, ResSearchFlight.class);
					lstVols = res.getData();
				}
				System.out.println(res);
			
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		
		return lstVols;		
	}
	
	public Vol getVolByTrajetAndIdAndStatut(String idOrigin,String idDestination,String idVol,String statut){		
		Vol vol =null;
		ResSearchFlight res = null;
		
	      try {
				//ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				

				OkHttpClient client = new OkHttpClient();
			      Request request = new Request.Builder()
			          .url( this.url
			          + "access_key=" 
			          + accessKey
			          + "&flight_status="
			          + statut
			          + "&flight_number="
			          + idVol
			          + "&dep_iata="
			          + idOrigin
			          + "&arr_iata="
			          + idDestination)
			          .get().addHeader("Accept", "*/*")
				      .build();
			
			      Response response = client.newCall(request).execute();
			      String json = response.body().string();
			
			      System.out.println(json);
			    
				if(response !=null) {
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(Feature.AUTO_CLOSE_SOURCE, true);
					res = mapper.readValue(json, ResSearchFlight.class);
					if(res.getData()!= null && res.getData().size()>0)
						vol = res.getData().get(0);
				}
				System.out.println(res);
			
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		
		return vol;		
	}
}
