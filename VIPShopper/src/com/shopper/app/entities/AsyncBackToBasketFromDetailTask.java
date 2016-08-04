package com.shopper.app.entities;

import com.shopper.app.R;
import com.shopper.app.activities.Basket;
import com.shopper.app.activities.Home;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

import android.os.AsyncTask;

/**
 * @author Tanvir Ahmed Chowdhury use for return back to basket from article
 *         details Asynchronously determine the basket loading depending on menu
 *         press dynamically refresh the basket list incase of empty selection
 */

public class AsyncBackToBasketFromDetailTask extends
		AsyncTask<Void, Void, Boolean> {

	Basket basket;

	public AsyncBackToBasketFromDetailTask(Basket bsk) {
		basket = bsk;
	}

	
	@Override
	protected Boolean doInBackground(Void... arg0) {
		basket.isFinishedItemAddingToBasket = false;

		// If Is changed then>

		CommonTask.addBasketFromDetail(basket, Basket.orderLine,
				Basket.orderLine.quantity);

		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {

		if (CommonValues.getInstance().IsServerConnectionError) {
			CommonTask.ShowMessage(basket.getParent(),basket.getString(R.string.servercontactfaillistnotfound));
//					"Server kontakt fejl\nlist ikke fundet");
		} else {
			// determine the basket loading depending on menu press
			// dynamically
			Home.showBasketMenu();
			// basket.updateBasketQuantity();

			// Update the list adapter after dataselection changed
			Basket.listAdaptor.updateList();
			Basket.displayTotalPrice();
			// refresh the basket list incase of empty selection

			basket.enableDisableCheckoutButton();
			// Delete any teporary views left in item details
			basket.setBasketQtyToOther();
			CommonBasketValues.getInstance().Basket.resetHasChanged();
			CommonBasketValues.getInstance().updateBasket();
			basket.isFinishedItemAddingToBasket = true;
			// basket.orderLine = new OrderLine();
			Basket.basketLoadedFromEan = true;
			//basket.refreshBasketList();
			basket.deleteAllTemporaryViews();
		}
		
		basket.stopProgress();
	}

	@Override
	protected void onPreExecute() {
		Basket.basketLoadedFromEan = false;
		basket.startProgress();
	}

}
