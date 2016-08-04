package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.activities.LatestUpdateActivity;

import android.os.AsyncTask;

public class LatestUpdateTask extends AsyncTask<Void, Void, Boolean>{

	LatestUpdateActivity latestupdateactivity;
	public LatestUpdateTask(LatestUpdateActivity _latestupdateactivity)
	{
		latestupdateactivity=_latestupdateactivity;
	}
	
	@Override
	protected void onPreExecute() {
		latestupdateactivity.showLoader();
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		return latestupdateactivity.GetDate();

	}

	@Override
	protected void onPostExecute(Boolean result) {
		latestupdateactivity.hideLoader();
		if(result)
		{
			latestupdateactivity.showData();
		}
	}
}
