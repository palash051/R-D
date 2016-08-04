package com.shopper.app.base;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;
import com.shopper.app.entities.AsyncLoadUserInfoByCustomerId;
import com.shopper.app.entities.UserInformation;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonValues;

/**
 * @author Tanvir Ahmed Chowdhury used for initializing log in information and
 *         user details information handles the login button click
 *         event,validate log in screen input and display error message if left
 *         blank set the user information for already logged in user handles the
 *         log out event in background when necessary
 */

public class LogInFunctionsBase extends MainActionbarBase {

	// LogIn Event handlers.
	private static List<LogInEventHandler> handlers = new ArrayList<LogInEventHandler>();

	public static void addLogInHandler(LogInEventHandler handler) {
		handlers.add(handler);
	}

	public static int backStateMore;
	public TextView tvOnlinePaymentLoginHeadingText,
			tvPaymentModeTotalValue, tvPaymentModeSubtotalValue,
			tvOnlinePaymentDeleverHeadingText, tvOnlinePaymentHeadingText,
			tvOnlinePaymentModeHeadingText, tvUserPass, tvUserPassConfirm,
			tvUserInfoName,tvUserInfoMendatory,tvUserPassMendatory,tvUserPassConfirmMendatory;

	public EditText etUserName, etUserPassword, etFirstName, etLastName,
			etRoadName, etHouseNo, etFloor, etZipCode, etDoorCode, etTelephone,
			etMobile, etUserId, etUserPass, etUserPassConfirm, etAddress1,
			etAddress2, etCity;

	public LinearLayout loggedIn;
	public RelativeLayout rlUserName, rlUserPass, rlUserPassConfirm,
			rlBottombar;

	public Button bForgottenPassword, bCreateNew, bUserLogin, bDibsPayment,
			bNextFromUser, tvCreateNew, bUpatepro;

	public UserInformation userInformation;

	public AsyncLoadUserInfoByCustomerId asyncLoadUserInfoByCustomerId;

