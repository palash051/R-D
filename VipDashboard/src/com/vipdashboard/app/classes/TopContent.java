package com.vipdashboard.app.classes;

import java.util.ArrayList;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.TopContactAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.widget.ListView;

public class TopContent implements IAsynchronousTask {
	
	private Context context;
	private TopContactAdapter adapter;
	DownloadableAsyncTask asyncTask;
	ArrayList<PhoneCallInformation> phoneCallSummeryListNyNumber;
	Activity activity;
	ProgressDialog progress;
	Dialog dialog;
	ListView topContactList;
	//phoneCallSummeryListNyNumber=myNetDatabase.getTotalCallInfoByPhoneNumber();
	
	public TopContent(Context _context) {
		this.context = _context;
	}
	
	public void showTopContentList(){
		dialog = new Dialog(this.context);
		dialog.setTitle("Top Contacts");
		dialog.setContentView(R.layout.health_check_routines);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
		topContactList = (ListView) dialog.findViewById(R.id.lvHelthCheckRoutines);
		LoadInformation();
		dialog.show();
	}
	
	private void LoadInformation(){
		if(asyncTask != null)
			asyncTask.cancel(true);
		asyncTask = new DownloadableAsyncTask(this);
		asyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progress = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		progress.setCancelable(false);
		progress.setMessage("Loading...");
		progress.show();
		
	}

	@Override
	public void hideProgressLoader() {
		progress.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		MyNetDatabase database = new MyNetDatabase(this.context);
		database.open();
		phoneCallSummeryListNyNumber = database.getTotalCallInfoByPhoneNumber();
		database.close();
		return phoneCallSummeryListNyNumber;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			ArrayList<PhoneCallInformation> callInformations = (ArrayList<PhoneCallInformation>) data;
			adapter = new TopContactAdapter(this.context, R.layout.top_content_item_layout, callInformations);
			topContactList.setAdapter(adapter);
		}
	}

}
