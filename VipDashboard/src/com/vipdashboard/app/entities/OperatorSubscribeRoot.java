package com.vipdashboard.app.entities;

import com.google.gson.annotations.SerializedName;

public class OperatorSubscribeRoot {
	@SerializedName("OperatorSubscribe")
	public OperatorSubscribe operatorSubscribe;
	public OperatorSubscribeRoot() {
		operatorSubscribe=new OperatorSubscribe();
	}
}
