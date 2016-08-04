package com.shopper.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.ViewFlipper;

import com.shopper.app.R;
import com.shopper.app.activities.Search.StateSearch;
import com.shopper.app.base.MainActionbarBase;
import com.shopper.app.utils.CommonTask;

/**
 * This class will be called if you type anything in seach widget edit box  
 * and performs the function of searching.after clicking on a search result item 
 * this activity will take you to the article details page and user can perform
 * checkout and payment related tasks as usual from there.
 *
 * @author TAC
 * 
 */


public class SearchDetails extends MainActionbarBase {

	public static ViewFlipper vfSearchDetails;

	InputMethodManager imm;
	public String articleId;
//	private AsyncArticleDetailsLoading asyncArticleDetailsLoading = null;

	public static StateSearch backState = StateSearch.INITIAL_STATE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_detail_activity_layout);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		mSupportActionBar.setHomeButtonEnabled(true);
		InitializeControls();
		processExtraData();
		

	}

	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// muststore the newintent unless getIntent() return the oldone
		setIntent(intent);
		processExtraData();
	};

	// use the data received here from the previous intent

	private void processExtraData() {
		Intent intent = getIntent();
		String artIdFromSearchWidget = intent.getStringExtra("id");
		if (artIdFromSearchWidget != null && !artIdFromSearchWidget.equals("")) {
			articleId = artIdFromSearchWidget;
//			LoadArticleDetailsContent();
			vfSearchDetails.setInAnimation(CommonTask.inFromRightAnimation());
			vfSearchDetails.setOutAnimation(CommonTask.outToLeftAnimation());
			if(vfSearchDetails!=null && vfSearchDetails.getChildCount()>0){
				vfSearchDetails.removeAllViews();
			}
			new DisplayItemDetails(this, vfSearchDetails, articleId);
			
		}
	}

	private void InitializeControls() {
		//flipper.setInAnimation(CommonTask.inFromRightAnimation());
		//flipper.setOutAnimation(CommonTask.outToLeftAnimation());
		
		vfSearchDetails = (ViewFlipper) findViewById(R.id.vfSearchDetails);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		

	}


	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
//		updateBasket();
	}

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		
		if(vfSearchDetails.getChildCount()>1){
			vfSearchDetails.setInAnimation(CommonTask.inFromLeftAnimation());
			vfSearchDetails.setOutAnimation(CommonTask.outToRightAnimation());
			vfSearchDetails.setDisplayedChild(vfSearchDetails.getChildCount()-2);
			vfSearchDetails.removeViewAt(vfSearchDetails.getChildCount()-1);
		}else{
			currentMenuIndex = getLastIndex();
			manageActivity();
		}

	}


}
