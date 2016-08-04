package com.mobilink.app.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONException;

import com.mobilink.app.R;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.ContactsContract.PhoneLookup;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;


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
	public static boolean isOnline(Context context) {
	    ConnectivityManager cm =
	        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public static  void showMessage(final Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {						
						context.startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	public static BitmapDrawable getDrawableImage(String url) {
		try {
			URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8 * 512);
			Bitmap bmp = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return new BitmapDrawable(null, bmp);

		} catch (Exception e) {
		}
		return null;
	}
	
	public static Bitmap getBitmapImage(String url) {
		try {
			URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8 * 512);
			Bitmap bmp = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return  bmp;

		} catch (Exception e) {
			String log=e.getMessage();
			log=log+log;
		}
		return null;
	}
	
	public static String getRealPathFromURI(Context context, Uri contentUri) {

		Cursor cursor = context.getContentResolver()
				.query(contentUri,
						new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
						null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	private static final String[] PHOTO_BITMAP_PROJECTION = new String[] {
	    ContactsContract.CommonDataKinds.Photo.PHOTO
	};
	public static Bitmap fetchContactImageThumbnail(Context context, final int thumbnailId) {

	    final Uri uri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, thumbnailId);
	    ContentResolver contentResolver = context.getContentResolver();
	    final Cursor cursor = contentResolver.query(uri, PHOTO_BITMAP_PROJECTION, null, null, null);

	    try {
	        Bitmap thumbnail = null;
	        if (cursor.moveToFirst()) {
	            final byte[] thumbnailBytes = cursor.getBlob(0);
	            if (thumbnailBytes != null) {
	                thumbnail = BitmapFactory.decodeByteArray(thumbnailBytes, 0, thumbnailBytes.length);
	            }
	        }
	        return thumbnail;
	    }
	    finally {
	        cursor.close();
	    }

	}
	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		 int targetWidth = scaleBitmapImage.getWidth()-10;
		int targetHeight = scaleBitmapImage.getHeight()-10;
		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, targetHeight,
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) targetWidth - 1) / 2,
				((float) targetHeight - 1) / 2,
				(Math.min(((float) targetWidth), ((float) targetHeight)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, targetWidth,
				targetHeight), null);
		return targetBitmap;
	}
	
	public static String getContentName(Context context, String Number){
		String name = "";
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(Number));
		Cursor cursor = context.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME}, null, null, null);
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			name = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
			cursor.close();
		}
		return name;
	}
	
	public static int getContentPhotoId(Context context, String Number){
		int photoId = -1;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(Number));
		Cursor cursor = context.getContentResolver().query(uri, new String[]{PhoneLookup.DISPLAY_NAME,PhoneLookup.PHOTO_ID}, null, null, null);
		if(cursor != null && cursor.getCount() > 0){
			cursor.moveToFirst();
			photoId = cursor.getInt(cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_ID));
			cursor.close();
		}
		return photoId;
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
			return vals[1].length() > 1 ? dVal
					: (!vals[1].equals("0") ? (vals[0] + "," + vals[1] + "0")
							: "");
		}
		return dVal;

	}

	public static int convertToDimensionValue(Context context, int inputValue) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				inputValue, context.getResources().getDisplayMetrics());
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

	public static String toCamelCase(String s) {
		return s.substring(0, 1).toUpperCase(Locale.US)
				+ s.substring(1).toLowerCase(Locale.US);
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
		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(context);
		 * builder.setTitle(R.string.app_name) .setMessage(message)
		 * .setCancelable(false) .setNegativeButton(R.string.button_ok, new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { dialog.cancel(); } });
		 * AlertDialog alert = builder.create(); alert.show();
		 */
	}

	public static void ShowConfirmation(Context context, String message,
			DialogInterface.OnClickListener event) {
		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(context);
		 * builder.setTitle(R.string.app_name).setMessage(message)
		 * .setPositiveButton(R.string.button_yes, event)
		 * .setNegativeButton(R.string.button_no, event); AlertDialog alert =
		 * builder.create(); alert.show();
		 */
	} // confirmation dialogue used to exit from the app

	/*
	 * public static void CloseApplication(Context context) {
	 * CommonTask.ShowConfirmation(context, "Vil du forlade?",
	 * CommonTask.ProgramExitEvent(context)); }
	 */

	public static DialogInterface.OnClickListener ProgramExitEvent(
			final Context context) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					Intent intent = new Intent(Intent.ACTION_MAIN);
					intent.addCategory(Intent.CATEGORY_HOME);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					dialog.cancel();
					break;
				}
			}
		};
		return dialogClickListener;

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

	// Methods for saving data to shared preferences
	public static void savePreferences(Context context, String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void savePreferencesForReasonCode(Context context,
			String key, Integer value) {

		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static void saveLinkedInPreferencesForLogIn(Context context, String key, String value){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static void savePreferenceForCallNOC(Context context, String Key, String value){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor callNocEditor = sharedPreferences.edit();
		callNocEditor.putString(Key, value);
		callNocEditor.commit();
	}
	
	public static void savePreferenceForCallManager(Context context, String Key, String value){
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor callManagerEditor = sharedPreferences.edit();
		callManagerEditor.putString(Key, value);
		callManagerEditor.commit();
	}

	public static String getPreferences(Context context, String prefKey) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		return sharedPreferences.getString(prefKey, "");
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
	
	public static Bitmap decodeImage(File f){
		try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=512;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale++;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        	e.printStackTrace();
        }
        return null;
	}
	
	public static ArrayList<String> getContainStringArrayPref(Context context,
			String key, String charsequence){
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String json = prefs.getString(key, null);
		ArrayList<String> urls = null;
		if(json != null){
			urls = new ArrayList<String>();
			JSONArray a;
			try {
				a = new JSONArray(json);
				for(int i=0;i<a.length();i++){
					if(a.optString(i).toString().contains(charsequence)){
						String value = a.optString(i);
						urls.add(value);
					}
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

	/**
	 * This method checks that whether the server address field is kept empty or
	 * notkept empty or not If no vlaue is provided it will remind the user of
	 * provisding a valid server address with a error message of server not
	 * found.
	 */

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
			if (networkInfo != null) {
				networkState = networkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED
						|| networkState == NetworkInfo.State.CONNECTING) {
					return true;
				}
			}
			networkInfo = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (networkInfo != null) {
				networkState = networkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED
						|| networkState == NetworkInfo.State.CONNECTING) {
					return true;
				}
			}
			networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				networkState = networkInfo.getState();
				if (networkState == NetworkInfo.State.CONNECTED
						|| networkState == NetworkInfo.State.CONNECTING) {
					return true;
				}
			}
		}
		// CommonTask.ShowMessage(context,context.getString(R.string.networkError));
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

		/*
		 * switch (exceptionCode) { case CommonConstraints.GENERAL_EXCEPTION:
		 * return context.getString(R.string.GeneralExceptionMessage); case
		 * CommonConstraints.CLIENT_PROTOCOL_EXCEPTION: return
		 * context.getString(R.string.ClientProtocolExceptionMessage); case
		 * CommonConstraints.ILLEGAL_STATE_EXCEPTION: return
		 * context.getString(R.string.IllegalStateExceptionMessage); case
		 * CommonConstraints.IO_EXCEPTION: return
		 * context.getString(R.string.IOExceptionMessage); case
		 * CommonConstraints.UNSUPPORTED_ENCODING_EXCEPTION: return context
		 * .getString(R.string.UnsupportedEncodingExceptionMessage); default:
		 * return ""; }
		 */
		return "";
	}

	public static Date parseDate(String input) throws java.text.ParseException {

		/*
		 * Date result = null; input = input.replaceAll("[^0-9]", ""); if
		 * (!TextUtils.isEmpty(input)) { try { result= new
		 * Date(Long.parseLong(input)); } catch (NumberFormatException e) { } }
		 * return result;
		 */

		String result = input.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		return new Date(Long.parseLong(result));

		/*
		 * String timeString = input.substring(input.indexOf("(")+1,
		 * input.indexOf(")")); String timeSegment1 = timeString.substring(0,
		 * timeString.indexOf("+")); String timeSegment2 =
		 * timeString.substring(timeString.indexOf("+")+1); // May have to
		 * handle negative timezones int timeZoneOffSet =
		 * Integer.valueOf(timeSegment2) * 36000; // (("0100" / 100) * 3600 *
		 * 1000) int millis = Integer.valueOf(timeSegment1); return new
		 * Date(millis + timeZoneOffSet);
		 */

	}

	public static String toStringDate(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return df.format(date);

	}
	public static String convertDateToString(Date date) {

		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");

		return df.format(date);

	}
	
	public static long convertDatetoString(String inputDate){
		if(inputDate==null)
			return 0;
		if(inputDate.equals(""))
			return 0;
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
		return Long.parseLong(df.format(inputDate));
	}
	
	public static String convertCallDateToString(String time){
		long millisecond = Long.parseLong(time);
		SimpleDateFormat callDate = new SimpleDateFormat("EEE, dd MMM HH:MM:SS");
		return callDate.format(new Date(millisecond));
	}
	
	public static String convertDateToString(long time){		
		SimpleDateFormat callDate = new SimpleDateFormat("dd-MM-yyyy");
		return callDate.format(new Date(time));
	}
	
	public static String convertDateToStringWithCustomFormat(long time){		
		SimpleDateFormat callDate = new SimpleDateFormat("dd MMM yyyy");
		return callDate.format(new Date(time));
	}
	public static String toMessageDateAsString(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy");
		return df.format(new Date(Long.parseLong(result)));

	}
	
	public static String toMessageDateShortFormat(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");
		return df.format(new Date(Long.parseLong(result)));

	}

	public static String toMessageTimeAsString(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date=new Date(Long.parseLong(result));	
		
		if(date.before(df.parse(df.format(new Date()))))
			df = new SimpleDateFormat("EEE, dd MMM hh:mm:ss aa"); 
		else
			df = new SimpleDateFormat("hh:mm:ss aa");
		return df.format(date);

	}
	public static long convertJsonDateToLong(String inputDate){
		if(inputDate==null)
			return 0;
		if(inputDate.equals(""))
			return 0;
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		return Long.parseLong(result);
	}	
	public static String convertJsonDateToGraphTime(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("HH");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String convertJsonDateToDailyKPI(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String convertJsonDateToTTStatusTime(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String convertJsonDateToLiveAlarm(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd MMM HH:MM:SS");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String convertJsonDateToCriticalAlarmWorkingUser(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("HH:MM");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String convertJsonDateToUserGroup(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String convertJsonDateToDateWithDay(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy hh:mm aa");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	
	

	public static void makeLinkedTextview(Context context,  TextView tv, String text) {

		SpannableString content = new SpannableString(text);
		content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
		tv.setText(content);
		tv.setTextColor(context.getResources().getColor(R.color.blue));
	}
	

	public static String convertJsonDateToMessageTime(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static long convertJsonDateToMessageTimeLong(String inputDate) throws java.text.ParseException{
		if(inputDate==null)
			return 0;
		if(inputDate.equals(""))
			return 0;
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa");
		//return df.format(new Date(Long.parseLong(result)));
		return Long.parseLong(result);
	}
	
	public static String convertJsonDateToTTDetails(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:MM");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	public static String getCurrentDateTimeAsString() {		
		SimpleDateFormat df =new SimpleDateFormat("dd MMM yyyy");		
		return df.format(new Date());
	}
	
	public static String convertJsonDateToDateofBirth(String inputDate)
			throws java.text.ParseException {
		if(inputDate==null)
			return "";
		if(inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
		return df.format(new Date(Long.parseLong(result)));
	}
	
	
	public static boolean isWifiEnabled(Context c){
	       ConnectivityManager cm = (ConnectivityManager) c.getSystemService(
	                            Context.CONNECTIVITY_SERVICE);
	       NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

	       if (wifiNetwork != null && wifiNetwork.isConnected()) {
	    	   
	          return true;
	       }
	    return false;
	}
	
	 public static int distanceCalculationInMeter(double lat1, double lng1, double lat2, double lng2) {
		    double earthRadius = 3958.75;
		    double dLat = Math.toRadians(lat2-lat1);
		    double dLng = Math.toRadians(lng2-lng1);
		    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
		               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
		               Math.sin(dLng/2) * Math.sin(dLng/2);
		    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		    double dist = earthRadius * c;

		    int meterConversion = 1609;

		    return (int) (dist * meterConversion);
	 }
	 
	 

}
