package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class SubArticleInquiery {
	@SerializedName("Family")
	public SubArticleFamily subArticleFamily;

	public SubArticleInquiery() {
		subArticleFamily = new SubArticleFamily();
	}
}
