package com.mobilink.app.interfaces;

public interface IAsynchronousTask {
	void showProgressLoader();

	void hideProgressLoader();
	
	Object doBackgroundPorcess();

	void processDataAfterDownload(Object data);

}
