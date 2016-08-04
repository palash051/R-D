package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class SearchArticleInquiryRoot {
	@SerializedName("ArticleInq")
	public SearchArticleInquiry searchArticleInquiry;

	public SearchArticleInquiryRoot() {
		searchArticleInquiry = new SearchArticleInquiry();
	}
}
