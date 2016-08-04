package com.vipdashboard.app.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import com.facebook.Session;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.LiveFeedCallMemoAdapter;
import com.vipdashboard.app.adapter.LiveFeedListAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.EndlessScrollListener;
import com.vipdashboard.app.classes.MyNetPost;
import com.vipdashboard.app.entities.ContactUser;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.LiveFeeds;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveFeedManager;
import com.vipdashboard.app.manager.LiveFeedManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ExperinceLiveActivity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask {

	ListView lvLiveFeedList;
	TextView tvAddFeed;

	DownloadableAsyncTask downloadableAsyncTask;
	LiveFeedListAdapter liveFeedListAdapter;
	int lastSyncFeedId = 0;
	private final static int DOWNLOAD_ALL_FEED = 0;
	private final static int DOWNLOAD_SEND_FEED = 1;
	private final static int FEED_TYPE_EXPERINCE = 1;
	private final static int FEED_TYPE_CALLMEMO = 2;
	private final static int FEED_TYPE_CHECKIN = 3;
	private final static int FEED_TYPE_APPS = 4;
	private final static int FEED_TYPE_PAGING = 5;
	private final static int FEED_TYPE_REFRESHDATA = 6;
	int FEED_TYPE = FEED_TYPE_EXPERINCE;

	private static int downloadState = DOWNLOAD_ALL_FEED;

	LiveFeed liveFeed;

	public static byte[] selectedFile;
	public static String filename = "";

	boolean isSelectedFile = false, bRecentCall, bFamily, bFriends, bCheckIn,
			bExperience, bCallMemo, bApps, isDownloadProcessRunning = false;

	// InputMethodManager imm;
	LiveFeedCallMemoAdapter callMemoAdapter;
	String callMemoList = new String();

	ProgressDialog progress;
	boolean isPressedList;
	MyNetPost myNetPost;

	int pageIndex = 0;

	EndlessScrollListener endlessScrollListener;

	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		if (!locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			showMessage("Your Location service is not available.\nPlease enable first.");
		}
		if (!CommonTask.isOnline(this)) {
			showMessage("Network connection error.\nPlease enable your connection first.");
		} else {
			setContentView(R.layout.experince_live_main);
			recieveMessageHandler = new Handler();
			//initializeProgress();
			initialization();
			initThread();			
		}
	}

	private void initThread() {

		recieveMessageRunnable = new Runnable() {
			public void run() {
				if (!isDownloadProcessRunning) {
					downloadState = FEED_TYPE_REFRESHDATA;
					isDownloadProcessRunning = true;
					if (downloadableAsyncTask != null) {
						downloadableAsyncTask.cancel(true);
					}
					downloadableAsyncTask = new DownloadableAsyncTask(
							ExperinceLiveActivity.this);
					downloadableAsyncTask.execute();
				}
				recieveMessageHandler
						.postDelayed(recieveMessageRunnable, 30000);
			}
		};

		recieveMessageHandler.postDelayed(recieveMessageRunnable, 30000);

	}

	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(ExperinceLiveActivity.this,
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

	private void initialization() {

		tvAddFeed = (TextView) findViewById(R.id.tvAddFeed);

		lvLiveFeedList = (ListView) findViewById(R.id.lvLiveFeedList);

		tvAddFeed.setOnClickListener(this);

		

		endlessScrollListener = new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {

				pageIndex++;
				downloadState = FEED_TYPE_PAGING;
				isDownloadProcessRunning = true;
				if (downloadableAsyncTask != null) {
					downloadableAsyncTask.cancel(true);
				}
				downloadableAsyncTask = new DownloadableAsyncTask(
						ExperinceLiveActivity.this);
				downloadableAsyncTask.execute();
			}
		};

	}


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvAddFeed) {
			if (myNetPost == null)
				myNetPost = new MyNetPost(this, this);
			myNetPost.showPost();
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

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MyNetApplication.activityResumed();
		registerLitsener();
		pageIndex = 0;
		lastSyncFeedId = 0;
		if (liveFeedListAdapter != null) {
			liveFeedListAdapter.clear();
			liveFeedListAdapter.notifyDataSetChanged();
		}	
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{	if (!isFinishing()) 
		{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
		}
		/*if (!isSelectedFile) {

			LoadLiveFeedList();
		}*/

	}
	
	

	@Override
	protected void onStart() {
		
		super.onStart();
		if(CommonValues.getInstance().ConatactUserList==null || CommonValues.getInstance().ConatactUserList.size()==0){
			MyNetDatabase db=new MyNetDatabase(this);
			CommonValues.getInstance().ConatactUserList=db.GetUserHashMap();
		}
		if (!isSelectedFile) {
			LoadLiveFeedList();		
		}
	}
	
	

	private void registerLitsener() {
		lvLiveFeedList.setOnScrollListener(endlessScrollListener);
	}

	private void LoadLiveFeedList() {
		if (downloadState >= 0)
			downloadState = DOWNLOAD_ALL_FEED;
		pageIndex = 0;
		isDownloadProcessRunning = true;
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void initializeProgress() {
		progress = new ProgressDialog(this);
		progress.setIcon(null);
		progress.setTitle("");
		progress.setCancelable(false);
		progress.setMessage("");
	}

	@Override
	public void showProgressLoader() {
		/*if (downloadState != FEED_TYPE_PAGING
				&& downloadState != FEED_TYPE_REFRESHDATA)
			progress.show();*/
	}

	@Override
	public void hideProgressLoader() {
		/*if (downloadState != FEED_TYPE_PAGING
				&& downloadState != FEED_TYPE_REFRESHDATA)
			progress.dismiss();*/
	}

	@Override
	public Object doBackgroundPorcess() {
		ILiveFeedManager liveFeedManager = new LiveFeedManager();
		if (downloadState == DOWNLOAD_ALL_FEED) {
			return liveFeedManager.GetAllParentLiveFeed();
		} else if (downloadState == FEED_TYPE_PAGING) {
			return liveFeedManager.GetAllParentLiveFeed(pageIndex);
		} else if (downloadState == DOWNLOAD_SEND_FEED) {
			return liveFeedManager
					.SetLiveFeed(liveFeed, filename, selectedFile);
		} else if (downloadState == FEED_TYPE_REFRESHDATA) {
			return liveFeedManager.GetAllParentLiveFeedByTime(lastSyncFeedId);
		}
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (downloadState == DOWNLOAD_ALL_FEED) {
				LiveFeeds liveFeeds = (LiveFeeds) data;
				if (liveFeeds.liveFeedList != null
						&& liveFeeds.liveFeedList.size() > 0) {
					liveFeedListAdapter = new LiveFeedListAdapter(this,
							R.layout.feed_item_layout, new ArrayList<LiveFeed>(
									liveFeeds.liveFeedList));
					lvLiveFeedList.setAdapter(liveFeedListAdapter);
				}

			} else if (downloadState == FEED_TYPE_PAGING) {

				LiveFeeds liveFeeds = (LiveFeeds) data;
				if (liveFeeds.liveFeedList != null
						&& liveFeeds.liveFeedList.size() > 0) {
					liveFeedListAdapter.addAll(liveFeeds.liveFeedList);
					liveFeedListAdapter.notifyDataSetChanged();
				}

			} else if (downloadState == FEED_TYPE_REFRESHDATA) {

				LiveFeeds liveFeeds = (LiveFeeds) data;
				if (liveFeeds.liveFeedList != null
						&& liveFeeds.liveFeedList.size() > 0) {
					if (liveFeedListAdapter == null) {
						liveFeedListAdapter = new LiveFeedListAdapter(this,
								R.layout.feed_item_layout,
								new ArrayList<LiveFeed>(liveFeeds.liveFeedList));
						lvLiveFeedList.setAdapter(liveFeedListAdapter);
					} else {
						for (LiveFeed feed : liveFeeds.liveFeedList) {
							liveFeedListAdapter.addItemOnTop(feed);
						}
						liveFeedListAdapter.notifyDataSetChanged();
					}
				}

			}
			if(liveFeedListAdapter!=null && liveFeedListAdapter.getCount()>0)
				lastSyncFeedId=liveFeedListAdapter.getFirstItem().FeedID;
		
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) 
			{
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		isDownloadProcessRunning = false;
	}

	public void processFeed(Object data) {
		LiveFeeds liveFeeds = (LiveFeeds) data;
		if (liveFeeds.liveFeedList != null && liveFeeds.liveFeedList.size() > 0) {

			liveFeedListAdapter = new LiveFeedListAdapter(this,
					R.layout.feed_item_layout, new ArrayList<LiveFeed>(
							liveFeeds.liveFeedList));
			lvLiveFeedList.setAdapter(liveFeedListAdapter);
			lastSyncFeedId = liveFeedListAdapter.getFirstItem().FeedID;
			downloadState = DOWNLOAD_ALL_FEED;
			filename = "";
			selectedFile = null;
			isSelectedFile = false;
			isDownloadProcessRunning = false;
		}
	}

	public void updateCommentCounter(int feedId, int count) {
		for (LiveFeed liveFeed : liveFeedListAdapter.getAllItems()) {
			if (liveFeed.FeedID == feedId)
				liveFeed.CommentCount = count;
		}
		liveFeedListAdapter.notifyDataSetChanged();
	}

}
