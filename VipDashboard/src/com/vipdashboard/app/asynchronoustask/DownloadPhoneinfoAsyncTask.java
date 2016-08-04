package com.vipdashboard.app.asynchronoustask;

import android.os.AsyncTask;

import com.vipdashboard.app.interfaces.IDownloadPhoneinfoAsyncTask;

public class DownloadPhoneinfoAsyncTask extends AsyncTask<Void, Void, Object>{
	IDownloadPhoneinfoAsyncTask asynchronousTask;

	public DownloadPhoneinfoAsyncTask(IDownloadPhoneinfoAsyncTask activity) {
		this.asynchronousTask = activity;
		
	}
	@Override
	protected void onPreExecute() {		
	}

	@Override
	protected Object doInBackground(Void... cap) {
		try{
			if(asynchronousTask!=null)
				return asynchronousTask.doBackgroundTask();
		}catch (Exception e) {
			String dss="";
			dss=e.getMessage();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object data) {	
		if(asynchronousTask!=null)
			asynchronousTask.processPostDataDownload(data);
	}

}
