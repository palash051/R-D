package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class TTStatu {
	@SerializedName("TTStatus")
	public TTStatus TTstatus;
	public TTStatu() {
		TTstatus=new TTStatus();
	}
}
