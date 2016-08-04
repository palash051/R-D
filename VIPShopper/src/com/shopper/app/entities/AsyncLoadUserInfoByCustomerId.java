package com.shopper.app.entities;

import android.os.AsyncTask;

import com.shopper.app.R;
import com.shopper.app.activities.More;
import com.shopper.app.base.LogInFunctionsBase;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;
/**@author 
 * use for checking Log in screen user information 
 * and also load that information Asynchronously.
 * show log in confirmation message
 */

public class AsyncLoadUserInfoByCustomerId extends
		AsyncTask<Void, Void, Boolean> {
	private static final int INITIAL_STATE = -1;
//	Basket onlineCheckout;
	More more;
	

	public AsyncLoadUserInfoByCustomerId(More m) {
		more = m;
	}

	@Override
	protected void onPreExecute() {
		//starts the progrss bar
		more.startProgress();
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
//		if (more == null)
//			//Load the user information from online checkout
//			onlineCheckout.loadUserInformation(onlineCheckout);
//		else
			//Load the user information from user tab
			more.loadUserInformation(more);
		return false;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		more.stopProgress();
			if (!CommonValues.getInstance().IsServerConnectionError) {
				//log in confirmation message
				more.ShowLogInCnfMsg();
			} else {
				More.backStateMore=INITIAL_STATE;
				CommonTask.ShowMessage(more,
						more.getString(R.string.serverswitchError));
			}

//		}

	}
}
