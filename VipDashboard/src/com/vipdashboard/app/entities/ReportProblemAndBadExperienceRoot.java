package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ReportProblemAndBadExperienceRoot {
	@SerializedName("ReportProblemAndBadExperience")
	public List<ReportProblemAndBadExperience> ReportProblemAndBadExperienceList;

	public ReportProblemAndBadExperienceRoot() {
		
		ReportProblemAndBadExperienceList = new ArrayList<ReportProblemAndBadExperience>();
	}
}
