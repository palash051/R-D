package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorMailEntityHolder {
	
	@SerializedName("OperatorMail")
	public OperatorMail operatorMail;
	
	public OperatorMailEntityHolder()
	{
		operatorMail=new OperatorMail();
	}

}
