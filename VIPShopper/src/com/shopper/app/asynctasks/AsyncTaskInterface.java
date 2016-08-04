package com.shopper.app.asynctasks;

public interface AsyncTaskInterface {
	void onTaskPreExecute();
	void onDoInBackground();
	void onTaskPostExecute(Object result);	
}
