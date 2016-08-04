package com.vipdashboard.app.interfaces;

import android.view.View;

public interface IAsynchronousTask {
	void showProgressLoader();

	void hideProgressLoader();
	
	Object doBackgroundPorcess();

	void processDataAfterDownload(Object data);	

}
