package com.shopper.app.entities;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SubArticleFamily {
	@SerializedName("Article")
	public List<ArticleForSearch> articleList;

}
