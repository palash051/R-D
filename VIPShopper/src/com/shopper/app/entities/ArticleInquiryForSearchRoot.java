package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class ArticleInquiryForSearchRoot {
	@SerializedName("ArticleInq")
	public ArticleInquiryForSearch articleInquiry;

	public ArticleInquiryForSearchRoot() {
		articleInquiry = new ArticleInquiryForSearch();
	}

}
