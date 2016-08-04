package com.mobilink.app.asynchronoustask;




import com.mobilink.app.interfaces.IAsynchronousTask;

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
			
		}
		return null;
	}

	@Override
	protected void onPostExecute(Object data) {	
		if(asynchronousTask!=null){
			asynchronousTask.hideProgressLoader();
			asynchronousTask.processDataAfterDownload(data);
		}
	}
}
