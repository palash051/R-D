package com.vipdashboard.app.asynchronoustask;

import java.util.ArrayList;
import java.util.Queue;

import com.androidquery.AQuery;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.UserProfilePicture;
import com.vipdashboard.app.entities.UserProfilePictureListRoot;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.ImageLoader;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class LoadImageAsyncTask extends AsyncTask<Void, Void, Object>{
	
	Context context;
	int userNumber;
	ImageView ivImageView;
	ImageLoader loader=null;
	LinearLayout llImageViewList;
	boolean isLoadSingleImage=true;
	String userNumbers;
	String url="";
	
	AQuery aq;
	
	public boolean isDownloadRunning=false;
	public LoadImageAsyncTask(Context context,int userNumber,ImageView iv) {
		this.context = context;	
		this.userNumber = userNumber;
		this.ivImageView = iv;
		 loader=new ImageLoader(this.context);
		 loader.clearCache();
		 isLoadSingleImage=true;
		 isDownloadRunning=false;
		 aq = new AQuery(iv);
	}
	public LoadImageAsyncTask(Context context,String url,ImageView iv) {
		this.context = context;	
		this.url = url;
		this.ivImageView = iv;
		 loader=new ImageLoader(this.context);
		 loader.clearCache();
		 isLoadSingleImage=true;
		 isDownloadRunning=false;
	}
	public LoadImageAsyncTask(Context context,String userNumbers,LinearLayout ll) {
		this.context = context;	
		this.userNumbers = userNumbers;
		/*if(llImageViewList==null){
			llImageViewList=new ArrayList<LinearLayout>();
		}*/
		llImageViewList= ll;
		loader=new ImageLoader(this.context);
		loader.clearCache();
		isLoadSingleImage=false;
		isDownloadRunning=false;
		
		aq = new AQuery(ll);
		
	}
	@Override
	protected void onPreExecute() {	
		isDownloadRunning=true;
	}

	@Override
	protected Object doInBackground(Void... cap) {
		IUserManager manager=new UserManager();
		if(isLoadSingleImage){
			return manager.GetUserProfilePicturePath(userNumber);
		}
		else
			return manager.GetUserProfilePicturePathList(userNumbers);
		
	}

	@Override
	protected void onPostExecute(Object data) {
		if(data!=null){
			String profImg=CommonURL.getInstance().getImageServer+"DafaultImage\\defaultuser.png";
			if(isLoadSingleImage){				
				if(data!=null){
					profImg=String.valueOf(data);
					if(!profImg.contains("graph.facebook.com")){
						profImg=CommonURL.getInstance().getImageServer+profImg;
					}		
					profImg=profImg.replace("large", "small");
					
				}
					
				aq.id(ivImageView).image(profImg, false, false, 0, R.drawable.user_icon, null, 0, AQuery.RATIO_PRESERVE);
				ivImageView.setVisibility(View.VISIBLE);
				//loader.DisplayImage(profImg, ivImageView);
				
			}else{
				UserProfilePictureListRoot root=(UserProfilePictureListRoot)data;
	
				if(root.userProfilePictureList!=null && root.userProfilePictureList.size()>0){
					ImageView imv;
					LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(45, 45);
					//LinearLayout ll=llImageViewList.get(0);
					for (String  str : userNumbers.split(",")) {
						for (UserProfilePicture profimg : root.userProfilePictureList) {
							if(Integer.parseInt(str)==profimg.UserNumber){								
								profImg=profimg.PPPath;
								if(!profImg.contains("graph.facebook.com")){
									profImg=CommonURL.getInstance().getImageServer+profImg;
								}		
								profImg=profImg.replace("large", "small");
								break;
							}
						}
						imv=new ImageView(context);
						imv.setLayoutParams(layoutParams);
						//loader.DisplayImage(profImg, imv);
						aq.id(imv).image(profImg, true, true, 0, 0, null, 0, 1.0f);
						llImageViewList.addView(imv);
						
					}
					llImageViewList.setVisibility(View.VISIBLE);
					//llImageViewList.remove(0);
				}
			}
		}
		isDownloadRunning=false;
	}
}
