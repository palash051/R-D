package com.vipdashboard.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportAdapter;
import com.vipdashboard.app.adapter.ReportItemAdapter;
import com.vipdashboard.app.adapter.ReportMainAdapter;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class ReportDetailsActivity extends MainActionbarBase implements OnItemSelectedListener, OnItemClickListener{
	
	public static final int OPTIMIZATION_CS_CORE=0;
	public static final int OPTIMIZATION_PS_CORE=1;
	public static final int OPTIMIZATION_RAN=2;
	public static final int OPERATION_VAS=3;
	public static final int OPERATION_IN=4;
	public static final int OPERATION_NOC_FOPS=5;	
	
	public static int selectedReport=0;
	
	Spinner spinner;
	ListView lview;
	ArrayList<Report> sreport;
	ArrayList<Report> lreport;
	ReportMainAdapter adapter;
	ReportItemAdapter ladapter;
	TextView titleText;
	int lavelID;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reportdetails_layout);
		if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		spinner = (Spinner) findViewById(R.id.reportSpinner);
		lview = (ListView) findViewById(R.id.report_list_item);
		titleText = (TextView) findViewById(R.id.tvReportTitle);
		sreport = new ArrayList<Report>();
		
		if(selectedReport == OPTIMIZATION_CS_CORE){
			titleText.setText("CS Core Report");			
			sreport.add(new Report("Accessibility", 1));
			//sreport.add(new Report("Retainibility", 2));			
			adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, sreport);
			spinner.setAdapter(adapter);
		}else if(selectedReport == OPTIMIZATION_PS_CORE){
			titleText.setText("PS Core Report");			
			sreport.add(new Report("Accessibility", 1));
			sreport.add(new Report("Retainibility", 2));		
			adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, sreport);
			spinner.setAdapter(adapter);
		}else if(selectedReport == OPTIMIZATION_RAN){
			titleText.setText("RAN Report");			
			sreport.add(new Report("2G", 1));
			sreport.add(new Report("3G", 2));
			sreport.add(new Report("LTE", 3));
			adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, sreport);
			spinner.setAdapter(adapter);
		}else if(selectedReport == OPERATION_VAS){
			titleText.setText("VAS Report");			
			sreport.add(new Report("SMS", 1));
			sreport.add(new Report("USSD GW", 2));
			sreport.add(new Report("CRBT", 3));
			sreport.add(new Report("MMS", 4));
			sreport.add(new Report("WAP", 5));
			//sreport.add(new Report("SMS GM", 6));
			adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, sreport);
			spinner.setAdapter(adapter);
		}else if(selectedReport == OPERATION_IN){
			titleText.setText("IN Report");			
			sreport.add(new Report("AIR", 1));
			sreport.add(new Report("CCN", 2));
			sreport.add(new Report("EMA", 3));
			sreport.add(new Report("EMM", 4));
			adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, sreport);
			spinner.setAdapter(adapter);
		}else if(selectedReport == OPERATION_NOC_FOPS){
			titleText.setText("NOC Report");			
			sreport.add(new Report("RAN", 1));
			sreport.add(new Report("Transmission", 2));
			sreport.add(new Report("CORE", 3));
			sreport.add(new Report("VAS", 4));
			sreport.add(new Report("IN", 5));
			sreport.add(new Report("NOC Report", 6));
			adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, sreport);
			spinner.setAdapter(adapter);
		}
		else if(CommonValues.getInstance().ExceptionCode == CommonConstraints.IO_EXCEPTION)
			{
				CommonTask.DryConnectivityMessage(this,CommonValues.SERVER_DRY_CONNECTIVITY_MESSAGE);
				CommonValues.getInstance().ExceptionCode= CommonConstraints.NO_EXCEPTION;
			}
		spinner.setOnItemSelectedListener(this);
		lview.setOnItemClickListener(this);
		
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		lavelID = Integer.parseInt(String.valueOf(view.getTag()));
		//Log.e("ID", lavelID);
		lreport = new ArrayList<Report>();
		if (selectedReport == OPTIMIZATION_CS_CORE) {
			if(lavelID == 1){
				lreport.add(new Report("Overall MSS Accessibility", 1));
				lreport.add(new Report("Top impacting Cause Code/Clear Code/End Selection", 2));
				lreport.add(new Report("Element Level ABR", 3));
				lreport.add(new Report("Incoming POI ASR", 4));
				lreport.add(new Report("Outgoing ABR", 5));
				lreport.add(new Report("Call setup Success rate", 6));
				lreport.add(new Report("RTP Packet Loss", 7));
				lreport.add(new Report("SIGTRAN/TDM load and signalling symetry", 8));
				lreport.add(new Report("Hand over success rate", 9));
				lreport.add(new Report("MAP failures", 10));
				lreport.add(new Report("Paging Success rate", 11));
				lreport.add(new Report("SMS(MO/MT) success rate", 12));
				lreport.add(new Report("Location Update Success Rate", 13));
				lreport.add(new Report("License Utilization", 14));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}
		}else if(selectedReport == OPTIMIZATION_PS_CORE){
			
			if(lavelID == 1){
				lreport.add(new Report("Attach Failure Ratio", 1));
				lreport.add(new Report("PDP Context Activation Failure Ratio", 2));
				//lreport.add(new Report("PDP Context Cutoff Ratio", 3));
				//lreport.add(new Report("WCDMA Service Request Failure Ratio", 4));
				//lreport.add(new Report("WCDMA Radio Access Bearer Establishment Failure Ratio", 5));
				//lreport.add(new Report("Paging Failure Ratio", 6));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);

			}else{
				lreport.add(new Report("Mean Data Rate ( User Throughput 2G: GSM/EDGE)", 1));
				lreport.add(new Report("WCDMA Service Request Failure Ratio", 2));
				lreport.add(new Report("WCDMA Radio Access Bearer Establishment Failure Ratio", 3));
				lreport.add(new Report("Intra Sgsn Rau Failure Ratio", 4));
				//lreport.add(new Report("Attach Setup Time", 5));
				//lreport.add(new Report("PDP Context Activation Time", 6));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);

			}
		}else if(selectedReport == OPTIMIZATION_RAN){
			if(lavelID == 1){
				lreport.add(new Report("Voice Traffic", 1));
				lreport.add(new Report("Call Setup Success Rate CS", 2));
				lreport.add(new Report("Random Access Success Rate", 3));
				lreport.add(new Report("TCH Assignment Success Rate", 4));
				lreport.add(new Report("SDCCH Drop Rate", 5));
				lreport.add(new Report("Handover Success Rate", 6));
				lreport.add(new Report("TCH Drop Rate CS", 7));
				lreport.add(new Report("TCH Availability", 8));
				lreport.add(new Report("Cell Availability", 9));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 2){
				lreport.add(new Report("CS Traffic", 1));
				lreport.add(new Report("DL Data Volume R99", 2));
				lreport.add(new Report("Call Setup Success Rate PS", 3));
				lreport.add(new Report("IRAT Handover Success Rate for Speech", 4));
				lreport.add(new Report("Drop Call Rate Speech", 5));
				lreport.add(new Report("Drop Call Rate Video", 6));
				lreport.add(new Report("Call Completion Success Rate Speech", 7));
				lreport.add(new Report("Call Completion Success Rate Video", 8));
				lreport.add(new Report("Cell Availability", 9));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 3){
				lreport.add(new Report("Session Setup Success Rate", 1));
				lreport.add(new Report("Cell Availability", 2));
				lreport.add(new Report("Session Abnormal Release Rate", 3));
				lreport.add(new Report("DL Data Volume", 4));
				lreport.add(new Report("UL Data Volume", 5));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}
		}else if(selectedReport == OPERATION_VAS){
			if(lavelID == 1){
				lreport.add(new Report("SMS Delivery time (sec)", 1));
				lreport.add(new Report("License Utilization", 2));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 2){
				lreport.add(new Report("E Recharge success rate", 1));
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 3){
				lreport.add(new Report("CRBT Connection Success Rate", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 4){
				lreport.add(new Report("MMS Delivery time (sec)", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 5){
				lreport.add(new Report("WAP Success Rate", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}
		}else if(selectedReport == OPERATION_IN){
			if(lavelID == 1){
				lreport.add(new Report("Refill Failure Statistics ", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 2){
				lreport.add(new Report("Diameter Success Rate (%)", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 3){
				lreport.add(new Report("SIM Change Statistics", 1));
				lreport.add(new Report("Provisioning Statistics", 2));
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 4){
				lreport.add(new Report("Transaction Success Rate (%)", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}
		}else if(selectedReport == OPERATION_NOC_FOPS){
			if(lavelID == 1){
				lreport.add(new Report("Overall Cell Availability", 1));
				lreport.add(new Report("CP Load for 2G", 2));
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 2){
				lreport.add(new Report("Transmission Availability", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 3){
				lreport.add(new Report("SGSN Node Availability", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 4){
				lreport.add(new Report("SMS-C Service Availability", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 5){
				lreport.add(new Report("Overall IN Nodes Availability", 1));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}else if(lavelID == 6){
				lreport.add(new Report("Number of Open TT", 1));
				lreport.add(new Report("FLO Response Time", 2));
				lreport.add(new Report("TT Closed", 3));
				lreport.add(new Report("TT Closed breaching", 4));
				lreport.add(new Report("MTTR report", 5));
				lreport.add(new Report("Change Request Success Rate", 6));
				
				ladapter = new ReportItemAdapter(this, R.layout.report_item_layout, lreport);
				lview.setAdapter(ladapter);
			}
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		int id = Integer.parseInt(String.valueOf(view.getTag()));
		if(selectedReport == OPTIMIZATION_CS_CORE && lavelID == 1){
			if(id == 1){
				DailyKPIActivity.name = "Overall MSS Accessibility";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/OverallMSSAccessibility.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				/*Intent intent = new Intent(this,DemoScreenActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);*/
			}else if(id == 2){						
				DailyKPIActivity.name = "Top impacting Caused";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/Topimpacting.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 3){										
				DailyKPIActivity.name = "Element Level ABR";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/ElementLevelABR.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 4){
										
				DailyKPIActivity.name = "Incoming POI ASR";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/IncomingPOIASR.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 5){
											
				DailyKPIActivity.name = "Outgoing ABR";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/OutgoingABR.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 6){
								
				DailyKPIActivity.name = "Call setup Success rate";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSSR.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 7){
										
				DailyKPIActivity.name = "RTP Packet Loss";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/RTPPacketLoss.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 8){
							
				DailyKPIActivity.name = "SIGTRAN/TDM load and signalling symetry";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/SIGTRANTDMload.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 9){
									
				DailyKPIActivity.name = "Hand over success rate";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/HOSR.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 10){
											
				DailyKPIActivity.name = "MAP failures";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/MAPfailures.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 11){
									
				DailyKPIActivity.name = "Paging Success rate";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PagingSuccessRate.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 12){
									
				DailyKPIActivity.name = "SMS(MO/MT) success rate";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMSSuccessRate.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 13){
									
				DailyKPIActivity.name = "Location Update Success Rate";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSCore/LocationUpdate SuccessRate.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}else if(id == 14){
									
				DailyKPIActivity.name = "License Utilization";
				DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SMSUtilization.aspx";
				Intent intent = new Intent(this,DailyKPIActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		}else if(selectedReport == OPTIMIZATION_PS_CORE){
			
			if(lavelID == 1){
				if(id == 1){
					DailyKPIActivity.name = "Attach Failure Ratio";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PSCore/AttachFailureRatio.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "PDP Context Activation Failure Ratio";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/GPRSPDP.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 2){
				if(id == 1){
					DailyKPIActivity.name = "Mean Data Rate(User Throughput 2G: GSM/EDGE)";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PSCore/MeanDataRate.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "WCDMA Service Request Failure Ratio";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PSCore/WCDMARadioAccessBearer.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 3){
					/*DailyKPIActivity.name = "WCDMA Radio Access Bearer Establishment Failure Ratio";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PSCore/WCDMAService.aspx";*/
					Intent intent = new Intent(this,DemoScreenActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 4){
					DailyKPIActivity.name = "Intra Sgsn Rau Failure Ratio";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/PSCore/IntraSgsnRauFailureRatio.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}
		}else if(selectedReport == OPTIMIZATION_RAN){
			
			if(lavelID == 1){
				if(id == 1){
					DailyKPIActivity.name = "Voice Traffic";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/VoiceTraffic.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "Call Setup Success Rate CS";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/CSSR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 3){
					DailyKPIActivity.name = "Random Access Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/RACH.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 4){
					DailyKPIActivity.name = "TCH Assignment Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHAssignmentSR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 5){
					DailyKPIActivity.name = "SDCCH Drop Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/SDCCH.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 6){
					DailyKPIActivity.name = "Handover Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/HOSR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 7){
					DailyKPIActivity.name = "TCH Drop Rate CS";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHDrop.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 8){		
					DailyKPIActivity.name = "TCH Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/TCHAll.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 9){			
					DailyKPIActivity.name = "Cell Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/CellAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 2){
				if(id == 1){
												
					DailyKPIActivity.name = "CS Traffic";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/VoiceTraffic.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
											
					DailyKPIActivity.name = "DL Data Volume R99";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/DataVolume.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 3){
										
					DailyKPIActivity.name = "Call Setup Success Rate PS";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/PSCSSR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 4){
								
					DailyKPIActivity.name = "IRAT Handover Success Rate for Speech";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/IRATHOSR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 5){
										
					DailyKPIActivity.name = "Drop Call Rate Speech";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/DropCallrateSpeech.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 6){
											
					DailyKPIActivity.name = "Drop Call Rate Video";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/DropCallrateVideo.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 7){
								
					/*DailyKPIActivity.name = "Call Completion Success Rate Speech";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/3GCCSRS.aspx";*/
					Intent intent = new Intent(this,DemoScreenActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 8){
									
					DailyKPIActivity.name = "Call Completion Success Rate Video";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/3GCCSRSVideo.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 9){
											
					DailyKPIActivity.name = "Cell Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/3G/CellAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 3){
				if(id == 1){
										
					/*DailyKPIActivity.name = "Session Setup Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Report/LTE/SSSR.aspx";*/
					Intent intent = new Intent(this,DemoScreenActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
											
					DailyKPIActivity.name = "Cell Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/CellAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 3){
					 				
					DailyKPIActivity.name = "Session Abnormal Release Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/SARR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 4){
											
					DailyKPIActivity.name = "DL Data Volume";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/DataVolume.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 5){
											
					DailyKPIActivity.name = "UL Data Volume";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/LTE/DataVolume.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}
		}else if(selectedReport == OPERATION_VAS){
			
			if(lavelID == 1){
				if(id==1){
					DailyKPIActivity.name = "SMS Delivery time (sec)";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/VASOperation/SMSDeliverytime.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "E Recharge success rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/VASOperation/ERechargeSuccessRate.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 2){
				if(id == 1){
					DailyKPIActivity.name = "CRBT Connection Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/VASOperation/CRBTConnectionSuccessRate.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 3){
				if(id == 1){
					DailyKPIActivity.name = "MMS Delivery time (sec)";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/VASOperation/MMSDeliverytime.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 4){
				if(id == 1){
					DailyKPIActivity.name = "WAP Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/VASOperation/WAPSuccessRate.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 5){
				if(id == 1){
					DailyKPIActivity.name = "License Utilization";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/VASOperation/LicenseUtilization.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}
		}else if(selectedReport == OPERATION_IN){
			
			if(lavelID == 1){
				if(id == 1){
					DailyKPIActivity.name = "Refill Failure Statistics";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/INOPeration/RefillFailureStatistics.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 2){
				if(id == 1){
					DailyKPIActivity.name = "Diameter Success Rate (%)";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/INOperation/DiameterSuccessRate.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 3){
				if(id == 1){
					DailyKPIActivity.name = "SIM Change Statistics";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/INOPeration/SIMChangeStatistics.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "Provisioning Statistics";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/INOPeration/ProvisioningStatistics.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 4){
				if(id == 1){
					DailyKPIActivity.name = "Transaction Success Rate (%)";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/INOPeration/TransactionCounters.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}
		}else if(selectedReport == OPERATION_NOC_FOPS){
			
			if(lavelID == 1){
				if(id == 1){
					DailyKPIActivity.name = "Overall Cell Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/NOCRANOverallCellAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "CP Load for 2G";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/NOCCPLoadfor2G.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 2){
				if(id == 1){
					DailyKPIActivity.name = "Transmission Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/NOCTransmissionAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 3){
				if(id == 1){
					DailyKPIActivity.name = "SGSN Node Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/SGSNNodeAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 4){
				if(id == 1){
					DailyKPIActivity.name = "SMS-C Service Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/SMSCServiceAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 5){
				if(id == 1){
					DailyKPIActivity.name = "Overall IN Nodes Availability";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/OverallINNodesAvailability.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}else if(lavelID == 6){
				if(id == 1){
					DailyKPIActivity.name = "Number of Open TT";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/OpenTT.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 2){
					DailyKPIActivity.name = "FLO Response Time";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/FLOResponseTime.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 3){
					DailyKPIActivity.name = "TT Closed";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/ClosedTT.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 4){
					DailyKPIActivity.name = "TT Closed breaching";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/ClosedTTBreaching.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 5){
					DailyKPIActivity.name = "MTTR report";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/MTTR.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}else if(id == 6){
					DailyKPIActivity.name = "Change Request Success Rate";
					DailyKPIActivity.URL = "http://120.146.188.232:9200/Reports/NocTT/ChangeRequestSuccessRate.aspx";
					Intent intent = new Intent(this,DailyKPIActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					startActivity(intent);
				}
			}
		}
	}

}
