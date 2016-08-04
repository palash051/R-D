package com.vipdashboard.app.fragments;

import java.text.ParseException;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.Notification;
import com.vipdashboard.app.entities.Notifications;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveCollaborationManager;
import com.vipdashboard.app.interfaces.INotificationManager;
import com.vipdashboard.app.manager.LiveCollaborationManager;
import com.vipdashboard.app.manager.NotificationManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class DiscussionFragment extends MainActionbarBase implements
		IAsynchronousTask, OnClickListener {
	/*public static Fragment newInstance(Context context) {
		DiscussionFragment f = new DiscussionFragment();
		return f;
	}*/

	private static Runnable recieveMessageRunnable;
	private Handler recieveMessageHandler;
	ProgressBar pbGroupDiscussion;
	EditText etMessageGroup;
	Button bSendMessageGroup;
	public static int selectedGroupId;
	DownloadableAsyncTask downloadableAsyncTask;
	TextView tvMessageBoxGroup;
	private final int DOWNLOAD_GROUP_CHAT_DATA = 0;
	private final int SEND_GROUP_CHAT_DATA = 1;
	int selectedBackgroundProcess = SEND_GROUP_CHAT_DATA;

	boolean isCallFromNotification;

	/*@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(
				R.layout.collaboration_discussion, null);
		isCallFromNotification = CommonValues.getInstance().isCallFromNotification;
		selectedBackgroundProcess = DOWNLOAD_GROUP_CHAT_DATA;
		pbGroupDiscussion = (ProgressBar) root
				.findViewById(R.id.pbGroupDiscussion);
		tvMessageBoxGroup = (TextView) root
				.findViewById(R.id.tvMessageBoxGroup);
		recieveMessageHandler = new Handler();
		RelativeLayout rlMessageSend = (RelativeLayout) root
				.findViewById(R.id.rlMessageSend);
		if (isCallFromNotification) {
			rlMessageSend.setVisibility(RelativeLayout.GONE);
			runDownloadable();
		} else {
			rlMessageSend.setVisibility(RelativeLayout.VISIBLE);
			etMessageGroup = (EditText) root.findViewById(R.id.etMessageGroup);
			bSendMessageGroup = (Button) root
					.findViewById(R.id.bSendMessageGroup);
			bSendMessageGroup.setOnClickListener(this);
			runDownloadable();
			initThread();
		}

		return root;
	}*/

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collaboration_discussion);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		//mSupportActionBar.setTitle("MyNet (Welcome " + CommonValues.getInstance().LoginUser.Name.toString() + ")");
		isCallFromNotification = CommonValues.getInstance().isCallFromNotification;
		selectedBackgroundProcess = DOWNLOAD_GROUP_CHAT_DATA;
		pbGroupDiscussion = (ProgressBar) findViewById(R.id.pbGroupDiscussion);
		tvMessageBoxGroup = (TextView) findViewById(R.id.tvMessageBoxGroup);
		recieveMessageHandler = new Handler();
		RelativeLayout rlMessageSend = (RelativeLayout) findViewById(R.id.rlMessageSend);
		if (isCallFromNotification) {
			rlMessageSend.setVisibility(RelativeLayout.GONE);
			runDownloadable();
		} else {
			rlMessageSend.setVisibility(RelativeLayout.VISIBLE);
			etMessageGroup = (EditText) findViewById(R.id.etMessageGroup);
			bSendMessageGroup = (Button) findViewById(R.id.bSendMessageGroup);
			bSendMessageGroup.setOnClickListener(this);
			runDownloadable();
			initThread();
		}
	}
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	}
	

	private void initThread() {
		selectedBackgroundProcess = DOWNLOAD_GROUP_CHAT_DATA;

		recieveMessageRunnable = new Runnable() {
			public void run() {

				runDownloadable();

				recieveMessageHandler
						.postDelayed(recieveMessageRunnable, 20000);
			}
		};

		recieveMessageHandler.postDelayed(recieveMessageRunnable, 20000);

	}

	private void runDownloadable() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(
				DiscussionFragment.this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void onClick(View v) {
		if(etMessageGroup.getText().toString().equals("")) 
			return;
		selectedBackgroundProcess = SEND_GROUP_CHAT_DATA;
		runDownloadable();
	}

	@Override
	public void showProgressLoader() {
		if (selectedBackgroundProcess == SEND_GROUP_CHAT_DATA) {
			pbGroupDiscussion.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void hideProgressLoader() {
		if (selectedBackgroundProcess == SEND_GROUP_CHAT_DATA) {
			pbGroupDiscussion.setVisibility(View.GONE);
		}
	}

	@Override
	public Object doBackgroundPorcess() {

		if (!isCallFromNotification) {
			ILiveCollaborationManager liveCollaborationManager = new LiveCollaborationManager();
			if (selectedBackgroundProcess == DOWNLOAD_GROUP_CHAT_DATA) {
				return liveCollaborationManager
						.GetLiveCollaborationsByGroupId(selectedGroupId);
			} else if (selectedBackgroundProcess == SEND_GROUP_CHAT_DATA) {
				Collaboration collaboration = new Collaboration();
				collaboration.GroupID = selectedGroupId;
				collaboration.UserType="G";
				collaboration.MsgFrom = CommonValues.getInstance().LoginUser.UserNumber;
				collaboration.MsgText = etMessageGroup.getText().toString();
				return liveCollaborationManager
						.SendLiveCollaboration(collaboration);
			}
		} else {
			INotificationManager notificationManager = new NotificationManager();
			return notificationManager
					.GetLiveNotificationsByGroupId(selectedGroupId);			
		}
		return null;

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			try {
				String sbMessage = "";
				if (!isCallFromNotification) {
					if (selectedBackgroundProcess == DOWNLOAD_GROUP_CHAT_DATA) {
						Collaborations collaborations = (Collaborations) data;
						for (Collaboration collaboration : collaborations.collaborationList) {
							sbMessage = sbMessage
									+ collaboration.user.Name
									+ "("
									+ CommonTask
											.toStringDate(CommonTask
													.parseDate(collaboration.PostedDate))
									+ ") : " + collaboration.MsgText + "\r\n";
						}
					} else if (selectedBackgroundProcess == SEND_GROUP_CHAT_DATA) {
						Collaboration collaboration = (Collaboration) data;
						sbMessage = tvMessageBoxGroup.getText().toString()
								+ CommonValues.getInstance().LoginUser.Name
								+ "("
								+ CommonTask.toStringDate(CommonTask
										.parseDate(collaboration.PostedDate))
								+ ") : " + collaboration.MsgText + "\r\n";
						etMessageGroup.setText("");
					}
				}else{
					Notifications notifications = (Notifications) data;
					for (Notification notification : notifications.notificationList) {
						sbMessage = sbMessage
								+ notification.msgFromUser.Name
								+ "("
								+ CommonTask
										.toStringDate(CommonTask
												.parseDate(notification.ProcessTime))
								+ ") : " + notification.NotificationText + "\r\n";
					}
				}
				tvMessageBoxGroup.setText(sbMessage);
				selectedBackgroundProcess = DOWNLOAD_GROUP_CHAT_DATA;
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}
