package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class VIPDCustomerIssueRoot {
	@SerializedName("CustomerIssue")
	public VIPDCustomerIssue vIPDCustomerIssue;
	public VIPDCustomerIssueRoot() {
		vIPDCustomerIssue=new VIPDCustomerIssue();
	}
}
