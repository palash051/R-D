package com.shopper.app.entities;

import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.asynctasks.AsyncTaskAddBasketFromSearchInterface;
import com.shopper.app.utils.CommonValues;

import android.os.AsyncTask;

/**
 * @author Tanvir Ahmed Chowdhury use for adding articles into basket from
 *         search
 * 
 */

public class AsyncAddBasketFromSearch extends AsyncTask<Void, Void, Boolean> {
//	Search search = null;
//	CatalogueBase catBase = null;
	AsyncTaskAddBasketFromSearchInterface mListener;
	public AsyncAddBasketFromSearch(AsyncTaskAddBasketFromSearchInterface listener) {
//		catBase = cat;
		mListener  = listener;
	}

//	public AsyncAddBasketFromSearch(Search src) {
//		search = src;
//	}

	@Override
	protected void onPreExecute() {
//		if (search != null) {
//			// starts the progress bar for search
//			search.startProgressForSearch();
//		} else if (catBase != null) {
//			// starts the progress bar for catalogue
//			catBase.startProgress();
//		}
		mListener.onTaskPreExecute();
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		return add();
	}

	@Override
	protected void onPostExecute(Boolean result) {
		mListener.onTaskPostExecute(result);
//		if (search != null) {
//			search.stopProgressForSearch();
//		} else if (catBase != null) {
//			catBase.stopProgress();
//		}
//		
////		if (!CommonValues.getInstance().IsServerConnectionError) {
//		if (CommonValues.getInstance().ErrorCode==CommonConstraints.NO_EXCEPTION) {
//			if (search != null) {
//				// after selecting basket item update the basket screen from
//				// search
//				search.finishAddingTask();
//			} else if (catBase != null) {
//				// after selecting basket item update the basket screen from
//				// catalogue
//				catBase.finishAddingTask();
//			}
//
//		} else {
//			Context con = search != null ? search : catBase;
//			CommonTask.ShowMessage(con,
//					CommonTask.getCustomExceptionMessage(con, CommonValues.getInstance().ErrorCode));
//		}
	}

	private boolean add() {
		while (CaptureActivity.isBasketAddingFinished()) {
			mListener.startAddTask();
//			if (search != null) {
//				// Add article to the order line dynamically from search
//				search.startAddingTask();
//			} else if (catBase != null) {
//				// Add article to the order line dynamically from catalogue
//				catBase.startAddingTask();
//			}
			break;
		}
		return true;
	}
}
