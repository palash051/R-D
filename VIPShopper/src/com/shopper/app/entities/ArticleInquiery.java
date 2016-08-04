package com.shopper.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class ArticleInquiery {
	@SerializedName("PriceInquiery")
	public ArrayList<PriceInquiery> priceInquieryList;
	public ArticleInquiery() {
		priceInquieryList=new ArrayList<PriceInquiery>();
	}

}
