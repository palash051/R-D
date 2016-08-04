package com.shopper.app.utils;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.shopper.app.R;
import com.shopper.app.entities.AlphaAnimationListener;
import com.shopper.app.entities.ArticleImage;
import com.shopper.app.entities.ArticleInq;
import com.shopper.app.entities.AsyncImageLoader;
import com.shopper.app.entities.DiscountGroup;
import com.shopper.app.entities.DisplayNextView;
import com.shopper.app.entities.Flip3dAnimation;
import com.shopper.app.entities.OrderLine;
import com.shopper.app.entities.OrderReply;
import com.shopper.app.entities.OrderReplyRoot;
import com.shopper.app.entities.PriceInquiery;
import com.shopper.app.entities.UserInformation;

/**
 * @author Shafiqul Alam Some common methods are defined in this class that is
 *         used all through the application such as email validation,robotto
 *         font method,cofirmation message,alert message,loading user info etc.
 */

@SuppressLint("DefaultLocale")
public class CommonTask {
	public static String getString(String value) {

		if (value == null)
			return "";
		try {
			return new String(value.getBytes(CommonConstraints.EncodingCode));
		} catch (UnsupportedEncodingException e) {
			return e.toString();
		}
	}

	public static String getString(double value) {

		String dVal = NumberFormat.getInstance(Locale.FRANCE).format(
				CommonTask.round(value, 2, BigDecimal.ROUND_HALF_UP));
		String[] vals = dVal.split(",");
		if (vals.length == 1) {
			return vals[0] + "," + "00";
		} else if (vals.length > 1) {
			return vals[1].length() > 1 ? dVal : vals[0] + "," + vals[1] + "0";
		}
		return dVal;

	}

	public static String getContentString(double value) {

		String dVal = NumberFormat.getInstance(Locale.FRANCE).format(
				CommonTask.round(value, 2, BigDecimal.ROUND_HALF_UP));
		String[] vals = dVal.split(",");
		if (vals.length > 1) {
			return vals[1].length() > 1 ? dVal : (vals[1] != "0" ? (vals[0]
					+ "," + vals[1] + "0") : "");
		}
		return dVal;

	}

	public static void initCommonValuesForBasket() {
		CommonValues.getInstance().IsAnyNewBasketItemAdded = false;
		CommonBasketValues.getInstance().Basket = new OrderReply();
		CommonBasketValues.getInstance().updateBasket();
	}

