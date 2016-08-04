package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class ArticleInquiryForSearch {
	@SerializedName("Articles")
	public ArticlesForSearch articles;

	public ArticleInquiryForSearch() {
		articles = new ArticlesForSearch();
	}
}
