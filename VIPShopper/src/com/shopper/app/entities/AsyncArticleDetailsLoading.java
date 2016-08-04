package com.shopper.app.entities;

import android.content.Context;
import android.os.AsyncTask;

import com.shopper.app.asynctasks.AsyncTaskInterface;
import com.shopper.app.utils.CommonValues;

/**
 * @author 
 * use for loading article details Asynchronously
 * 
 */
public class AsyncArticleDetailsLoading extends AsyncTask<Void, Void, ArticleInq> {
	//Search search;
	private AsyncTaskInterface listener;
	private String articleId;
	Context context;
	
	public AsyncArticleDetailsLoading(AsyncTaskInterface src, String articleId, Context context)
	{	
		listener = src;
		this.articleId = articleId;
		this.context = context;
//		search.isSearchDetailsReady = false;
	}
	@Override
	protected void onPreExecute() {	
		//starts teh progress bar
//		search.startProgress();
		listener.onTaskPreExecute();
		
	}	
	@Override
	protected ArticleInq doInBackground(Void... arcs) {		
		CommonValues.getInstance().IsServerConnectionError = false;
		//load the Article Inquiry details by articleId
//		search.loadArticleDetailsListView();			
		return new ArticleInq(context, articleId);
	}
	@Override
	protected void onPostExecute(ArticleInq result) {	
//		search.stopProgress();		
//		if(CommonValues.getInstance().IsArticleDetailsRecordFound)
//		{			
//			if(CommonValues.getInstance().IsServerConnectionError)
//			{
//				CommonTask.ShowMessage(search, search.getString(R.string.serverswitchError));		
//			}
//			else
//			{
//				//view the Article Inquiry details from articleinqobject
//				search.viewArticleDetailsListView(search);
//				
//			}
//		}
//		search.isSearchDetailsReady = true;
		listener.onTaskPostExecute(result);
	}	
}
