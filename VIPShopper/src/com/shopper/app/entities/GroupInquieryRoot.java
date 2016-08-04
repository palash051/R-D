package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class GroupInquieryRoot {

	@SerializedName("ArticleInq")
	public GroupInquiery groupInquiery;

	public GroupInquieryRoot() {
		groupInquiery = new GroupInquiery();
	}

}
