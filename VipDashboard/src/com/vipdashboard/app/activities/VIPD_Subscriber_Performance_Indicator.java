package com.vipdashboard.app.activities;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.PMKPIDatas;
import com.vipdashboard.app.entities.VIPDSubscriberServiceType;
import com.vipdashboard.app.entities.VIPDSubscriberServiceTypes;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IStatisticsReportManager;
import com.vipdashboard.app.manager.StatisticsReportManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.R;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;



public class VIPD_Subscriber_Performance_Indicator extends MainActionbarBase implements IAsynchronousTask {
	
	TextView tvSMSOriginating,tvSMSTerminating,tvSMSDuration;
	TextView tvMMSOriginating,tvMMSTerminating,tvMMSDuration;
	TextView tvVideoCallOriginating,tvVideoCallTerminating,tvVideoCallDuration;
	TextView tv2GOriginating,tv2GTerminating,tv2GDuration;
	TextView tv3GOriginating,tv3GTerminating,tv3GDuration;
	
	TextView tvDashboardMessage;
	
	DownloadableAsyncTask downloadableAsyncTask;
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.vipd_subscriber_performance_indicator);
		
		Initialization();
	}	
	private void Initialization() {
		tvSMSOriginating=(TextView) findViewById(R.id.tvSMSOriginating);
		tvSMSTerminating=(TextView) findViewById(R.id.tvSMSTerminating);
		tvSMSDuration=(TextView) findViewById(R.id.tvSMSDuration);
		
		tvMMSOriginating=(TextView) findViewById(R.id.tvMMSOriginating);
		tvMMSTerminating=(TextView) findViewById(R.id.tvMMSTerminating);
		tvMMSDuration=(TextView) findViewById(R.id.tvMMSDuration);
		
		tvVideoCallOriginating=(TextView) findViewById(R.id.tvVideoCallOriginating);
		tvVideoCallTerminating=(TextView) findViewById(R.id.tvVideoCallTerminating);
		tvVideoCallDuration=(TextView) findViewById(R.id.tvVideoCallDuration);
		
		tv2GOriginating=(TextView) findViewById(R.id.tv2GOriginating);
		tv2GTerminating=(TextView) findViewById(R.id.tv2GTerminating);
		tv2GDuration=(TextView) findViewById(R.id.tv2GDuration);
		
		tv3GOriginating=(TextView) findViewById(R.id.tv3GOriginating);
		tv3GTerminating=(TextView) findViewById(R.id.tv3GTerminating);
		tv3GDuration=(TextView) findViewById(R.id.tv3GDuration);
		tvDashboardMessage=(TextView) findViewById(R.id.tvDashboardMessage);
		
		
		tvSMSOriginating.setText("0");
		tvSMSTerminating.setText("0");
		tvSMSDuration.setText("0");
		
		
		tvMMSOriginating.setText("0");
		tvMMSTerminating.setText("0");
		tvMMSDuration.setText("0");
		
		tv2GOriginating.setText("0");
		tv2GTerminating.setText("0");
		tv2GDuration.setText("0");
		
		tv3GDuration.setText("0");
		tv3GTerminating.setText("0");
		tv3GOriginating.setText("0");
		
		tvVideoCallOriginating.setText("0");
		tvVideoCallTerminating.setText("0");
		tvVideoCallDuration.setText("0");	
		
		
		tvDashboardMessage.setText("Update as of "+CommonTask.getCurrentDateTimeAsString());
		
	}
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();			
		super.onResume();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
			if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		}
		LoadInformation();
	}
	@Override
	public void showProgressLoader() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void hideProgressLoader() {
		// TODO Auto-generated method stub
	}
	
	private void LoadInformation() {
		if(downloadableAsyncTask != null){
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	
	@Override
	public Object doBackgroundPorcess() {
		IStatisticsReportManager manager=new StatisticsReportManager();
		return manager.getSubscriberServiceType("882");
	}
	@Override
	public void processDataAfterDownload(Object data) {
		if(data!=null){
			VIPDSubscriberServiceTypes obje=(VIPDSubscriberServiceTypes)data;
			if(obje!=null){				
				
				
				//tvSMSOriginating.setText(""+obje.VIPDSubscriberServiceTs.SMS);
				
				tvSMSOriginating.setText(""+obje.VIPDSubscriberServiceTs.SMS);
				tvSMSTerminating.setText(""+obje.VIPDSubscriberServiceTs.SMS);
				tvSMSDuration.setText(""+obje.VIPDSubscriberServiceTs.SMS);
				
				
				tvMMSOriginating.setText(""+obje.VIPDSubscriberServiceTs.MMS);
				tvMMSTerminating.setText(""+obje.VIPDSubscriberServiceTs.MMS);
				tvMMSDuration.setText(""+obje.VIPDSubscriberServiceTs.MMS);
				
				tv2GOriginating.setText(""+obje.VIPDSubscriberServiceTs.C2G);
				tv2GTerminating.setText(""+obje.VIPDSubscriberServiceTs.C2G);
				tv2GDuration.setText(""+obje.VIPDSubscriberServiceTs.C2G);
				
				tv3GDuration.setText(""+obje.VIPDSubscriberServiceTs.C3G);
				tv3GTerminating.setText(""+obje.VIPDSubscriberServiceTs.C3G);
				tv3GOriginating.setText(""+obje.VIPDSubscriberServiceTs.C3G);
				
				tvVideoCallOriginating.setText(""+obje.VIPDSubscriberServiceTs.VideoCall);
				tvVideoCallTerminating.setText(""+obje.VIPDSubscriberServiceTs.VideoCall);
				tvVideoCallDuration.setText(""+obje.VIPDSubscriberServiceTs.VideoCall);			
				
				
				
				/*for(int i=0;i<obje.size();i++){
					
					Toast.makeText(this, "Fired ", Toast.LENGTH_LONG);
					
					tvSMSOriginating.setText(""+obje.VIPDSubscriberServiceTs.SMS);
					
					tvSMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).SMS);
					tvSMSTerminating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).SMS);
					tvSMSDuration.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).SMS);
					
					tvMMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).MMS);
					tvMMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).MMS);
					tvMMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).MMS);
					
					tvSMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).VideoCall);
					tvSMSTerminating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).VideoCall);
					tvSMSDuration.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).VideoCall);
					
					tvSMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).C3G);
					tvSMSTerminating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).C3G);
					tvSMSDuration.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).C3G);
					
					tvSMSOriginating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).SMS);
					tvSMSTerminating.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).SMS);
					tvSMSDuration.setText(""+vIPDSubscriberServiceTypes.VIPDSubscriberServiceTypes.get(i).SMS);
					}*/
			}
			/*VIPD pMKPIDatas=(PMKPIDatas)data;
			DashboradDetailsActivity.pMKPIDatas = pMKPIDatas;
			if(pMKPIDatas!=null &&  pMKPIDatas.PMKPIDatas.size()>0){
				for(int i=0;i<pMKPIDatas.PMKPIDatas.size();i++){
					switch(i)
					{
					case 0:
						gvDashboardAcquisition.setTargetValue( (float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
						tvDashboardFirstTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardAcquisitionValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						rlDashboardAcquisition.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
					case 1:
						gvDashboardRetention.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(i).KPIValue);
						tvDashboardSecondTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardRetentionValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).KPIValue));
						rlDashboardRetention.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
					case 2:
						gvDashboardEfficiency.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(2).KPIValue);
						tvDashboardThirdTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardEfficiencyValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(2).KPIValue));
						rlDashboardEfficiency.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
					case 3:
						gvDashboardTraffic.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(3).KPIValue);
						tvDashboardForthTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardTrafficValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(3).KPIValue));
						rlDashboardTraffic.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
						
					case 4:
						gvDashboardMTR.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(4).KPIValue);
						tvDashboardFifthTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardMTRValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(4).KPIValue));
						rlDashboardMTR.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
						
					case 5:
						gvDashboardSelfService.setTargetValue((float) pMKPIDatas.PMKPIDatas.get(5).KPIValue);
						tvDashboardSixthTitle.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(i).PMKPI.DisplayName));
						tvDashboardSelfServiceValue.setText(String.valueOf(pMKPIDatas.PMKPIDatas.get(5).KPIValue));
						rlDashboardSelfService.setTag(pMKPIDatas.PMKPIDatas.get(i).PMKPI.KPIID);
						break;
						
					}
				}
			}*/
		}
		
		else{
		tvSMSOriginating.setText("0");
		tvSMSTerminating.setText("");
		tvSMSDuration.setText("");
		
		
		tvMMSOriginating.setText("0");
		tvMMSTerminating.setText("0");
		tvMMSDuration.setText("0");
		
		tv2GOriginating.setText("0");
		tv2GTerminating.setText("0");
		tv2GDuration.setText("0");
		
		tv3GDuration.setText("0");
		tv3GTerminating.setText("0");
		tv3GOriginating.setText("0");
		
		tvVideoCallOriginating.setText("0");
		tvVideoCallTerminating.setText("0");
		tvVideoCallDuration.setText("0");	
		}
		 if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
		{
				if (!isFinishing()) {
			CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
			CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
				}
		}
		
	}

	
	

}