	public InputMethodManager imm;
	private ProgressBar loginprogressBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSupportActionBar.setDisplayHomeAsUpEnabled(true);
	}

	public void getUserDetailInfo() {
	}

	// Load the user information
	public void loadUserInformation(Context context) {
		userInformation = new UserInformation(context, etUserName.getText()
				.toString(), etUserPassword.getText().toString());
	}

	// initialize necessary controlls
	public void initBase() {
		imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

		tvUserInfoName = (TextView) findViewById(R.id.tvUserInfoName);
		tvUserInfoMendatory= (TextView) findViewById(R.id.tvUserInfoMendatory);
		tvUserPassMendatory= (TextView) findViewById(R.id.tvUserPassMendatory);
		tvUserPassConfirmMendatory= (TextView) findViewById(R.id.tvUserPassConfirmMendatory);
		etUserId = (EditText) findViewById(R.id.etUserId);
		tvUserPass = (TextView) findViewById(R.id.tvUserPass);
		etUserPass = (EditText) findViewById(R.id.etUserPass);
		tvUserPassConfirm = (TextView) findViewById(R.id.tvUserPassConfirm);
		etUserPassConfirm = (EditText) findViewById(R.id.etUserPassConfirm);
		bUserLogin = (Button) findViewById(R.id.bUserLogin);
		bDibsPayment = (Button) findViewById(R.id.bDibsPayment);
		bUpatepro = (Button) findViewById(R.id.bUpatepro);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etUserName.requestFocus();
		etUserPassword = (EditText) findViewById(R.id.etUserPassword);
		tvOnlinePaymentLoginHeadingText = (TextView) findViewById(R.id.tvOnlinePaymentLoginHeadingText);
		tvOnlinePaymentDeleverHeadingText = (TextView) findViewById(R.id.tvOnlinePaymentDeleverHeadingText);
		tvOnlinePaymentHeadingText = (TextView) findViewById(R.id.tvOnlinePaymentHeadingText);
		tvOnlinePaymentModeHeadingText = (TextView) findViewById(R.id.tvOnlinePaymentModeHeadingText);

		rlBottombar = (RelativeLayout) findViewById(R.id.rlBottombar);
		etZipCode = (EditText) findViewById(R.id.etZipCode);
		etTelephone = (EditText) findViewById(R.id.etTelephone);
		etFirstName = (EditText) findViewById(R.id.etFirstName);
		etFirstName.requestFocus();
		etAddress1 = (EditText) findViewById(R.id.etAddress1);
		etAddress2 = (EditText) findViewById(R.id.etAddress2);
		etCity = (EditText) findViewById(R.id.etCity);
		etLastName = (EditText) findViewById(R.id.etLastName);
		tvPaymentModeTotalValue = (TextView) findViewById(R.id.tvPaymentModeTotalValue);
		tvPaymentModeSubtotalValue = (TextView) findViewById(R.id.tvPaymentModeSubtotalValue);
		bNextFromUser = (Button) findViewById(R.id.bNextFromUser);
		// log in button click event
		bUserLogin.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (ValidateLogin()) {
					hideLoginUserPassControl();
					getUserDetailInfo();
					imm.hideSoftInputFromWindow(etUserName.getWindowToken(), 0);
					imm.hideSoftInputFromWindow(
							etUserPassword.getWindowToken(), 0);
					// Trigger the LogIn Event for all Listeners
					onLogIn();

				}
			}
		});
	}

	public void hideLoginUserPassControl() {

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

	/***
	 * Notify each handler that the LogIn event has occured
	 */
	private void onLogIn() {
		for (LogInEventHandler handler : handlers) {
			handler.onLogIn();
		}
	}

	// For Tak VM-232
	// validation for login window
	public boolean ValidateLogin() {
		if (etUserName.length() == 0) {
			etUserName.setError(getString(R.string.usernameerror));
			etUserName.requestFocus();
			return false;
		} else if (!CommonTask.checkEmail(etUserName.getText().toString())) {
			etUserName.setError(getString(R.string.emailerror));
			etUserName.requestFocus();
			return false;
		} else if (etUserPassword.length() == 0) {
			etUserPassword.setError(getString(R.string.passworderror));
			etUserPassword.requestFocus();
			return false;
		}
		return true;
	}

	public void prepareLoginView() {
		logOut();
	}

	// set user information if user is logged in
	public void loggedInView() {
		if (CommonTask.isUserLoggedIn()) {
			etUserName.setText(CommonValues.getInstance().loginuser.Email);
			etUserName.setSelection(etUserName.getText().length());
			etUserPassword
					.setText(CommonValues.getInstance().loginuser.UserPassword);
//			tvUserPasswordStatus.setVisibility(View.GONE);
			bUpatepro.setVisibility(View.VISIBLE);

			/*
			 * blocked for vm 389 by TAC
			 * if (tvOnlinePaymentDeleverHeadingText != null
					&& CommonValues.getInstance().loginuser != null)
				tvOnlinePaymentDeleverHeadingText.setText(CommonValues
						.getInstance().loginuser.FirstName);
			if (tvOnlinePaymentModeHeadingText != null
					&& CommonValues.getInstance().loginuser != null)
				tvOnlinePaymentModeHeadingText.setText(CommonValues
						.getInstance().loginuser.FirstName);*/
		} else {
			if(!CommonTask.isUserLoggedIn()){
			bUpatepro.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void logOut() {
		if (CommonValues.getInstance().loginuser != null) {
			bUpatepro.setVisibility(View.INVISIBLE);

			tvOnlinePaymentLoginHeadingText
					.setText(getString(R.string.indtast_login_));
			/* blocked for vm 389 by TAC
			 * if (tvOnlinePaymentDeleverHeadingText != null)
				tvOnlinePaymentDeleverHeadingText.setText(CommonValues
						.getInstance().loginuser.FirstName);
			if (tvOnlinePaymentHeadingText != null)
				tvOnlinePaymentHeadingText
						.setText(CommonValues.getInstance().loginuser.FirstName);
			if (tvOnlinePaymentModeHeadingText != null)
				tvOnlinePaymentModeHeadingText.setText(CommonValues
						.getInstance().loginuser.FirstName);*/
			etUserName.setText("");
			etUserPassword.setText("");
		}
	}

	public void showLoginView() {
		if (CommonTask.isUserLoggedIn()) {
			loggedInView();
		} else {
			logOut();
		}
	}
	

}
