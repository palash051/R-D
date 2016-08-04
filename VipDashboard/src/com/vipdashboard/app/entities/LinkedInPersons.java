package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class LinkedInPersons {
	@SerializedName("LinkedIn_Person")
	public LinkedInPerson linkedInPerson;

	public LinkedInPersons() {
		linkedInPerson = new LinkedInPerson();
	}
}
