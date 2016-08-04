package com.vipdashboard.app.fragments;

import android.graphics.drawable.Drawable;

public class HistoryItem {
	private Drawable imgApp;
	private String nameApp;
	private float cache;

	public HistoryItem() {
		super();
	}

	public HistoryItem(Drawable imgApp, String nameApp, float cache) {
		super();
		this.imgApp = imgApp;
		this.nameApp = nameApp;
		this.cache = cache;
	}

	public Drawable getImgApp() {
		return imgApp;
	}

	public void setImgApp(Drawable imgApp) {
		this.imgApp = imgApp;
	}

	public String getNameApp() {
		return nameApp;
	}

	public void setNameApp(String nameApp) {
		this.nameApp = nameApp;
	}

	public float getCache() {
		return cache;
	}

	public void setCache(float cache) {
		this.cache = cache;
	}

}
