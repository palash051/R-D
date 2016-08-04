package com.shopper.app.entities;

import com.shopper.app.activities.Basket;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;

import com.shopper.app.R;
import android.os.AsyncTask;

/**
 * @author 
 * use for loading articles in basket Asynchronously
 * determine the basket loading depending on menu press dynamically
 * display alert message if unknown product or order number is given
 * download the realted article/product info that has been searched for
 */

public class BasketLoadingTask extends AsyncTask<Void, Void, ArticleInq> {
	Basket basket;

	public BasketLoadingTask(Basket bask) {
		basket = bask;
	}

	@Override
	protected void onPreExecute() {
		Basket.basketLoadedFromEan = false;
		basket.startProgress();

	}

	@Override
	protected ArticleInq doInBackground(Void... bas) {
		CommonValues.getInstance().IsServerConnectionError = false;
		return downloadProduct();
	}

	
	@Override
	protected void onPostExecute(ArticleInq result) {

		if (!CommonValues.getInstance().IsServerConnectionError) {
			if (!basket.ean.equals("")) {
				if (result == null) {
					basket.initialize(basket);
					basket.enableDisableCheckoutButton();
					//determine the basket loading depending on menu press dynamically
//					Home.showBasketMenu();
				} else if (ArticleInq.IsProductFound) {
					basket.initialize(basket);
					basket.enableDisableCheckoutButton();
//					Home.showBasketMenu();
				} else if (!ArticleInq.IsProductFound) {

					if (!basket.eanAddingMode.equals("ean")) {
						CommonTask.ShowMessage(basket, basket.getString(R.string.ordernumError)
								+ basket.ean);
					}
					if (!basket.eanAddingMode.equals("basket")) {
						CommonTask.ShowMessage(basket, basket.getString(R.string.productError)
								+ basket.ean);
					}
				}

			}
		}

		else {
			CommonTask.ShowMessage(basket,basket.getString(R.string.serverswitchError));

		}
		basketFinalTask();

	}

	private void basketFinalTask() {
		Basket.basketLoadedFromEan = true;
		basket.stopProgress();
	}

	@SuppressWarnings("static-access")
	//download the realted article/product info that has been searched for
	private ArticleInq downloadProduct() {
		if (!basket.ean.equals("")) {
			if (basket.eanAddingMode.equals("ean")) {

				ArticleInq arc = new ArticleInq(basket, basket.ean);
				if (arc.IsProductFound) {
					arc.getPriceInquiery().drawableImage = CommonTask
							.getDrawableImage(String.format(
									CommonURL.getInstance().ProductImageURL, basket.ean),
									basket.ean);
					CommonTask.addBasketObject(basket, arc.getPriceInquiery());
				}
				return arc;

			} else if (basket.eanAddingMode.equals("basket")
					&& basket.ean.length() < 10) {

				CommonTask.initCommonValuesForBasket();
				CommonBasketValues.getInstance().Basket.OrderNo = Integer
						.parseInt(basket.ean);
				CommonTask.fillBasketFromOrderNo(basket);
				CommonBasketValues.getInstance().updateBasket();
				return null;

			}
		}

		return new ArticleInq();
	}

}
