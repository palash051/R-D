package com.shopper.app.entities;

import android.os.AsyncTask;

import com.shopper.app.R;
import com.shopper.app.activities.More;
import com.shopper.app.base.MainActionbarBase;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury use for saving users information from basket
 *         tab Asynchronously
 */

public class AsyncSaveUserInformation extends AsyncTask<Void, Void, Boolean> {
	More more;

	public AsyncSaveUserInformation(More src) {
		more = src;
	}

	@Override
	protected void onPreExecute() {
		// starts teh progress bar
		more.startProgress();
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		return doSearch();
		

	}

	@Override
	protected void onPostExecute(Boolean result) {
		more.stopProgress();
		if (!CommonValues.getInstance().IsServerConnectionError) {
			if (!CommonValues.getInstance().IsCallFromBasket) {
				MainActionbarBase.stackIndex.removeAllElements();
				MainActionbarBase.currentMenuIndex =MainActionbarBase.INITAIL_STATE;
			} else {
				MainActionbarBase.currentMenuIndex =MainActionbarBase.BASKET_ACTIVITY;
				More.flipper.setDisplayedChild(4);
			}
			more.manageActivity();

		} else {
			CommonTask.ShowMessage(more,
					more.getString(R.string.serverswitchError));
		}
	}

	private boolean doSearch() {
		// saving new user Information
		more.saveNewUser();
		return true;
	}
}
