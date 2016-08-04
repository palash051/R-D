package com.vipdashboard.app.asynchronoustask;


import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.ReportProblemAndBadExperienceRoot;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonValues;

import android.content.Context;
import android.os.AsyncTask;

public class ProblemTrackingIntegrationAsyncTask extends AsyncTask<Void, Void, Object>{
	Context context;
	
	public ProblemTrackingIntegrationAsyncTask(Context context){
		this.context = context;	
		
	}
	
	@Override
	protected void onPreExecute() {	
	}

	@Override
	protected Object doInBackground(Void... cap) {
		IStatisticsReportManager statisticsReportManager = new StatisticsReportManager();
		return statisticsReportManager.GetAllReportProblemAndBadExperience(CommonValues.getInstance().LoginUser.Mobile);
	}
	
	@Override
	protected void onPostExecute(Object data) {	
		if(data!=null){
			ReportProblemAndBadExperienceRoot reportProblemAndBadExperienceRoot=(ReportProblemAndBadExperienceRoot)data;
			if(reportProblemAndBadExperienceRoot!=null && reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList.size()>0){
				MyNetDatabase db=new MyNetDatabase(context);
				db.open();
				db.deleteReportProblemAndBadExperience();
				for (ReportProblemAndBadExperience rpbe : reportProblemAndBadExperienceRoot.ReportProblemAndBadExperienceList) {
				
					db.CreateReportProblemAndBadExperience(rpbe);
				}
				db.close();
			}
		}
	}

}
