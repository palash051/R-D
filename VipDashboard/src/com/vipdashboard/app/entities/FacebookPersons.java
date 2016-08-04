package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class FacebookPersons {
	@SerializedName("Facebook_Person")
	public FacebokPerson facebokPerson;

	public FacebookPersons() {
		facebokPerson = new FacebokPerson();
	}
}
