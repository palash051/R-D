package com.shopper.app.entities;

import com.shopper.app.activities.Search;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

import android.os.AsyncTask;

/**
 * @author use for loading group inquiry list Asynchronously Prepare
 *         groupInquieryList Adaptor Fill Group Inquiry object
 */

public class AsyncGroupInquieryLoading extends AsyncTask<Void, Void, Boolean> {
	Search search;
	public AsyncGroupInquieryLoading(Search src) {
		search = src;
	}

	@Override
	protected void onPreExecute() {
		search.startProgress();

	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		// Fill Group Inquiry object
		search.loadGroupListView();
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		search.stopProgress();
		if (CommonValues.getInstance().ErrorCode==CommonConstraints.NO_EXCEPTION) {
			// Prepare groupInquieryList Adaptor
			search.viewGroupListView(search);
		} else {
			CommonTask.ShowMessage(search,CommonTask.getCustomExceptionMessage(search, CommonValues.getInstance().ErrorCode));
		}

	}
}
