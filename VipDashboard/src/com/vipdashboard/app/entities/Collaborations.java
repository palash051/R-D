package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Collaborations {
	@SerializedName("Collaborations")
	public List<Collaboration> collaborationList;

	public Collaborations() {
		collaborationList = new ArrayList<Collaboration>();
	}
}
