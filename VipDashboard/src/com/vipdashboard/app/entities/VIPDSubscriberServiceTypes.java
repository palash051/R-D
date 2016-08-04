package com.vipdashboard.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class VIPDSubscriberServiceTypes {
	@SerializedName("SubscriberServiceType")
	public VIPDSubscriberServiceType VIPDSubscriberServiceTs;

	public VIPDSubscriberServiceTypes() {
		VIPDSubscriberServiceTs = new VIPDSubscriberServiceType();

	}

	
}
