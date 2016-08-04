package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class LineDiscount {
	@SerializedName("quantity")
	public int Quantity;
	@SerializedName("text")
	public String Text;
	@SerializedName("amount")
	public double Amount;
	@SerializedName("contents")
	public String Contents;
	@SerializedName("contentsdesc")
	public String Contentsdesc;
	@SerializedName("priceper")
	public double Priceper;
	@SerializedName("priceperdesc")
	public String Priceperdesc;

	public LineDiscount() {
		Quantity = 0;
		Text = "";
		Amount = 0;
		Contents = "";
		Contentsdesc = "";
		Priceper = 0;
		Priceperdesc = "";
	}

}
