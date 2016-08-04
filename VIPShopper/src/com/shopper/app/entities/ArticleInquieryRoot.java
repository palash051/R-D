package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class ArticleInquieryRoot {
	@SerializedName("ArticleInq")
	public ArticleInquiery articleInquiery;
	public ArticleInquieryRoot() {
		articleInquiery=new ArticleInquiery();
	}

}
