package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

import android.graphics.drawable.BitmapDrawable;

public class OrderLine {
	public int RowId;//for internal use...
	@SerializedName("id")
	public String Id;
	@SerializedName("text")
	public String Text;
	public String Text2;
	public String ArticleFullText;
	@SerializedName("antal")
	public int quantity;
	@SerializedName("price")
	public double Price;
	@SerializedName("total")
	public double TotalPrice;
	@SerializedName("group")
	public int Group;
	@SerializedName("grouptxt")
	public String GroupText;
	public String ContentsDesc;
	@SerializedName("discount")
	public LineDiscount Discount;
	public String DiscountText;
	public transient BitmapDrawable drawableImage;
	public DiscountGroup LineDiscountGroup;
	public boolean hasChanged = false;
	
	/**
	 * Change hasChanged=true when quantity update 
	 * @param quantity
	 */
	public void setQuantityAndHasChange(int quantity) {		
		hasChanged = true;
		this.quantity = quantity;
	}	
	
	public OrderLine() {
		RowId = -1;
		Id = "";
		Text = "";
		Text2 = "";
		ContentsDesc = "";
		DiscountText="";
		quantity = 0;
		Price = 0;
		TotalPrice = 0;
		Group = 0;
		GroupText = "";
		ArticleFullText="";
		Discount = new LineDiscount();		
		LineDiscountGroup=new DiscountGroup();
	}
}
