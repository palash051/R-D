package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class TempTableUserRoot {
	@SerializedName("TempUserTable")
	public TempUserTable tempTableUser;
	public TempTableUserRoot() {
		tempTableUser=new TempUserTable();
	}
}
