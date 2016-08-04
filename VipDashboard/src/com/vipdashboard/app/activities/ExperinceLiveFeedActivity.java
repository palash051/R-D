package com.vipdashboard.app.activities;




import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.Session;
import com.google.gson.Gson;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LiveFeedAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.MyNetPost;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeeds;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;

public class ExperinceLiveFeedActivity extends MainActionbarBase implements OnClickListener{
	
	public static byte[] selectedFile;
	public static String filename = "";
	MyNetPost myNetPost;
	
	ListView lvLiveFeedList;
	TextView tvAddFeed,tvRefreshData;
	
	String URL_FEED="";
	Gson gson = null;	
	
	LiveFeedAdapter liveFeedListAdapter;
	private boolean click;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (!locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			showMessage("Your Location service is not available.\nPlease enable first.");
		}
		if (!CommonTask.isOnline(this)) {
			showMessage("Network connection error.\nPlease enable your connection first.");
		} else {
			setContentView(R.layout.live_experince_feed_main);	
			gson = new Gson();
			initialization();			
		}
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		LoadData();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	public void onClick(View v) {
		int id=v.getId();
		if (id == R.id.tvAddFeed) {
			if (myNetPost == null)
				myNetPost = new MyNetPost(this, this);
			myNetPost.showPost();
		}else if (id == R.id.tvRefreshData) {
			loadDataFromServer();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			if (resultCode == RESULT_OK) {
				Uri currImageURI = data.getData();
				if (requestCode == 1) {
					File file = new File(getRealPathFromURI(currImageURI));
					filename = file.getName().replaceAll("[-+^:,]", "")
							.replace(" ", "");
					Bitmap b = CommonTask.decodeImage(file);
					
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
					if (b.getByteCount() > (1024 * 1024)) {
						b.compress(Bitmap.CompressFormat.JPEG, 20, stream);
					}
					if (b.getByteCount() > (1024 * 512)) {
						b.compress(Bitmap.CompressFormat.JPEG, 40, stream);
					}
					if (b.getByteCount() > (1024 * 256)) {
						b.compress(Bitmap.CompressFormat.JPEG, 60, stream);
					} else {
						b.compress(Bitmap.CompressFormat.JPEG, 100, stream);
					}
					selectedFile = stream.toByteArray();
					if (myNetPost.ivPostImage != null)
						myNetPost.ivPostImage.setImageBitmap(b);
				} else {
					filename = currImageURI.getLastPathSegment()
							.replaceAll("[-+^:,]", "").replace(" ", "");
					;
					InputStream inputStream = new FileInputStream(
							currImageURI.getPath());
					selectedFile = convertInputStreamToByteArray(inputStream);
				}
			}
		} catch (Exception e) {
			
		}
		try {
			Session.getActiveSession().onActivityResult(this, requestCode,
					resultCode, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void initialization() {

		tvAddFeed = (TextView) findViewById(R.id.tvAddFeed);
		
		tvRefreshData = (TextView) findViewById(R.id.tvRefreshData);

		lvLiveFeedList = (ListView) findViewById(R.id.lvFeedList);

		tvAddFeed.setOnClickListener(this);
		tvRefreshData.setOnClickListener(this);
		
		liveFeedListAdapter = new LiveFeedAdapter(this,
				R.layout.live_feed_item_layout, new ArrayList<LiveFeed>());
		lvLiveFeedList.setAdapter(liveFeedListAdapter);
	}
	private void LoadData() {		
		URL_FEED=String.format(CommonURL.getInstance().GetAllParentLiveFeedByUserNumber,CommonValues.getInstance().LoginUser.UserNumber,0);
		Cache cache = MyNetApplication.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} 		
		loadDataFromServer();
	}



	private void loadDataFromServer() {
		// making fresh volley request and getting json
		
		JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
				URL_FEED, null, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						if (response != null) {
							parseJsonFeed(response);							
						}
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});
		
		jsonReq.setRetryPolicy(
	            new DefaultRetryPolicy(
	                    DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
	                    0,
	                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

		// Adding request to volley request queue
		MyNetApplication.getInstance().addToRequestQueue(jsonReq);
	}
	
	public void processFeed(Object data) {
		LiveFeeds liveFeeds = (LiveFeeds) data;
		setDataAdapter(liveFeeds);
	}	
	
	private void parseJsonFeed(JSONObject response) {
		LiveFeeds liveFeeds =gson.fromJson(response.toString(),LiveFeeds.class);			
		setDataAdapter(liveFeeds);		
	}
	private void setDataAdapter(LiveFeeds liveFeeds) {
		if(click)
			return;
		click=true;
		if (liveFeeds.liveFeedList != null && liveFeeds.liveFeedList.size() > 0) {
			liveFeedListAdapter = new LiveFeedAdapter(this,
					R.layout.live_feed_item_layout, new ArrayList<LiveFeed>(liveFeeds.liveFeedList));
			lvLiveFeedList.setAdapter(liveFeedListAdapter);
			click=false;
		}
	}
	public static byte[] convertInputStreamToByteArray(InputStream input)
			throws IOException {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
		return output.toByteArray();
	}
	public String getRealPathFromURI(Uri contentUri) {

		Cursor cursor = getContentResolver()
				.query(contentUri,
						new String[] { android.provider.MediaStore.Images.ImageColumns.DATA },
						null, null, null);

		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}
	
	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(ExperinceLiveFeedActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						startActivity(new Intent(
								android.provider.Settings.ACTION_SETTINGS));
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	public void updateCommentCounter(int feedId, int count) {
		for (LiveFeed liveFeed : liveFeedListAdapter.getAllItems()) {
			if (liveFeed.FeedID == feedId)
				liveFeed.CommentCount = count;
		}
		liveFeedListAdapter.notifyDataSetChanged();
	}
	
}
