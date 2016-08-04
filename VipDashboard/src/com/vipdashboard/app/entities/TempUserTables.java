package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TempUserTables {
	@SerializedName("TempUserTable")
	public List<TempUserTable> TempUserTableList;

	public TempUserTables() {
		TempUserTableList = new ArrayList<TempUserTable>();

	}
}
