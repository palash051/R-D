package com.shopper.app.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.shopper.app.R;
import com.shopper.app.base.LogInFunctionsBase;
import com.shopper.app.entities.AsyncLoadUserInfoByCustomerId;
import com.shopper.app.entities.AsyncSaveUserInformation;
import com.shopper.app.entities.UserInformation;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * This class call from menu after selecting "Bruger" Also call from basket on
 * the online payment mode if there are no user login in the system other wise
 * payment procedure will be continue from basket if user already login. No need
 * to create this class again Basically use for manage user information User can
 * login, add new user and update old user information If user put wrong user
 * and password on login screen there will be a confirmation alert for new user
 * creation Server related all task like loading & saving user info done by
 * Asynchronously using AsyncSaveUserInformation & AsyncSaveUserInfo class After
 * pressing back button with backState = -1 always gone to home screen otherwise
 * came back to the previous screen and If created from basket then redirect to
 * basket again
 * 
 * @author jib
 * 
 */
@TargetApi(14)
public class More extends LogInFunctionsBase implements OnClickListener {

	private static final int BACK_FROM_USER_DETAIL_SCREEN = 1;
	private static final int BACK_FROM_LOGIN = 0;
	private static final int SHOW_USER_DETAIL_SCREEN = 1;
	private static final int SHOW_LOGIN_SCREEN = 0;
	private static final int INITIAL_STATE = -1;
	public static ViewFlipper flipper;	
	private Button bUpatepro;	
	private ProgressBar progressBar;		
	private InputMethodManager imm;
	private AsyncSaveUserInformation asyncSaveUserInformation = null;	

	/**
	 * Automatically call once when class created initialize all control
	 * variable load pre-saved user data
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.more);
		initBase();
		flipper = (ViewFlipper) findViewById(R.id.vfMore);
		progressBar = (ProgressBar) findViewById(R.id.pbMoreProgressBar);
		
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);		
		bNextFromUser.setOnClickListener(this);
		init();

		bUpatepro = (Button) findViewById(R.id.bUpatepro);

		bUpatepro.setOnClickListener(this);
		loggedInView();
	}
	
	public void manuReselect() {

		if (flipper.getDisplayedChild() > 0) {
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			flipper.setDisplayedChild(SHOW_LOGIN_SCREEN);// Show login screen
			bUpatepro.setVisibility(View.VISIBLE);

		}
	}

	private void init() {
		flipper.setDisplayedChild(SHOW_LOGIN_SCREEN);
		prepareLoginView();
		backStateMore = INITIAL_STATE;
	}
	/**
	 * Reset to initial stage more screen after setting changed
	 */
	public static void resetMoreAfterSettingChanged(){
		if (flipper !=null && flipper.getDisplayedChild() > 0) {
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			flipper.setDisplayedChild(SHOW_LOGIN_SCREEN);
		}		
		backStateMore = INITIAL_STATE;
	}

	/**
	 * After pressing back button with backState = -1 always gone to home screen
	 * otherwise came back to the previous screen if login screen appear from
	 * basket then go back to basket screen and continue checkout procedure
	 */
	@Override
	public void onBackPressed() {
		switch (backStateMore) {
		case INITIAL_STATE:
			currentMenuIndex = getLastIndex();
			if(CommonValues.getInstance().IsCallFromBasket){
				Basket.backState=Basket.BACK_FROM_CHECKOUT_BASKET;
				startActivity(basket);				
			}
			else
				manageActivity();
			break;
		case BACK_FROM_LOGIN:
		case BACK_FROM_USER_DETAIL_SCREEN:		
			flipper.setInAnimation(CommonTask.inFromLeftAnimation());
			flipper.setOutAnimation(CommonTask.outToRightAnimation());
			backStateMore = INITIAL_STATE;
			flipper.setDisplayedChild(SHOW_LOGIN_SCREEN);
			break;	
		}
	}

	/**
	 * Asynchronously load user info data by using AsyncLoadUserInfoByCustomerId
	 * class
	 */
	@Override
	public void getUserDetailInfo() {
		try {
			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			CommonTask.resetPreferenceValue(this);
			loadUserInfoByCusId();
			backStateMore = BACK_FROM_LOGIN;

		} catch (Exception e) {

		}
	}

	/**
	 * make call to AsyncLoadUserInfoByCustomerId class
	 * for loading user info Asynchronously
	 */
	public void loadUserInfoByCusId() {
		if (asyncLoadUserInfoByCustomerId != null) {
			asyncLoadUserInfoByCustomerId.cancel(true);
		}
		asyncLoadUserInfoByCustomerId = new AsyncLoadUserInfoByCustomerId(
				this);
		asyncLoadUserInfoByCustomerId.execute();
	}

