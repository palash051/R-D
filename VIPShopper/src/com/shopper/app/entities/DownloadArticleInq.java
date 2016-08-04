package com.shopper.app.entities;

import android.os.AsyncTask;
import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.R;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * @author 
 * use for loading article details from Scanner Asynchronously
 */

public class DownloadArticleInq  extends AsyncTask<CaptureActivity, Void, ArticleInq> {
	CaptureActivity ca;


@Override
protected ArticleInq doInBackground(CaptureActivity... caps) {  
	ca = caps[0];
	CommonValues.getInstance().IsServerConnectionError = false;
    return downloadArticle();
}

@Override
protected void onPostExecute(ArticleInq result) {  
	if(!CommonValues.getInstance().IsServerConnectionError)
	{
		if(result!=null)
		{
			try
			{
				CaptureActivity.articleInqList.add(result);				
				CaptureActivity.basketAddingStarted = true;
				while(!CaptureActivity.basketRemovingStarted)
				{
					CaptureActivity.basketArticleList.add(result);	
					CaptureActivity.basketAddingStarted = false;
					break;
				}
				CaptureActivity.displayAddingStarted = true;
				while(!CaptureActivity.displayRemovingStarted)
				{					
					CaptureActivity.displayArticleList.add(result);
					CaptureActivity.displayAddingStarted = false;
					break;
				}
			}catch(Exception oEx)
			{}
			
		}
	}
	else
	{
		CommonTask.ShowMessage(ca, ca.getString(R.string.serverswitchError));
	}	
	try
	{
		
		CaptureActivity.scanEanRemovingStarted = true;
		while(!CaptureActivity.scanEanAddingStarted )
		{
			CaptureActivity.sacannedItemListForArticle.remove(0);
			CaptureActivity.scanEanRemovingStarted = false;
			break;
		}
	}catch(Exception oEx)
	{}
	ca.currentPoolEanForArticle = "";
}



private ArticleInq downloadArticle() {
	try
	{
		ArticleInq art = ca.getArticle(ca.currentPoolEanForArticle);	
		if(art==null)
		{
			return new ArticleInq(ca,ca.currentPoolEanForArticle);
		}
		else
		{
			CaptureActivity.basketArticleList.add(art);
			CaptureActivity.displayArticleList.add(art);
			
		}
	}catch(Exception oEx)
	{		
	}
	return null;
}

}
