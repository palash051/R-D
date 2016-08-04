package com.vipdashboard.app.asynchronoustask;



import com.vipdashboard.app.interfaces.IAsynchronousTask;

import android.os.AsyncTask;

public class DownloadableAsyncTask extends AsyncTask<Void, Void, Object>{
	IAsynchronousTask asynchronousTask;

	public DownloadableAsyncTask(IAsynchronousTask activity) {
		this.asynchronousTask = activity;
		
	}
	@Override
	protected void onPreExecute() {
		if(asynchronousTask!=null)
		asynchronousTask.showProgressLoader();
	}

	@Override
	protected Object doInBackground(Void... cap) {
		try{
			if(asynchronousTask!=null)
				return asynchronousTask.doBackgroundPorcess();
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object data) {	
		android.os.AsyncTask.Status status = getStatus();
		if (status != AsyncTask.Status.FINISHED && !isCancelled()) {
			if (asynchronousTask != null) {
				asynchronousTask.processDataAfterDownload(data);
				asynchronousTask.hideProgressLoader();

			}
		}/*
		if(asynchronousTask!=null){
			asynchronousTask.hideProgressLoader();
			asynchronousTask.processDataAfterDownload(data);
		}*/
	}
}
