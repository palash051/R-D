package com.khareeflive.app.asynchronoustask;

import com.khareeflive.app.activities.SiteDownActivity;

import android.os.AsyncTask;

public class SiteDownTask extends AsyncTask<Void, Void, Boolean>{

	SiteDownActivity sitedownactivity;
	public SiteDownTask (SiteDownActivity _sitedownactivity)
	{
		sitedownactivity=_sitedownactivity;
	}
	@Override
	protected void onPreExecute() {
		
	}
	
	@Override
	protected Boolean doInBackground(Void... params) {
		return sitedownactivity.GetDate();

	}

	@Override
	protected void onPostExecute(Boolean result) {
		if(result)
		{
			sitedownactivity.showData();
		}
	}

	
}
