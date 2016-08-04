package com.vipdashboard.app.interfaces;

public interface IDownloadPhoneinfoAsyncTask {
	Object doBackgroundTask();

	void processPostDataDownload(Object data);
}
