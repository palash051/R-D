package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class SearchArticleInquiry {
	@SerializedName("Search")
	public SearchArticles searchArticles;

	public SearchArticleInquiry() {
		searchArticles = new SearchArticles();
	}

}
