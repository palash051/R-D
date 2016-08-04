package com.vipdashboard.app.entities;

import com.vipdashboard.app.utils.CommonURL;

public class Country {
	public String CountryCodeIso;
	public String CountryName;
	public String CountryPhoneCode;
	public String CountryFlagUrl;

	public Country(String countryCodeIso, String countryName,
			String countryPhoneCode) {
		this.CountryCodeIso = countryCodeIso;
		this.CountryName = countryName;
		this.CountryPhoneCode = countryPhoneCode;
		this.CountryFlagUrl=CommonURL.getInstance().getImageServer.replace("CareSolutoin", "CountryFlags")+ countryName.replace(" ", "_")+".png";		
	}
}
