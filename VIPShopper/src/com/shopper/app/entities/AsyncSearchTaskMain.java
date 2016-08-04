package com.shopper.app.entities;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.shopper.app.base.MainActionbarBase;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury
 * use for loading article information in search screen from server  Asynchronously
 * search for the selected article
 * load the searched article in listview
 */

public class AsyncSearchTaskMain extends AsyncTask<Void, Void, Boolean> {
	MainActionbarBase search;
	//private view mSearchView;
	protected ProgressDialog progressDialog;
	
	public AsyncSearchTaskMain(MainActionbarBase src)
	{
		search = src;
		
	}
	@Override
	
	protected void onPreExecute() {			
//		search.startProgressForSearch();	
		
		/*ProgressBar pb = new ProgressBar(this.search);
		ProgressDialog pb = ProgressDialog.show(this.search, null, this.search.getString(android.R.attr.progressBarStyleSmall), true);
		progressDialog = ProgressDialog.show(this.search, "", "Searching, Please wait..", true, false);
		ProgressBar pb = new ProgressBar(this.search, null, android.R.attr.progressBarStyleSmall);
        pb.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        pb.setIndeterminate(true);
        pb.setVisibility(View.VISIBLE);
        search.mSearchView.addView(pb);*/
        
        
	}
	@Override
	protected Boolean doInBackground(Void... arcs) {		
		CommonValues.getInstance().IsServerConnectionError = false;
		return doSearch();
	}

	@Override
	protected void onPostExecute(Boolean result) {		
//		search.stopProgressForSearch();	
		//progressDialog.dismiss();
		search.applySearchResultInWidget(search,result);
		if(CommonValues.getInstance().ErrorCode==CommonConstraints.NO_EXCEPTION)
		{
			//load the searched article in listview
							
		}
		else
		{
			CommonTask.ShowMessage(search, CommonTask.getCustomExceptionMessage(search, CommonValues.getInstance().ErrorCode));
//			search.applySearchResultInWidget(search, result);
		}
	}

	private boolean doSearch() {
		//search for the selected article
		return search.doSearchInWidget();
	}
}