	public static int convertToDimensionValue(Context context, int inputValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				inputValue, context.getResources().getDisplayMetrics());
	}

	// add the article to basket from basket orderline and update it accordingly
	public static void addBasketObject(Context context,
			PriceInquiery objPriceInquiery) {
		while (!CommonValues.getInstance().IsBasketUpdateInProgress) {
			CommonValues.getInstance().IsBasketUpdateInProgress = true;

			CommonValues.getInstance().IsAnyNewBasketItemAdded = false;
			int qty = objPriceInquiery.quantity
					+ CommonBasketValues.getInstance().Basket
							.getItemQuantity(objPriceInquiery.id.trim());
			OrderReplyRoot orderReplyRoot = (OrderReplyRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonURL.getInstance().BasketURL, "1",
							getShopNumber(context),
							CommonBasketValues.getInstance().Basket.OrderNo,
							objPriceInquiery.EAN, qty), OrderReplyRoot.class);

			if (orderReplyRoot != null) {
				fillOrderValue(objPriceInquiery, orderReplyRoot);
			}

			CommonValues.getInstance().IsBasketUpdateInProgress = false;
			break;
		}
	}

	// add the article detail to basket and update it accordingly
	public static void addBasketFromDetail(Context context,
			OrderLine objOrderLine, int TotalQty) {
		while (!CommonValues.getInstance().IsBasketUpdateInProgress) {
			CommonValues.getInstance().IsBasketUpdateInProgress = !CommonValues
					.getInstance().IsBasketUpdateInProgress;
			OrderReplyRoot orderReplyRoot = (OrderReplyRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonURL.getInstance().BasketURL, "1",
							getShopNumber(context),
							CommonBasketValues.getInstance().Basket.OrderNo,
							objOrderLine.Id, TotalQty), OrderReplyRoot.class);
			if (orderReplyRoot != null) {
				fillOrderValue(objOrderLine, orderReplyRoot);
			}
			CommonValues.getInstance().IsBasketUpdateInProgress = false;
			break;
		}
	}

	// Fill the article to basket from details screen and update it accordingly
	private static void fillOrderValue(PriceInquiery objOrderLine,
			OrderReplyRoot orderReplyRoot) {		
		if(orderReplyRoot.orderReply.Lines!=null){
			OrderLine _orderLine = null;
			for (OrderLine orderLine : orderReplyRoot.orderReply.Lines) {
				_orderLine = CommonBasketValues.getInstance().Basket
						.getOrderLineById(orderLine.Id);
				if (_orderLine != null && !_orderLine.Id.equals(objOrderLine.EAN)) {
					orderLine.drawableImage = _orderLine.drawableImage;
					orderLine.Text2 = _orderLine.Text2;
					orderLine.ContentsDesc = _orderLine.ContentsDesc;
					orderLine.DiscountText = _orderLine.DiscountText;
					orderLine.LineDiscountGroup = _orderLine.LineDiscountGroup;
					orderLine.ArticleFullText = _orderLine.ArticleFullText;
				} else {
					orderLine.drawableImage = objOrderLine.drawableImage;
					orderLine.Text2 = objOrderLine.text2;
					orderLine.ContentsDesc = getContentsDescription(objOrderLine);
					orderLine.DiscountText = getDiscountTextFromPriceInquiry(objOrderLine);
					orderLine.LineDiscountGroup = objOrderLine.getDiscountGroup();
					orderLine.ArticleFullText = objOrderLine.text;
					CommonValues.getInstance().IsAnyNewBasketItemAdded = true;
				}
			}
		}else{
			orderReplyRoot.orderReply.Lines=new ArrayList<OrderLine>();
		}
		CommonBasketValues.getInstance().Basket = orderReplyRoot.orderReply;
		CommonBasketValues.getInstance().updateBasket();
	}

	// Fill the article to basket from capture, add from search & catalouge and
	// update it accordingly
	private static void fillOrderValue(OrderLine objOrderLine,
			OrderReplyRoot orderReplyRoot) {
		if(orderReplyRoot.orderReply.Lines!=null){
			OrderLine _orderLine = null;
			for (OrderLine orderLine : orderReplyRoot.orderReply.Lines) {
				_orderLine = CommonBasketValues.getInstance().Basket
						.getOrderLineById(orderLine.Id);
				if (_orderLine != null && !_orderLine.Id.equals(objOrderLine.Id)) {
					orderLine.drawableImage = _orderLine.drawableImage;
					orderLine.Text2 = _orderLine.Text2;
					orderLine.ContentsDesc = _orderLine.ContentsDesc;
					orderLine.DiscountText = _orderLine.DiscountText;
					orderLine.LineDiscountGroup = _orderLine.LineDiscountGroup;
					orderLine.ArticleFullText = _orderLine.ArticleFullText;
				} else {
					orderLine.drawableImage = objOrderLine.drawableImage;
					orderLine.Text2 = objOrderLine.Text2;
					orderLine.ContentsDesc = objOrderLine.ContentsDesc;
					orderLine.DiscountText = objOrderLine.DiscountText;
					orderLine.LineDiscountGroup = objOrderLine.LineDiscountGroup;
					orderLine.ArticleFullText = objOrderLine.ArticleFullText;
				}
			}
		}else{
			orderReplyRoot.orderReply.Lines=new ArrayList<OrderLine>();
		}
		CommonBasketValues.getInstance().Basket = orderReplyRoot.orderReply;
		CommonBasketValues.getInstance().updateBasket();
	}

	// Fill all article to basket from Enter EAN screen by a given orderno
	private static void fillOrderValue(Context context,
			OrderReplyRoot orderReplyRoot) {
		if(orderReplyRoot.orderReply.Lines!=null){
			ArticleInq objArticleInq = null;
			
			for (OrderLine orderLine : orderReplyRoot.orderReply.Lines) {
	
				orderLine.drawableImage = CommonTask.getDrawableImage(String
						.format(CommonURL.getInstance().ProductImageURL,
								orderLine.Id), orderLine.Id);
				objArticleInq = new ArticleInq(context, orderLine.Id);
				if (ArticleInq.IsProductFound) {
					PriceInquiery objPriceInquiery = objArticleInq
							.getPriceInquiery();
					orderLine.Text2 = objPriceInquiery.text2;
					orderLine.ArticleFullText = objPriceInquiery.text;
					orderLine.ContentsDesc = getContentsDescription(objPriceInquiery);
					if (objPriceInquiery.getDiscount().quantity > 0) {
						orderLine.DiscountText = getDiscountTextFromPriceInquiry(objPriceInquiery);
					}
					orderLine.LineDiscountGroup = objPriceInquiery
							.getDiscountGroup();
				} else {
					orderLine.Text2 = "";
					orderLine.ContentsDesc = "";
					orderLine.DiscountText = "";
					orderLine.ArticleFullText = "";
					orderLine.LineDiscountGroup = new DiscountGroup();
				}
			}
		}else{
			orderReplyRoot.orderReply.Lines=new ArrayList<OrderLine>();
		}
		CommonBasketValues.getInstance().Basket = orderReplyRoot.orderReply;
		CommonBasketValues.getInstance().updateBasket();
	}

	/**
	 * Fill order by a order no from enter EAN screen
	 * 
	 * @param context
	 */
	public static void fillBasketFromOrderNo(Context context) {
		while (!CommonValues.getInstance().IsBasketUpdateInProgress) {
			CommonValues.getInstance().IsBasketUpdateInProgress = !CommonValues
					.getInstance().IsBasketUpdateInProgress;
			if (CommonBasketValues.getInstance().Basket == null
					|| CommonBasketValues.getInstance().Basket.OrderNo == 0)
				return;

			OrderReplyRoot orderReplyRoot = (OrderReplyRoot) JSONfunctions
					.retrieveDataFromStream(String.format(
							CommonURL.getInstance().CheckoutURL, "1",
							getShopNumber(context),
							CommonBasketValues.getInstance().Basket.OrderNo),
							OrderReplyRoot.class);
			if (orderReplyRoot != null) {
				fillOrderValue(context, orderReplyRoot);
			}
			CommonValues.getInstance().IsBasketUpdateInProgress = false;
			break;
		}

	}

	// Use for Making Discount Text from PriceInquiery
	private static String getDiscountTextFromPriceInquiry(
			PriceInquiery objPriceInquiery) {
		if(objPriceInquiery.getDiscount().quantity>0){
		return String.valueOf(objPriceInquiery.getDiscount().quantity) + " "
				+ objPriceInquiery.getDiscount().text + "\r\n"
				+ Math.round(objPriceInquiery.getDiscount().amount);
		}
		return "";
	}

	// Use for Making Contents Description from PriceInquiery
	private static String getContentsDescription(PriceInquiery objPriceInquiery) {
		return getContentString(objPriceInquiery.contents)
				+ objPriceInquiery.contentsdesc + " ("
				+ objPriceInquiery.priceperdesc + "-pris" + " "
				+ CommonTask.getString(objPriceInquiery.priceper) + ")";
	}

	public static OrderLine getOrderLineFromBasketByEan(String Ean) {
		OrderLine orderLine;
		for (int index = 0; index < CommonBasketValues.getInstance().Basket.Lines
				.size(); index++) {
			orderLine = (OrderLine) CommonBasketValues.getInstance().Basket.Lines
					.get(index);
			if (orderLine.Id.trim().equals(Ean.trim())) {
				return orderLine;
			}
		}
		return null;
	}

	private static ArrayList<ArticleImage> internetImages = new ArrayList<ArticleImage>();

	public static BitmapDrawable getArticleImageFromCache(String id) {
//		try {
			if(null != internetImages){
				for (ArticleImage artimage : internetImages) {
					if( null != artimage && artimage.eanNo.equals(id) )
						return artimage.image;
				}
			}
//		} catch (Exception oEx) {
//		}
		return null;

	}

	// Load the prodct image

	public static BitmapDrawable getDrawableImage(String url, String id) {
		BitmapDrawable img = getArticleImageFromCache(id);
		if (img != null){
			return img;
		} else {
			BitmapDrawable bd = getDrawableImage(url);
			if(bd!=null){
				bd = new BitmapDrawable(null,CommonTask.getRoundedCornerBitmap(bd.getBitmap()));
				internetImages.add(new ArticleImage(id, bd));
			}
			
			
			return bd;
		}
	}

	private static BitmapDrawable getDrawableImage(String url) {
		try {
			URL urlAdd = new URL(url);
			URLConnection connection = urlAdd.openConnection();
			connection.setConnectTimeout(CommonConstraints.TIMEOUT_MILLISEC);
			connection.setReadTimeout(CommonConstraints.TIMEOUT_MILLISEC);
			connection.connect();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8 * 512);

			Bitmap bmp = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return new BitmapDrawable(null, bmp);

		} catch (Exception e) {
		}
		return null;
	}

	public static double round(double unrounded, int precision, int roundingMode) {
		BigDecimal bd = new BigDecimal(unrounded);
		BigDecimal rounded = bd.setScale(precision, roundingMode);
		return rounded.doubleValue();
	}

	public static String getstring(int i) {
		String aString = Integer.toString(i);
		return aString;

	}

	@SuppressLint("DefaultLocale")
	public static String toCamelCase(String s) {
		return s.substring(0, 1).toUpperCase(Locale.getDefault()) + s.substring(1).toLowerCase(Locale.getDefault());
	}

	public static String toCamelCase(String s, String separator) {
		String[] parts = s.split(separator);
		String camelCaseString = "";
		for (String part : parts) {
			if (!part.equals(null) && !part.isEmpty() && !part.equals("")) {
				camelCaseString = camelCaseString + toCamelCase(part);
				camelCaseString = camelCaseString + " ";
			} else {
				camelCaseString = part;
			}
		}
		return camelCaseString;
	}

	public static void ShowMessage(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name)
				.setMessage(message)
				.setCancelable(false)
				.setNegativeButton(R.string.button_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void ShowConfirmation(Context context, String message,
			DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setPositiveButton(R.string.button_yes, event)
				.setNegativeButton(R.string.button_no, event);
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	

	// method for exit from the app
	public static void CloseApplication(Context context) {

		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	public static void ApplyRotation(float start, float end,
			boolean is1stLayout, LinearLayout layout1, LinearLayout layout2,
			int rotateDuration, InputMethodManager imm, int keyboardStatus) {
		final float centerX = layout1.getWidth() / 2.0f;
		final float centerY = layout1.getHeight() / 2.0f;
		final Flip3dAnimation rotation = new Flip3dAnimation(start, end,
				centerX, centerY);
		rotation.setDuration(rotateDuration);
		rotation.setAnimationListener(new DisplayNextView(is1stLayout, layout1,
				layout2, imm, keyboardStatus));
		if (is1stLayout) {
			layout1.startAnimation(rotation);

		} else {
			layout2.startAnimation(rotation);
		}
	}

	public static void setAlphaVisible(View view, boolean isVisible) {
		AlphaAnimation animation = isVisible ? new AlphaAnimation(0.0f, 1.0f)
				: new AlphaAnimation(1.0f, 0.0f);
		animation.setDuration(1000);
		view.startAnimation(animation);
		animation.setAnimationListener(new AlphaAnimationListener(view,
				isVisible));
	}

	public static void setAsyncImageBackground(View view, String id) {
		view.setTag(id);
		new AsyncImageLoader().execute(view);
	}

	// for the previous movement
	public static Animation inFromRightAnimation() {
		return inFromRightAnimation(350);
	}

	public static Animation outToLeftAnimation() {
		return outToLeftAnimation(350);
	}

	public static Animation inFromLeftAnimation() {
		return inFromLeftAnimation(350);
	}

	public static Animation outToRightAnimation() {
		return outToRightAnimation(350);
	}

	public static Animation inFromRightAnimation(int speed) {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromRight.setDuration(speed);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	public static Animation outToLeftAnimation(int speed) {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoLeft.setDuration(speed);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	// for the next movement
	public static Animation inFromLeftAnimation(int speed) {
		Animation inFromLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, -1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		inFromLeft.setDuration(speed);
		inFromLeft.setInterpolator(new AccelerateInterpolator());
		return inFromLeft;
	}

	public static Animation outToRightAnimation(int speed) {
		Animation outtoRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, +1.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f,
				Animation.RELATIVE_TO_PARENT, 0.0f);
		outtoRight.setDuration(speed);
		outtoRight.setInterpolator(new AccelerateInterpolator());
		return outtoRight;
	}

	public static int SWIPE_MIN_DISTANCE() {
		return 25;
	}

	public static int SWIPE_MAX_OFF_PATH() {
		return 25;
	}

	public static int SWIPE_THRESHOLD_VELOCITY() {
		return 50;
	}

	// method for saving data to shared preference
	public static void SavePreferences(Context context, String sharedId,
			String key, String value) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				sharedId, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void loadSettings(Context context) {
		String baseUrl = getBaseUrl(context);
		if (!baseUrl.equals("")) {
			CommonURL.getInstance().assignValues(baseUrl,
					getShopNumber(context));
		}
	}

	public static String getBaseUrl(Context context) {
		String baseUrl = "";
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		baseUrl = sharedPreferences.getString(
				CommonConstraints.PREF_URL_KEY, "");
		return baseUrl;
	}

	public static String getShopNumber(Context context) {
		String shopNumber = "";
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"settings", Context.MODE_PRIVATE);
		shopNumber = sharedPreferences.getString(
				CommonConstraints.PREF_SHOPNUMBER_KEY, "");
		return shopNumber;
	}

	public static void loadLoginUser(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				CommonConstraints.PREF_LOGINUSER_NAME, Context.MODE_PRIVATE);
		CommonValues.getInstance().loginuser.CustomerId = sharedPreferences
				.contains(CommonConstraints.PREF_CUSTOMERID_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_CUSTOMERID_KEY, "") : "";
		CommonValues.getInstance().loginuser.FirstName = sharedPreferences
				.contains(CommonConstraints.PREF_FIRSTNAME_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_FIRSTNAME_KEY, "") : "";
		CommonValues.getInstance().loginuser.LastName = sharedPreferences
				.contains(CommonConstraints.PREF_LASTNAME_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_LASTNAME_KEY, "") : "";
		CommonValues.getInstance().loginuser.ZipCode = sharedPreferences
				.contains(CommonConstraints.PREF_ZIPCODE_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_ZIPCODE_KEY, "") : "";
		CommonValues.getInstance().loginuser.Email = sharedPreferences
				.contains(CommonConstraints.PREF_EMAIL_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_EMAIL_KEY, "") : "";
		CommonValues.getInstance().loginuser.Telephone = sharedPreferences
				.contains(CommonConstraints.PREF_TELEPHONE_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_TELEPHONE_KEY, "") : "";
		CommonValues.getInstance().loginuser.Address1 = sharedPreferences
				.contains(CommonConstraints.PREF_ADDRESS1_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_ADDRESS1_KEY, "") : "";
		CommonValues.getInstance().loginuser.Address2 = sharedPreferences
				.contains(CommonConstraints.PREF_ADDRESS2_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_ADDRESS2_KEY, "") : "";
		CommonValues.getInstance().loginuser.City = sharedPreferences
				.contains(CommonConstraints.PREF_CITY_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_CITY_KEY, "") : "";
		CommonValues.getInstance().loginuser.UserPassword = sharedPreferences
				.contains(CommonConstraints.PREF_PASSWORD_KEY) ? sharedPreferences
				.getString(CommonConstraints.PREF_PASSWORD_KEY, "") : "";
	}

	public static void resetPreferenceValue(Context context) {
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_CUSTOMERID_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_FIRSTNAME_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_LASTNAME_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ZIPCODE_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_EMAIL_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_TELEPHONE_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ADDRESS1_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_ADDRESS2_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_CITY_KEY, "");
		CommonTask.SavePreferences(context,
				CommonConstraints.PREF_LOGINUSER_NAME,
				CommonConstraints.PREF_PASSWORD_KEY, "");
		loadLoginUser(context);
	}

	public static UserInformation getLoginUser() {
		if (isUserLoggedIn()) {
			return CommonValues.getInstance().loginuser;
		} else {
			return null;
		}
	}

	public static boolean isUserLoggedIn() {
		if (CommonValues.getInstance().loginuser == null)
			return false;
		return !CommonValues.getInstance().loginuser.CustomerId.equals("");
	}

	public static void setApplicationUrls(Context context,
			ArrayList<String> values) {
		setStringArrayPref(context, CommonConstraints.PREF_BASEURLS_KEY,
				values);
	}

	public static ArrayList<String> getApplicationUrls(Context context) {
		return getStringArrayPref(context,
				CommonConstraints.PREF_BASEURLS_KEY);
	}

	public static void setStringArrayPref(Context context, String key,
			ArrayList<String> values) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		JSONArray a = new JSONArray();
		for (int i = 0; i < values.size(); i++) {
			a.put(values.get(i));
		}
		if (!values.isEmpty()) {
			editor.putString(key, a.toString());
		} else {
			editor.putString(key, null);
		}
		editor.commit();
	}

	public static ArrayList<String> getStringArrayPref(Context context,
			String key) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String json = prefs.getString(key, null);
		ArrayList<String> urls = null;
		if (json != null) {
			try {
				urls = new ArrayList<String>();
				JSONArray a = new JSONArray(json);
				for (int i = 0; i < a.length(); i++) {
					String url = a.optString(i);
					urls.add(url);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return urls;
	}

	// Method for Checking Email Validation..Tanvir
	public static boolean checkEmail(String email) {
		return Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	// Method for displaying log in Confirmationmessage..Tanivr
	public static void showLogInConfirmation(Context context, String message,
			DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setPositiveButton(R.string.button_yes, event)
				.setNegativeButton(R.string.button_no, event);
		AlertDialog alert = builder.create();
		alert.show();
	}
	// Method for displaying cancel basket Confirmationmessage..Tanivr
	
	public static void showCancelBasketConfirmation(Context context, String message,
			DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message)
				.setPositiveButton(R.string.button_yes, event)
				.setNegativeButton(R.string.button_no, event);
		AlertDialog alert = builder.create();
		alert.show();
	}

	// Method for displaying Checkout Confirmationmessage..Tanivr
	public static void showCheckoutConfirmation(Context context,
			String message, DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message).setPositiveButton(
				R.string.button_ok, event);
		AlertDialog alert = builder.create();
		alert.show();
	}
	// Method for displaying serversettings Confirmationmessage..Tanivr
	public static void showServerSettingConfirmation(Context context, String message,
			DialogInterface.OnClickListener event) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setPositiveButton(R.string.button_server_setting_yes, event)
				.setNegativeButton(R.string.button_server_setting_no, event);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Indicates whether network connectivity exists or is in the process of
	 * being established. For the latter, call isConnected() instead, which
	 * guarantees that the network is fully usable.
	 * 
	 * @param context
	 * @return Returns true if network connectivity exists or is in the process
	 *         of being established, false otherwise.
	 */
	public static boolean isNetworkAvailable(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = null;
		State networkState = null;
		if (connectivityManager != null) {
			networkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			networkState = networkInfo.getState();
			if (networkState == NetworkInfo.State.CONNECTED
					|| networkState == NetworkInfo.State.CONNECTING) {
				return true;
			}
			networkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			networkState = networkInfo.getState();
			if (networkState == NetworkInfo.State.CONNECTED
					|| networkState == NetworkInfo.State.CONNECTING) {
				return true;
			}
		}
		//we wont show the alert from here.
//		CommonTask.ShowMessage(context,
//				context.getString(R.string.networkError));
		return false;
	}

	/**
	 * User for getting exception message
	 * 
	 * @param context
	 * @param exceptionCode
	 * @return
	 */
	public static String getCustomExceptionMessage(Context context,
			int exceptionCode) {

		switch (exceptionCode) {
		case CommonConstraints.GENERAL_EXCEPTION:
			return context.getString(R.string.GeneralExceptionMessage);
		case CommonConstraints.CLIENT_PROTOCOL_EXCEPTION:
			return context.getString(R.string.ClientProtocolExceptionMessage);
		case CommonConstraints.ILLEGAL_STATE_EXCEPTION:
			return context.getString(R.string.IllegalStateExceptionMessage);
		case CommonConstraints.IO_EXCEPTION:
			return context.getString(R.string.IOExceptionMessage);
		case CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION:
			return context
					.getString(R.string.UnsupportedEncodingExceptionMessage);
		case CommonConstraints.NO_INTERNET_AVAILABLE:
			return context
					.getString(R.string.networkError);
		default:
			return "";
		}
	}
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = null;
		if(bitmap!=null){
		    output = Bitmap.createBitmap(bitmap.getWidth(),
		        bitmap.getHeight(), Config.ARGB_8888);
		    Canvas canvas = new Canvas(output);
		 
		    final int color = 0xff424242;
		    final Paint paint = new Paint();
		    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		    final RectF rectF = new RectF(rect);
		    final float roundPx = 30;
		 
		    paint.setAntiAlias(true);
		    canvas.drawARGB(0, 0, 0, 0);
		    paint.setColor(color);
		    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		 
		    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		    canvas.drawBitmap(bitmap, rect, rect, paint);
		}
	    return output;
	}
	
	/**
	 * Creates a 'ghost' bitmap version of the given source drawable (ideally a BitmapDrawable).
	 * In the ghost bitmap, the RGB values take on the values from the 'color' argument, while
	 * the alpha values are derived from the source's grayscaled RGB values. The effect is that
	 * you can see through darker parts of the source bitmap, while lighter parts show up as
	 * the given color. The 'invert' argument inverts the computation of alpha values, and looks
	 * best when the given color is a dark.
	 */
	public static Bitmap createGhostIcon(Drawable src, int color, boolean invert) {
	    int width = src.getIntrinsicWidth();
	    int height = src.getIntrinsicHeight();
	    if (width <= 0 || height <= 0) {
	        throw new UnsupportedOperationException("Source drawable needs an intrinsic size.");
	    }
	 
	    Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap);
	    Paint colorToAlphaPaint = new Paint();
	    int invMul = invert ? -1 : 1;
	    colorToAlphaPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{
	            0, 0, 0, 0, Color.red(color),
	            0, 0, 0, 0, Color.green(color),
	            0, 0, 0, 0, Color.blue(color),
	            invMul * 0.213f, invMul * 0.715f, invMul * 0.072f, 0, invert ? 255 : 0,
	    })));
	    canvas.saveLayer(0, 0, width, height, colorToAlphaPaint, Canvas.ALL_SAVE_FLAG);
	    canvas.drawColor(invert ? Color.WHITE : Color.BLACK);
	    src.setBounds(0, 0, width, height);
	    src.draw(canvas);
	    canvas.restore();
	    return bitmap;
	}
}
