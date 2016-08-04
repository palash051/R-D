package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class OrderReplyForCheckoutRoot {
	@SerializedName("OrderReply")
	public OrderReplyForCheckout orderReplyForCheckout;

	public OrderReplyForCheckoutRoot() {
		orderReplyForCheckout = new OrderReplyForCheckout();
	}
}
