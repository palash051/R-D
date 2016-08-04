package com.mobilink.app.activities;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;
import com.mobilink.app.R;
import com.mobilink.app.asynchronoustask.DownloadableAsyncTask;
import com.mobilink.app.entities.NetworkKPI;
import com.mobilink.app.entities.NetworkKPIs;
import com.mobilink.app.interfaces.IAsynchronousTask;
import com.mobilink.app.interfaces.INetworkKPIManager;
import com.mobilink.app.manager.NetworkKPIManager;
import com.mobilink.app.utils.CommonURL;
import com.mobilink.app.utils.CommonValues;

import android.app.Activity;

import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Bundle;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;



public class MobilinkNetworkKPIActivity extends Activity implements OnClickListener,IAsynchronousTask {
	
	RelativeLayout rlOperatorNetworkKpiVoice,rlOperatorNetworkKpiData;
	ProgressDialog progress;
	
	private final String KPITYPEVOICE="VOICE";
	private final String KPITYPEDATA="DATA";
	
	private String selectedKpi="";
	DownloadableAsyncTask downloadableAsyncTask ; 
	ImageView ivOperatorIcon;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobilink_network_kpi);		
		rlOperatorNetworkKpiVoice=(RelativeLayout)findViewById(R.id.rlOperatorNetworkKpiVoice);
		rlOperatorNetworkKpiData=(RelativeLayout)findViewById(R.id.rlOperatorNetworkKpiData);
		rlOperatorNetworkKpiVoice.setOnClickListener(this);
		rlOperatorNetworkKpiData.setOnClickListener(this);	
		ivOperatorIcon=(ImageView)findViewById(R.id.ivOperatorIcon);
	}

	@Override
	public void onClick(View v) {
		int id=v.getId();
		if(id==R.id.rlOperatorNetworkKpiVoice){
			/*selectedKpi=KPITYPEVOICE;
			if(downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/
			NetworkKpiVoice.type = "voice";
			Intent intent = new Intent(this, NetworkKpiVoice.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(id==R.id.rlOperatorNetworkKpiData){
			NetworkKpiVoice.type = "data";
			Intent intent = new Intent(this, NetworkKpiVoice.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			/*selectedKpi=KPITYPEDATA;
			if(downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();*/
			
		} 
	}
	@Override
	public void showProgressLoader() {
		progress= ProgressDialog.show(this, "","Please wait", true);
		progress.setCancelable(false);
		progress.setIcon(null);			
	}
	@Override
	public void hideProgressLoader() {
		progress.dismiss();		
	}
	
	@Override
	protected void onResume() {
		
		super.onResume();
		if(!CommonValues.getInstance().SelectedCompany.CompanyLogo.isEmpty()){
			ivOperatorIcon.setVisibility(View.VISIBLE);
			AQuery aq = new AQuery(ivOperatorIcon);
			ImageOptions imgOptions = CommonValues.getInstance().defaultImageOptions;
			imgOptions.fileCache=false;
			imgOptions.memCache=false;
			imgOptions.ratio=0;			
			aq.id(ivOperatorIcon).image(CommonURL.getInstance().getImageServer+CommonValues.getInstance().SelectedCompany.CompanyLogo, imgOptions);
		}else{
			ivOperatorIcon.setVisibility(View.GONE);
		}
	}

	@Override
	public Object doBackgroundPorcess() {
		
		INetworkKPIManager manager=new NetworkKPIManager();
		
		return manager.GetAllNetworkKPIByType(selectedKpi);
	}
	@Override
	public void processDataAfterDownload(Object data) {
		
		if(data!=null){
			NetworkKPIs networkKPIs=(NetworkKPIs)data;
			if(networkKPIs.NetworkKPIList!=null && networkKPIs.NetworkKPIList.size()>0){
				String chxl="chxl=0:|";
				String q1="",q2="",q3="",q4="";
				int maxVal=0;
				for (NetworkKPI networkKPI : networkKPIs.NetworkKPIList) {
					if(!chxl.contains(networkKPI.ReportSubType)){
						chxl=chxl+networkKPI.ReportSubType+"|";
						if(networkKPI.Value> maxVal){
							maxVal=(int) networkKPI.Value;
						}
						for (NetworkKPI kpi : networkKPIs.NetworkKPIList) {
							if(kpi.ReportSubType.equals(networkKPI.ReportSubType)){
								if(kpi.QuarterName.equals("Q1")){
									q1=q1+kpi.Value+",";
								}else if(kpi.QuarterName.equals("Q2")){
									q2=q2+kpi.Value+",";
								}else if(kpi.QuarterName.equals("Q3")){
									q3=q3+kpi.Value+",";
								}else if(kpi.QuarterName.equals("Q4")){
									q4=q4+kpi.Value+",";
								}
							}
						}
					}
				}
				if(!q1.isEmpty()){
					q1=q1.substring(0,q1.length()-1);
				}
				if(!q2.isEmpty()){
					q2=q2.substring(0,q2.length()-1);
				}
				if(!q3.isEmpty()){
					q3=q3.substring(0,q3.length()-1);
				}
				if(!q4.isEmpty()){
					q4=q4.substring(0,q4.length()-1);
				}
				if(!chxl.isEmpty()){
					chxl=chxl.substring(0,chxl.length()-1);
				}
				String chd="chd=t:",chm="",chco="",chdl="chdl=";
				chm="chm=N,000000,-1,10";
				chco="chco=407FCA";				
				if(!q1.isEmpty()){
					chd=chd+q1;	
					chdl=chdl+"Q1";
				}
				if(!q2.isEmpty()){
					if(!chd.isEmpty()){
						chd=chd+"|"+q2;
						chm=chm+"|N,000000,-1,10";
						chco=chco+",CE413E";
						chdl=chdl+"|Q2";
					}
					else{
						chd=chd+q2;
						chdl=chdl+"Q2";
					}
				}
				if(!q3.isEmpty()){
					if(!chd.isEmpty()){
						chd=chd+"|"+q3;
						chm=chm+"|N,000000,-1,10";
						chco=chco+",82A33B";
						chdl=chdl+"|Q3";
					}
					else{
						chd=chd+q3;
						chdl=chdl+"|Q3";
					}
				}
				if(!q4.isEmpty()){
					if(!chd.isEmpty()){
						chm=chm+"|N,000000,-1,10";
						chco=chco+",715099";
						chd=chd+"|"+q4;
						chdl=chdl+"|Q4";
					}
					else{
						chd=chd+q4;
						chdl=chdl+"Q4";
					}
				}
				
				maxVal=(int) (Math.round((maxVal + 10)/ 10) * 10);
				
				String chds="chds=0,"+maxVal;
				String chxr="";
				if(maxVal<100)
					chxr="chxr=1,0,"+maxVal+",20";
				else if(maxVal<200)
					chxr="chxr=1,0,"+maxVal+",40";
				else if(maxVal<500)
					chxr="chxr=1,0,"+maxVal+",100";
				else if(maxVal<1000)
					chxr="chxr=1,0,"+maxVal+",200";
				else if(maxVal<2000)
					chxr="chxr=1,0,"+maxVal+",400";
				else if(maxVal<4000)
					chxr="chxr=1,0,"+maxVal+",800";
				else if(maxVal>4000)
					chxr="chxr=1,0,"+maxVal+",1000";
				
				
				String url=CommonURL.getInstance().GoogleBarChartUrl;//+ "chdl=1|2";
				url=url+""+chds+"&"+chxr+"&"+chm+"&"+chco+"&"+chdl;
				url=url+"&"+chxl+"&"+chd;
				url=url.replace(" ", "_");
				GraphActivity.setUrl(url);
				Intent intent = new Intent(this, GraphActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
				//"chds=0,70&chxr=1,0,70,5&chm=N,FF0000,0,-1,10|N,0000FF,1,-1,10&chdl=1|2&chco=407FCA,CC403D&chxl=0:|0~50|50~70|70~85|85~95|95%3E&chd=t:20,30,60,38,50|50,39,55,30,60";
			}
		}
	}
}

