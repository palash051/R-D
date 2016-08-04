package com.shopper.app.entities;

import com.shopper.app.activities.Search;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

import android.os.AsyncTask;

/**
 * @author Tanvir Ahmed Chowdhury
 * use for loading article information in search screen from server  Asynchronously
 * load familyitems in familyInq object
 * load searched article using SearchFamily class 
 */

public class AsyncSearchLoading extends AsyncTask<Void, Void, Boolean> {
	Search search;

	public AsyncSearchLoading(Search src) {
		search = src;
	}

	@Override
	protected void onPreExecute() {
		if (search.isFamilyNotLoaded()) {
			//starts the progress bar
			search.startProgress();
		}
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		if (search.isFamilyNotLoaded()) {
			CommonValues.getInstance().IsServerConnectionError = false;
			//load familyitems in familyInq object
			search.loadListView();
		}
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (search.isFamilyNotLoaded()) {
			search.stopProgress();

			if (CommonValues.getInstance().ErrorCode!=CommonConstraints.NO_EXCEPTION) {
				CommonTask.ShowMessage(search,CommonTask.getCustomExceptionMessage(search, CommonValues.getInstance().ErrorCode));
			} else {
				//load searched article using SearchFamily class 
				search.viewListView(search);

			}
		}
	}
}
