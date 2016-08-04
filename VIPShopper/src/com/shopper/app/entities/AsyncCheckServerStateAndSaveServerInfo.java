package com.shopper.app.entities;

import android.os.AsyncTask;

import com.shopper.app.R;
import com.shopper.app.activities.Home;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury use for saving users information from home tab
 *         Asynchronously
 */

public class AsyncCheckServerStateAndSaveServerInfo extends AsyncTask<Void, Void, Boolean> {

	Home home;

	public AsyncCheckServerStateAndSaveServerInfo(Home src) {
		home = src;
	}

	@Override
	protected void onPreExecute() {
		try {
			// progress bar starts with the method call
			home.startmenuProgress();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		return doSearch();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		home.stopmenuProgress();
		if (!CommonValues.getInstance().IsServerConnectionError) {
			// after saving userinformation,system will prompt restart with a
			// confirmation dialogue
			home.setUserInfoAfterSave();
		} else {
			CommonTask.ShowMessage(home,
					home.getString(R.string.serverswitchError));
		}

	}

	private boolean doSearch() {
		// save settings screen information in shared preference
		if (home.isConnectedToServer()) {
			home.saveServerUrlandShopNumber();
		}else{
			CommonValues.getInstance().IsServerConnectionError=true;
		}
			
		return true;
	}

}
