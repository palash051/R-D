package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class OrderReplyRoot {
	@SerializedName("OrderReply")
	public OrderReply orderReply;
	public OrderReplyRoot() {
		orderReply=new OrderReply();
	}

}
