package com.vipdashboard.app.fragments;

import android.graphics.drawable.Drawable;

public class InstalledAppItem {
	private Drawable imgApp;
	private String nameApp;
	private String cache;
	private String pkgName;

	public InstalledAppItem() {
		super();
	}

	public InstalledAppItem(String pkgname, Drawable imgApp, String nameApp, String cache) {
		super();
		this.pkgName= pkgname;
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

	public String getCache() {
		return cache;
	}

	public void setCache(String cache) {
		this.cache = cache;
	}

	public String getPkgName() {
		return pkgName;
	}

	public void setPkgName(String pkgName) {
		this.pkgName = pkgName;
	}

}
