package com.shopper.app.entities;
import com.shopper.app.R;
import com.shopper.app.activities.Basket;
import com.shopper.app.activities.Search;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;
import android.os.AsyncTask;

/**
 * @author 
 * use for updating basket values from searched article details Asynchronously
 * add the article detail to basket  and update it accordingly
 */

public class AsyncUpdateBasketFromArticleDetailTask extends AsyncTask<Void, Void, Boolean>{
	
	Search search;
	public AsyncUpdateBasketFromArticleDetailTask(Search bsk) {
		search = bsk;
	}

	
	@Override
	protected Boolean doInBackground(Void... arg0) {
		//add the article detail to basket  and update it accordingly
		CommonTask.addBasketFromDetail(search, Search.orderLine,
				search.selectedItemQty);
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		
		
		if (CommonValues.getInstance().IsServerConnectionError) {
			CommonTask.ShowMessage(search, search.getString(R.string.servercontactfaillistnotfound));
//					"Server kontakt fejl\nlist ikke fundet");
		} else {
			//determine the basket loading depending on menu press dynamically
//			Home.showBasketMenu();			
		}
		search.updateOrderline();
//		search.stopProgress();
		Basket.basketLoadedFromEan = true;
		if(Basket.listAdaptor!=null){
			Basket.listAdaptor.updateList();			
			//Change basket title with total amount and item quantity of basket
			Basket.displayTotalPrice();
		}
		
	}

	@Override
	protected void onPreExecute() {
		Basket.basketLoadedFromEan = false;
//		search.startProgress();
	}
	
	

}

