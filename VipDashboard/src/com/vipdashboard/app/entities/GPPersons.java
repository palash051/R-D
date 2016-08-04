package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class GPPersons {
	@SerializedName("GP_Person")
	public GPPerson GPPerson;

	public GPPersons() {
		GPPerson = new GPPerson();
	}
}
