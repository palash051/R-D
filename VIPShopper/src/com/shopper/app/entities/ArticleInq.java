package com.shopper.app.entities;

import android.content.Context;
import android.widget.TextView;

import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;
import com.shopper.app.utils.JSONfunctions;

/**
 * @author Shafiqul Alam use for loading article information these informations
 *         are fetched through JSon functions from server.
 */

public class ArticleInq {

	private PriceInquiery mPriceInquiery;
	public String EAN;
	public TextView mtextView;
	public Context mcontext;
	public static boolean IsProductFound = false;

	public ArticleInq() {
		mPriceInquiery = new PriceInquiery();
		EAN = "";
	}

	public ArticleInq(Context context, String ean) {		
		ArticleInquieryRoot articleInquieryRoot = (ArticleInquieryRoot) JSONfunctions
				.retrieveDataFromStream(String.format(
						CommonURL.getInstance().ProductDetailsURL,
						CommonTask.getShopNumber(context),
						CommonConstraints.ClientId, ean),
						ArticleInquieryRoot.class);	
		EAN = ean;
		CommonValues.getInstance().IsArticleDetailsRecordFound = false;

		if (articleInquieryRoot != null) {
			mPriceInquiery = articleInquieryRoot.articleInquiery.priceInquieryList.get(0);
			if (mPriceInquiery != null) {
				mPriceInquiery.quantity = 1;
				mPriceInquiery.EAN = EAN;
				IsProductFound = true;
			}else{
				IsProductFound = false;
			}
			CommonValues.getInstance().IsArticleDetailsRecordFound = true;

		}		
	}

	public PriceInquiery getPriceInquiery() {
		return mPriceInquiery;
	}

	public void addBasketAndImage(Context context) {
		mcontext = context;
		new AsyncAddBasketTask().execute(this);
	}	
}
