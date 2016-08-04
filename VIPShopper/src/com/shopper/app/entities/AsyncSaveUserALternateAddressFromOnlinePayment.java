package com.shopper.app.entities;
import android.os.AsyncTask;

import com.shopper.app.R;
import com.shopper.app.activities.Basket;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury
 * use for saving users alternate address  Asynchronously
 * set the user information after payment
 * 
 */

public class AsyncSaveUserALternateAddressFromOnlinePayment extends AsyncTask<Void, Void, Boolean> {
	Basket bsk;

	public AsyncSaveUserALternateAddressFromOnlinePayment(Basket src) {
		bsk = src;
	}

	@Override
	protected void onPreExecute() {
		//starts the p[rogress bar
		bsk.startProgress();
	}

	@Override
	protected Boolean doInBackground(Void... arcs) {
		CommonValues.getInstance().IsServerConnectionError = false;
		//saving users alternate address
		bsk.saveAlternetAddress();
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		bsk.stopProgress();
		if (!CommonValues.getInstance().IsServerConnectionError) {
			//set the user information after payment
			bsk.setDataAfterDeliveryPayment();
		} else {
			CommonTask.ShowMessage(bsk.getParent(),
					bsk.getString(R.string.serverswitchError));
		}
	}
	
}
