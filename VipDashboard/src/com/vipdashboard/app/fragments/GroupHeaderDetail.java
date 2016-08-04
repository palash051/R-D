package com.vipdashboard.app.fragments;

import java.util.ArrayList;

public class GroupHeaderDetail {
	private String name;
	private ArrayList<ResidualFileItem> residualList = new ArrayList<ResidualFileItem>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ResidualFileItem> getResidualFileItemsList() {
		return residualList;
	}

	public void setResidualFileItemList(ArrayList<ResidualFileItem> residualList) {
		this.residualList = residualList;
	}

}
