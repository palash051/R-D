package com.vipdashboard.app.classes;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.ExperinceLiveActivity;
import com.vipdashboard.app.activities.ExperinceLiveFeedActivity;
import com.vipdashboard.app.activities.FB_PostActivity;
import com.vipdashboard.app.activities.FB_ShareActivity;
import com.vipdashboard.app.activities.MakeCallActivity;
import com.vipdashboard.app.adapter.CallMemoItemPostAdapter;
import com.vipdashboard.app.adapter.LiveFeedCallMemoAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.LiveFeed;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IFacebookManager;
import com.vipdashboard.app.interfaces.ILiveFeedManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.FacebookManager;
import com.vipdashboard.app.manager.LiveFeedManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyNetPost implements OnClickListener, IAsynchronousTask,
		OnItemClickListener {

	Context context;
	Dialog MyNetPostDialog;
	MainActionbarBase mainActionbarBase;
	EditText etPost;
	GoogleMap postMap;
	public ImageView ivPostImage;
	ListView lvMynetPostCallMemo;
	ImageView ivPrivate, ivpublic, ivFacebook, ivMyNet;
	RelativeLayout rlMyNetPostStatus, rlMyNetPostCheckIn, rlMyNetPostPhotos,
			rlMyNetPostCallMemo;
	LinearLayout llMap;
	View vsMap, vsPhoto, vsCallmemo;
	MyNetPostCheckIn myNetPostCheckIn;
	TextView tvMyNetPostTitle;
	boolean isVoiceUploading=false;
	String voiceUrl="";
	
	TextView tvNotavailableInfo;

	private final static int FEED_TYPE_EXPERINCE = 1;
	private final static int FEED_TYPE_CALLMEMO = 2;
	private final static int FEED_TYPE_CHECKIN = 3;
	private final static int FEED_TYPE_PHOTO = 4;
	int FEED_TYPE = FEED_TYPE_EXPERINCE;
	LiveFeed liveFeed;
	DownloadableAsyncTask downloadableAsyncTask;
	//ProgressDialog progressDialog;
	boolean isCallFromCheckIn, isCallFromPhoto, isCallFromCallMemo,
			isPressedList, isCallFromPostFacebook;
	String callMemoList = "";
	CallMemoItemPostAdapter callMemoAdapter;
	byte[] voiceCall = null;

	public MyNetPost(Context _context,
			MainActionbarBase _mainActionbarBase) {
		context = _context;
		mainActionbarBase = _mainActionbarBase;
	}

	public void showPost() {
		if (MyNetPostDialog == null) {
			MyNetPostDialog = new Dialog(context);
			MyNetPostDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			MyNetPostDialog.setContentView(R.layout.mynet_post);
			MyNetPostDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;			
			// MyNetPostDialog.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
			// R.layout.custom_title);
			tvMyNetPostTitle = (TextView) MyNetPostDialog
					.findViewById(R.id.tvMyNetPostTitle);
			etPost = (EditText) MyNetPostDialog
					.findViewById(R.id.etMyNetPostEditbox);
			ivPrivate = (ImageView) MyNetPostDialog
					.findViewById(R.id.ivMyNetPostPrivate);
			ivpublic = (ImageView) MyNetPostDialog
					.findViewById(R.id.ivMyNetPostPublic);
			ivFacebook = (ImageView) MyNetPostDialog
					.findViewById(R.id.ivMyNetPostFacebook);
			ivMyNet = (ImageView) MyNetPostDialog
					.findViewById(R.id.ivMyNetPostMynet);
			rlMyNetPostStatus = (RelativeLayout) MyNetPostDialog
					.findViewById(R.id.rlMyNetPostStatus);
			rlMyNetPostCheckIn = (RelativeLayout) MyNetPostDialog
					.findViewById(R.id.rlMyNetPostCheckIn);
			rlMyNetPostPhotos = (RelativeLayout) MyNetPostDialog
					.findViewById(R.id.rlMyNetPostPhotos);
			rlMyNetPostCallMemo = (RelativeLayout) MyNetPostDialog
					.findViewById(R.id.rlMyNetPostCallMemo);
			vsMap = ((ViewStub) MyNetPostDialog.findViewById(R.id.vsCheckIn))
					.inflate();
			vsPhoto = ((ViewStub) MyNetPostDialog.findViewById(R.id.vsPhoto))
					.inflate();
			vsCallmemo = ((ViewStub) MyNetPostDialog
					.findViewById(R.id.vsCallMemo)).inflate();

			ivPostImage = (ImageView) vsPhoto
					.findViewById(R.id.ivMyNetPostPicture);
			llMap = (LinearLayout) vsMap.findViewById(R.id.llMapPost);
			
			tvMyNetPostTitle.setText("Share with your friends");
			ivPrivate.setOnClickListener(this);
			ivpublic.setOnClickListener(this);
			ivFacebook.setOnClickListener(this);
			ivMyNet.setOnClickListener(this);
			rlMyNetPostStatus.setOnClickListener(this);
			rlMyNetPostCheckIn.setOnClickListener(this);
			rlMyNetPostPhotos.setOnClickListener(this);
			rlMyNetPostCallMemo.setOnClickListener(this);
		}
		isCallFromPostFacebook = false;
		etPost.setText("");
		isCallFromCallMemo = isCallFromCheckIn = isCallFromPhoto = isCallFromPostFacebook = false;
		ivMyNet.setVisibility(ImageView.VISIBLE);
		ivFacebook.setVisibility(ImageView.GONE);
		ivPostImage.setImageBitmap(null);
		vsMap.setVisibility(View.GONE);
		vsPhoto.setVisibility(View.VISIBLE);
		vsCallmemo.setVisibility(View.GONE);
		MyNetPostDialog.show();

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivMyNetPostPrivate) {
			ivPrivate.setVisibility(ImageView.GONE);
			ivpublic.setVisibility(ImageView.VISIBLE);
		} else if (view.getId() == R.id.ivMyNetPostPublic) {
			ivpublic.setVisibility(ImageView.GONE);
			ivPrivate.setVisibility(ImageView.VISIBLE);
		} else if (view.getId() == R.id.ivMyNetPostFacebook) {
			ivFacebook.setVisibility(ImageView.GONE);
			ivMyNet.setVisibility(ImageView.VISIBLE);
			isCallFromPostFacebook = false;
		} else if (view.getId() == R.id.ivMyNetPostMynet) {
			isCallFromPostFacebook = true;
			ivMyNet.setVisibility(ImageView.GONE);
			ivFacebook.setVisibility(ImageView.VISIBLE);
		} else if (view.getId() == R.id.rlMyNetPostStatus) {
			if (etPost.getText().toString().trim().equals("")) {
				Toast.makeText(context, "Please write feed text",
						Toast.LENGTH_SHORT).show();
				etPost.setText("");
				return;
			} else {
				if (isCallFromCallMemo) {
					if(voiceUrl != ""){
						isVoiceUploading=false;
						liveFeed = new LiveFeed();
						liveFeed.FeedText = etPost.getText().toString() + "\n"
								+ callMemoList;
						liveFeed.FeedType = FEED_TYPE_CALLMEMO;
						liveFeed.UserNumber = CommonValues.getInstance().LoginUser.UserNumber;
						liveFeed.Latitude = MyNetService.currentLocation
								.getLatitude();
						liveFeed.Longitude = MyNetService.currentLocation
								.getLongitude();
						/*if(isCallFromPostFacebook){
							CallMemoSnapShot();
						}
						else{
							LoadInformation();
						}*/
						
					
							CallMemoSnapShot();
					}else{
						liveFeed = new LiveFeed();
						liveFeed.FeedText = etPost.getText().toString() + "\n"
								+ callMemoList;
						liveFeed.FeedType = FEED_TYPE_CALLMEMO;
						liveFeed.UserNumber = CommonValues.getInstance().LoginUser.UserNumber;
						liveFeed.Latitude = MyNetService.currentLocation
								.getLatitude();
						liveFeed.Longitude = MyNetService.currentLocation
								.getLongitude();
						/*if(isCallFromPostFacebook){
							CallMemoSnapShot();
						}
						else{
							LoadInformation();
						}*/
						
							CallMemoSnapShot();
					}					
				} else if (isCallFromPhoto) {
					liveFeed = new LiveFeed();
					liveFeed.FeedText = etPost.getText().toString();
					liveFeed.FeedType = FEED_TYPE;
					liveFeed.UserNumber = CommonValues.getInstance().LoginUser.UserNumber;
					liveFeed.Latitude = MyNetService.currentLocation
							.getLatitude();
					liveFeed.Longitude = MyNetService.currentLocation
							.getLongitude();
					LoadInformation();
				} else if (isCallFromCheckIn) {
					liveFeed = new LiveFeed();
					liveFeed.FeedText = etPost.getText().toString();
					liveFeed.FeedType = FEED_TYPE;
					liveFeed.UserNumber = CommonValues.getInstance().LoginUser.UserNumber;
					liveFeed.Latitude = MyNetService.currentLocation
							.getLatitude();
					liveFeed.Longitude = MyNetService.currentLocation
							.getLongitude();
					mapImageShapShot();
				} else if (isCallFromPostFacebook) {
					Intent intent = new Intent(context, FB_PostActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("MESSAGE", etPost.getText().toString());
					context.startActivity(intent);
					
				} else {
					liveFeed = new LiveFeed();
					liveFeed.FeedText = etPost.getText().toString();
					liveFeed.FeedType = FEED_TYPE;
					liveFeed.UserNumber = CommonValues.getInstance().LoginUser.UserNumber;
					liveFeed.Latitude = MyNetService.currentLocation
							.getLatitude();
					liveFeed.Longitude = MyNetService.currentLocation
							.getLongitude();
					LoadInformation();
				}
				//etPost.setText("");
				if(!isCallFromCheckIn)
					MyNetPostDialog.dismiss();
			}

		} else if (view.getId() == R.id.rlMyNetPostCheckIn) {
			FEED_TYPE = FEED_TYPE_CHECKIN;
			isCallFromCheckIn = true;
			isCallFromPhoto = isCallFromCallMemo = false;
			tvMyNetPostTitle.setText("Let your friend know where you are");
			vsMap.setVisibility(View.VISIBLE);
			vsPhoto.setVisibility(ViewStub.GONE);
			vsCallmemo.setVisibility(ViewStub.GONE);
			myNetPostCheckIn = new MyNetPostCheckIn(context, mainActionbarBase);
			/*map = ((SupportMapFragment) mainActionbarBase.getSupportFragmentManager()
					.findFragmentById(R.id.myNetPostmap)).getMap();*/
			postMap = ((SupportMapFragment) ((ExperinceLiveFeedActivity)context).getSupportFragmentManager().findFragmentById(
					R.id.myNetPostmap)).getMap();
			myNetPostCheckIn.ShowMap(postMap, vsMap);
		} else if (view.getId() == R.id.rlMyNetPostPhotos) {
			ivPostImage.setImageBitmap(null);
			FEED_TYPE = FEED_TYPE_PHOTO;
			isCallFromPhoto = true;
			isCallFromCallMemo = isCallFromCheckIn = false;
			tvMyNetPostTitle.setText("Share your moments in photo");
			vsMap.setVisibility(View.GONE);
			vsPhoto.setVisibility(ViewStub.VISIBLE);
			vsCallmemo.setVisibility(ViewStub.GONE);
			// ivPostImage = (ImageView)
			// vsPhoto.findViewById(R.id.ivMyNetPostPicture);
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			mainActionbarBase.startActivityForResult(
					Intent.createChooser(intent, "Select Picture"), 1);
		} else if (view.getId() == R.id.rlMyNetPostCallMemo) {
			FEED_TYPE = FEED_TYPE_CALLMEMO;
			isPressedList = true;
			isCallFromCallMemo = true;
			isCallFromPhoto = isCallFromCheckIn = false;
			tvMyNetPostTitle.setText("Share a voice call");
			vsMap.setVisibility(View.GONE);
			vsPhoto.setVisibility(ViewStub.GONE);
			vsCallmemo.setVisibility(ViewStub.VISIBLE);
			lvMynetPostCallMemo = (ListView) vsCallmemo
					.findViewById(R.id.lvMyNetPostCallMemo);
			tvNotavailableInfo= (TextView) vsCallmemo
					.findViewById(R.id.tvNotavailableInfo);
			//Should come here
			// load data from database and bind into listview
			MyNetDatabase database = new MyNetDatabase(context);
			database.open();
			ArrayList<PhoneCallInformation> callInformation = database
					.getAllCallInformationByCallMemo();
			database.close();

			callMemoAdapter = new CallMemoItemPostAdapter(context,
					R.layout.call_memo_item_post, callInformation);
			lvMynetPostCallMemo.setAdapter(callMemoAdapter);

			lvMynetPostCallMemo.setOnItemClickListener(this);
			
			if(lvMynetPostCallMemo.getCount()<=0)
			{
				tvNotavailableInfo.setVisibility(View.VISIBLE);
			}
			
		}
	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		/*progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please wait...");
		progressDialog.show();*/
	}

	@Override
	public void hideProgressLoader() {
		//progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		ILiveFeedManager liveFeedManager = new LiveFeedManager();
		IFacebookManager facebookManager = new FacebookManager();
		if(isVoiceUploading){
			return facebookManager.SetAudio(
					voiceCall,CommonValues.getInstance().LoginUser.UserNumber);
		}else{
			if (isCallFromPostFacebook) {
				return userManager.SetFBPostPicture(
						ExperinceLiveFeedActivity.selectedFile,
						CommonValues.getInstance().LoginUser.UserNumber);
			} else {
				if(voiceUrl.isEmpty())
					return liveFeedManager.SetLiveFeed(liveFeed,
							ExperinceLiveFeedActivity.filename,
							ExperinceLiveFeedActivity.selectedFile);
				else
					return liveFeedManager.SetLiveFeed(liveFeed,
						ExperinceLiveFeedActivity.filename,
						ExperinceLiveFeedActivity.selectedFile,voiceUrl);
			}
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if(!isVoiceUploading){
				if (isCallFromPostFacebook) {
					isCallFromCallMemo = isCallFromCheckIn = isCallFromPhoto = isCallFromPostFacebook = false;
					String path = "";
					path += CommonURL.getInstance().getCareImageServer
							+ String.valueOf(data);
					
					ExperinceLiveFeedActivity.filename = "";
					ExperinceLiveFeedActivity.selectedFile = null;
					voiceUrl="";
					voiceCall = null;
	
					Intent intent = new Intent(context, FB_ShareActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("PIC_LINK", path);
					intent.putExtra("DESCRIPTION", etPost.getText().toString());
					context.startActivity(intent);
				} else {
					isCallFromCallMemo = isCallFromCheckIn = isCallFromPhoto = false;
					ExperinceLiveFeedActivity.filename = callMemoList = "";
					ExperinceLiveFeedActivity.selectedFile = null;
					voiceUrl = "";
					voiceCall = null;
					
					((ExperinceLiveFeedActivity) context).processFeed(data);
				}
			}else{
				voiceUrl="";
				isVoiceUploading=false;
				voiceUrl += CommonURL.getInstance().getCareImageServer
						+ String.valueOf(data);
				voiceCall = null;
				//VoiceCallPlayer voiceCallPlayer = new VoiceCallPlayer(context);
				//voiceCallPlayer.PlayVoiceCallRecorder(voiceUrl);
			}
		}else{
			Toast.makeText(context, "Feed post unsuccessful", Toast.LENGTH_SHORT).show();	
		}
	}

	// select call memo from list
	@Override
	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long ID) {
		if (isPressedList) {
			view.setBackgroundColor(context.getResources().getColor(
					R.color.header_text));
			PhoneCallInformation callInformation = (PhoneCallInformation) view
					.getTag();
			callMemoList += callInformation.TextCallMemo;
			//voice memo
			if (callInformation.VoiceRecordPath != null) {
				InputStream inputStream;
				try {
					inputStream = new FileInputStream(
							callInformation.VoiceRecordPath);
					voiceCall = convertInputStreamToByteArray(inputStream);
					isVoiceUploading = true;
					LoadInformation();
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			isPressedList = false;
		}
	}

	public static byte[] convertInputStreamToByteArray(InputStream input) {
		byte[] buffer = new byte[8192];
		int bytesRead;
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		try {
			while ((bytesRead = input.read(buffer)) != -1) {
				output.write(buffer, 0, bytesRead);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return output.toByteArray();
	}
	
	private void mapImageShapShot() {
		GoogleMap.SnapshotReadyCallback snapshotReadyCallback = new SnapshotReadyCallback() {
			@Override
			public void onSnapshotReady(Bitmap shapshot) {
				vsMap.setDrawingCacheEnabled(true);
				Bitmap backBitmap = vsMap.getDrawingCache();
                Bitmap bmOverlay = Bitmap.createBitmap(
                        backBitmap.getWidth(), backBitmap.getHeight(),
                        backBitmap.getConfig());
                Canvas canvas = new Canvas(bmOverlay);
                canvas.drawBitmap(shapshot, new Matrix(), null);
                canvas.drawBitmap(backBitmap, 0, 0, null);
				String external_path = Environment
						.getExternalStorageDirectory().getPath()
						+ "/MyNet/";
				String filePath = String
						.format(CommonValues.getInstance().LoginUser.UserNumber
								+ ".jpg");
				File cduFileDir = new File(external_path);
				if (!cduFileDir.exists())
					cduFileDir.mkdir();
				File pictureFile = new File(cduFileDir, filePath);
				ExperinceLiveFeedActivity.filename = pictureFile.getName();

				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, stream);
				ExperinceLiveFeedActivity.selectedFile = stream.toByteArray();
				MyNetPostDialog.dismiss();
				LoadInformation();
			}
		};
		postMap.snapshot(snapshotReadyCallback);
	}

	private void CallMemoSnapShot() {
		try {
			if(tvNotavailableInfo.getVisibility()==View.GONE)
			{
			Bitmap bmOverlay = null;
			vsCallmemo.setDrawingCacheEnabled(true);
			bmOverlay = vsCallmemo.getDrawingCache();
			String external_path = Environment.getExternalStorageDirectory()
					.getPath() + "/MyNet/";
			String filePath = String
					.format(CommonValues.getInstance().LoginUser.UserNumber
							+ ".jpg");
			File cduFileDir = new File(external_path);
			if (!cduFileDir.exists())
				cduFileDir.mkdir();
			File pictureFile = new File(cduFileDir, filePath);
			ExperinceLiveFeedActivity.filename = pictureFile.getName();
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, stream);
			ExperinceLiveFeedActivity.selectedFile = stream.toByteArray();
			/*while(!isCallFromPostFacebook){
				LoadInformation();
				break;
			}*/
			
			}
			LoadInformation();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
