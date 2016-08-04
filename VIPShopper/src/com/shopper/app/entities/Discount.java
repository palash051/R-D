package com.shopper.app.entities;

public class Discount {
	public int quantity;
	public String text;
	public double amount;
	public double contents;
	public String contentsdesc;
	public double priceper;
	public String priceperdesc;

	public Discount() {
		quantity = 0;
		text = "";
		amount = 0;
		contents = 0;
		contentsdesc = "";
		priceper = 0;
		priceperdesc = "";
	}
}