	/**
	 * Set user information to relative controls
	 */
	public void setUserInformation() {
		if (CommonTask.isUserLoggedIn() && userInformation != null
				&& userInformation.CustomerId != null
				&& userInformation.CustomerId != "") {
			flipper.setDisplayedChild(SHOW_USER_DETAIL_SCREEN);
			backStateMore = BACK_FROM_USER_DETAIL_SCREEN;
			etFirstName.setText(userInformation.FirstName);
			if (tvOnlinePaymentHeadingText != null)
				tvOnlinePaymentHeadingText.setText(getString(R.string.update_profile) +  "("+userInformation.FirstName+")");
			etZipCode.setText(userInformation.ZipCode);
			etTelephone.setText(userInformation.Telephone);
			etUserId.setText(userInformation.Email);
			etAddress1.setText(userInformation.Address1);
			etAddress2.setText(userInformation.Address2);
			etCity.setText(userInformation.City);
			etUserPass.setText(userInformation.UserPassword);
			etUserPassConfirm.setText(userInformation.UserPassword);

		} 
	}

	/**
	 * Call if login fail
	 */
	public void setUserInfoAftrLoginFailure() {
		if (userInformation != null && userInformation.CustomerId != null
				&& userInformation.CustomerId != "") {
			flipper.setDisplayedChild(SHOW_USER_DETAIL_SCREEN);
			backStateMore = BACK_FROM_USER_DETAIL_SCREEN;

		} else {
			etUserId.setText(etUserName.getText().toString());
			etUserPass.setText(etUserPassword.getText().toString());
		}
	}

