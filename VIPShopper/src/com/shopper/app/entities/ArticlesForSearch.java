package com.shopper.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ArticlesForSearch {
	@SerializedName("Article")
	public List<ArticleForSearch> articleList;

	public ArticlesForSearch() {
		articleList = new ArrayList<ArticleForSearch>();

	}

}
