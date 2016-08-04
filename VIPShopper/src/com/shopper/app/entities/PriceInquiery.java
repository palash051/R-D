package com.shopper.app.entities;


import com.google.gson.annotations.SerializedName;

import android.graphics.drawable.BitmapDrawable;

public class PriceInquiery {
	public String id;
	public String text;
	public String text2;
	public double price;
	public int group;
	public String grouptxt;
	public double contents;
	public String contentsdesc;
	public double priceper;
	public String priceperdesc;
	public int quantity;
	public String EAN;
	@SerializedName("discount")
	private Discount mDiscount;
	@SerializedName("discountgroup")
	private DiscountGroup mDiscountGroup;
    public transient BitmapDrawable drawableImage;
	
	public PriceInquiery() {
		mDiscount = new Discount();
		mDiscountGroup = new DiscountGroup();
		id = "";
		text = "";
		text2 = "";
		price = 0;
		group = 0;
		grouptxt = "";
		contents = 0;
		contentsdesc = "";
		priceper = 0;
		priceperdesc = "";
		quantity = 0;
		EAN = "";		
	}

	public Discount getDiscount() {
		if(mDiscount==null)
			mDiscount=new Discount();
		return mDiscount;
	}

	public DiscountGroup getDiscountGroup() {
		if(mDiscountGroup==null)
			mDiscountGroup=new DiscountGroup();
		return mDiscountGroup;
	}
}
