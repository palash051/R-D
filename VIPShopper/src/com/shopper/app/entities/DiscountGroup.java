package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class DiscountGroup {
	@SerializedName("id")
	public String id;
	@SerializedName("text")
	public String text;

	public DiscountGroup() {
		id = "";
		text = "";
	}

}
