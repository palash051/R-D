package com.vipdashboard.app.fragments;

public class ResidualFileItem {
	public String fileName;
	public boolean willBeDelete;
	public String absolutePath;
	public String sizeFile;

	public String getAbsolutePath() {
		return absolutePath;
	}
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

	public ResidualFileItem() {
		// TODO Auto-generated constructor stub
	}

	public ResidualFileItem(String absPath, String fName, boolean b, String size) {
		fileName = fName;
		absolutePath = absPath;
		willBeDelete = b;
		sizeFile = size;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isWillBeDelete() {
		return willBeDelete;
	}

	public void setWillBeDelete(boolean willBeDelete) {
		this.willBeDelete = willBeDelete;
	}

	public String getSizeFile() {
		return sizeFile;
	}

	public void setSizeFile(String sizeFile) {
		this.sizeFile = sizeFile;
	}
}
