package com.shopper.app.entities;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.R;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury use for adding selected article info (that has
 *         been scanned) in basket Asynchronously
 * 
 */

public class AsyncAddBasketTask extends AsyncTask<ArticleInq, Void, String> {
	ArticleInq articleInq;
	CaptureActivity cap;

	@Override
	protected String doInBackground(ArticleInq... arcs) {
		articleInq = arcs[0];
		cap = (CaptureActivity) articleInq.mcontext;
		CaptureActivity.isBasketAddingReady = false;
		CommonValues.getInstance().IsServerConnectionError = false;
		return downloadImage(null);
	}

	@Override
	protected void onPostExecute(String result) {
		if (CommonValues.getInstance().IsServerConnectionError) {
			CommonTask.ShowMessage(cap,
					cap.getString(R.string.serverswitchError));
		} else {
			// determine the basket loading depending on menu press dynamically
//			Home.showBasketMenu();
		}
		try {
			CaptureActivity.basketRemovingStarted = true;
			while (!CaptureActivity.basketAddingStarted) {
				CaptureActivity.basketArticleList.remove(0);
				CaptureActivity.basketRemovingStarted = false;
				break;
			}

		} catch (Exception oEx) {

		}
		CaptureActivity.isBasketAddingReady = true;
	}

	// Download the particular image for the scanned article
	private String downloadImage(String url) {
		try {
			PriceInquiery pi = articleInq.getPriceInquiery();
			BitmapDrawable image = cap.getArticleImage(articleInq.EAN);
			if (pi != null) {
				if (image == null) {
					pi.drawableImage = CommonTask.getDrawableImage(String
							.format(CommonURL.getInstance().ProductImageURL,
									articleInq.EAN), articleInq.EAN);
				} else {
					pi.drawableImage = image;
				}
			}

			CommonTask.addBasketObject(cap, pi);
		} catch (Exception oEx) {
			CommonValues.getInstance().IsBasketUpdateInProgress = false;
		}
		return null;
	}
}
