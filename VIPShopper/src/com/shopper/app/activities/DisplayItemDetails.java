package com.shopper.app.activities;

import com.shopper.app.R;
import com.shopper.app.asynctasks.AsyncTaskAddBasketFromSearchInterface;
import com.shopper.app.asynctasks.AsyncTaskInterface;
import com.shopper.app.custom.controls.CustomEditText;
import com.shopper.app.entities.ArticleInq;
import com.shopper.app.entities.AsyncAddBasketFromSearch;
import com.shopper.app.entities.AsyncArticleDetailsLoading;
import com.shopper.app.entities.AsyncImageLoader;
import com.shopper.app.entities.DiscountGroup;
import com.shopper.app.entities.DiscoutWidgetImageTask;
import com.shopper.app.entities.OrderLine;
import com.shopper.app.entities.PriceInquiery;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.TextView.OnEditorActionListener;

public class DisplayItemDetails implements OnClickListener{

	Activity parentActivity;
	ViewFlipper parentFlipper;
	String articleId;
	boolean isItemDetailsReady = false;

	public DisplayItemDetails(Activity _parentActivity,
			ViewFlipper _parenFlipper, String _articleId) {
		this.parentActivity = _parentActivity;
		this.parentFlipper = _parenFlipper;
		this.articleId = _articleId;
		InitializeControls();
		LoadArticleDetailsContent();
	}

	private RelativeLayout rlSearchtDetail;
	private ViewFlipper flipper1;
	private TextView tvSearchItemImage, tvSearchItemDiscountImage,
			tvSearchItemText1, tvSearchItemText2, tvSearchItemText3,
			tvSearchItemPrice, tvSearchItemPriceFraction, discountHeaderText,
			discountFooterText;
	private Button bSearchItemAddBusket, bSearchItemSubtract, bSearchItemAdd;
	public CustomEditText etSearchItemQuantity;
	public LinearLayout lldiscount;
	public ProgressBar progressBar,llDiscountProgressBar,pbItemAdd;
	InputMethodManager imm;
	
	private void InitializeControls() {
		// flipper.setInAnimation(CommonTask.inFromRightAnimation());
		// flipper.setOutAnimation(CommonTask.outToLeftAnimation());
		RelativeLayout searchItemDetails;

		LayoutInflater inflater = (LayoutInflater) parentActivity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		searchItemDetails = (RelativeLayout) inflater.inflate(
				R.layout.search_item_detail, null);
		
		
		rlSearchtDetail = (RelativeLayout) searchItemDetails
				.findViewById(R.id.rlSearchDetail);

		flipper1 = (ViewFlipper) searchItemDetails
				.findViewById(R.id.vfSearchItemFilpper);
		tvSearchItemImage = (TextView) searchItemDetails
				.findViewById(R.id.ivSearchItemImage);
		tvSearchItemDiscountImage = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchItemDiscountImage);
		tvSearchItemText1 = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchItemText1);
		tvSearchItemText2 = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchItemText2);
		tvSearchItemText3 = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchItemText3);
		tvSearchItemPrice = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchItemPrice);
		tvSearchItemPriceFraction = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchItemPriceFraction);
		bSearchItemAddBusket = (Button) searchItemDetails
				.findViewById(R.id.bSearchItemAddBusket);
		bSearchItemSubtract = (Button) searchItemDetails
				.findViewById(R.id.bSearchItemSubtract);
		bSearchItemAdd = (Button) searchItemDetails
				.findViewById(R.id.bSearchItemAdd);
		etSearchItemQuantity = (CustomEditText) searchItemDetails
				.findViewById(R.id.etSearchItemQuantity);
		etSearchItemQuantity.setOnKeyPreListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
					quantityChanged();
