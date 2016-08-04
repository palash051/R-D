package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CompanySetups {
	@SerializedName("CompanySetups")
	public List<CompanySetup> companySetupList;

	public CompanySetups() {
		companySetupList = new ArrayList<CompanySetup>();
	}
}
