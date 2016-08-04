package com.shopper.app.entities;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.content.Context;

import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonTask;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.CommonValues;
import com.shopper.app.utils.JSONfunctions;

/**
 * @author use for holding user information and displaying when necessary
 *         Asynchronously
 */

public class UserInformation {

	public String CustomerId;
	public String UserName;
	public String UserPassword;
	public String FirstName;
	public String LastName;
	public String RoadName;
	public String HouseNo;
	public String Floor;
	public String ZipCode;
	public String DoorCode;
	public String Email;
	public String Telephone;
	public String Mobile;
	public String Address1;
	public String Address2;
	public String City;

	public UserInformation() {
		CustomerId = "";
		UserName = "";
		UserPassword = "";
		FirstName = "";
		LastName = "";
		RoadName = "";
		HouseNo = "";
		Floor = "";
		ZipCode = "";
		DoorCode = "";
		Email = "";
		Telephone = "";
		Mobile = "";
		Address1 = "";
		Address2 = "";
		City = "";
	}
	
	/**
	 * Load Customer information using Gson and assign data to user info
	 * @param context
	 * @param email
	 * @param password
	 */

	public UserInformation(Context context, String email, String password) {
		try {
			CustomerRoot customerRoot= (CustomerRoot) JSONfunctions.retrieveDataFromStream(
					String.format(CommonURL.getInstance().LoginCustomerURL, URLEncoder.encode(email, CommonConstraints.EncodingCode),URLEncoder.encode(password, CommonConstraints.EncodingCode) ),CustomerRoot.class);
			
			if(customerRoot!=null ){
				CommonValues.getInstance().loginuser.UserPassword = password;				
				CustomerId = customerRoot.customer.CustomerId;
				FirstName = customerRoot.customer.Name;
				ZipCode = customerRoot.customer.Zipcode;
				Telephone = customerRoot.customer.Phone;
				Email = customerRoot.customer.Email;
				UserName = customerRoot.customer.Email;
				Address1 = customerRoot.customer.Address1;
				Address2 = customerRoot.customer.Address2;
				City = customerRoot.customer.City;
				UserPassword = CommonValues.getInstance().loginuser.UserPassword;
				
				assignPreferenceDataAndReload(context, password);
				
			}		
			

		} catch (Exception e) {

		}
	}

	private void assignPreferenceDataAndReload(Context context, String password) {
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_CUSTOMERID_KEY,
				CustomerId);
		CommonTask
				.SavePreferences(context,
						CommonConstraints.PREF_LOGINUSER_NAME,
						CommonConstraints.PREF_FIRSTNAME_KEY,
						FirstName);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_LASTNAME_KEY, LastName);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ZIPCODE_KEY, ZipCode);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ADDRESS1_KEY, Address1);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ADDRESS2_KEY, Address2);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_CITY_KEY, City);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_EMAIL_KEY, Email);
		CommonTask
				.SavePreferences(context,
						CommonConstraints.PREF_LOGINUSER_NAME,
						CommonConstraints.PREF_TELEPHONE_KEY,
						Telephone);
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_PASSWORD_KEY, password);
		CommonTask.loadLoginUser(context);
	}
	// json method for saving new user information...Tanvir

	public boolean saveNewUserInformation() {
		try {
			JSONObject json;
			if (!CommonTask.isUserLoggedIn()) {

				json = JSONfunctions.getJSONfromURL( String.format(
						CommonURL.getInstance().SaveNewCustomerURL,
						URLEncoder.encode(FirstName, CommonConstraints.EncodingCode),
						URLEncoder.encode(Address1, CommonConstraints.EncodingCode),
						URLEncoder.encode(Address2, CommonConstraints.EncodingCode),
						URLEncoder.encode(ZipCode, CommonConstraints.EncodingCode),
						URLEncoder.encode(City, CommonConstraints.EncodingCode),
						URLEncoder.encode(Telephone, CommonConstraints.EncodingCode),
						URLEncoder.encode(Email, CommonConstraints.EncodingCode),
						URLEncoder.encode(UserPassword, CommonConstraints.EncodingCode)),
						CommonConstraints.POST);
				if (json != null) {
					JSONObject customerJson = json.getJSONObject("Customer");
					if (customerJson != null) {
						CustomerId = customerJson.has("CustomerId") ? customerJson
								.getString("CustomerId") : "";
					}
				}
			} else {
				json = JSONfunctions.getJSONfromURL( String.format(
						CommonURL.getInstance().UpdateCustomerURL, 
						URLEncoder.encode(CommonValues.getInstance().loginuser.CustomerId,CommonConstraints.EncodingCode),
						URLEncoder.encode(FirstName,CommonConstraints.EncodingCode), 
						URLEncoder.encode(Address1,CommonConstraints.EncodingCode), 
						URLEncoder.encode(Address2,CommonConstraints.EncodingCode), 
						URLEncoder.encode(ZipCode,CommonConstraints.EncodingCode), 
						URLEncoder.encode(City,CommonConstraints.EncodingCode),
						URLEncoder.encode(Telephone,CommonConstraints.EncodingCode), 
						URLEncoder.encode(CommonValues.getInstance().loginuser.Email,CommonConstraints.EncodingCode),
						URLEncoder.encode(CommonValues.getInstance().loginuser.UserPassword,CommonConstraints.EncodingCode))
						,CommonConstraints.POST);
				
				
				
				CustomerId = CommonValues.getInstance().loginuser.CustomerId;
				UserPassword = CommonValues.getInstance().loginuser.UserPassword;
				Email = CommonValues.getInstance().loginuser.Email;
			}
			CommonValues.getInstance().loginuser = this;

		} catch (Exception e) {
			return false;
		}
		return true;
	}

	// json method for saving users alternate address...Tanvir

	public boolean saveUsersAlternateAddress(String customerId,
			String address1, String address2, String zipCode, String city,
			String telephone) {
		try {
			JSONfunctions.getJSONfromURL(String.format(
					CommonURL.getInstance().SaveUserAlternateAddress,
					URLEncoder.encode(customerId,CommonConstraints.EncodingCode),
					URLEncoder.encode(address1,CommonConstraints.EncodingCode),
					URLEncoder.encode(address2,CommonConstraints.EncodingCode),
					URLEncoder.encode(zipCode,CommonConstraints.EncodingCode),
					URLEncoder.encode(city,CommonConstraints.EncodingCode),
					URLEncoder.encode(telephone,CommonConstraints.EncodingCode)), CommonConstraints.POST);

		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
