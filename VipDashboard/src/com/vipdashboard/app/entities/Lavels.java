package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Lavels {
	@SerializedName("LevelConfigurations")
	public List<Lavel> lavelList;

	public Lavels() {
		lavelList = new ArrayList<Lavel>();
	}
}
