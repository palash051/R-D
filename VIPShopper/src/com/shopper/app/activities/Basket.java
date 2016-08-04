package com.shopper.app.activities;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.actionbarsherlock.view.Menu;
import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.R;
import com.shopper.app.activities.Search.StateSearch;
import com.shopper.app.base.CatalogueBase;
import com.shopper.app.base.LogInFunctionsBase;
import com.shopper.app.entities.AnimationFactory;
import com.shopper.app.entities.AnimationFactory.FlipDirection;
import com.shopper.app.entities.ArticleInq;
import com.shopper.app.entities.AsyncBackToBasketFromDetailTask;
import com.shopper.app.entities.AsyncCheckoutLoadingFromMore;
import com.shopper.app.entities.AsyncSaveUserALternateAddressFromOnlinePayment;
import com.shopper.app.entities.BasketLoadingTask;
import com.shopper.app.entities.CustomBasketListAdapter;
import com.shopper.app.entities.CustomBasketListAdapter.BasketViewHolder;
import com.shopper.app.entities.OrderLine;
import com.shopper.app.entities.OrderReply;
import com.shopper.app.entities.PriceInquiery;
import com.shopper.app.entities.UserInformation;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

import dk.dibs.android.library.DibsPayment;
import dk.dibs.android.library.PaymentData;
import dk.dibs.android.library.PaymentResultListener;
import dk.dibs.android.library.PurchasePaymentData;
import dk.dibs.android.library.Theme;

/**
 * Automatically call when basket menu select Manage all basket related task
 * like update order, in-house and online checkout system user can update order
 * quantity of any basket item All server related task are done Asynchronously
 * using BasketLoadingTask,AsyncArticleDetailsFromSubItemLoading,
 * AsyncAddBasketFromBasket and more Here create sub article/related article
 * details screen dynamically by selecting the sub/related item image from the
 * article detail screen After pressing back button with backState = -1 always
 * gone to home screen otherwise came back to the previous screen User can load
 * a pre-saved/uncompleted order for continue/finish the order For Online
 * checkout system here we use DibsPayment If user not login the system and try
 * to online payment, user will redirect to login screen for doing login And
 * back to the online payment mode after finishing login After pressing back
 * button with backState = -1 always gone to home screen otherwise came back to
 * the previous screen
 * 
 * @author jib
 * 
 */

