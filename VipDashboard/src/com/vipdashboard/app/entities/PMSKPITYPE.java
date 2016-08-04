package com.vipdashboard.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class PMSKPITYPE {
	@SerializedName("TypeId")
	public int TypeId;
	@SerializedName("KPI_Type")
	public String KPI_Type;
	@SerializedName("PMKPIs")
	public ArrayList<PMKPI> PMKPIs;
	public PMSKPITYPE() {
		PMKPIs = new ArrayList<PMKPI>();
	}
}
