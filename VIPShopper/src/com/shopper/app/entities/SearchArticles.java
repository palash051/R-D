package com.shopper.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class SearchArticles {
	
	@SerializedName("Article")
	public List<ArticleForSearch> articleList;
	public String next;
	public SearchArticles() {
		articleList = new ArrayList<ArticleForSearch>();
		next="";
	}

}
