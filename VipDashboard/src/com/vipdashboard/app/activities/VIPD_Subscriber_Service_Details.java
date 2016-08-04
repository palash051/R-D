package com.vipdashboard.app.activities;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.R;
import android.os.Bundle;
import android.app.Activity;



public class VIPD_Subscriber_Service_Details extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.vipd_subscriber_performance_indicator);
	}

	
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();			
		super.onResume();
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
				if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
		}
	}

	
	

}
