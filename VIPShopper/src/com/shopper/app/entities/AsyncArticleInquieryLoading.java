package com.shopper.app.entities;

import com.shopper.app.activities.Search;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

import android.os.AsyncTask;

public class AsyncArticleInquieryLoading extends AsyncTask<Void, Void, Boolean> {
	Search search;

	public AsyncArticleInquieryLoading(Search src) {
		search = src;
	}

	@Override
	protected void onPreExecute() {
		search.startProgress();

	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		// Fill Article Inquiry object
		search.loadArticleListView();
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		search.stopProgress();

		if (CommonValues.getInstance().ErrorCode==CommonConstraints.NO_EXCEPTION) {
			// Prepare articleList Adaptor
			search.viewArticleListView(search);

		} else {
			CommonTask.ShowMessage(search,CommonTask.getCustomExceptionMessage(search, CommonValues.getInstance().ErrorCode));
		}

	}
}
