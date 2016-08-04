package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorLicenseEntityHolder {
	
	@SerializedName("OperatorLicense")
	public OperatorLicense operatorLicense;
	
	public OperatorLicenseEntityHolder()
	{
		operatorLicense=new OperatorLicense();
	}

}
