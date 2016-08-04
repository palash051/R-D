package com.shopper.app.base;

import java.util.Hashtable;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import com.shopper.app.entities.DiscountGroup;
import com.shopper.app.entities.OrderLine;
import com.shopper.app.utils.CommonTask;

/**
 * Manage Catalogue item details 
 * User can order the selected item directly from detail screen
 * User can update the order qty for selected item directly from detail screen
 * In  this class initialize menu for Catalogue using MainActionbarBase 
 * and also enable menu up button using mSupportActionBar.setDisplayHomeAsUpEnabled(true)
 * @author Shafiqul Alam
 * 
 */

@TargetApi(14)
public class CatalogueBase extends MainActionbarBase {
	InputMethodManager imm;

	public GestureDetector gestureScanner;
	public float oldScaleForFullScreen = 0f;// save catalogue page scale before
										// fullscreen mode.
	// Catalogue page & viewflipper related variables
	public ViewFlipper catalogueFlipper;

	// Product details related variables
	public View basketButton;
	public static ViewFlipper flipper1;
	public static EditText etSearchItemQuantity;
//	AsyncCatalogueDetailsLoading asyncCatalogueDetailsLoading;
	public static OrderLine orderLine;
	public int selectedItemQty;
	//private CatalogueBase catalogueBase;
	public DiscountGroup discountGroup;
//	CatalogueDiscountFamilyImageTask catalogueDiscountFamilyImageTask;
	public String articleId;
//	AsyncUpdateCatalogueFromArticleDetailTask asyncUpdateCatalogueFromArticleDetailTask;
	public Hashtable<Object, Object> selectedSubitemCollectionSearch = null;
	public boolean isBasketAdding = false;
	public ProgressBar detailProgressBar, llDiscountProgressBar;
	public boolean isCatalogueDetailsReady = false;
	public static boolean isDetailsVisible = false;
	public boolean isArticleDetailsReady = true;
	public static boolean isBackToArticle = false;
	public int oldFlipperControls = 0;
	public int lastVisibleView = 0;
	public InputMethodManager inputMethodManager;

	/**
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		//catalogueBase = this;
	}
	
	// Start of product details related methods
	
	/**
	 * Initialize Article Inquiry 
	 */
	public void loadArticleDetailsListView() {
		catalogueFlipper.setScaleX(1f);
		catalogueFlipper.setScaleY(1f);
	}
	/**
	 * View Article Inquiry details list
	 */

	
	
	
	
	

	/**
	 * Back from detail screen to catalogue main screen after back button pressed
	 * Re-design catalogue screen
	 */
	public void backFromArticleDetail() {
//		if (isCatalogueDetailsReady) {
//			isBackToArticle = false;
//			catalogueFlipper.setInAnimation(CommonTask.inFromLeftAnimation());
//			catalogueFlipper.setOutAnimation(CommonTask.outToRightAnimation());
//			if (catalogueFlipper.getChildCount() > oldFlipperControls) {
//				catalogueFlipper.showPrevious();
//				updateBasket();
//				initializeArticleDetailsContent("old");
//				catalogueFlipper.removeViewAt(catalogueFlipper
//						.getDisplayedChild() + 1);
//
//			}
//			if (catalogueFlipper.getChildCount() == oldFlipperControls) {
//				isDetailsVisible = false;
//				catalogueFlipper.setScaleX(oldScaleForFullScreen);
//				catalogueFlipper.setScaleY(oldScaleForFullScreen);
//				stopProgress();
//			}
//		}
		
		if(catalogueFlipper.getChildCount()>oldFlipperControls){
			catalogueFlipper.setInAnimation(CommonTask.inFromLeftAnimation());
			catalogueFlipper.setOutAnimation(CommonTask.outToRightAnimation());
			catalogueFlipper.showPrevious();
			catalogueFlipper.removeViewAt(catalogueFlipper
					.getDisplayedChild() + 1);
		}
		
		if (catalogueFlipper.getChildCount() == oldFlipperControls) {
			isDetailsVisible = false;
			catalogueFlipper.setScaleX(oldScaleForFullScreen);
			catalogueFlipper.setScaleY(oldScaleForFullScreen);
			stopProgress();
		}

	}

	/**
	 * Initialize catalogue screen again after menu reselect again and update the basket information
	 */
	public void manuReselect() {
		if (isCatalogueDetailsReady) {
			isBackToArticle = false;
			catalogueFlipper.setInAnimation(CommonTask.inFromLeftAnimation());
			catalogueFlipper.setOutAnimation(CommonTask.outToRightAnimation());
			if (catalogueFlipper.getChildCount() > oldFlipperControls) {
				catalogueFlipper.setDisplayedChild(lastVisibleView);
				isDetailsVisible = false;
				catalogueFlipper.setScaleX(oldScaleForFullScreen);
				catalogueFlipper.setScaleY(oldScaleForFullScreen);
//				updateBasket();
				int cont = catalogueFlipper.getChildCount();
				for (int i = oldFlipperControls; i < cont; i++) {
					catalogueFlipper.removeViewAt(oldFlipperControls);
				}

			}
		}
	}

	
	

	public int getFlipperDisplayIndex() {
		return catalogueFlipper.getDisplayedChild();
	}

	public void startProgress() {
		detailProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {

		detailProgressBar.setVisibility(View.INVISIBLE);
	}

	public void startDiscountProgressBar() {

		llDiscountProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDiscountProgressBar() {

		llDiscountProgressBar.setVisibility(View.INVISIBLE);
	}
	/**
	 * Asynchronously run the AsyncAddBasketFromCat from detail screen 
	 */
	
	
	

}