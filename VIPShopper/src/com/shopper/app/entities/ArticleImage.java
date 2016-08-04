package com.shopper.app.entities;

import android.graphics.drawable.BitmapDrawable;

public class ArticleImage {
	public String eanNo;
	public BitmapDrawable image;
	
	public ArticleImage(String ean,BitmapDrawable pic)
	{
		eanNo = ean;
		image = pic;
	}

}
