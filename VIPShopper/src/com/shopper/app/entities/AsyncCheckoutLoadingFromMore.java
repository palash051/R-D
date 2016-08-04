package com.shopper.app.entities;

import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;

import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.R;
import com.shopper.app.activities.Basket;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;
import com.shopper.app.utils.JSONfunctions;

/**
 * @author 
 * use for loading checkout screen from More/User tab Asynchronously
 * json fuction to load the image of the product from barcode
 */

public class AsyncCheckoutLoadingFromMore extends AsyncTask<Void, Void, Boolean> {
	Basket checkout;
	String barCode;
	BitmapDrawable image;

	public AsyncCheckoutLoadingFromMore(Basket src) {
		checkout = src;
		barCode = "";
		image = null;
	}
	
	

	
	@Override
	protected void onPostExecute(Boolean result) {
		checkout.cancelBasket.setEnabled(true);
		if (CommonValues.getInstance().ErrorCode!=CommonConstraints.NO_EXCEPTION) {
			CommonTask.ShowMessage(checkout,CommonTask.getCustomExceptionMessage(checkout, CommonValues.getInstance().ErrorCode));
		} else {
			if (barCode != "" && image != null) {
				checkout.checkoutBarcode.setText(barCode);
				checkout.checkoutBarcodeImage.setBackgroundDrawable(image
						.getConstantState().newDrawable());
				CommonTask.setAlphaVisible(checkout.checkoutBarcodeImage, true);

			} else {
				checkout.checkoutBarcode.setText("");
				checkout.checkoutBarcodeImage.setBackgroundDrawable(null);
				CommonTask.ShowMessage(checkout, checkout.getString(R.string.servercontactfaillistnotfound));
//						"Server kontakt fejl\nlist ikke fundet");
			}

		}
		checkout.stopProgress();
	}

	@Override
	protected void onPreExecute() {
		checkout.cancelBasket.setEnabled(false);
		checkout.startProgress();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		CommonValues.getInstance().IsServerConnectionError = false;
		try {
			//json fuction to load the image of the product from barcode
			OrderReplyForCheckoutRoot orderReplyForCheckoutRoot= (OrderReplyForCheckoutRoot) JSONfunctions.retrieveDataFromStream( String.format(
					CommonURL.getInstance().CheckoutURL, 2, CommonTask.getShopNumber(checkout),
					CommonBasketValues.getInstance().Basket.OrderNo),OrderReplyForCheckoutRoot.class);
			
			
			
			if (orderReplyForCheckoutRoot != null&&orderReplyForCheckoutRoot.orderReplyForCheckout!=null) {
				barCode = orderReplyForCheckoutRoot.orderReplyForCheckout.Barcode;
				if (barCode != "") {
					image = CommonTask.getDrawableImage(
							String.format(CommonURL.getInstance().BarcodeURL, barCode),
							barCode);

				}
			} else {
				barCode = "";
			}
			while (!CaptureActivity.isBasketAddingFinished()
					|| !Basket.basketLoadedFromEan) {
				// Waiting for loading basket...
			}

		} catch (Exception e) {
			barCode = "";
			image = null;

		}

		return null;
	}
}
