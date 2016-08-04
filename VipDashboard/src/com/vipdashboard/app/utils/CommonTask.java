package com.vipdashboard.app.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Country;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Dialog;
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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author Shafiqul Alam Some common methods are defined in this class that is
 *         used all through the application such as email validation,robotto
 *         font method,cofirmation message,alert message,loading user info etc.
 */

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
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean isMyServiceRunning(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if ("com.vipdashboard.app.base.MyNetService".equals(service.service
					.getClassName())) {
				return true;
			}
		}
		return false;
	}

	public static int getContentPhotoId(Context context, String Number) {
		int photoId = -1;
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(Number));
		Cursor cursor = context.getContentResolver()
				.query(uri,
						new String[] { PhoneLookup.DISPLAY_NAME,
								PhoneLookup.PHOTO_ID }, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			photoId = cursor.getInt(cursor
					.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_ID));
			cursor.close();
		}
		return photoId;
	}
	
	
	
	/**
	 * Decodes and scales a contact's image from a file pointed to by a Uri in the contact's data,
	 * and returns the result as a Bitmap. The column that contains the Uri varies according to the
	 * platform version.
	 *
	 * @param photoData For platforms prior to Android 3.0, provide the Contact._ID column value.
	 *                  For Android 3.0 and later, provide the Contact.PHOTO_THUMBNAIL_URI value.
	 * @param imageSize The desired target width and height of the output image in pixels.
	 * @return A Bitmap containing the contact's image, resized to fit the provided image size. If
	 * no thumbnail exists, returns null.
	 */
	public static Bitmap loadContactPhotoThumbnail(Context context,String Number) {

	    // Ensures the Fragment is still added to an activity. As this method is called in a
	    // background thread, there's the possibility the Fragment is no longer attached and
	    // added to an activity. If so, no need to spend resources loading the contact photo.
	    

	    // Instantiates an AssetFileDescriptor. Given a content Uri pointing to an image file, the
	    // ContentResolver can return an AssetFileDescriptor for the file.
	    InputStream stream = null;

	    // This "try" block catches an Exception if the file descriptor returned from the Contacts
	    // Provider doesn't point to an existing file.
	    try {
	    	String  photoData="";
	    	Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,Uri.encode(Number));	    	
			Cursor cursor = context.getContentResolver().query(uri,new String[] { Phone._ID,	Phone.PHOTO_THUMBNAIL_URI }, null, null, null);
			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				if (android.os.Build.VERSION.SDK_INT <=android.os.Build.VERSION_CODES.HONEYCOMB) {
					photoData = cursor.getString(cursor.getColumnIndex( Phone._ID));
				}else{
					photoData = cursor.getString(cursor.getColumnIndex( Phone.PHOTO_THUMBNAIL_URI));
				}
				
				cursor.close();
			}
			
			
	        Uri thumbUri;
	        // If Android 3.0 or later, converts the Uri passed as a string to a Uri object.
	       
	        if (android.os.Build.VERSION.SDK_INT >android.os.Build.VERSION_CODES.HONEYCOMB) {
	            thumbUri = Uri.parse(photoData);
	        } else {
	            // For versions prior to Android 3.0, appends the string argument to the content
	            // Uri for the Contacts table.
	            final Uri contactUri = Uri.withAppendedPath(Contacts.CONTENT_URI, photoData);

	            // Appends the content Uri for the Contacts.Photo table to the previously
	            // constructed contact Uri to yield a content URI for the thumbnail image
	            thumbUri = Uri.withAppendedPath(contactUri, Photo.CONTENT_DIRECTORY);
	        }
	        
	        stream = context.getContentResolver().openInputStream(thumbUri);

	        return BitmapFactory.decodeStream(stream);
	    } catch (FileNotFoundException e) {
	        // If the file pointed to by the thumbnail URI doesn't exist, or the file can't be
	        // opened in "read" mode, ContentResolver.openAssetFileDescriptor throws a
	        // FileNotFoundException.
	        
	    } finally {
	        // If an AssetFileDescriptor was returned, try to close it
	        if (stream != null) {
	            try {
	                stream.close();
	            } catch (IOException e) {
	                // Closing a file descriptor might cause an IOException if the file is
	                // already closed. Nothing extra is needed to handle this.
	            }
	        }
	    }

	    // If the decoding failed, returns null
	    return null;
	}

	public static Bitmap getContactImage(Context context, String phoneNumber) {
		final String[] PHOTO_ID_PROJECTION = new String[] { ContactsContract.Contacts._ID };

		final String[] PHOTO_BITMAP_PROJECTION = new String[] { ContactsContract.CommonDataKinds.Photo.PHOTO };
		ContentResolver contentResolver = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(
				ContactsContract.CommonDataKinds.Phone.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = contentResolver.query(uri, PHOTO_ID_PROJECTION, null,
				null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

		try {
			if (cursor.moveToFirst()) {
				Integer thumbnailId = cursor.getInt(cursor
						.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
				uri = ContentUris.withAppendedId(
						ContactsContract.Data.CONTENT_URI, thumbnailId);
				cursor = contentResolver.query(uri, PHOTO_BITMAP_PROJECTION,
						null, null, null);
				Bitmap thumbnail = null;
				if (cursor.moveToFirst()) {
					final byte[] thumbnailBytes = cursor.getBlob(0);
					if (thumbnailBytes != null) {
						thumbnail = BitmapFactory.decodeByteArray(
								thumbnailBytes, 0, thumbnailBytes.length);
					}
				}
				return thumbnail;
			}

		} finally {
			cursor.close();
		}
		return null;
	}

	private static final String[] PHOTO_BITMAP_PROJECTION = new String[] { ContactsContract.CommonDataKinds.Photo.PHOTO };

	public static Bitmap fetchContactImageThumbnail(Context context,final int thumbnailId) {
		if (android.os.Build.VERSION.SDK_INT >18) {
			try {
				String[] projection = { MediaStore.Images.Media.DATA };
		            String whereClause = MediaStore.Images.Media._ID + "=?";
		            String IMAGE_FILEPATH="";
		            Cursor cursor =context. getContentResolver().query(getUri(), projection, whereClause, new String[]{String.valueOf( thumbnailId)}, null);
		            if( cursor != null ){
		                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		                if (cursor.moveToFirst()) {
		                    IMAGE_FILEPATH = cursor.getString(column_index);
		                }

		                cursor.close();
		            } 
		            
		            File imgFile = new  File(IMAGE_FILEPATH);
		            if(imgFile.exists()){

		               return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
		            }
		        
		    } catch (Exception e) {
		       
		    }
		}else{
			final Uri uri = ContentUris.withAppendedId(	ContactsContract.Data.CONTENT_URI, thumbnailId);
			ContentResolver contentResolver = context.getContentResolver();
			final Cursor cursor = contentResolver.query(uri,
					PHOTO_BITMAP_PROJECTION, null, null, null);
	
			try {
				Bitmap thumbnail = null;
				if (cursor != null) {
					if (cursor.moveToFirst()) {
						final byte[] thumbnailBytes = cursor.getBlob(0);
						if (thumbnailBytes != null) {
							thumbnail = BitmapFactory.decodeByteArray(
									thumbnailBytes, 0, thumbnailBytes.length);
						}
					}
				}
				return thumbnail;
			} catch (Exception ex) {
				ex.printStackTrace();
			} finally {
				cursor.close();
			}
		}

		return null;

	}
	
	private static Uri getUri() {
	    String state = Environment.getExternalStorageState();
	    if(!state.equalsIgnoreCase(Environment.MEDIA_MOUNTED))
	        return MediaStore.Images.Media.INTERNAL_CONTENT_URI;

	    return MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
	}

	public static Uri getContactPhotoUri(long contactId) {
		Uri photoUri = ContentUris.withAppendedId(
				ContactsContract.Contacts.CONTENT_URI, contactId);
		photoUri = Uri.withAppendedPath(photoUri,
				ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
		return photoUri;
	}

	public static Bitmap getBitmapFromURL(final String src) {
		new AsyncTask<Void, Void, Bitmap>() {

			@Override
			protected Bitmap doInBackground(Void... params) {
				try {
					URL url = new URL(src);
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setDoInput(true);
					connection.connect();
					InputStream input = connection.getInputStream();
					CommonConstraints.ProfilePicture = BitmapFactory
							.decodeStream(input);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return CommonConstraints.ProfilePicture;
			}
		}.execute();
		return CommonConstraints.ProfilePicture;
	}

	public static Bitmap getBitmapImage(String url) {
		try {
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 8 * 512);
			Bitmap bmp = BitmapFactory.decodeStream(bis);
			bis.close();
			is.close();
			return bmp;

		} catch (Exception e) {
		}
		return null;
	}

	public static InputStream getInputStream(String url) {
		try {
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			return conn.getInputStream();

		} catch (Exception e) {
		}
		return null;
	}

	public static int getDownloadSpeed(String url) {
		int len = 0;
		try {

			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is, 1024);
			ByteArrayBuffer baf = new ByteArrayBuffer(1024);
			int current = 0;
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			len = baf.length();
			bis.close();
			is.close();

		} catch (Exception e) {
		}
		return len;
	}

	public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		int targetWidth = scaleBitmapImage.getWidth() - 10;
		int targetHeight = scaleBitmapImage.getHeight() - 10;
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

	public static String getContentName(Context context, String Number) {
		/*
		 * String name = ""; Uri uri =
		 * Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
		 * Uri.encode(Number)); Cursor cursor =
		 * context.getContentResolver().query(uri, new
		 * String[]{PhoneLookup.DISPLAY_NAME}, null, null, null); if(cursor !=
		 * null && cursor.getCount() > 0){ cursor.moveToFirst(); name =
		 * cursor.getString
		 * (cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)); }
		 * cursor.close(); return name;
		 */
		Uri uri;
		String[] projection;
		Uri mBaseUri = Contacts.Phones.CONTENT_FILTER_URL;
		projection = new String[] { android.provider.Contacts.People.NAME };
		try {
			Class<?> c = Class
					.forName("android.provider.ContactsContract$PhoneLookup");
			mBaseUri = (Uri) c.getField("CONTENT_FILTER_URI").get(mBaseUri);
			projection = new String[] { "display_name" };
		} catch (Exception e) {
		}

		uri = Uri.withAppendedPath(mBaseUri, Uri.encode(Number));
		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);

		String contactName = "";

		if (cursor.moveToFirst()) {
			contactName = cursor.getString(0);
		}

		cursor.close();
		cursor = null;

		return contactName;

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

	public static void saveLinkedInPreferencesForLogIn(Context context,
			String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static void savePreferenceForCallNOC(Context context, String Key,
			String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor callNocEditor = sharedPreferences.edit();
		callNocEditor.putString(Key, value);
		callNocEditor.commit();
	}

	public static void savePreferenceForCallManager(Context context,
			String Key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(context);
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

	public static Bitmap decodeImage(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale++;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static ArrayList<String> getContainStringArrayPref(Context context,
			String key, String charsequence) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String json = prefs.getString(key, null);
		ArrayList<String> urls = null;
		if (json != null) {
			urls = new ArrayList<String>();
			JSONArray a;
			try {
				a = new JSONArray(json);
				for (int i = 0; i < a.length(); i++) {
					if (a.optString(i).toString().contains(charsequence)) {
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
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
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

	public static String convertCallDateToString(String time) {
		long millisecond = Long.parseLong(time);
		SimpleDateFormat callDate = new SimpleDateFormat("EEE, dd MMM HH:MM:SS");
		return callDate.format(new Date(millisecond));
	}

	public static String toMessageDateAsString(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy");
		return df.format(new Date(Long.parseLong(result)));

	}

	public static String toMessageTimeAsString(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date(Long.parseLong(result));

		if (date.before(df.parse(df.format(new Date()))))
			df = new SimpleDateFormat("EEE, dd MMM hh:mm:ss aa");
		else
			df = new SimpleDateFormat("hh:mm:ss aa");
		return df.format(date);

	}

	public static long convertJsonDateToLong(String inputDate) {
		if (inputDate == null)
			return 0;
		if (inputDate.equals(""))
			return 0;
		String result = inputDate.replaceAll("^/Date\\(", "");

		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		return Long.parseLong(result);
	}

	public static String convertJsonDateToGraphTime(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("HH");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToDailyKPI(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToTTStatusTime(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToLiveAlarm(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd MMM HH:MM:SS");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToCriticalAlarmWorkingUser(
			String inputDate) throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("HH:MM");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToUserGroup(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToDateWithDay(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM yyyy hh:mm aa");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static void makeLinkedTextview(Context context, TextView tv,
			String text) {

		SpannableString content = new SpannableString(text);
		content.setSpan(new UnderlineSpan(), 0, text.length(), 0);
		tv.setText(content);
		tv.setTextColor(context.getResources().getColor(R.color.blue));
	}

	public static String convertJsonDateToMessageTime(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss aa");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String convertJsonDateToTTDetails(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy HH:MM");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String getCurrentDateTimeAsString() {
		SimpleDateFormat df = new SimpleDateFormat("EEE dd MMM hh:mm:ss aa");
		return df.format(new Date());
	}

	public static String convertJsonDateToDateofBirth(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
		return df.format(new Date(Long.parseLong(result)));
	}

	public static String toMessageDateShortFormat(String inputDate)
			throws java.text.ParseException {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		SimpleDateFormat df = new SimpleDateFormat("dd MMM yy");

		String resultDate = df.format(new Date(Long.parseLong(result)));
		String toDay = df.format(new Date(System.currentTimeMillis()));
		if (toDay.equals(resultDate))
			resultDate = "Today";
		return resultDate;

	}

	public static boolean isWifiEnabled(Context c) {
		ConnectivityManager cm = (ConnectivityManager) c
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiNetwork = cm
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (wifiNetwork != null && wifiNetwork.isConnected()) {

			return true;
		}
		return false;
	}

	public static String ConvertUTCtoLocalDateTimeString(String inputDate) {
		if (inputDate == null)
			return "";
		if (inputDate.equals(""))
			return "";
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));

		long ts = Long.parseLong(result);// System.currentTimeMillis();
		Date localTime = new Date(ts);
		String format = "yyyy/MM/dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);

		// Convert Local Time to UTC (Works Fine)
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date gmtTime = new Date(sdf.format(localTime));

		return String.valueOf(gmtTime);
	}

	/*
	 * public static void DryServerConnectionivityMessage(final Context
	 * context,String message) { AlertDialog.Builder builder = new
	 * AlertDialog.Builder(context);
	 * builder.setTitle(R.string.app_name).setMessage(message)
	 * .setCancelable(false) .setNegativeButton("OK", new
	 * DialogInterface.OnClickListener() { public void onClick(DialogInterface
	 * dialog, int id) { dialog.dismiss(); } }); AlertDialog alert =
	 * builder.create(); alert.show(); }
	 */

	public static void DryConnectivityMessage(final Context context,
			String message) {
		/*
		 * AlertDialog.Builder builder = new AlertDialog.Builder(context);
		 * builder.setTitle(R.string.app_name).setMessage(message)
		 * .setCancelable(false) .setNegativeButton("OK", new
		 * DialogInterface.OnClickListener() { public void
		 * onClick(DialogInterface dialog, int id) { Intent intent = new
		 * Intent(context, LoginActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 * context.startActivity(intent); context.startActivity(new
		 * Intent(android.provider.Settings.ACTION_SETTINGS)); } }); AlertDialog
		 * alert = builder.create(); alert.show();
		 */

		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.internet_connectivity_custom);
		dialog.setTitle("Connection Dry!!!");

		// set the custom dialog components - text, image and button
		TextView text = (TextView) dialog.findViewById(R.id.text);
		text.setText(message);
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.dry_internet);

		Button dialogTryagain = (Button) dialog
				.findViewById(R.id.dialogTryagain);
		Button dialogSettings = (Button) dialog
				.findViewById(R.id.dialogSettings);
		Button dialogCancel = (Button) dialog.findViewById(R.id.dialogCancel);
		// if button is clicked, close the custom dialog
		dialogTryagain.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (CommonTask.isOnline(context)) {
					dialog.dismiss();

				}

			}
		});

		// if button is clicked, close the custom dialog
		dialogSettings.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
				context.startActivity(new Intent(
						android.provider.Settings.ACTION_SETTINGS));
			}
		});

		dialogCancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public static long convertDatetoLong(String inputDate) {
		if (inputDate == null)
			return 0;
		if (inputDate.equals(""))
			return 0;
		String result = inputDate.replaceAll("^/Date\\(", "");
		result = result.replace("-", "+");
		if (result.indexOf('+') > -1)
			result = result.substring(0, result.indexOf('+'));
		// SimpleDateFormat df = new SimpleDateFormat("dd MMM yyyy hh:mm aa");

		// Coded here because of local time conversion from UTC ----START---

		/*
		 * long ts = Long.parseLong(result);//System.currentTimeMillis(); Date
		 * localTime = new Date(ts); String format = "yyyy/MM/dd HH:mm:ss";
		 * SimpleDateFormat sdf = new SimpleDateFormat(format);
		 * 
		 * // Convert Local Time to UTC (Works Fine)
		 * sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
		 * 
		 * Date gmtTime = new Date(sdf.format(localTime));
		 * result=String.valueOf(gmtTime.getTime());
		 */

		/*
		 * System.out.println("Local:" + localTime.toString() + "," +
		 * localTime.getTime() + " --> UTC time:" + gmtTime.toString() + "," +
		 * gmtTime.getTime());
		 */

		// **** YOUR CODE **** END ****

		/*
		 * // Convert UTC to Local Time Date fromGmt = new
		 * Date(gmtTime.getTime() +
		 * TimeZone.getDefault().getOffset(localTime.getTime()));
		 * System.out.println("UTC time:" + gmtTime.toString() + "," +
		 * gmtTime.getTime() + " --> Local:" + fromGmt.toString() + "-" +
		 * fromGmt.getTime());
		 */

		/*
		 * Date fromGmt = new Date(gmtTime.getTime() +
		 * TimeZone.getDefault().getOffset(localTime.getTime()));
		 */

		/*
		 * SimpleDateFormat simpleDateFormat = new
		 * SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 * simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC")); Date
		 * myDate = null; try { myDate =
		 * simpleDateFormat.parse(String.valueOf(result)); } catch
		 * (ParseException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } result=String.valueOf(myDate.toString());
		 */

		// Coded here because of local time conversion from UTC ----END---

		return Long.parseLong(result);
	}

	public static boolean isInternationalPhoneNumber(Context context,
			String strPhoneNumer) {

		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

		Phonenumber.PhoneNumber number;
		try {
			number = phoneNumberUtil.parse(strPhoneNumer, tm
					.getNetworkCountryIso().toUpperCase());
			if (number.getCountryCode() != phoneNumberUtil
					.getCountryCodeForRegion(tm.getNetworkCountryIso()
							.toUpperCase()))
				return true;
		} catch (NumberParseException e) {
			return false;
		}

		return false;
	}

	public static String GetOffsetFromCurrentGMT() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"),
				Locale.getDefault());
		Date currentLocalTime = calendar.getTime();
		DateFormat date = new SimpleDateFormat("Z");
		String localTime = date.format(currentLocalTime);
		return localTime;
	}

	public static String trimTentativeCountryCode(String phoneNumber) {
		if (phoneNumber == null)
			return "";
		if (phoneNumber.contains("+") && phoneNumber.length() > 3) {
			phoneNumber = phoneNumber.substring(3);
		} else if (phoneNumber.startsWith("00")) {
			phoneNumber = phoneNumber.substring(2);
		}

		return phoneNumber;
	}

	public static ArrayList<Country> getCoutryList() {
		ArrayList<Country> countryList = new ArrayList<Country>();
		countryList.add(new Country("", "Please select country", ""));
		countryList.add(new Country("AF", "Afghanistan", "+93"));
		countryList.add(new Country("AL", "Albania", "+355"));
		countryList.add(new Country("DZ", "Algeria", "+213"));
		countryList.add(new Country("AS", "American Samoa", "+1684"));
		countryList.add(new Country("AD", "Andorra", "+376"));
		countryList.add(new Country("AO", "Angola", "+244"));
		countryList.add(new Country("AI", "Anguilla", "+1264"));
		countryList.add(new Country("AQ", "Antarctica", "+672"));
		countryList.add(new Country("AG", "Antigua and Barbuda", "+1268"));
		countryList.add(new Country("AR", "Argentina", "+54"));
		countryList.add(new Country("AM", "Armenia", "+374"));
		countryList.add(new Country("AW", "Aruba", "+297"));
		countryList.add(new Country("AU", "Australia", "+61"));
		countryList.add(new Country("AT", "Austria", "+43"));
		countryList.add(new Country("AZ", "Azerbaijan", "+994"));
		countryList.add(new Country("BS", "Bahamas", "+1 242"));
		countryList.add(new Country("BH", "Bahrain", "+973"));
		countryList.add(new Country("BD", "Bangladesh", "+880"));
		countryList.add(new Country("BB", "Barbados", "+1 246"));
		countryList.add(new Country("BY", "Belarus", "+375"));
		countryList.add(new Country("BE", "Belgium", "+32"));
		countryList.add(new Country("BZ", "Belize", "+501"));
		countryList.add(new Country("BJ", "Benin", "+229"));
		countryList.add(new Country("BM", "Bermuda", "+1 441"));
		countryList.add(new Country("BT", "Bhutan", "+975"));
		countryList.add(new Country("BO", "Bolivia", "+591"));
		countryList.add(new Country("BA", "Bosnia and Herzegovina", "+387"));
		countryList.add(new Country("BW", "Botswana", "+267"));
		countryList.add(new Country("BR", "Brazil", "+55"));
		countryList.add(new Country("VG", "British Virgin Islands", "+1284"));
		countryList.add(new Country("BN", "Brunei", "+673"));
		countryList.add(new Country("BG", "Bulgaria", "+359"));
		countryList.add(new Country("BF", "Burkina Faso", "+226"));
		countryList.add(new Country("MM", "Burma (Myanmar)", "+95"));
		countryList.add(new Country("BI", "Burundi", "+257"));
		countryList.add(new Country("KH", "Cambodia", "+855"));
		countryList.add(new Country("CM", "Cameroon", "+237"));
		countryList.add(new Country("CA", "Canada", "+1"));
		countryList.add(new Country("CV", "Cape Verde", "+238"));
		countryList.add(new Country("KY", "Cayman Islands", "+1 345"));
		countryList.add(new Country("CF", "Central African Republic", "+236"));
		countryList.add(new Country("TD", "Chad", "+235"));
		countryList.add(new Country("CL", "Chile", "+56"));
		countryList.add(new Country("CN", "China", "+86"));
		countryList.add(new Country("CX", "Christmas Island", "+61"));
		countryList.add(new Country("CC", "Cocos (Keeling) Islands", "+61"));
		countryList.add(new Country("CO", "Colombia", "+57"));
		countryList.add(new Country("KM", "Comoros", "+269"));
		countryList.add(new Country("CK", "Cook Islands", "+682"));
		countryList.add(new Country("CR", "Costa Rica", "+506"));
		countryList.add(new Country("HR", "Croatia", "+385"));
		countryList.add(new Country("CU", "Cuba", "+53"));
		countryList.add(new Country("CY", "Cyprus", "+357"));
		countryList.add(new Country("CZ", "Czech Republic", "+420"));
		countryList.add(new Country("CD", "Democratic Republic of the Congo",
				"+243"));
		countryList.add(new Country("DK", "Denmark", "+45"));
		countryList.add(new Country("DJ", "Djibouti", "+253"));
		countryList.add(new Country("DM", "Dominica", "+1 767"));
		countryList.add(new Country("DO", "Dominican Republic", "+1809"));
		countryList.add(new Country("EC", "Ecuador", "+593"));
		countryList.add(new Country("EG", "Egypt", "+20"));
		countryList.add(new Country("SV", "El Salvador", "+503"));
		countryList.add(new Country("GQ", "Equatorial Guinea", "+240"));
		countryList.add(new Country("ER", "Eritrea", "+291"));
		countryList.add(new Country("EE", "Estonia", "+372"));
		countryList.add(new Country("ET", "Ethiopia", "+251"));
		countryList.add(new Country("FK", "Falkland Islands", "+500"));
		countryList.add(new Country("FO", "Faroe Islands", "+298"));
		countryList.add(new Country("FJ", "Fiji", "+679"));
		countryList.add(new Country("FI", "Finland", "+358"));
		countryList.add(new Country("FR", "France", "+33"));
		countryList.add(new Country("PF", "French Polynesia", "+689"));
		countryList.add(new Country("GA", "Gabon", "+241"));
		countryList.add(new Country("GM", "Gambia", "+220"));
		countryList.add(new Country("GE", "Georgia", "+995"));
		countryList.add(new Country("DE", "Germany", "+49"));
		countryList.add(new Country("GH", "Ghana", "+233"));
		countryList.add(new Country("GI", "Gibraltar", "+350"));
		countryList.add(new Country("GR", "Greece", "+30"));
		countryList.add(new Country("GL", "Greenland", "+299"));
		countryList.add(new Country("GD", "Grenada", "+1 473"));
		countryList.add(new Country("GU", "Guam", "+1 671"));
		countryList.add(new Country("GT", "Guatemala", "+502"));
		countryList.add(new Country("GN", "Guinea", "+224"));
		countryList.add(new Country("GW", "Guinea-Bissau", "+245"));
		countryList.add(new Country("GY", "Guyana", "+592"));
		countryList.add(new Country("HT", "Haiti", "+509"));
		countryList.add(new Country("VA", "Holy See (Vatican City)", "+39"));
		countryList.add(new Country("HN", "Honduras", "+504"));
		countryList.add(new Country("HK", "Hong Kong", "+852"));
		countryList.add(new Country("HU", "Hungary", "+36"));
		countryList.add(new Country("IS", "Iceland", "+354"));
		countryList.add(new Country("IN", "India", "+91"));
		countryList.add(new Country("ID", "Indonesia", "+62"));
		countryList.add(new Country("IR", "Iran", "+98"));
		countryList.add(new Country("IQ", "Iraq", "+964"));
		countryList.add(new Country("IE", "Ireland", "+353"));
		countryList.add(new Country("IM", "Isle of Man", "+44"));
		countryList.add(new Country("IL", "Israel", "+972"));
		countryList.add(new Country("IT", "Italy", "+39"));
		countryList.add(new Country("CI", "Ivory Coast", "+225"));
		countryList.add(new Country("JM", "Jamaica", "+1 876"));
		countryList.add(new Country("JP", "Japan", "+81"));
		countryList.add(new Country("JE", "Jersey", "+"));
		countryList.add(new Country("JO", "Jordan", "+962"));
		countryList.add(new Country("KZ", "Kazakhstan", "+7"));
		countryList.add(new Country("KE", "Kenya", "+254"));
		countryList.add(new Country("KI", "Kiribati", "+686"));
		countryList.add(new Country("KW", "Kuwait", "+965"));
		countryList.add(new Country("KG", "Kyrgyzstan", "+996"));
		countryList.add(new Country("LA", "Laos", "+856"));
		countryList.add(new Country("LV", "Latvia", "+371"));
		countryList.add(new Country("LB", "Lebanon", "+961"));
		countryList.add(new Country("LS", "Lesotho", "+266"));
		countryList.add(new Country("LR", "Liberia", "+231"));
		countryList.add(new Country("LY", "Libya", "+218"));
		countryList.add(new Country("LI", "Liechtenstein", "+423"));
		countryList.add(new Country("LT", "Lithuania", "+370"));
		countryList.add(new Country("LU", "Luxembourg", "+352"));
		countryList.add(new Country("MO", "Macau", "+853"));
		countryList.add(new Country("MK", "Macedonia", "+389"));
		countryList.add(new Country("MG", "Madagascar", "+261"));
		countryList.add(new Country("MW", "Malawi", "+265"));
		countryList.add(new Country("MY", "Malaysia", "+60"));
		countryList.add(new Country("MV", "Maldives", "+960"));
		countryList.add(new Country("ML", "Mali", "+223"));
		countryList.add(new Country("MT", "Malta", "+356"));
		countryList.add(new Country("MH", "Marshall Islands", "+692"));
		countryList.add(new Country("MR", "Mauritania", "+222"));
		countryList.add(new Country("MU", "Mauritius", "+230"));
		countryList.add(new Country("YT", "Mayotte", "+262"));
		countryList.add(new Country("MX", "Mexico", "+52"));
		countryList.add(new Country("FM", "Micronesia", "+691"));
		countryList.add(new Country("MD", "Moldova", "+373"));
		countryList.add(new Country("MC", "Monaco", "+377"));
		countryList.add(new Country("MN", "Mongolia", "+976"));
		countryList.add(new Country("ME", "Montenegro", "+382"));
		countryList.add(new Country("MS", "Montserrat", "+1 664"));
		countryList.add(new Country("MA", "Morocco", "+212"));
		countryList.add(new Country("MZ", "Mozambique", "+258"));
		countryList.add(new Country("NA", "Namibia", "+264"));
		countryList.add(new Country("NR", "Nauru", "+674"));
		countryList.add(new Country("NP", "Nepal", "+977"));
		countryList.add(new Country("NL", "Netherlands", "+31"));
		countryList.add(new Country("AN", "Netherlands Antilles", "+599"));
		countryList.add(new Country("NC", "New Caledonia", "+687"));
		countryList.add(new Country("NZ", "New Zealand", "+64"));
		countryList.add(new Country("NI", "Nicaragua", "+505"));
		countryList.add(new Country("NE", "Niger", "+227"));
		countryList.add(new Country("NG", "Nigeria", "+234"));
		countryList.add(new Country("NU", "Niue", "+683"));
		countryList.add(new Country("KP", "North Korea", "+850"));
		countryList.add(new Country("MP", "Northern Mariana Islands", "+1670"));
		countryList.add(new Country("NO", "Norway", "+47"));
		countryList.add(new Country("OM", "Oman", "+968"));
		countryList.add(new Country("PK", "Pakistan", "+92"));
		countryList.add(new Country("PW", "Palau", "+680"));
		countryList.add(new Country("PA", "Panama", "+507"));
		countryList.add(new Country("PG", "Papua New Guinea", "+675"));
		countryList.add(new Country("PY", "Paraguay", "+595"));
		countryList.add(new Country("PE", "Peru", "+51"));
		countryList.add(new Country("PH", "Philippines", "+63"));
		countryList.add(new Country("PN", "Pitcairn Islands", "+870"));
		countryList.add(new Country("PL", "Poland", "+48"));
		countryList.add(new Country("PT", "Portugal", "+351"));
		countryList.add(new Country("PR", "Puerto Rico", "+1"));
		countryList.add(new Country("QA", "Qatar", "+974"));
		countryList.add(new Country("CG", "Republic of the Congo", "+242"));
		countryList.add(new Country("RO", "Romania", "+40"));
		countryList.add(new Country("RU", "Russia", "+7"));
		countryList.add(new Country("RW", "Rwanda", "+250"));
		countryList.add(new Country("BL", "Saint Barthelemy", "+590"));
		countryList.add(new Country("SH", "Saint Helena", "+290"));
		countryList.add(new Country("KN", "Saint Kitts and Nevis", "+1869"));
		countryList.add(new Country("LC", "Saint Lucia", "+1758"));
		countryList.add(new Country("MF", "Saint Martin", "+1599"));
		countryList.add(new Country("PM", "Saint Pierre and Miquelon", "+508"));
		countryList.add(new Country("VC", "Saint Vincent and the Grenadines",
				"+1784"));
		countryList.add(new Country("WS", "Samoa", "+685"));
		countryList.add(new Country("SM", "San Marino", "+378"));
		countryList.add(new Country("ST", "Sao Tome and Principe", "+239"));
		countryList.add(new Country("SA", "Saudi Arabia", "+966"));
		countryList.add(new Country("SN", "Senegal", "+221"));
		countryList.add(new Country("RS", "Serbia", "+381"));
		countryList.add(new Country("SC", "Seychelles", "+248"));
		countryList.add(new Country("SL", "Sierra Leone", "+232"));
		countryList.add(new Country("SG", "Singapore", "+65"));
		countryList.add(new Country("SK", "Slovakia", "+421"));
		countryList.add(new Country("SI", "Slovenia", "+386"));
		countryList.add(new Country("SB", "Solomon Islands", "+677"));
		countryList.add(new Country("SO", "Somalia", "+252"));
		countryList.add(new Country("ZA", "South Africa", "+27"));
		countryList.add(new Country("KR", "South Korea", "+82"));
		countryList.add(new Country("ES", "Spain", "+34"));
		countryList.add(new Country("LK", "Sri Lanka", "+94"));
		countryList.add(new Country("SD", "Sudan", "+249"));
		countryList.add(new Country("SR", "Suriname", "+597"));
		countryList.add(new Country("SJ", "Svalbard", "0"));
		countryList.add(new Country("SZ", "Swaziland", "+268"));
		countryList.add(new Country("SE", "Sweden", "+46"));
		countryList.add(new Country("CH", "Switzerland", "+41"));
		countryList.add(new Country("SY", "Syria", "+963"));
		countryList.add(new Country("TW", "Taiwan", "+886"));
		countryList.add(new Country("TJ", "Tajikistan", "+992"));
		countryList.add(new Country("TZ", "Tanzania", "+255"));
		countryList.add(new Country("TH", "Thailand", "+66"));
		countryList.add(new Country("TL", "Timor-Leste", "+670"));
		countryList.add(new Country("TG", "Togo", "+228"));
		countryList.add(new Country("TK", "Tokelau", "+690"));
		countryList.add(new Country("TO", "Tonga", "+676"));
		countryList.add(new Country("TT", "Trinidad and Tobago", "+1868"));
		countryList.add(new Country("TN", "Tunisia", "+216"));
		countryList.add(new Country("TR", "Turkey", "+90"));
		countryList.add(new Country("TM", "Turkmenistan", "+993"));
		countryList.add(new Country("TC", "Turks and Caicos Islands", "+1649"));
		countryList.add(new Country("TV", "Tuvalu", "+688"));
		countryList.add(new Country("UG", "Uganda", "+256"));
		countryList.add(new Country("UA", "Ukraine", "+380"));
		countryList.add(new Country("AE", "United Arab Emirates", "+971"));
		countryList.add(new Country("GB", "United Kingdom", "+44"));
		countryList.add(new Country("US", "United States", "+1"));
		countryList.add(new Country("UY", "Uruguay", "+598"));
		countryList.add(new Country("VI", "US Virgin Islands", "+1340"));
		countryList.add(new Country("UZ", "Uzbekistan", "+998"));
		countryList.add(new Country("VU", "Vanuatu", "+678"));
		countryList.add(new Country("VE", "Venezuela", "+58"));
		countryList.add(new Country("VN", "Vietnam", "+84"));
		countryList.add(new Country("WF", "Wallis and Futuna", "+681"));
		countryList.add(new Country("YE", "Yemen", "+967"));
		countryList.add(new Country("ZM", "Zambia", "+260"));
		countryList.add(new Country("ZW", "Zimbabwe", "+263"));

		return countryList;
	}

	public static int distanceCalculationInMeter(double lat1, double lng1,
			double lat2, double lng2) {
		double earthRadius = 3958.75;
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;

		int meterConversion = 1609;

		return (int) (dist * meterConversion);
	}

	public static Integer TryParseInt(String someText) {
		try {
			return Integer.parseInt(someText);
		} catch (NumberFormatException ex) {
			return 0;
		}
	}

	public static Double TryParseDouble(String someText) {
		try {
			return Double.valueOf(someText.trim()).doubleValue();
		} catch (NumberFormatException nfe) {
			return 0.0;
		}
	}

	public static Long TryParseLong(String someText) {
		try {
			return Long.parseLong(someText);
		} catch (NumberFormatException ex) {
			return (long) 0;
		}
	}

	public static int dpToPx(Context context, int dp) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int px = Math.round(dp
				* (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return px;
	}

	public static int pxToDp(Context context, int px) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		int dp = Math.round(px
				/ (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		return dp;
	}

}
