package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SiteInformations {
	@SerializedName("SiteInformations")
	public List<SiteInformation> siteNotificationsList;

	public SiteInformations() {
		siteNotificationsList = new ArrayList<SiteInformation>();
	}
}
