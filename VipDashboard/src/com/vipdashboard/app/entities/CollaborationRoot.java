package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class CollaborationRoot {
	@SerializedName("Collaboration")
	public Collaboration collaboration;

	public CollaborationRoot() {
		collaboration = new Collaboration();
	}
}
