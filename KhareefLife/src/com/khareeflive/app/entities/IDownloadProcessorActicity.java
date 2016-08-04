package com.khareeflive.app.entities;

public interface IDownloadProcessorActicity {
	void showProgressLoader();

	void hideProgressLoader();

	Object doBackgroundDownloadPorcess();

	void processDownloadedData(Object data);
}
