package com.mobilink.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CompanyHolder {
	@SerializedName("CompanySetup")
	public CompanySetup companySetup;

	public CompanyHolder() {
		companySetup = new CompanySetup();
	}
}