//					Log.d("ineditorhere", "backpressed"+event.getKeyCode());
				}
				return false;
			}
		});
		etSearchItemQuantity.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void afterTextChanged(Editable s) {
//				if (orderLine != null) {
//					Home.showBasketCounter(CommonBasketValues.getInstance().Basket
//							.getTotalItemCount()
//							- orderLine.quantity
//							+ Integer
//									.valueOf("0"
//											+ etSearchItemQuantity.getText()
//													.toString()));
//					updateBasket();
//
//					if (Integer.valueOf("0"
//							+ etSearchItemQuantity.getText().toString()) == 0) {
//						flipper1.setDisplayedChild(0);
//						bSearchItemAddBusket.setVisibility(View.VISIBLE);
//						imm.hideSoftInputFromWindow(
//								etSearchItemQuantity.getWindowToken(), 0);
//					}
//					if (Integer.valueOf("0"
//							+ etSearchItemQuantity.getText().toString()) >= 99) {
//						bSearchItemAdd.setEnabled(false);
//					} else {
//						bSearchItemAdd.setEnabled(true);
//					}
//					
//
//				}
			}
		});
		
		etSearchItemQuantity.setOnEditorActionListener(new OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_DONE) {
		            // do your stuff here
		        	quantityChanged();
		        }
		        return false;
		    }

			
		});


		bSearchItemAddBusket.setOnClickListener(this);
		bSearchItemAdd.setOnClickListener(this);
		bSearchItemSubtract.setOnClickListener(this);

		lldiscount = (LinearLayout) searchItemDetails
				.findViewById(R.id.llDiscountFromSearch);
		discountHeaderText = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchDiscountHeaderText);
		discountFooterText = (TextView) searchItemDetails
				.findViewById(R.id.tvSearchDiscountFooterText);

		flipper1.setDisplayedChild(0);

		imm = (InputMethodManager) parentActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		progressBar = (ProgressBar) searchItemDetails.findViewById(R.id.itemLoadingProgressBar);
		llDiscountProgressBar = (ProgressBar) searchItemDetails.findViewById(R.id.itemDiscountProgressBar);
		pbItemAdd = (ProgressBar) searchItemDetails.findViewById(R.id.itemAddProgressBar);
		progressBar.setVisibility(View.INVISIBLE);
		pbItemAdd.setVisibility(View.INVISIBLE);
		llDiscountProgressBar.setVisibility(View.INVISIBLE);
		
		parentFlipper.addView(searchItemDetails);
		parentFlipper.setDisplayedChild(parentFlipper.getChildCount()-1);
		
		
		//adding an empty view to check the visibility of this view, so that orderline can be updated
		// whenever this view is visible on screen.
		// why this is added? A potential issue was say you changed the item quantity here, now you went to 
		// some other screen and updated the quantity of the same item, now if you get back to this screen
		// then still it was showing previous item quantity. the item quantity should be consistent in all places
		// 
		View v = new View(parentActivity){
			@Override
			protected void onVisibilityChanged(View changedView, int visibility) {
				super.onVisibilityChanged(changedView, visibility);
				if(visibility == View.VISIBLE){
					//our view just got visible, lets update the quantity
					if(isItemDetailsReady){
						onVisibleShowUpdatedProductQuantity();
					}
//					Log.d("visibilitychanged","article id: "+articleId+" visibility: "+visibility);
				}
			}
		};
		searchItemDetails.addView(v);

	}
	
	public OrderLine orderLine;
	private AsyncAddBasketFromSearch asyncAddBasketfromwidget;
	public void onClick(View v) {

		// flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		 //flipper.setOutAnimation(CommonTask.outToRightAnimation());
		switch (v.getId()) {
		case R.id.bSearchItemAddBusket: {
			//currently some work is going in background
			if(isAddStarted==true)
				return;
			orderLine = CommonTask
					.getOrderLineFromBasketByEan(((PriceInquiery) etSearchItemQuantity
							.getTag()).EAN);
			if (asyncAddBasketfromwidget != null) {
				asyncAddBasketfromwidget.cancel(true);
			}
			asyncAddBasketfromwidget = new AsyncAddBasketFromSearch(
					asyncAddBasketListener);
			asyncAddBasketfromwidget.execute();
			break;
		}
		case R.id.bSearchItemAdd: {
			selectedItemQty = Integer.valueOf("0"
					+ etSearchItemQuantity.getText().toString());
			if(selectedItemQty<99){
				selectedItemQty++;
				etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
				etSearchItemQuantity.setSelection(etSearchItemQuantity.getText()
						.length());
				imm.hideSoftInputFromWindow(etSearchItemQuantity.getWindowToken(),
						0);
				quantityChanged();
			}
			
//			Home.showBasketCounter(CommonBasketValues.getInstance().Basket
//					.getTotalItemCount() - orderLine.quantity + selectedItemQty);
			break;
		}
		case R.id.bSearchItemSubtract: {
			selectedItemQty = Integer.valueOf("0"
					+ etSearchItemQuantity.getText().toString());
			if (selectedItemQty > 0)
				selectedItemQty--;
			else
				break;
			etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
			etSearchItemQuantity.setSelection(etSearchItemQuantity.getText()
					.length());
			imm.hideSoftInputFromWindow(etSearchItemQuantity.getWindowToken(),
					0);
			quantityChanged();
//			Home.showBasketCounter(CommonBasketValues.getInstance().Basket
//					.getTotalItemCount() - orderLine.quantity + selectedItemQty);
			
			break;
		}
		default:
			break;
		}
	}
	
	void quantityChanged(){
		if (orderLine != null) {
			Home.showBasketCounter(CommonBasketValues.getInstance().Basket
					.getTotalItemCount()
					- orderLine.quantity
					+ Integer
							.valueOf("0"
									+ etSearchItemQuantity.getText()
											.toString()));
			updateBasket();

			if (Integer.valueOf("0"
					+ etSearchItemQuantity.getText().toString()) == 0) {
				flipper1.setDisplayedChild(0);
				bSearchItemAddBusket.setVisibility(View.VISIBLE);
				imm.hideSoftInputFromWindow(
						etSearchItemQuantity.getWindowToken(), 0);
			}
			if (Integer.valueOf("0"
					+ etSearchItemQuantity.getText().toString()) >= 99) {
				bSearchItemAdd.setEnabled(false);
			} else {
				bSearchItemAdd.setEnabled(true);
			}
			

		}
	}

	// load & show data for the Article Detail screen
	public void LoadArticleDetailsContent() {
		loadArticleDetails();
		rlSearchtDetail.setVisibility(View.INVISIBLE);
		flipper1.setVisibility(View.INVISIBLE);
		lldiscount.setVisibility(View.INVISIBLE);
		discountHeaderText.setVisibility(View.INVISIBLE);
		discountFooterText.setVisibility(View.INVISIBLE);

		flipper1.setDisplayedChild(0);
		bSearchItemAddBusket.setVisibility(View.VISIBLE);

	}

	private AsyncArticleDetailsLoading asyncArticleDetailsLoading = null;

	/**
	 * call async class for loading article Details...Tanvir
	 */
	public void loadArticleDetails() {

		if (asyncArticleDetailsLoading != null) {
			asyncArticleDetailsLoading.cancel(true);
		}
		asyncArticleDetailsLoading = new AsyncArticleDetailsLoading(
				detailsAsyncTaskListener, articleId, parentActivity);
		asyncArticleDetailsLoading.execute();
	}

	public void startProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}
	
	public void stopProgress() {

		progressBar.setVisibility(View.INVISIBLE);
	}
	private ArticleInq articleInqObj;
	AsyncTaskInterface detailsAsyncTaskListener = new AsyncTaskInterface() {
		@Override
		public void onTaskPreExecute() {
			isItemDetailsReady = false;
			startProgress();
		}

		@Override
		public void onTaskPostExecute(Object object) {
			stopProgress();
			if (CommonValues.getInstance().IsArticleDetailsRecordFound) {
				if (CommonValues.getInstance().IsServerConnectionError) {
					CommonTask.ShowMessage(parentActivity,
							parentActivity
									.getString(R.string.serverswitchError));
				} else {
					if (object instanceof ArticleInq) {
						articleInqObj = (ArticleInq) object;
						// view the Article Inquiry details from
						// articleinqobject
						viewArticleDetailsListView();
					}

				}
			}
			isItemDetailsReady = true;
		}

		@Override
		public void onDoInBackground() {

		}
	};
	
	public void viewArticleDetailsListView() {
		if (articleInqObj != null && articleInqObj.EAN != ""
				&& ArticleInq.IsProductFound) {

			AssignValues(articleInqObj);

		}
	}
	public DiscountGroup discountGroup;
	private void AssignValues(ArticleInq articleInq) {
		try {
			PriceInquiery priceInquiry = articleInq.getPriceInquiery();
			etSearchItemQuantity.setTag(priceInquiry);
			updateOrderline();
			if (ArticleInq.IsProductFound) {
				tvSearchItemText1.setText(CommonTask.toCamelCase(
						priceInquiry.text, " "));
				if (priceInquiry.text2 != null) {
					tvSearchItemText2.setText(CommonTask.toCamelCase(
							priceInquiry.text2, " "));
					tvSearchItemText2.setVisibility(View.VISIBLE);
				} else {
					tvSearchItemText2.setText("");
					tvSearchItemText2.setVisibility(View.INVISIBLE);
				}

				if (priceInquiry.contents > 0 && priceInquiry.priceper > 0) {
					tvSearchItemText3
							.setText(CommonTask
									.getContentString(priceInquiry.contents)
									+ priceInquiry.contentsdesc
									+ " ("
									+ priceInquiry.priceperdesc
									+ "-pris"
									+ " "
									+ CommonTask
											.getString(priceInquiry.priceper)
									+ ")");
					tvSearchItemText3.setVisibility(View.VISIBLE);
				} else {
					tvSearchItemText3.setText("");
					tvSearchItemText3.setVisibility(View.INVISIBLE);
				}

				// TODO: why are we not directly splitting with .(dots)
				String[] vals = String.valueOf(priceInquiry.price)
						.replace('.', ':').split(":");
				tvSearchItemPrice.setText(vals[0]);
				tvSearchItemPriceFraction
						.setText(vals[1].length() > 1 ? vals[1] : vals[1] + "0");

				if (priceInquiry.getDiscount().quantity > 0) {
					String dVal = String
							.valueOf(priceInquiry.getDiscount().quantity)
							+ " "
							+ priceInquiry.getDiscount().text
							+ " "
							+ Math.round(priceInquiry.getDiscount().amount);
					tvSearchItemDiscountImage.setText(dVal);
					tvSearchItemDiscountImage.setVisibility(View.VISIBLE);
				} else {
					tvSearchItemDiscountImage.setVisibility(View.INVISIBLE);
				}

				tvSearchItemImage.setTag(articleInq.EAN);
				AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
				asyncImageLoader.execute(tvSearchItemImage);

				discountGroup = priceInquiry.getDiscountGroup();

				if (discountGroup != null && discountGroup.id != "") {
					discountHeaderText.setVisibility(View.VISIBLE);
					discountFooterText.setVisibility(View.VISIBLE);
					lldiscount.setVisibility(View.VISIBLE);
					discountFooterText.setText(discountGroup.text);
					lldiscount.removeAllViews();
					DiscoutWidgetImageTask searchItemDiscoutWidgetImageTask = new DiscoutWidgetImageTask(
							parentActivity,lldiscount,articleId, discountGroup.id, subItemClickListener, DisplayItemDetails.this);
					searchItemDiscoutWidgetImageTask.execute();
				} else {
					discountHeaderText.setVisibility(View.INVISIBLE);
					discountFooterText.setVisibility(View.INVISIBLE);
					lldiscount.setVisibility(View.INVISIBLE);
					discountFooterText.setText("");
				}

			} else {
				tvSearchItemText1.setText(parentActivity.getString(R.string.productError));
				tvSearchItemText2.setText("");
				tvSearchItemPrice.setText("");
				tvSearchItemPriceFraction.setText("");
				tvSearchItemText3.setText("");
				tvSearchItemImage.setBackgroundDrawable(CommonTask.getDrawableImage(
						String.format(CommonURL.getInstance().ProductImageURL,
								articleId), articleId));
			}
		} catch (Exception e) {
			tvSearchItemText1.setText(parentActivity.getString(R.string.productError));
			tvSearchItemText2.setText("");
			tvSearchItemPrice.setText("");
			tvSearchItemPriceFraction.setText("");
			tvSearchItemText3.setText("");
			tvSearchItemImage.setBackgroundDrawable(CommonTask.getDrawableImage(
					String.format(CommonURL.getInstance().ProductImageURL,
							articleId), articleId));
		}
		rlSearchtDetail.setVisibility(View.VISIBLE);
		Animation animation = AnimationUtils.loadAnimation(parentActivity,
				R.anim.right_to_left);
		if(orderLine!=null){
			flipper1.setDisplayedChild(1);
			selectedItemQty = orderLine.quantity;
			etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
		}
		flipper1.startAnimation(animation);
		flipper1.setVisibility(View.VISIBLE);
	}
	
	OnClickListener subItemClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			parentFlipper.setInAnimation(CommonTask.inFromRightAnimation());
			parentFlipper.setOutAnimation(CommonTask.outToLeftAnimation());
			String subarticleId = (String) v
					.getTag();
			new DisplayItemDetails(parentActivity, parentFlipper, subarticleId);
		}
	};
	
	
	AsyncTaskAddBasketFromSearchInterface asyncAddBasketListener = new AsyncTaskAddBasketFromSearchInterface() {

		@Override
		public void onTaskPreExecute() {
			startProgressForSearch();
		}

		@Override
		public void onTaskPostExecute(Object object) {
			stopProgressForSearch();
			if (CommonValues.getInstance().ErrorCode == CommonConstraints.NO_EXCEPTION) {
				finishAddingTask();
			} else {
				CommonTask.ShowMessage(parentActivity, CommonTask
						.getCustomExceptionMessage(parentActivity,
								CommonValues.getInstance().ErrorCode));
			}
		}

		@Override
		public void startAddTask() {
			startAddingTask();
		}

		@Override
		public void onDoInBackground() {
		}
	};
	
	boolean isAddStarted = false;
	public void startProgressForSearch() {
		isAddStarted = true;
		pbItemAdd.setVisibility(View.VISIBLE);
	}
	
	public void stopProgressForSearch() {

		pbItemAdd.setVisibility(View.INVISIBLE);
		isAddStarted = false;
	}
	
	public void startDiscountProgress() {
		llDiscountProgressBar.setVisibility(View.VISIBLE);
	}
	
	public void stopDiscountProgress() {

		llDiscountProgressBar.setVisibility(View.INVISIBLE);
	}
	
	public int selectedItemQty;
	/**
	 * Add article to the order line if the order line(selected article) is old
	 * one or a continuous order then just add the quantity Or if the order line
	 * (selected article) are new the add it with the order as a new orderline
	 * with quantity 1
	 */
	public void startAddingTask() {
		if (orderLine != null && !orderLine.Id.equals("")) {
			selectedItemQty = orderLine.quantity + 1;
			CommonTask.addBasketFromDetail(parentActivity, orderLine, selectedItemQty);
		} else {
			selectedItemQty = 1;

			CommonTask.addBasketObject(parentActivity,
					(PriceInquiery) etSearchItemQuantity.getTag());
		}
	}
	
	/**
	 * Update basket title & quantity Show update basket screen for the selected
	 * item
	 */
	public void finishAddingTask() {
		updateOrderline();
		if (orderLine != null) {
			selectedItemQty = orderLine.quantity;
			etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
			etSearchItemQuantity.setSelection(etSearchItemQuantity.getText()
					.length());
//			Home.showBasketMenu();
//			if(Basket.listAdaptor!=null){
//				Basket.listAdaptor.updateList();
//			}
			flipper1.showNext();
		}
	}
	
	public void updateOrderline() {
		if (null != etSearchItemQuantity
				&& null != etSearchItemQuantity.getTag()) {
			orderLine = CommonTask
					.getOrderLineFromBasketByEan(((PriceInquiery) etSearchItemQuantity
							.getTag()).EAN);
		}
	}
	
	
	public void updateBasket() {

		if (isItemDetailsReady && flipper1 != null
				&& flipper1.getDisplayedChild() > 0) {
			updateOrderline();
			if (orderLine != null && orderLine.Id != "") {
				selectedItemQty = Integer.valueOf("0"
						+ etSearchItemQuantity.getText().toString());
				if (orderLine.quantity != selectedItemQty) {
					//quantity has changed so we have to send the update to server
					updateBasketAsync();
				}
//				imm.hideSoftInputFromWindow(
//						etSearchItemQuantity.getWindowToken(), 0);
			}
		}
	}
	
	
	private AsyncAddBasketFromSearch asyncUpdateBasketfromwidget; 
	public void updateBasketAsync() {
		if (asyncUpdateBasketfromwidget != null) {
			asyncUpdateBasketfromwidget.cancel(true);
		}
		asyncUpdateBasketfromwidget = new AsyncAddBasketFromSearch(
				asyncUpdateBasketListener);
		asyncUpdateBasketfromwidget.execute();
	}
	
	
	/**
	 * Update basket title & quantity Show update basket screen for the selected
	 * item
	 */
	public void finishUpdateTask() {
		updateOrderline();
		if (orderLine != null) {
			selectedItemQty = orderLine.quantity;
		}
		if(Basket.listAdaptor!=null){
			Basket.listAdaptor.updateList();
		}
	}
	
	AsyncTaskAddBasketFromSearchInterface asyncUpdateBasketListener = new AsyncTaskAddBasketFromSearchInterface() {

		@Override
		public void onTaskPreExecute() {
			startProgressForSearch();
		}

		@Override
		public void onTaskPostExecute(Object object) {
			stopProgressForSearch();
			if (CommonValues.getInstance().ErrorCode == CommonConstraints.NO_EXCEPTION) {
				finishUpdateTask();
			} else {
				CommonTask.ShowMessage(parentActivity, CommonTask
						.getCustomExceptionMessage(parentActivity,
								CommonValues.getInstance().ErrorCode));
			}
		}

		@Override
		public void startAddTask() {
			if (orderLine != null && !orderLine.Id.equals("")) {
				CommonTask.addBasketFromDetail(parentActivity, orderLine, selectedItemQty);
			}
		}

		@Override
		public void onDoInBackground() {
		}
	};
	
	
	void onVisibleShowUpdatedProductQuantity(){
		updateOrderline();
		if(orderLine!=null){
			flipper1.setDisplayedChild(1);
			selectedItemQty = orderLine.quantity;
			etSearchItemQuantity.setText(String.valueOf(selectedItemQty));
		}else{
			flipper1.setDisplayedChild(0);
			bSearchItemAddBusket.setVisibility(View.VISIBLE);
			imm.hideSoftInputFromWindow(
					etSearchItemQuantity.getWindowToken(), 0);
		}
	}


}
