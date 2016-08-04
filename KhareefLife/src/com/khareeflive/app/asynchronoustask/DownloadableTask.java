package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.entities.IDownloadProcessorActicity;

import android.os.AsyncTask;

public class DownloadableTask extends AsyncTask<Void, Void, Object> {
	IDownloadProcessorActicity processDownloadAcivity;

	public DownloadableTask(IDownloadProcessorActicity activity) {
		this.processDownloadAcivity = activity;
	}

	@Override
	protected void onPreExecute() {
		processDownloadAcivity.showProgressLoader();
	}

	@Override
	protected Object doInBackground(Void... cap) {
		return processDownloadAcivity.doBackgroundDownloadPorcess();
	}

	@Override
	protected void onPostExecute(Object data) {
		processDownloadAcivity.hideProgressLoader();
		processDownloadAcivity.processDownloadedData(data);
	}
}
