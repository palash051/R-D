package com.khareeflive.app.base;

import com.khareeflive.app.activities.LatestUpdateActivity;
import com.khareeflive.app.activities.WarRoomGroupActivity;
import com.khareeflive.app.asynchronoustask.DownloadableTask;
import com.khareeflive.app.entities.IDownloadProcessorActicity;
import com.khareeflive.app.manager.LatestUpdateManager;
import com.khareeflive.app.utils.CommonValues;

import android.app.AlertDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class KhareefLiveService extends Service implements
		IDownloadProcessorActicity {
	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	DownloadableTask downloadableTask;

	public KhareefLiveService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onCreate() {
		recieveMessageHandler = new Handler();
	}

	@Override
	public void onStart(Intent intent, int startId) {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initThread();
		return super.onStartCommand(intent, flags, startId);
	}

	private void initThread() {

		recieveMessageRunnable = new Runnable() {
			public void run() {
				if (!KhareefLiveApplication.isActivityVisible()) {
					if (!CommonValues.getInstance().lastWarMessageTime
							.equals("")) {
						if (downloadableTask != null) {
							downloadableTask.cancel(true);
						}
						downloadableTask = new DownloadableTask(
								KhareefLiveService.this);
						downloadableTask.execute();
					}					
				}
				recieveMessageHandler.postDelayed(recieveMessageRunnable,
						20000);
			}
		};

		recieveMessageHandler.postDelayed(recieveMessageRunnable, 20000);

	}

	@Override
	public void onDestroy() {
		recieveMessageHandler.removeCallbacks(recieveMessageRunnable);
	}

	@Override
	public void showProgressLoader() {

	}

	@Override
	public void hideProgressLoader() {

	}

	@Override
	public Object doBackgroundDownloadPorcess() {

		return LatestUpdateManager
				.isNewMessageFound(CommonValues.getInstance().lastWarMessageTime);
	}

	@Override
	public void processDownloadedData(Object data) {		 		
		if ((String) data == "LATESTNEWS") {
			final Intent intent = new Intent(this, LatestUpdateActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction("android.intent.action.VIEW");
			startActivity(intent);
			
			/*new AlertDialog.Builder(this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Show Warroom")
	        .setMessage("There have a new message in warroom. Do you want to see it?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	            	startActivity(intent);           
	            }

	        })
	        .setNegativeButton("No", null)
	        .show();*/
		}
		if ((String) data == "CHAT") {
			final Intent intent = new Intent(this, WarRoomGroupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setAction("android.intent.action.VIEW");	
			startActivity(intent);
			
			/*new AlertDialog.Builder(KhareefLiveService.this)
	        .setIcon(android.R.drawable.ic_dialog_alert)
	        .setTitle("Show Latest news")
	        .setMessage("There have a new Latest news. Do you want to see it?")
	        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {

	            	startActivity(intent);           
	            }

	        })
	        .setNegativeButton("No", null)
	        .show();*/
		}
		
		CommonValues.getInstance().lastWarMessageTime = "";
	}

}
