package com.shopper.app.activities;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import com.google.zxing.client.android.CaptureActivity;
import com.shopper.app.R;
import com.shopper.app.base.MainActionbarBase;
import com.shopper.app.entities.AsyncCheckServerStateAndSaveServerInfo;
import com.shopper.app.entities.UserInformation;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * This are call from splash screen after finishing the shown of splash. This
 * class is use for managing all action related with home activity. In here also
 * set the server baseurl & shop number from where all data are come. For doing
 * this setting needs to long pressing on the home image and select the setting
 * context menu item. In every time from other activity return to home. Home
 * also use for exit the application by pressing back button. Here use
 * AsyncLogInSettingsCheck, AsyncSaveUserInfoHome class for loading & saving
 * user information Asynchronously One static variable backState are use for
 * controlling the back button press state
 * 
 * @author jib
 * 
 */
@TargetApi(13)
public class Home extends MainActionbarBase implements OnFocusChangeListener,
		OnClickListener {

	private static final int INITIAL_STATE = -1;

	/**
	 * backState use for manage back button
	 */
	public static int backState;

	/**
	 * main ViewFlipper use for home screens
	 */
	public static ViewFlipper flipper;

	private ImageView imageView;

	private static Button bSettingCancel, bSettingSave;

	private static EditText etSettingShopNumber;//, etSettingPassword;
	static AutoCompleteTextView etSettingAddress;
	InputMethodManager imm;
	public ProgressBar progressBar, menuProgressBar;

	// baseUrlList use for setting screen autocomplete main server url list
	static ArrayList<String> baseUrlList = null;
	private ArrayAdapter<String> baseUrlAdapter = null;

	// use for save info from setting screen Asynchronously
	AsyncCheckServerStateAndSaveServerInfo asyncSaveServerInfo = null;
	
	public static Context context;

	/**
	 * initialize all controls variables & load pre-saved user information &
	 * also load login user information Asynchronously this fuction
	 * automatically call from
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Home.context = this;
		setContentView(R.layout.main);
		mSupportActionBar.setDisplayHomeAsUpEnabled(false);
		backState = INITIAL_STATE;
		initialization();
		CommonTask.loadSettings(this);
		CommonTask.loadLoginUser(this);
		//processExtraData();

	}

	// initialize all controls variables
	private void initialization() {
		flipper = (ViewFlipper) findViewById(R.id.vfHome);
		flipper.setDisplayedChild(0);
		imageView = (ImageView) findViewById(R.id.ivMainScreen);
		registerForContextMenu(imageView);
		progressBar = (ProgressBar) findViewById(R.id.pbMoreProgressBar);
		menuProgressBar = (ProgressBar) findViewById(R.id.menuProgressBar);
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		bSettingCancel = (Button) findViewById(R.id.bSettingCancel);
		bSettingSave = (Button) findViewById(R.id.bSettingSave);
		etSettingShopNumber = (EditText) findViewById(R.id.etSettingShopNumber);
		etSettingAddress = (AutoCompleteTextView) findViewById(R.id.etSettingAddress);
		etSettingAddress.requestFocus();
		etSettingAddress.setSelection(etSettingAddress.getText().length());
		etSettingShopNumber.setOnFocusChangeListener(this);
		etSettingAddress.setOnFocusChangeListener(this);
		bSettingCancel.setOnClickListener(this);
		bSettingSave.setOnClickListener(this);

	}
	
	void cancelFromSettingsScreen(){
		LoadPreferences();
		toggleTopButtonView(true);
		backToHomeScreen();
		imm.hideSoftInputFromWindow(bSettingCancel.getWindowToken(), 0);
	}

	// use for manage all click events of buttons centrally
	public void onClick(View v) {
		etSettingShopNumber.clearFocus();
		etSettingAddress.clearFocus();

		switch (v.getId()) {
		case R.id.bSettingCancel:
			cancelFromSettingsScreen();
			break;

		case R.id.bSettingSave:	
			//MRB: we can check if the current data is same as previous data and just get back if it is same.
			if( ( !etSettingAddress.getText().toString().isEmpty() && CommonTask.getBaseUrl(this).equals(etSettingAddress.getText().toString()) &&
					!etSettingShopNumber.getText().toString().isEmpty() && CommonTask.getShopNumber(this).equals(etSettingShopNumber.getText().toString()))){
				cancelFromSettingsScreen();
				return;
			}
			//data changes than previous so lets save it.
			imm.hideSoftInputFromWindow(bSettingSave.getWindowToken(), 0);
			backState = INITIAL_STATE;
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(getString(R.string.beforesaveUrlmsg))
					.setCancelable(false)
					.setPositiveButton(getString(R.string.button_yes),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									saveServerInfo();
								}

							})
					.setNegativeButton(getString(R.string.button_no),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();

			break;

		}
	}

	/**
	 * Save Server URL and SHopNumber through asyncSaveUserInfoHome call
	 */
	public void saveServerInfo() {
		if (asyncSaveServerInfo != null) {
			asyncSaveServerInfo.cancel(true);
		}
		asyncSaveServerInfo = new AsyncCheckServerStateAndSaveServerInfo(Home.this);
		asyncSaveServerInfo.execute();
	}

	/**
	 * Manage back-button pressed event in anywhere from home screen
	 * backState=-1 is use for application exit and 0 is use for back from
	 * setting screen to home
	 */
	@Override
	public void onBackPressed() {
		if (backState == INITIAL_STATE)
			CommonTask.CloseApplication(this);
		else if (backState == 0) {
			backToHomeScreen();
		} else {
			backState = INITIAL_STATE;
			CommonTask.CloseApplication(this);
		}
	}

	private void backToHomeScreen() {
		flipper.setInAnimation(CommonTask.inFromLeftAnimation());
		flipper.setOutAnimation(CommonTask.outToRightAnimation());
		backState = INITIAL_STATE;
		mSupportActionBar.setDisplayHomeAsUpEnabled(false);
		mSupportActionBar.setHomeButtonEnabled(false);
		flipper.setDisplayedChild(0);
	}

	@Override
	protected void onDestroy() {
	}

	@Override
	protected void onResume() {
		super.onResume();
		setBaseUrlsData();
		showBasketMenu();

	}


	/**
	 * use for set the basket title here use 5 different basketTotal textview
	 * for controlling this tible use 5 basketTotal because of each time of
	 * menuitem pressed a new basketTotal are initialize
	 */
	public static void showBasketMenu() {

		String basketTitle = CommonTask.getString(CommonBasketValues
				.getInstance().Basket.TotalPrice) + " DKK";

		if (basketTotal != null && basketCount != null) {
			basketTotal.setText(basketTitle);
			basketTotal.setSelected(true);
		}
		if (basketTotal1 != null && basketCount1 != null) {
			basketTotal1.setText(basketTitle);
			basketTotal1.setSelected(true);
		}
		if (basketTotal2 != null && basketCount2 != null) {
			basketTotal2.setText(basketTitle);
			basketTotal2.setSelected(true);
		}
		if (basketTotal3 != null && basketCount3 != null) {
			basketTotal3.setText(basketTitle);
			basketTotal3.setSelected(true);
		}
		if (basketTotal4 != null && basketCount4 != null) {
			basketTotal4.setText(basketTitle);
			basketTotal4.setSelected(true);
		}
		if (basketTotal5 != null && basketCount5 != null) {
			basketTotal5.setText(basketTitle);
			basketTotal5.setSelected(true);
		}
		if (basketTotal6 != null && basketCount6 != null) {
			basketTotal6.setText(basketTitle);
			basketTotal6.setSelected(true);
		}
		showBasketCounter(CommonBasketValues.getInstance().Basket
				.getTotalItemCount());
	}

	public static void showBasketCounter(int count) {

//		String displayCountText = String.valueOf(count);
		String displayCountText = getBasketItemCountDisplayText(count);
		if (basketCount != null) {
			if (count > 0) {
				basketCount.setVisibility(View.VISIBLE);
				basketCount.setText(displayCountText);
			} else
				basketCount.setVisibility(View.INVISIBLE);
		}
		if (basketCount1 != null) {
			if (count > 0) {
				basketCount1.setVisibility(View.VISIBLE);
				basketCount1.setText(displayCountText);
			} else
				basketCount1.setVisibility(View.INVISIBLE);
		}
		if (basketCount2 != null) {
			if (count > 0) {
				basketCount2.setVisibility(View.VISIBLE);
				basketCount2.setText(displayCountText);
			} else
				basketCount2.setVisibility(View.INVISIBLE);
		}
		if (basketCount3 != null) {
			if (count > 0) {
				basketCount3.setVisibility(View.VISIBLE);
				basketCount3.setText(displayCountText);
			} else
				basketCount3.setVisibility(View.INVISIBLE);
		}
		if (basketCount4 != null) {
			if (count > 0) {
				basketCount4.setVisibility(View.VISIBLE);
				basketCount4.setText(displayCountText);
			} else
				basketCount4.setVisibility(View.INVISIBLE);
		}
		if (basketCount5 != null) {
			if (count > 0) {
				basketCount5.setVisibility(View.VISIBLE);
				basketCount5.setText(displayCountText);
			} else
				basketCount5.setVisibility(View.INVISIBLE);
		}
		if (basketCount6 != null) {
			if (count > 0) {
				basketCount6.setVisibility(View.VISIBLE);
				basketCount6.setText(displayCountText);
			} else
				basketCount6.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Creating Context Menu for Home Screen by home image pressed. Use to enter
	 * into the setting screen (non-Javadoc)
	 * 
	 * @see android.app.Activity#onCreateContextMenu(android.view.ContextMenu,
	 *      android.view.View, android.view.ContextMenu.ContextMenuInfo)
	 */
	@Override
	public void onCreateContextMenu(ContextMenu cmenu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		   cmenu.add(getString(R.string.settings));

	}

	/**
	 * After selected the setting menu, setting screen will be come on flipper
	 * position 2 on and also set backState to 0
	 */
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		if (item.getTitle() == getString(R.string.settings)) {
			showServerSettingsScreen();

		}
		return true;
	}

	/**
	 * Show the settings screen to configure server information..TAC
	 */
	public static void showServerSettingsScreen() {
		if (CommonTask.isNetworkAvailable(Home.context)) {
			if(flipper.getDisplayedChild() !=2 ){
				flipper.setInAnimation(CommonTask.inFromRightAnimation());
				flipper.setOutAnimation(CommonTask.outToLeftAnimation());
				backState = 0;
				mSupportActionBar.setDisplayHomeAsUpEnabled(true);
				flipper.setDisplayedChild(2);
				toggleTopButtonView(true);
				LoadPreferences();
			}
		} else {
			CommonTask.ShowMessage(Home.context,
					Home.context.getString(R.string.networkError));
		}
	}

	// Context Menu code Ends
	public static void toggleTopButtonView(boolean isDefaultView) {
		if (isDefaultView) {
			bSettingCancel.setVisibility(View.INVISIBLE);
			bSettingSave.setVisibility(View.INVISIBLE);

		} else {
			bSettingCancel.setVisibility(View.VISIBLE);
			bSettingSave.setVisibility(View.VISIBLE);

		}
	}

	/**
	 * Use for loading the default baseurl & shop number from SharedPreferences
	 * which is already saved before from setting screen
	 */
	public static void LoadPreferences() {
		etSettingAddress.setText(CommonTask.getBaseUrl(Home.context));
		etSettingShopNumber.setText(CommonTask.getShopNumber(Home.context));
	}

	public void startmenuProgress() {
		menuProgressBar.setVisibility(View.VISIBLE);
	}

	public void stopmenuProgress() {
		menuProgressBar.setVisibility(View.INVISIBLE);
	}

	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {
		case R.id.etSettingShopNumber:
		case R.id.etSettingAddress: {
			toggleTopButtonView(false);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * save settings screen info ie->ServerURL and ShopNumber in shared
	 * preferences
	 */

	public void saveServerUrlandShopNumber() {
		CommonTask.SavePreferences(this,
				CommonConstraints.PREF_SETTINGS_NAME, CommonConstraints.PREF_URL_KEY, etSettingAddress.getText().toString());

		if (baseUrlList.indexOf(etSettingAddress.getText().toString()) < 0) {
			baseUrlList.add(etSettingAddress.getText().toString());
			CommonTask.setApplicationUrls(this, baseUrlList);

		}
		CommonTask.SavePreferences(this,
				CommonConstraints.PREF_SETTINGS_NAME,
				CommonConstraints.PREF_SHOPNUMBER_KEY, etSettingShopNumber
						.getText().toString());
	}

	/**
	 * After save user information with a confirmation, system will restart
	 * automatically
	 */
	public void setUserInfoAfterSave() {
		CommonTask.loadSettings(this);
		CommonValues.getInstance().loginuser = new UserInformation();
		CaptureActivity.resetScanningValuesAfterSettingChanged();
		Basket.resetBasketAfterSettingChanged();
		Search.resetSearchAfterSettingChanged();
		More.resetMoreAfterSettingChanged();
		showBasketMenu();				
		backToHomeScreen();
		
		/*Intent i = getBaseContext().getPackageManager()
				.getLaunchIntentForPackage(getBaseContext().getPackageName());
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		AlarmManager mgr = (AlarmManager) getBaseContext().getSystemService(
				Context.ALARM_SERVICE);
		mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000,
				PendingIntent.getActivity(getBaseContext(), 0, i, i.getFlags()));
		System.exit(1);*/
	}

	// the below method stores the setting screen URLs in baseUrlAdpter
	// for showing as suggestion in Autocompletetextview

	private void setBaseUrlsData() {
		baseUrlList = CommonTask.getApplicationUrls(this);
		if (baseUrlList == null) {
			baseUrlList = new ArrayList<String>();
		}

		baseUrlAdapter = new ArrayAdapter<String>(this,
				R.layout.dropdown_item_view, R.id.tvDropdownItemText,
				baseUrlList);

		etSettingAddress.setAdapter(baseUrlAdapter);
		etSettingAddress.setThreshold(1);
	}

	/**
	 * Methos that check whether the application is connected to server or not
	 */

	public boolean isConnectedToServer() {
		return isValidServer(etSettingAddress.getText().toString(),
				etSettingShopNumber.getText().toString());
	}
	
	public static String getBasketItemCountDisplayText(int count){
		if(count <=0){
			return "";
		} else if(count<=99) {
			return String.valueOf(count);
		} else if(count>99)
			return "99+";		
		return "";
	}
	
	/*protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		// muststore the newintent unless getIntent() return the oldone
		setIntent(intent);
		processExtraData();
	};
	
	private void processExtraData() {
		Intent intent = getIntent();
		Boolean showSettings = intent.getBooleanExtra("showsettings", false);
		if (showSettings) {
			showServerSettingsScreen();
			getIntent().removeExtra("showsettings");
		}
		
	}*/
}