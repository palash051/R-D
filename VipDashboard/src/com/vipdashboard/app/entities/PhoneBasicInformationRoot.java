package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class PhoneBasicInformationRoot {
	public PhoneBasicInformationRoot() {
		phoneBasicInformation = new PhoneBasicInformation();
	}

	@SerializedName("PhoneBasicInformation")
	public PhoneBasicInformation phoneBasicInformation;
}
