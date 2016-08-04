package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class FamilyInquieryRoot {
	@SerializedName("ArticleInq")
	public FamilyInquiery familyInquiery;
	public FamilyInquieryRoot() {
		familyInquiery=new FamilyInquiery();
	}

}