	public void onClick(View v) {
		

		switch (v.getId()) {		
		case R.id.bNextFromUser:

			if (checkSaveValidation()) {
				saveUsersDetailInformation();

				imm.hideSoftInputFromWindow(etUserId.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etUserPass.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etUserPassConfirm.getWindowToken(),
						0);
				imm.hideSoftInputFromWindow(etFirstName.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etLastName.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etAddress1.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etAddress2.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etZipCode.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etCity.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(etTelephone.getWindowToken(), 0);
			}
			break;
		case R.id.bUpatepro:
			flipper.setInAnimation(CommonTask.inFromRightAnimation());
			flipper.setOutAnimation(CommonTask.outToLeftAnimation());
			hideLoginUserPassControl();
			userInformation = CommonValues.getInstance().loginuser;
			setUserInformation();
			backStateMore = BACK_FROM_USER_DETAIL_SCREEN;
			break;

		}
	}

	/**
	 * Call the Async class asyncSaveUserInformation for saving user details information
	 */
	public void saveUsersDetailInformation() {
		if (asyncSaveUserInformation != null) {
			asyncSaveUserInformation.cancel(true);
		}
		asyncSaveUserInformation = new AsyncSaveUserInformation(this);
		asyncSaveUserInformation.execute();
	}

	/**
	 * User for creating new user
	 */
	public void showNewUser() {

		flipper.setInAnimation(CommonTask.inFromRightAnimation());
		flipper.setOutAnimation(CommonTask.outToLeftAnimation());
		showLoginUserPassControl();
		userInformation = CommonValues.getInstance().loginuser;
		setUserInfoAftrLoginFailure();

		flipper.showNext();
		backStateMore = BACK_FROM_USER_DETAIL_SCREEN;
		imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(etUserPassword.getWindowToken(), 0);

	}

	// log in confirmation message..Tanvir
	public boolean ShowLogInCnfMsg() {
		if (CommonTask.isUserLoggedIn()) {

			if (CommonValues.getInstance().IsCallFromBasket) {
				CommonTask.showCheckoutConfirmation(this, getString(R.string.loginsuccessmsg),
						ShowCheckoutEvent());

				return false;
			} else if (!CommonValues.getInstance().IsCallFromBasket) {

				CommonTask.showCheckoutConfirmation(this, getString(R.string.loginsuccessmsg),
						ShowLogInEvent());

			}
		}

		else if (CommonValues.getInstance().loginuser.Email.toString() != (etUserName
				.getText().toString())
				&& CommonValues.getInstance().loginuser.UserPassword.toString() != (etUserPassword
						.getText().toString())) {

			CommonTask.showLogInConfirmation(this,
					getString(R.string.loginerrorsmsg),
					ShowUserEvent());
			return false;
		}
		return true;
	}

	// Event In case of wrong Username/Password....Tanvir
	public DialogInterface.OnClickListener ShowUserEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					showNewUser();

					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					//etUserName.setText("");
					//etUserPassword.setText("");
					backStateMore = INITIAL_STATE;
					break;
				}
			}
		};
		return dialogClickListener;

	}

	// Event In case of Successful Log in While Checkout..Tanvir
	public DialogInterface.OnClickListener ShowCheckoutEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					if (CommonValues.getInstance().IsCallFromBasket) {
						CommonValues.getInstance().IsCallFromBasket = false;
						Basket.flipper.setDisplayedChild(4);
						//More.this.finish();
						currentMenuIndex = BASKET_ACTIVITY;
						startActivity(basket);
					}
					break;

				}
			}
		};
		return dialogClickListener;

	}

	// Event In case of Log in without any checkout functionality....Tanvir
	public DialogInterface.OnClickListener ShowLogInEvent() {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					bUpatepro.setVisibility(View.VISIBLE);
					goHome(More.this);
				}
			}
		};
		return dialogClickListener;

	}
	
	

	private void showLoginUserPassControl() {
		if (!CommonTask.isUserLoggedIn()) {
			requiredFieldsmakeVisible();
		} else {
			requiredFieldsmakeInvisible();
		}
		resetRequiredEdittextFields();
	}


	/**
	 * 
	 */
	public void resetRequiredEdittextFields() {
		etFirstName.setText("");
		etZipCode.setText("");
		etTelephone.setText("");
		etUserId.setText("");
		etAddress1.setText("");
		etAddress2.setText("");
		etCity.setText("");
		etUserPass.setText("");
		etUserPassConfirm.setText("");
	}


	/**
	 * 
	 */
	public void requiredFieldsmakeInvisible() {
		tvUserInfoName.setVisibility(TextView.GONE);
		tvUserInfoMendatory.setVisibility(TextView.GONE);
		etUserId.setVisibility(EditText.GONE);
		tvUserPass.setVisibility(TextView.GONE);
		tvUserPassMendatory.setVisibility(TextView.GONE);
		etUserPass.setVisibility(EditText.GONE);
		tvUserPassConfirm.setVisibility(TextView.GONE);
		tvUserPassConfirmMendatory.setVisibility(TextView.GONE);
		etUserPassConfirm.setVisibility(EditText.GONE);
	}


	/**
	 * 
	 */
	public void requiredFieldsmakeVisible() {
		tvUserInfoName.setVisibility(TextView.VISIBLE);
		tvUserInfoMendatory.setVisibility(TextView.VISIBLE);
		etUserId.setVisibility(EditText.VISIBLE);
		tvUserPass.setVisibility(TextView.VISIBLE);
		tvUserPassMendatory.setVisibility(TextView.VISIBLE);
		etUserPass.setVisibility(EditText.VISIBLE);
		tvUserPassConfirm.setVisibility(TextView.VISIBLE);
		tvUserPassConfirmMendatory.setVisibility(TextView.VISIBLE);
		etUserPassConfirm.setVisibility(EditText.VISIBLE);
	}
	

	// Method for saving new user Information...Tanvir

	public boolean saveNewUser() {
		userInformation = new UserInformation();
		userInformation.FirstName = etFirstName.getText().toString().trim();
		userInformation.UserPassword = etUserPass.getText().toString().trim();
		userInformation.Email = etUserId.getText().toString().trim();
		userInformation.Telephone = etTelephone.getText().toString().trim();
		userInformation.ZipCode = etZipCode.getText().toString().trim();
		userInformation.Address1 = etAddress1.getText().toString().trim();
		userInformation.Address2 = etAddress2.getText().toString().trim();
		userInformation.City = etCity.getText().toString().trim();
		userInformation.saveNewUserInformation();
		
		return true;
	}

	/**
	 * check validation for save user information
	 * 
	 * @return
	 */
	private boolean checkSaveValidation() {
		if (etUserId.length() == 0) {
			etUserId.setError(getString(R.string.usernameerror));
			return false;
		} else if (!CommonTask.checkEmail(etUserId.getText().toString())) {
			etUserId.setError(getString(R.string.emailerror));
			return false;
		} else if (etUserPass.getText().toString().length() == 0) {
			etUserPass.setError(getString(R.string.passworderror));
			return false;
		} else if (etUserPass.getText().toString().length() < 6) {
			etUserPass.setError(getString(R.string.passwordlengtherror));
			return false;
		} else if (!etUserPass.getText().toString().trim()
				.equals(etUserPassConfirm.getText().toString().trim())) {
			etUserPassConfirm.setError(getString(R.string.passwordcnfrmerror));
			return false;
		} else if (etFirstName.length() == 0) {
			etFirstName.setError(getString(R.string.firstnameerror));
			return false;
		} else if (etAddress1.length() == 0) {
			etAddress1.setError(getString(R.string.addresserror));
			return false;

		} else if (etZipCode.length() == 0) {
			etZipCode.setError(getString(R.string.postnumerror));
			return false;
		} else if (etCity.length() == 0) {
			etCity.setError(getString(R.string.citynameerror));
			return false;
		} else if (etTelephone.length() == 0) {
			etTelephone.setError(getString(R.string.telnumerror));
			return false;
		}
		return true;
	}

	public void startProgress() {
		progressBar.setVisibility(View.VISIBLE);
	}

	public void stopProgress() {
		progressBar.setVisibility(View.INVISIBLE);
	}

}