public class Basket extends LogInFunctionsBase implements OnItemClickListener,
		OnClickListener, OnGestureListener, OnCheckedChangeListener {

	private static final String ADDING_MODE_BASKET = "basket",
			ADDING_MODE_EAN = "ean";

	private static final int BACK_FROM_ENTER_EAN = 0,
			BACK_FROM_ENTER_EAN_PRODUCT = 1, BACK_FROM_BASKET_DETAIL = 2,
			BACK_FROM_INHOUSE_CHECKOUT = 4, BACK_FROM_ONLINE_CHECKOUT = 5,
			BACK_FROM_DELEVERY_PAYMENT = 6, BACK_FROM_ONLINE_PAYMENT_MODE = 7,
			BACK_FROM_DIBS_PAYMENT = 8, BACK_FROM_DIBS_PAYMENT_SUCCESS = 9,
			FLIPPER_DISPLAY_BASKET_LIST_SRCEEN = 0,
			FLIPPER_DISPLAY_BASKET_CHECKOUT_SCREEN = 1,
			FLIPPER_DISPLAY_INHOUSE_CHECKOUT = 7;

	public static final int INITIAL_STATE = -1, BACK_FROM_CHECKOUT_BASKET = 3;

	private static final int INITIAL_POSITION_FOR_DYNAMIC_SCREEN_ADD = 10,
			INITIAL_ROTATE_INTERVAL = 350, INITIAL_BASKET_QUANTITY = 0,
			NO_POSITION = -1;

	public static boolean isBackToBasket = false, basketLoadedFromEan = true;
	public boolean isArticleDetailsReady = true, isBasketDetailsReady = true,
			isFinishedItemAddingToBasket = false;
	private boolean isBasketUpdated = false;
	boolean isAnimWoeking = false;

	public static int backState = INITIAL_STATE;

	private int year, month, day;
	public int selectedItemQty = INITIAL_BASKET_QUANTITY;
	private static final int ROTATE_INTERVAL = INITIAL_ROTATE_INTERVAL;
	// Details view controls related

	int noOfDefaultFlipperControls; // depends on default controls
	int noOfOtherControlsFromPosition;// depends on rest of controls after
										// details views

	final int positionNo = INITIAL_POSITION_FOR_DYNAMIC_SCREEN_ADD;// starting
																	// from 0

	public String famId, eanAddingMode, ean;
	String array_spinner[];
	private static String emptyBasketText;

	public static ListView basketListView;

	private ViewFlipper mainflipper;
	public ViewFlipper basketQuantityflipper;
	public static ViewFlipper flipper;

	private static TextView totalPrice, totalPriceFrac, basketHeading;
	private TextView basketAddingStatus;
	public TextView checkoutBarcode;
	private TextView tvDeliverydate, tvReadTermsConditions, eanStatusView,
			checkoutBarcodeStatus;

	EditText eanEditView, basketItemQty, etAnotherAddress, etALtrntAddress1,
			etALtrntAddress2, etALtrntAddressPostnummer, etALtrntAddressBy,
			etALtrntAddressTelepon;
	public EditText etSMSNumber, etEnterCode;

	public Button cancelBasket;
	private Button addBasket, cancelProductEan, doneProductEan, addFromEan,
			addFromBasket, checkoutBasketButton;
	private Button bInhouseCheckout, bOnlineCheckout;

	public ProgressBar progressBar, basketDiscountProgressBar;

	public CheckBox cbSmsOnDelivery, cbTermsAndCondition, cbToAnotherAddress,
			cbToPay;

	private DibsPayment paymentWindow;

	public LinearLayout lldiscount, basketLayout;

	private RelativeLayout rlToALtrntAddress, rlSMSNumber,
			enterProductEanLayout;

	public ImageView checkoutBarcodeImage, tvArticleImageText, checkMark;


	public static OrderLine orderLine;

	ArticleInq objSubArticleInq = null;

	InputMethodManager imm;

	private static Thread basketThread;

	private Runnable bWaitRunnable;

	private Handler bHandler;

	public static CustomBasketListAdapter listAdaptor;

	BasketLoadingTask basketLoadingTask = null;
	AsyncBackToBasketFromDetailTask asyncBackToBasketFromDetailTask = null;
	AsyncSaveUserALternateAddressFromOnlinePayment asyncSaveUserALternateAddressFromOnlinePayment = null;

	AsyncCheckoutLoadingFromMore asyncCheckoutLoadingFromMore = null;

	private GestureDetector gestureScanner;

	public Hashtable<Object, Object> selectedSubitemCollectionBasket = null;

	java.util.Date deliveryDate;

	/**
	 * Automatically call from menu select once initialize all controls
	 * initialize DibsPayment initialize Roboto-font for controls
	 * 
	 * 
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.basket_main);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initializeTextViewControl();
		flipperandOtherControls();
		emptyBasketText = getString(R.string.empty_basket);

		initEanEnterBasket();
		progressbarControl();
		bHandler = new Handler();
		gestureScanner = new GestureDetector(Basket.this, this);
		initializeCheckBoxViewControl();
		initBase();
		initializeButtonControl();
		setCurrentDateOnView();

		prepareLoginView();
		if (!CommonTask.isUserLoggedIn()) {
			hideLoginUserPassControl();
		}
		backState = INITIAL_STATE;

	}

	/**
	 * 
	 */
	public void progressbarControl() {
		progressBar = (ProgressBar) findViewById(R.id.basketProgressBar);
		progressBar.setVisibility(View.INVISIBLE);
		basketDiscountProgressBar = (ProgressBar) findViewById(R.id.llBasketDiscountProgressBar);
		basketDiscountProgressBar.setVisibility(View.INVISIBLE);
	}

	/**
	 * 
	 */
	public void flipperandOtherControls() {
		flipper = (ViewFlipper) findViewById(R.id.vfBasket);
		noOfDefaultFlipperControls = flipper.getChildCount();
		noOfOtherControlsFromPosition = noOfDefaultFlipperControls - 1;
		mainflipper = (ViewFlipper) findViewById(R.id.vfBasketMain);
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		paymentWindow = (DibsPayment) flipper
				.findViewById(R.id.lDibsPaymentView);

	}

	// initialize all button controls
	private void initializeButtonControl() {
		Button bCancel1 = (Button) findViewById(R.id.bCancel1);
		Button bCancel2 = (Button) findViewById(R.id.bCancel2);
		bInhouseCheckout = (Button) findViewById(R.id.bInhouseCheckout);
		bOnlineCheckout = (Button) findViewById(R.id.bOnlineCheckout);
		Button bNextFromDeliveryPayment = (Button) findViewById(R.id.bNextFromDeliveryPayment);
		bDibsPayment = (Button) findViewById(R.id.bDibsPayment);
		bDibsPayment.setEnabled(false);
		bDibsPayment.setTextColor(Color.GRAY);
		Button bDeliverydate = (Button) findViewById(R.id.bDeliverydate);
		checkoutBasketButton = (Button) findViewById(R.id.bCheckoutBasket);
		enableDisableCheckoutButton();
		bDibsPayment.setOnClickListener(this);
		bCancel1.setOnClickListener(this);
		bCancel2.setOnClickListener(this);
		bInhouseCheckout.setOnClickListener(this);
		bOnlineCheckout.setOnClickListener(this);
		bNextFromDeliveryPayment.setOnClickListener(this);
		bDeliverydate.setOnClickListener(this);
		tvDeliverydate.setOnClickListener(this);
		tvReadTermsConditions.setOnClickListener(this);
		checkoutBasketButton.setOnClickListener(this);

		if (basketThread == null) {
			bWaitRunnable = new Runnable() {
				public void run() {

					if (CommonValues.getInstance().IsAnyNewBasketItemAdded
							&& basketLoadedFromEan) {
						try {
							isBasketUpdated = false;
							basketListView.setEnabled(false);

							startProgress();
						} catch (Exception e) {
						}

					}
					if (CaptureActivity.isBasketAddingFinished()
							&& basketLoadedFromEan && !isBasketUpdated) {
						updateBasket();
						basketListView.setEnabled(true);
						stopProgress();
					}
					bHandler.postDelayed(bWaitRunnable, 1);
				}
			};
		}
		basketThread = new Thread(bWaitRunnable);
		basketThread.start();
	}

	/**
	 * Stop Basket Thread after setting changed
	 */
	public static void resetBasketAfterSettingChanged() {
		CommonValues.getInstance().IsAnyNewBasketItemAdded = false;
		CommonBasketValues.getInstance().Basket = new OrderReply();
		CommonBasketValues.getInstance().updateBasket();
		if (basketThread != null) {
			basketThread.interrupt();
		}
	}

	// initialize all checkbox controls
	private void initializeCheckBoxViewControl() {
		cbSmsOnDelivery = (CheckBox) findViewById(R.id.cbSmsOnDelivery);
		cbTermsAndCondition = (CheckBox) findViewById(R.id.cbTermsAndCondition);
		cbToPay = (CheckBox) findViewById(R.id.cbToPay);
		cbToAnotherAddress = (CheckBox) findViewById(R.id.cbToAnotherAddress);

		cbSmsOnDelivery.setOnCheckedChangeListener(this);
		cbTermsAndCondition.setOnCheckedChangeListener(this);
		cbToPay.setOnCheckedChangeListener(this);
		cbToAnotherAddress.setOnCheckedChangeListener(this);
	}

	// initialize all Textbox controls and set font
	private void initializeTextViewControl() {
		basketListView = (ListView) findViewById(R.id.lvBasket);

		etSMSNumber = (EditText) findViewById(R.id.etSMSNumber);
		etEnterCode = (EditText) findViewById(R.id.etEnterCode);
		etALtrntAddress1 = (EditText) findViewById(R.id.etALtrntAddress1);
		etALtrntAddress2 = (EditText) findViewById(R.id.etALtrntAddress2);
		etALtrntAddressPostnummer = (EditText) findViewById(R.id.etALtrntAddressPostnummer);
		etALtrntAddressBy = (EditText) findViewById(R.id.etALtrntAddressBy);
		etALtrntAddressTelepon = (EditText) findViewById(R.id.etALtrntAddressTelepon);
		totalPrice = (TextView) findViewById(R.id.tvTotalPrice);
		basketHeading = (TextView) findViewById(R.id.tvBasketHeadingText);
		totalPriceFrac = (TextView) findViewById(R.id.tvTotalPriceFraction);
		tvReadTermsConditions = (TextView) findViewById(R.id.tvReadTermsConditions);
		tvDeliverydate = (TextView) findViewById(R.id.tvDeliverydate);
		rlToALtrntAddress = (RelativeLayout) findViewById(R.id.rlToALtrntAddress);
		rlToALtrntAddress.setVisibility(View.GONE);
		rlSMSNumber = (RelativeLayout) findViewById(R.id.rlSMSNumber);
		rlSMSNumber.setVisibility(View.GONE);
	}

	/**
	 * Use for enable/disable Checkout Button depend on BasketOrder Line
	 */
	public void enableDisableCheckoutButton() {
		if (!CommonBasketValues.getInstance().Basket.Lines.isEmpty()) {
			checkoutBasketButton.setEnabled(true);
		} else {
			checkoutBasketButton.setEnabled(false);
		}
	}

	@Override
	protected void onResume() {

		// we cann't not update basket any item without completing scaning
		if (CaptureActivity.isBasketAddingFinished() && basketLoadedFromEan) {
			initialize(this);
			// Hide the Terms and Conditions popup
			// Hide error massage for the blank or wrong mobile number
			etSMSNumber.setError(null);
			// Set enable/disable checkout button depends if basket is empty
			enableDisableCheckoutButton();
			// initialize EAN screen controls
			commonEanView();
			// Have BasketAcitivity register as an EventHandler.
			// Add onLogin() to BaketActivity..Tanvir
			loggedInView();
			// Update Title and basket count
			Home.showBasketMenu();
			setActionBarMenuVisibility(true);
			
		}
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// User is in Dibs payment window and has succesfully performed payment.
		if (flipper.getDisplayedChild() == 6
				&& backState == BACK_FROM_DIBS_PAYMENT_SUCCESS) {
			// Reset Basket view to initial state, after succesfull payment
			initBasketViewAfterCheckout();
		}
		// Set Initial state as active state when this activity is resumed
		if (flipper.getDisplayedChild() > FLIPPER_DISPLAY_BASKET_LIST_SRCEEN)
			flipper.setDisplayedChild(FLIPPER_DISPLAY_BASKET_LIST_SRCEEN);
		// Submit order to server for completing order
		// Update the basket title after successfully order submitted
		submitOrderAndUpdateBasketTitle();
		// Set Initial state as active state when this activity is resumed
		
		backState = INITIAL_STATE;

	}

	/***
	 * Update the Basket in the menu so the Quantity is correct.
	 */

	public void updateBasketQuantity() {
		// Update the quantity in the Basket in the Menu
		Home.showBasketCounter(CommonBasketValues.getInstance().Basket
				.getTotalItemCount());
	}

	/***
	 * Temporary views are inserted in the flipper between the List (initial
	 * index 0) and the CheckOut (initial index 1) If the list is longer than
	 * the initial length, it is because it contains temporary views. Delete
	 * anything after index 0, until you are back at the initial length.
	 */
	public void deleteAllTemporaryViews() {
		// If flippercontains morethan the
		// intialquantityofchildren,somearetemporary
		if (flipper != null
				&& flipper.getChildCount() > noOfDefaultFlipperControls) {
			// Delete the temporary views
			int removeCount = flipper.getChildCount()
					- noOfDefaultFlipperControls;
			for (int i = 0; i < removeCount; i++) {
				if (flipper.getChildCount() > INITIAL_POSITION_FOR_DYNAMIC_SCREEN_ADD)
					flipper.removeViewAt(INITIAL_POSITION_FOR_DYNAMIC_SCREEN_ADD);
			}
		
		}
		if(mainflipper !=null && mainflipper.getDisplayedChild()==1){
			
			cancelFromEnterProductEan();
			//backState = INITIAL_STATE;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	// Update basket after any update of current basket
	protected void updateBasket() {
		try {
			initialize(this);
		} catch (Exception oEx) {
			CommonTask.ShowMessage(this.getParent(), oEx.getMessage());
		}
		isBasketUpdated = true;
	}

	/**
	 * initialize all basket object & listview
	 * 
	 * @param basket
	 */
	public void initialize(Basket basket) {
		CommonValues.getInstance().IsAnyNewBasketItemAdded = false;
		listAdaptor = new CustomBasketListAdapter(Basket.this,
				R.layout.basket_item_view,
				CommonBasketValues.getInstance().Basket.Lines);
		basketListView.setOnItemClickListener(this);
		basketListView.setAdapter(listAdaptor);
		// Change total price for the basket if any change done
		displayTotalPrice();
		basketHeading
				.setText(CommonBasketValues.getInstance().Basket.OrderNo > 0 ? getString(R.string.basket)
						: emptyBasketText);
		// binding the context menu while basket item selected
		registerForContextMenu(basketListView);

	}

	/**
	 * Change basket title with total amount and item quantity of basket
	 */
	public static void displayTotalPrice() {
		if (CommonBasketValues.getInstance().Basket != null
				&& CommonBasketValues.getInstance().Basket.TotalPrice > 0) {
			double totalAmount = CommonTask.round(
					CommonBasketValues.getInstance().Basket.TotalPrice, 2,
					BigDecimal.ROUND_HALF_UP);

			String[] total = String.valueOf(totalAmount).replace(".", ":")
					.split(":");
			totalPrice.setText(total[0]);
			totalPriceFrac.setText(total[1].length() < 2 ? total[1] + "0"
					: total[1]);
		} else {
			totalPrice.setText("");
			totalPriceFrac.setText("");
		}
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long rowid) {
		if (CaptureActivity.isBasketAddingFinished() && basketLoadedFromEan) {
			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			try {

				listAdaptor.setSelection(position);
				basketListView.setSelection(position);
				basketListView.setSelectionFromTop(position, view.getTop());

				isBackToBasket = true;

				// we have to delete any previous temporary views
				int removeCount = flipper.getChildCount()
						- noOfDefaultFlipperControls;
				for (int i = 0; i < removeCount; i++) {
					if (flipper.getChildCount() > INITIAL_POSITION_FOR_DYNAMIC_SCREEN_ADD)
						flipper.removeViewAt(INITIAL_POSITION_FOR_DYNAMIC_SCREEN_ADD);
				}
				orderLine = (OrderLine) listAdaptor.getItemAtPosition(position);
				new DisplayItemDetails(Basket.this, flipper, orderLine.Id);
				backState = BACK_FROM_BASKET_DETAIL;

			} catch (Exception e) {
				CommonTask.ShowMessage(getParent(), e.getMessage());
			}
		}
	}

	/**
	 * After pressing back button with backState = -1 always gone to home screen
	 * otherwise came back to the previous screen
	 */
	@Override
	public void onBackPressed() {
		enableDisableCheckoutButton();

		etSMSNumber.setError(null);
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		switch (backState) {
		case BACK_FROM_ENTER_EAN_PRODUCT:
			backState = INITIAL_STATE;
			break;
		case INITIAL_STATE:
			currentMenuIndex = getLastIndex();
			// Home.showBasketMenu();
			manageActivity();
			break;
		case BACK_FROM_ENTER_EAN:// use for back from ProductEan
			cancelFromEnterProductEan();
			break;
		case BACK_FROM_BASKET_DETAIL: // use for back from basket detail
			backToBasket();
			break;
		case BACK_FROM_CHECKOUT_BASKET:
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			// show basketmainscreen
			flipper.setDisplayedChild(FLIPPER_DISPLAY_BASKET_LIST_SRCEEN);
			backState = INITIAL_STATE;
			break;
		case BACK_FROM_INHOUSE_CHECKOUT:
		case BACK_FROM_ONLINE_CHECKOUT:
		case BACK_FROM_DELEVERY_PAYMENT:
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			// showpaymentmodescreen
			flipper.setDisplayedChild(FLIPPER_DISPLAY_BASKET_CHECKOUT_SCREEN);
			backState = BACK_FROM_CHECKOUT_BASKET;
			break;

		case BACK_FROM_ONLINE_PAYMENT_MODE:
			flipper.setDisplayedChild(4);// Show Delivery payment screen for
											// online payment mode
			backState = BACK_FROM_DELEVERY_PAYMENT;
			break;
		case BACK_FROM_DIBS_PAYMENT:
			// Show Online paymentmode(VISA/MASTERCARD/DIBS)
			flipper.setDisplayedChild(5);
			backState = BACK_FROM_ONLINE_PAYMENT_MODE;
			setActionBarMenuVisibility(true);
			break;
		default:
			CommonTask.CloseApplication(getParent());
			break;
		}

	}

	public void onClick(View v) {
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		switch (v.getId()) {

		case R.id.bCheckoutBasket:

			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			// Show payment mode screen
			flipper.setDisplayedChild(FLIPPER_DISPLAY_BASKET_CHECKOUT_SCREEN);
			backState = BACK_FROM_CHECKOUT_BASKET;
			break;
		case R.id.bInhouseCheckout:
			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			// ShowIn-housepayment screen
			flipper.setDisplayedChild(FLIPPER_DISPLAY_INHOUSE_CHECKOUT);
			initializeCheckoutComponent();
			backState = BACK_FROM_INHOUSE_CHECKOUT;
			break;
		case R.id.bOnlineCheckout:
			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			backState = BACK_FROM_ONLINE_CHECKOUT;
			if (!CommonTask.isUserLoggedIn()) {
				CommonValues.getInstance().IsCallFromBasket = true;
				currentMenuIndex = SCANNING_ACTIVITY;
				More.backStateMore = -1;
				if (!stackIndex.contains(String.valueOf(4)))
					stackIndex.push(String.valueOf(4));
				/*
				 * if (more != null) { more = new Intent(Basket.this,
				 * More.class);
				 * more.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); }
				 */
				startActivity(more);

			} else {
				// Show Delivery payment screen for online payment mode
				flipper.setDisplayedChild(4);
			}

			break;

		case R.id.bCancel1:
		case R.id.bCancel2:
			// back to payment choose screen
			flipper.setDisplayedChild(FLIPPER_DISPLAY_BASKET_CHECKOUT_SCREEN);
			backState = BACK_FROM_CHECKOUT_BASKET;
			/*
			 * also reset the edittext fields in checkout screen
			 * resetOnlineCheckoutScreenEditTexts();
			 */
			break;

		case R.id.bNextFromDeliveryPayment:

			try {
				if (checkSmsValidation()) {
					if (cbToAnotherAddress.isChecked()) {
						if (checkAlternateAddressValidation()) {

							saveUserAlternateAddress();

						}
					} else {

						resetOnlineCheckoutScreenEditTexts();
						setDataAfterDeliveryPayment();
					}
				}
			} catch (Exception e) {
				e.getMessage();
			}

			break;
		case R.id.bDeliverydate:

			DatePickerDialog dateDialogue = new DatePickerDialog(this,
					datePickerListener, year, month, day);
			dateDialogue.show();

			break;

		case R.id.bDibsPayment:// manage dibs payment methods.....Tanvir
			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			try {
				if (paymentWindow != null) {
					((WebView) paymentWindow.getChildAt(0)).reload();
					flipper.showNext();
					PaymentData paymentData = constructPaymentData();
					paymentWindow.loadPaymentWindow(paymentData);
					paymentWindow
							.setPaymentResultListener(new PaymentResultListener() {

								public void paymentAccepted(
										Map<String, String> arg0) {
									paymentWindow.destroyDrawingCache();
									try {

										runOnUiThread(new Runnable() {
											public void run() {
												initBasketViewAfterCheckout();
												AlertDialog.Builder builder = new AlertDialog.Builder(
														Basket.this);
												builder.setTitle(
														getResources()
																.getString(
																		R.string.app_name))
														.setMessage(
																getString(R.string.payment_success))
														.setCancelable(false)
														.setPositiveButton(
																R.string.button_ok,
																new DialogInterface.OnClickListener() {
																	public void onClick(
																			DialogInterface dialog,
																			int id) {
																		goHome(Basket.this);
																		backState = INITIAL_STATE;
																	}
																});
												AlertDialog alert = builder
														.create();
												alert.show();

											}
										});

									} catch (Exception e) {
										System.out.println(e.getMessage());
									}
								}

								public void cancelUrlLoaded() {

								}

								public void failedLoadingPaymentWindow() {

								}

								public void paymentCancelled(
										Map<String, String> arg0) {
									backState = BACK_FROM_DIBS_PAYMENT_SUCCESS;
								}

								public void paymentWindowLoaded() {

								}

							});

				}
			} catch (Exception e) {
				e.getMessage();
			}
			backState = BACK_FROM_DIBS_PAYMENT;
			break;
		case R.id.tvReadTermsConditions:
			AlertDialog dialog = new AlertDialog.Builder(Basket.this)
					.setTitle(
							Basket.this.getResources().getString(
									R.string.terms_conditions))
					.setMessage(
							Basket.this.getResources().getString(
									R.string.accept_terms))
					.setPositiveButton(R.string.ok, null).show();
			dialog.setCanceledOnTouchOutside(true);
			break;

		}

	}

	void resetOnlineCheckoutScreenEditTexts() {
		etALtrntAddress1.setText("");
		etALtrntAddress2.setText("");
		etALtrntAddressPostnummer.setText("");
		etALtrntAddressBy.setText("");
		etALtrntAddressTelepon.setText("");
	}

	/**
	 * call AsyncSaveUserALternateAddressFromOnlinePayment class for saving
	 * useralternateaddress Tanvir
	 */
	public void saveUserAlternateAddress() {
		if (asyncSaveUserALternateAddressFromOnlinePayment != null) {
			asyncSaveUserALternateAddressFromOnlinePayment.cancel(true);
		}
		asyncSaveUserALternateAddressFromOnlinePayment = new AsyncSaveUserALternateAddressFromOnlinePayment(
				this);
		asyncSaveUserALternateAddressFromOnlinePayment.execute();
	}

	private void backToBasket() {
		if (isBasketDetailsReady) {
			try {
				isBackToBasket = false;
				flipper.setInAnimation(CommonTask.inFromLeftAnimation());
				flipper.setOutAnimation(CommonTask.outToRightAnimation());

				if (flipper.getDisplayedChild() > noOfDefaultFlipperControls) {
					flipper.showPrevious();
					flipper.removeViewAt(flipper.getChildCount() - 1);
				} else if (flipper.getDisplayedChild() == noOfDefaultFlipperControls) {
					flipper.setDisplayedChild(0);
					flipper.removeViewAt(flipper.getChildCount() - 1);
					backState = INITIAL_STATE;
				}
				imm.hideSoftInputFromWindow(basketItemQty.getWindowToken(), 0);
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Called after select other menu from basket menu and update basket again
	 * if there have any update
	 */
	public void updateBasketAfterTabChange() {
		try {
			if (isBasketDetailsReady && basketQuantityflipper != null
					&& basketQuantityflipper.getDisplayedChild() == 0) {
				OrderLine objOrderLine = CommonTask
						.getOrderLineFromBasketByEan(((PriceInquiery) basketItemQty
								.getTag()).EAN);

				selectedItemQty = Integer.valueOf("0"
						+ basketItemQty.getText().toString());
				if (objOrderLine != null
						&& objOrderLine.quantity != selectedItemQty) {
					orderLine = objOrderLine;
					submitOrderAndUpdateBasketTitle();
				}
				setBasketQtyToOther();
				imm.hideSoftInputFromWindow(basketItemQty.getWindowToken(), 0);
			}

		} catch (Exception e) {
		}

	}

	/**
	 * Use for empty selection of listview
	 */
	public void refreshBasketList() {
		listAdaptor.setSelection(NO_POSITION);
		basketListView.setSelection(NO_POSITION);
		basketListView.setSelectionFromTop(NO_POSITION, 0);
	}

	/**
	 * call from basket detail page when came back previous called from needs
	 * any update of basket Asynchronously using AsyncBackToBasketFromDetailTask
	 * Submit order to server for completing order Update the basket title after
	 * successfully order submitted
	 */

	private void submitOrderAndUpdateBasketTitle() {
		if (CommonBasketValues.getInstance().Basket.hasChanged()) {
			submitOrderAndBacktoBasket();
		} else {
			deleteAllTemporaryViews();
		}
		
	}

	/**
	 * any update of basket Asynchronously using AsyncBackToBasketFromDetailTask
	 * Submit order to server for completing order and Update the basket title
	 * after successfull order submission.Tanvir
	 */
	public void submitOrderAndBacktoBasket() {
		if (asyncBackToBasketFromDetailTask != null) {
			asyncBackToBasketFromDetailTask.cancel(true);

		}
		asyncBackToBasketFromDetailTask = new AsyncBackToBasketFromDetailTask(
				this);
		asyncBackToBasketFromDetailTask.execute();
	}

	/**
	 * initialize all controls for adding basket from EAN or old basket
	 */
	protected void initEanEnterBasket() {
		basketLayout = (LinearLayout) findViewById(R.id.basketview);
		enterProductEanLayout = (RelativeLayout) findViewById(R.id.enterproducteanview);
		enterProductEanLayout.setVisibility(RelativeLayout.GONE);

		addBasket = (Button) findViewById(R.id.addEnterEanBasket);
		cancelProductEan = (Button) findViewById(R.id.cancelProductEan);
		doneProductEan = (Button) findViewById(R.id.doneProductEan);
		doneProductEan.setEnabled(false);
		
		
        
		
		
		addFromEan = (Button) findViewById(R.id.addFromEan);
		addFromBasket = (Button) findViewById(R.id.addFromBasket);

		basketAddingStatus = (TextView) findViewById(R.id.basketAddingStatus);
		checkMark = (ImageView) findViewById(R.id.checkMark);

		eanEditView = (EditText) findViewById(R.id.eanEditView);
		
		

		eanStatusView = (TextView) findViewById(R.id.eanStatusView);
		
		//Go to EnterProductEan page..TAC
		addBasket.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				if (CaptureActivity.isBasketAddingFinished()
						&& basketLoadedFromEan) {

					eanEditView.requestFocus();
//					AnimationFactory.flipTransition(mainflipper,
//							FlipDirection.RIGHT_LEFT, imm, 2, ROTATE_INTERVAL,
//							animListener);
					imm.hideSoftInputFromWindow(eanEditView.getWindowToken(), 0);
//					final ViewAnimator viewAnimator = (ViewAnimator));
//					AnimationFactory.flipTransition(mainflipper, FlipDirection.LEFT_RIGHT);
					mainflipper.setInAnimation(CommonTask.inFromRightAnimation());
					mainflipper.setOutAnimation(CommonTask.outToLeftAnimation());
					mainflipper.setDisplayedChild(1);
					backState = BACK_FROM_ENTER_EAN;

				}
			}
		});
		cancelProductEan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				cancelFromEnterProductEan();
			}

		});
		doneProductEan.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				AnimationFactory.flipTransition(mainflipper,
//						FlipDirection.RIGHT_LEFT, null, 1, ROTATE_INTERVAL,
//						animListener);
				eanEditView.requestFocus();
				imm.hideSoftInputFromWindow(eanEditView.getWindowToken(), 0);
				cancelFromEnterProductEan();
				addBasket();
			}
		});

		addFromEan.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				basketAddingStatus.setText(getString(R.string.add_item));

				eanStatusView.setText(getString(R.string.indtastEan));
				eanAddingMode = ADDING_MODE_EAN;
				addFromEan
						.setBackgroundResource(R.drawable.left_curved_button_bg_selected);
				addFromBasket
						.setBackgroundResource(R.drawable.right_curved_button_bg);

			}
		});
		addFromBasket.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				basketAddingStatus.setText(getString(R.string.hentbon));
				eanStatusView.setText(getString(R.string.bonnummer));
				eanAddingMode = ADDING_MODE_BASKET;
				addFromEan
						.setBackgroundResource(R.drawable.left_curved_button_bg);
				addFromBasket
						.setBackgroundResource(R.drawable.right_curved_button_bg_selected);

			}
		});

		eanEditView.addTextChangedListener(new TextWatcher() {

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void afterTextChanged(Editable s) {
				String tean = ean;
				ean = eanEditView.getText().toString();
				int len = ean.length();
				if (len == 13) {
					CommonTask.setAlphaVisible(checkMark, true);
				} else if (len > 13) {
					eanEditView.setText(tean);
					ean = tean;
					eanEditView.setSelection(13);

				} else if (!ean.equals("")
						&& checkMark.getVisibility() == View.VISIBLE) {
					CommonTask.setAlphaVisible(checkMark, false);
				}
				if(len==0){
					doneProductEan.setEnabled(false);
				}
				else{
					doneProductEan.setEnabled(true);
				}

			}

		});
		
		
	}

   //Rotater the main flipper and set backstate to initial position ..actulaay refresh the previous screen
	private void cancelFromEnterProductEan() {
//		AnimationFactory.flipTransition(mainflipper, FlipDirection.RIGHT_LEFT,
//				null, 1, ROTATE_INTERVAL, animListener);
		eanEditView.requestFocus();
		imm.hideSoftInputFromWindow(eanEditView.getWindowToken(), 0);
		mainflipper.setInAnimation(CommonTask.inFromLeftAnimation());
		mainflipper.setOutAnimation(CommonTask.outToRightAnimation());
		mainflipper.setDisplayedChild(0);
		backState = INITIAL_STATE;
	}

	AnimationListener animListener = new AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			getWindow().getDecorView().postInvalidate();
		}
	};

	/**
	 * initialize controls for EnterEAN screen
	 */
	protected void commonEanView() {
		basketAddingStatus.setText(getString(R.string.add_item));
		eanStatusView.setText(getString(R.string.indtastEan));

		eanAddingMode = ADDING_MODE_EAN;

		eanEditView.setText("");
		checkMark.setVisibility(View.INVISIBLE);

		if (CommonBasketValues.getInstance().Basket.hasWorkingOrder()) {
			// if already there have an order, we allow to add any previous
			// order for farther processing without finish the current order
			addFromEan.setVisibility(View.INVISIBLE);
			addFromBasket.setVisibility(View.INVISIBLE);
		} else {
			addFromEan.setVisibility(View.VISIBLE);
			addFromBasket.setVisibility(View.VISIBLE);
			addFromEan
					.setBackgroundResource(R.drawable.left_curved_button_bg_selected);
			addFromBasket
					.setBackgroundResource(R.drawable.right_curved_button_bg);
		}
	}

	protected void addBasket() {
		if (basketLoadingTask != null) {
			basketLoadingTask.cancel(true);
		}
		basketLoadingTask = new BasketLoadingTask(this);
		basketLoadingTask.execute();
		backState = INITIAL_STATE;

	}

	public void startProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		progressBar.setVisibility(View.INVISIBLE);
	}

	public void executeFinal() {
		basketLoadingTask = null;
		stopProgress();
	}

	public void startDiscountProgress() {
		basketDiscountProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopDiscountProgress() {
		basketDiscountProgressBar.setVisibility(View.INVISIBLE);
	}

	public boolean onDown(MotionEvent arg0) {

		return false;
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// Check movement along the Y-axis. If it exceeds SWIPE_MAX_OFF_PATH,
		// then dismiss the swipe.
		if (Math.abs(e1.getY() - e2.getY()) > CommonTask.SWIPE_MAX_OFF_PATH())
			return false;
		// Swipe from right to left.
		// The swipe needs to exceed a certain distance (SWIPE_MIN_DISTANCE) and
		// a certain velocity (SWIPE_THRESHOLD_VELOCITY).
		if (e1.getX() - e2.getX() > CommonTask.SWIPE_MIN_DISTANCE()
				&& Math.abs(velocityX) > CommonTask.SWIPE_THRESHOLD_VELOCITY()) {
			// do stuff
			return true;
		}
		try {
			if (flipper.getDisplayedChild() > 0) {
				boolean isDetectable = true;
				if (lldiscount != null) {
					int[] pos = new int[2];
					lldiscount.getLocationInWindow(pos);
					int ypos = pos != null ? pos[1] - lldiscount.getHeight()
							/ 2 : 0;
					isDetectable = e1.getY() > 40 ? lldiscount.isShown() ? e1
							.getY() > ypos
							&& e1.getY() < ypos + lldiscount.getHeight() ? false
							: true
							: true
							: false;
				}
				// Swipe from left to right.
				// The swipe needs to exceed a certain distance
				// (SWIPE_MIN_DISTANCE) and a certain velocity
				// (SWIPE_THRESHOLD_VELOCITY).
				if (isDetectable
						&& e2.getX() - e1.getX() > CommonTask
								.SWIPE_MIN_DISTANCE()
						&& Math.abs(velocityX) > CommonTask
								.SWIPE_THRESHOLD_VELOCITY()) {
					flipper.setInAnimation(CommonTask.inFromLeftAnimation());
					flipper.setOutAnimation(CommonTask.outToRightAnimation());
					switch (backState) {
					case BACK_FROM_ENTER_EAN:// use for backfrom ProductEan
						cancelFromEnterProductEan();
						break;
					case BACK_FROM_BASKET_DETAIL:// use for backfrom
													// basketdetail
						backToBasket();
						break;
					default:
						break;
					}
					flipper.setInAnimation(CommonTask.inFromRightAnimation());
					flipper.setOutAnimation(CommonTask.outToLeftAnimation());
					return true;
				}
			}
		} catch (Exception oEx) {

		}

		return false;
	}

	public void onLongPress(MotionEvent e) {

	}

	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {

		return false;
	}

	public void onShowPress(MotionEvent e) {

	}

	public boolean onSingleTapUp(MotionEvent e) {

		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent me) {
		return gestureScanner.onTouchEvent(me);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (gestureScanner != null) {
			if (gestureScanner.onTouchEvent(ev))
				return true;
		}
		return super.dispatchTouchEvent(ev);
	}

	private void initializeCheckoutComponent() {
		checkoutBarcodeStatus = (TextView) findViewById(R.id.checkoutBarcodeStatus);
		cancelBasket = (Button) findViewById(R.id.cancelBasket);
		checkoutBarcodeImage = (ImageView) findViewById(R.id.checkoutBarcodeImage);
		checkoutBarcode = (TextView) findViewById(R.id.checkoutBarcode);
		cancelBasket.setEnabled(true);
		cancelBasket.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				CommonTask.showCancelBasketConfirmation(Basket.this,
						getString(R.string.CancelBasketmsg),
						ShowCancelBasketEvent());

			}
		});

		if (CommonBasketValues.getInstance().Basket != null
				&& CommonBasketValues.getInstance().Basket.OrderNo > 0) {
			checkoutBarcodeStatus.setText("Bon id: "
					+ CommonBasketValues.getInstance().Basket.OrderNo);
			checkoutBarcode.setVisibility(View.VISIBLE);
			checkoutBarcodeImage.setVisibility(View.VISIBLE);
			checkoutBarcode.setText("");
			if (CommonValues.beforeJB) {
				checkoutBarcodeImage.setBackgroundDrawable(null);
			} else {
				checkoutBarcodeImage.setBackgroundDrawable(null);
			}
			if (asyncCheckoutLoadingFromMore != null) {
				asyncCheckoutLoadingFromMore.cancel(true);

			}
			asyncCheckoutLoadingFromMore = new AsyncCheckoutLoadingFromMore(
					this);
			asyncCheckoutLoadingFromMore.execute();
		} else {
			checkoutBarcodeStatus
					.setText(getString(R.string.check_barcode_status));
			checkoutBarcode.setVisibility(View.INVISIBLE);
			checkoutBarcodeImage.setVisibility(View.INVISIBLE);
			checkoutBarcode.setText("");
			if (CommonValues.beforeJB) {
				checkoutBarcodeImage.setBackgroundDrawable(null);
			} else {
				checkoutBarcodeImage.setBackgroundDrawable(null);
			}
		}

	}

	// Code for refresh all screen after successful payment... Tanvir

	private void initBasketViewAfterCheckout() {
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		CommonTask.initCommonValuesForBasket();
		initializeSearchAndCatalogueFlipper();
		// Show basket main screen. Basket Itemlist screen.
		flipper.setDisplayedChild(FLIPPER_DISPLAY_BASKET_LIST_SRCEEN);
		initialize(Basket.this);
		backState = INITIAL_STATE;
		Home.showBasketMenu();
		if (etEnterCode != null)
			etEnterCode.setText("");
		if (etSMSNumber != null)
			etSMSNumber.setText("");
		if (cbTermsAndCondition != null)
			cbTermsAndCondition.setChecked(false);
		if (cbToAnotherAddress != null)
			cbToAnotherAddress.setChecked(false);
		if (cbToPay != null)
			cbToPay.setChecked(false);
		if (cbSmsOnDelivery != null)
			cbSmsOnDelivery.setChecked(false);
		if (tvDeliverydate != null) {
			Date d = new Date();
			SimpleDateFormat formatter = new SimpleDateFormat("E, dd MM yy",
					CommonConstraints.Locale);
			String s = formatter.format(d);
			tvDeliverydate.setText(s);
		}
		enableDisableCheckoutButton();
		setActionBarMenuVisibility(true);
	}

	public int getFlipperDisplayIndex() {
		return flipper.getDisplayedChild();
	}

	// Context Menu For Basket Selected Item Long Press... Tanvir
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	// Delete the selected basket Item On menuClick...
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		ViewGroup listItemLayout = (ViewGroup) info.targetView;
		// orderLine = (OrderLine) listItemLayout.getTag();

		orderLine = listAdaptor
				.getItemAtPosition(((BasketViewHolder) listItemLayout.getTag()).rowID);
		if (orderLine != null) {
			selectedItemQty = 0;
			orderLine.setQuantityAndHasChange(0);
			submitOrderAndUpdateBasketTitle();
			setBasketQtyToOther();
		}
		basketListView.setTag(null);
		return super.onContextItemSelected(item);
	}

	public void setBasketQtyToOther() {
		if (orderLine != null && Search.orderLine != null
				&& Search.orderLine.Id.equals(orderLine.Id)) {
			Search.etSearchItemQuantity.setText(Integer
					.toString(orderLine.quantity));
			if (orderLine.quantity == 0 && Search.flipper1 != null) {
				Search.flipper1.setDisplayedChild(0);
				Search.orderLine = orderLine;
			}
		} else if (orderLine != null && CatalogueBase.orderLine != null
				&& CatalogueBase.orderLine.Id.equals(orderLine.Id)) {
			CatalogueBase.etSearchItemQuantity.setText(Integer
					.toString(orderLine.quantity));
			if (orderLine.quantity == 0 && CatalogueBase.flipper1 != null) {
				CatalogueBase.flipper1.setDisplayedChild(0);
				CatalogueBase.orderLine = orderLine;
			}

		}
	}

	// code block for SMS checkbox validation...
	private boolean checkSmsValidation() {

		if (cbSmsOnDelivery.isChecked()
				&& etSMSNumber.getText().toString().trim().length() == 0) {

			etSMSNumber.setError(getString(R.string.smsnumber_error));
			etSMSNumber.requestFocus();
			return false;
		}
		return true;
	}

	// Validation For alternate Address...Tanvir
	private boolean checkAlternateAddressValidation() {
		if (etALtrntAddress1.length() == 0) {
			etALtrntAddress1.setError(getString(R.string.address_error));
			etALtrntAddress1.requestFocus();
			return false;
		} else if (etALtrntAddressPostnummer.length() == 0) {
			etALtrntAddressPostnummer
					.setError(getString(R.string.postnumber_error));
			etALtrntAddressPostnummer.requestFocus();
			return false;
		} else if (etALtrntAddressBy.length() == 0) {
			etALtrntAddressBy.setError(getString(R.string.cityname_error));
			etALtrntAddressBy.requestFocus();
			return false;
		}
		return true;
	}

	// Method for saving users alternate address.
	public boolean saveAlternetAddress() {
		if (CommonTask.isUserLoggedIn()) {
			imm.hideSoftInputFromWindow(etALtrntAddress1.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etALtrntAddress2.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(
					etALtrntAddressPostnummer.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(etALtrntAddressBy.getWindowToken(), 0);
			imm.hideSoftInputFromWindow(
					etALtrntAddressTelepon.getWindowToken(), 0);

			userInformation = new UserInformation();
			userInformation.saveUsersAlternateAddress(
					CommonValues.getInstance().loginuser.CustomerId,
					etALtrntAddress1.getText().toString().trim(),
					etALtrntAddress2.getText().toString().trim(),
					etALtrntAddressPostnummer.getText().toString().trim(),
					etALtrntAddressBy.getText().toString().trim(),
					etALtrntAddressTelepon.getText().toString().trim());
		}
		return true;
	}

	// set the user information after payment ...Tanvir
	public void setDataAfterDeliveryPayment() {

		flipper.setInAnimation(CommonTask.inFromRightAnimation());
		flipper.setOutAnimation(CommonTask.outToLeftAnimation());
		if (tvPaymentModeTotalValue != null)
			tvPaymentModeTotalValue
					.setText(CommonTask.getString(CommonBasketValues
							.getInstance().Basket.TotalPrice));
		if (tvPaymentModeSubtotalValue != null)
			tvPaymentModeSubtotalValue
					.setText(CommonTask.getString(CommonBasketValues
							.getInstance().Basket.TotalPrice));
		flipper.showNext();
		backState = BACK_FROM_ONLINE_PAYMENT_MODE;

	}

	/**
	 * Hide or show action bar menu items depending on visible parameter
	 * 
	 * @param visibility
	 */
	void setActionBarMenuVisibility(boolean visibility) {
		if (actionBarMenu != null) {
			int size = actionBarMenu.size();
			for (int i = 0; i < size; i++) {
				actionBarMenu.getItem(i).setVisible(visibility);
			}
		}
	}

	// method used at the time of loading DIBS window... Tanvir
	private PaymentData constructPaymentData() {

		mSearchView.setQuery("", false);
		mSearchView.setIconified(true);
		setActionBarMenuVisibility(false);

		String merchantId = getString(R.string.merchant_id);
		String currencyCode = getString(R.string.currency_code);
		String yourOrderId = getString(R.string.order_id);
		long amount = (long) (CommonBasketValues.getInstance().Basket.TotalPrice * 100);
		List<String> payTypes = new ArrayList<String>();
		payTypes.add(getString(R.string.dk));
		payTypes.add(getString(R.string.visa));
		payTypes.add(getString(R.string.v_dk));
		boolean calcfee = true;
		PurchasePaymentData paymentData = new PurchasePaymentData(merchantId,
				currencyCode, yourOrderId, amount, payTypes);
		paymentData.setCalcfee(calcfee);
		paymentData.setTest(true);
		paymentData.setUseUniqueOrderIdCheck(false);
		paymentData.setLanguage("da_DK");
		paymentData.setTheme(Theme.CUSTOM);
		String ss = "{\"appBgColor\":\"#253F62\",\"paybuttonBgColor\":\"#ffffff\",\"paybuttonFontColor\":\"#3f6618\"}";

		paymentData.setCustomThemeCSS(ss);

		return paymentData;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu actionBarMenu) {
		return super.onPrepareOptionsMenu(actionBarMenu);
	}

	private void initializeSearchAndCatalogueFlipper() {
		if (Search.flipper1 != null && Search.flipper1.getDisplayedChild() > 0) {
			// Show neworderscreen on the searcharticledetailpage
			Search.flipper1.setDisplayedChild(0);
			// Show familyscreen onthe searchpage
			Search.flipper.setDisplayedChild(0);
			Search.backState = StateSearch.INITIAL_STATE;
			Search.orderLine = null;
		}
		if (Catalogue.flipper1 != null
				&& Catalogue.flipper1.getDisplayedChild() > 0) {
			Catalogue.flipper1.setDisplayedChild(0);
			Catalogue.orderLine = null;
		}
	}

	// Enable the betal button after checking terms and conditions
	// Disable the SmsNummer Field If Not Checked ved Levering... Tanvir
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		switch (buttonView.getId()) {
		case R.id.cbTermsAndCondition:
			if (isChecked) {
				bDibsPayment.setEnabled(true);
				bDibsPayment.setTextColor(getResources().getColor(
						R.color.textColor));

			} else {
				bDibsPayment.setEnabled(false);
				bDibsPayment.setTextColor(Color.GRAY);

			}
			break;
		case R.id.cbSmsOnDelivery:
			if (isChecked) {
				rlSMSNumber.setVisibility(View.VISIBLE);
				etSMSNumber.requestFocus();
				etSMSNumber.setEnabled(true);
				if (CommonValues.getInstance().loginuser != null)
					etSMSNumber
							.setText(CommonValues.getInstance().loginuser.Telephone);
			} else {
				rlSMSNumber.setVisibility(View.GONE);
				etSMSNumber = (EditText) findViewById(R.id.etSMSNumber);
				etSMSNumber.setText("");
				etSMSNumber.setEnabled(false);
			}
			break;

		case R.id.cbToPay:
			if (cbToPay.isChecked()) {

				cbToPay.setChecked(true);
				cbToAnotherAddress.setChecked(false);
			} else {
			}
			break;
		case R.id.cbToAnotherAddress:
			if (cbToAnotherAddress.isChecked()) {

				cbToAnotherAddress.setChecked(true);
				cbToPay.setChecked(false);
				rlToALtrntAddress.setVisibility(View.VISIBLE);
			}

			else {

				resetAlternateAddressFieldValues();
			}

			break;

		default:
			break;
		}

	}

	/**
	 * 
	 */
	public void resetAlternateAddressFieldValues() {
		etALtrntAddress1.setText("");
		etALtrntAddress2.setText("");
		etALtrntAddressPostnummer.setText("");
		etALtrntAddressBy.setText("");
		etALtrntAddressTelepon.setText("");
		rlToALtrntAddress.setVisibility(View.GONE);
	}

	// set current date into textview... Tanvir
	public void setCurrentDateOnView() {

		final Calendar cal = Calendar.getInstance();
		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);

		Calendar todaysDate = Calendar.getInstance(CommonConstraints.Locale);
		String formattedTodaysDate = DateFormat.getDateInstance(
				CommonConstraints.DateFormat).format(todaysDate.getTime());
		tvDeliverydate.setText(formattedTodaysDate);
	}

	// Display toast message if date before current date is choosen... Tanvir
	private void WrongDateSelection() {

		Toast.makeText(this, getString(R.string.dateselectionError),
				Toast.LENGTH_SHORT).show();

	}

	// datepickerlistener for handling datechangeevent in calender..tanvir
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		// when dialog box is closed, below method will be called.... Tanvir
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			Calendar selectedDate = Calendar
					.getInstance(CommonConstraints.Locale);
			// Now the Calendar represents the selected day
			selectedDate.set(selectedYear, selectedMonth, selectedDay);
			// Expect new instance to be initialised with current time and date
			Calendar todaysDate = Calendar
					.getInstance(CommonConstraints.Locale);
			todaysDate.set(Calendar.HOUR_OF_DAY, 0);
			todaysDate.set(Calendar.MINUTE, 0);
			todaysDate.set(Calendar.SECOND, 0);
			if (selectedDate.before(todaysDate)) {
				// If we select a date before today, it is an error.
				// Display Error message
				WrongDateSelection();
			} else {
				// Format using currentlocalization,set the selecteddate in
				// textview.
				String formattedDate = DateFormat.getDateInstance(
						CommonConstraints.DateFormat).format(
						selectedDate.getTime());
				tvDeliverydate.setText(formattedDate);
			}
		}
	};

	public DialogInterface.OnClickListener ShowCancelBasketEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					initBasketViewAfterCheckout();
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					break;
				}
			}
		};
		return dialogClickListener;

	}
}
