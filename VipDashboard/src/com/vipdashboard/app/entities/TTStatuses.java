package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class TTStatuses {
	@SerializedName("TTStetuses")
	public List<TTStatus> TTstatusList;

	public TTStatuses() {
		TTstatusList = new ArrayList<TTStatus>();
	}
}
