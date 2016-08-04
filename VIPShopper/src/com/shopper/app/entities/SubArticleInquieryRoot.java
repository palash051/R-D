package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class SubArticleInquieryRoot {
	@SerializedName("ArticleInq")
	public SubArticleInquiery subArticleInquiery;

	public SubArticleInquieryRoot() {
		subArticleInquiery = new SubArticleInquiery();
	}
}
