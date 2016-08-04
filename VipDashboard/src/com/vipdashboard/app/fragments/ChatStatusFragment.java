package com.vipdashboard.app.fragments;

import java.util.ArrayList;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ChatStatusFragment  extends SherlockFragment implements OnItemClickListener, IAsynchronousTask{
	TextView etUserStatus;
	ListView lvStatusList;
	private ReportAdapter adapter;
	DownloadableAsyncTask downloadableAsyncTask;	
	private int userOnlinestatusID;
	
	boolean isCallFromGetUserStatus=true;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View root =  (ViewGroup) inflater.inflate(R.layout.chat_status_layout, container, false);
		etUserStatus = (TextView) root.findViewById(R.id.etStatus);
		lvStatusList = (ListView) root.findViewById(R.id.lvCollaborationUserStatusList);	
		return root;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MyNetApplication.activityPaused();
	}

	@Override
	public void onResume() {
		super.onResume();		
		 if (!CommonTask.isOnline(getActivity())) {
			CommonTask.DryConnectivityMessage(getActivity(),"No Internet Connection.\nPlease enable your connection first.");
		}
		MyNetApplication.activityResumed();
		Initialization();
		
		if(CommonValues.getInstance().LoginUser.UserOnlineAvailableStatusID> 0){
			setUserStatus(CommonValues.getInstance().LoginUser.UserOnlineAvailableStatusID);
		}
	}
	
	private void Initialization() {
		ArrayList<Report> reportList = new ArrayList<Report>();
		
	    reportList.add(new Report("Online", CommonConstraints.ONLINE));
	    reportList.add(new Report("Away", CommonConstraints.AWAY));
	    reportList.add(new Report("Do Not Disturb", CommonConstraints.DO_NOT_DISTURB));
	    reportList.add(new Report("Invisible", CommonConstraints.INVISIBLE));
	    reportList.add(new Report("Offline", CommonConstraints.OFFLINE));
	    reportList.add(new Report("Phoneoff", CommonConstraints.PHONEOFF));
	    reportList.add(new Report("Busy", CommonConstraints.BUSY));
		reportList.add(new Report("Available", CommonConstraints.AVAILABLE));
		reportList.add(new Report("At Office", CommonConstraints.AT_OFFICE));
		reportList.add(new Report("In a Meeting", CommonConstraints.IN_A_MEETING));
		reportList.add(new Report("Low Battery", CommonConstraints.LOW_BATTERY));
		reportList.add(new Report("Sleeping", CommonConstraints.SLEEPING));
		reportList.add(new Report("Emergency Call only", CommonConstraints.EMERGENCY_CALL_ONLY));
		reportList.add(new Report("Can’t talk, Care only", CommonConstraints.CARE_ONLY));

		adapter = new ReportAdapter(getActivity(), R.layout.lavel_item_layout,
				reportList);
		lvStatusList.setAdapter(adapter);
		lvStatusList.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		userOnlinestatusID = (Integer) view.getTag();
		
		if(userOnlinestatusID> 0){
			setUserStatus(userOnlinestatusID);
		}
		
		LoadInformation();
		
	}
	private void LoadInformation() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}
	private void setUserStatus(long userOnlineAvailableStatusID) {
		switch ((int)userOnlineAvailableStatusID) {
		case 1:
			etUserStatus.setText("Online");
			break;
		case 2:
			etUserStatus.setText("Away");
			break;
		case 3:
			etUserStatus.setText("Do Not Disturb");
			break;
		case 4:
			etUserStatus.setText("Invisible");
			break;
		case 5:
			etUserStatus.setText("Offline");
			break;
		case 6:
			etUserStatus.setText("Phoneoff");
			break;
		case 7:
			etUserStatus.setText("Busy");
			break;
		case 8:
			etUserStatus.setText("Available");
			break;
		case 9:
			etUserStatus.setText("At Office");
			break;
		case 10:
			etUserStatus.setText("In a Meeting");
			break;
		case 11:
			etUserStatus.setText("Sleeping");
			break;
		case 12:
			etUserStatus.setText("Emergency call only");
			break;
		case 13:
			etUserStatus.setText("Can't talk, Care only");
			break;
		case 14:
			etUserStatus.setText("Low battery");
			break;
		default:
			etUserStatus.setText("");
			break;
		}
	}

	@Override
	public void showProgressLoader() {
		
	}

	@Override
	public void hideProgressLoader() {
		
		
	}

	@Override
	public Object doBackgroundPorcess() {
		
		IUserManager userManager = new UserManager();
	    return userManager.SetUserOnlineAvailbleStatus(String.valueOf(userOnlinestatusID));
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			User user = (User) data;
			CommonValues.getInstance().LoginUser=user;
			setUserStatus(user.UserOnlineAvailableStatusID);
		}
		if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			CommonTask.DryConnectivityMessage(getActivity(),CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
		}
	}
}
