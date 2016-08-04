package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorUnSubscribeRoot {
	@SerializedName("OperatorUnSubscribe")
	public OperatorUnSubscribe operatorUnSubscribe;
	public OperatorUnSubscribeRoot() {
		operatorUnSubscribe=new OperatorUnSubscribe();
	}
}
