package com.mobilink.app.activities;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilink.app.R;
import com.mobilink.app.adapter.ListItemAdapter;
import com.mobilink.app.customcontrols.GaugeView;
import com.mobilink.app.entities.KPICustom;
import com.mobilink.app.entities.KPICustoms;
import com.mobilink.app.entities.CompareListItem;
import com.mobilink.app.utils.CommonURL;

public class KPI_CompareListActivity extends FragmentActivity implements  OnClickListener, OnItemClickListener{
	TextView tvReportHeader;
	public static String selectedHeader;
	public static int selectedid;
	public static KPICustoms kPICustoms;
	
	public static ArrayList<CompareListItem> ComapareList;
	ListView lvCompareList;
	RelativeLayout rlCompare,rlTCHTraffic,rlCSSR,rlDCR,rlHOSR,rlTCHCongestion,rlDLTBFSR,rlTCHAVG,rlNWAVG;
	
	String selectedNames="";
	String selectedIds="";
	
	String selectedKpi="";
	int selectedKpiId;
	
	static boolean isRegionData;
	
	GaugeView gvCSSR,gvDCR,gvHOSR,gvTCHCongestion,gvDLTBFSR,gvTCHAVG,gvNWAVG;
	
	TextView tvTCHTrafficValue,tvCSSRValue,tvDCRValue,tvHOSRValue,tvTCHCongestionValue,tvDLTBFSRValue,tvTCHAVGValue,tvNWAVGValue;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compare_list);
		tvReportHeader = (TextView) findViewById(R.id.tvReportHeader);
		lvCompareList = (ListView) findViewById(R.id.lvCompareList);
		rlCompare= (RelativeLayout) findViewById(R.id.rlCompare);
		rlCompare.setOnClickListener(this);
		lvCompareList.setOnItemClickListener(this);
		
		
		rlTCHTraffic = (RelativeLayout) findViewById(R.id.rlTCHTraffic);
		rlCSSR = (RelativeLayout) findViewById(R.id.rlCSSR);
		rlDCR = (RelativeLayout) findViewById(R.id.rlDCR );
		rlHOSR = (RelativeLayout) findViewById(R.id.rlHOSR );
		rlTCHCongestion = (RelativeLayout) findViewById(R.id.rlTCHCongestion );
		rlDLTBFSR = (RelativeLayout) findViewById(R.id.rlDLTBFSR );
		rlTCHAVG = (RelativeLayout) findViewById(R.id.rlTCHAVG );
		rlNWAVG = (RelativeLayout) findViewById(R.id.rlNWAVG );
		
		rlTCHTraffic.setOnClickListener(this);
		rlCSSR.setOnClickListener(this);
		rlDCR.setOnClickListener(this);
		rlHOSR.setOnClickListener(this);
		rlTCHCongestion.setOnClickListener(this);
		rlDLTBFSR.setOnClickListener(this);
		rlTCHAVG.setOnClickListener(this);
		rlNWAVG.setOnClickListener(this);		
		
		gvCSSR = (GaugeView) findViewById(R.id.gvCSSR);
		gvDCR = (GaugeView) findViewById(R.id.gvDCR);
		gvHOSR = (GaugeView) findViewById(R.id.gvHOSR);
		gvTCHCongestion = (GaugeView) findViewById(R.id.gvTCHCongestion);
		gvDLTBFSR = (GaugeView) findViewById(R.id.gvDLTBFSR);
		gvTCHAVG = (GaugeView) findViewById(R.id.gvTCHAVG);
		gvNWAVG = (GaugeView) findViewById(R.id.gvNWAVG);
		
		tvTCHTrafficValue= (TextView) findViewById(R.id.tvTCHTrafficValue);
		tvCSSRValue= (TextView) findViewById(R.id.tvCSSRValue);
		tvDCRValue= (TextView) findViewById(R.id.tvDCRValue);
		tvHOSRValue= (TextView) findViewById(R.id.tvHOSRValue);
		tvTCHCongestionValue= (TextView) findViewById(R.id.tvTCHCongestionValue);
		tvDLTBFSRValue= (TextView) findViewById(R.id.tvDLTBFSRValue);
		tvTCHAVGValue= (TextView) findViewById(R.id.tvTCHAVGValue);
		tvNWAVGValue= (TextView) findViewById(R.id.tvNWAVGValue);
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		selectedNames="";
		selectedIds="";
		tvReportHeader.setText(selectedHeader);
		lvCompareList.setAdapter(new ListItemAdapter(this, R.layout.operator_item_layout, ComapareList));
		processData();
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.rlCompare){
			if(selectedIds.isEmpty()){				
				Toast.makeText(this, "Please select region or zone for compare.", Toast.LENGTH_LONG).show();
				return;
			}
			if(selectedKpiId==0){				
				Toast.makeText(this, "Please select a KPI for compare.", Toast.LENGTH_LONG).show();
				return;
			}
			
			KPI_Compare_ReportActivity.compareFrom=selectedHeader;			
			KPI_Compare_ReportActivity.compareTo=selectedNames.substring(0,selectedNames.length()-1);
			KPI_Compare_ReportActivity.selectedSubheader=selectedKpi;
			if(isRegionData)				
				KPI_Compare_ReportActivity.selectedURL=CommonURL.getInstance().applicationReportUrl+"MLink/CompareRegionWise.aspx?reqd="+selectedid+","+selectedIds+"|"+selectedKpiId;
			else
				KPI_Compare_ReportActivity.selectedURL=CommonURL.getInstance().applicationReportUrl+"MLink/CompareZoneWise.aspx?reqd="+selectedid+","+selectedIds+"|"+selectedKpiId;
			Intent intent = new Intent(this, KPI_Compare_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(v.getId() == R.id.rlTCHTraffic){
			selectedKpi="TCH Traffic";	
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			setSelection(1);
			selectedKpiId=1;
		}else if(v.getId() == R.id.rlCSSR){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="Call Setup Success Rate %";
			setSelection(2);
			selectedKpiId=2;
		}else if(v.getId() == R.id.rlDCR){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="DCR";
			setSelection(3);
			selectedKpiId=3;
		}else if(v.getId() == R.id.rlHOSR){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="Hand Over Success Rate %";
			setSelection(4);
			selectedKpiId=4;
		}else if(v.getId() == R.id.rlTCHCongestion){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="TCH Congestion";
			setSelection(5);
			selectedKpiId=5;
		} else if(v.getId() == R.id.rlDLTBFSR){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="DL TBF SR";
			setSelection(6);
			selectedKpiId=6;
		}else if(v.getId() == R.id.rlTCHAVG){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="TCH Availability";
			setSelection(7);
			selectedKpiId=7;
		}else if(v.getId() == R.id.rlNWAVG){
			if(v.getTag()==null){
				Toast.makeText(this, "No data available for this KPI", Toast.LENGTH_SHORT).show();
				return;
			}
			selectedKpi="Network Availability";
			setSelection(8);
			selectedKpiId=7;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		try{
			
				CompareListItem item = (CompareListItem) view.getTag();
				if(item != null){
					String id = String.valueOf(item.ItemId);
					if(!selectedNames.contains(item.ItemName)){
						selectedNames=selectedNames+item.ItemName+",";
					}
					if (!id.equals("")) {
						
						if(selectedIds.isEmpty()){
							selectedIds=id;						
						}else{
							String newIds="";
							String[] ids =selectedIds.split(",");
							boolean isAdded=false;
							for (String st : ids) {
								if(!st.equalsIgnoreCase(id)){
									newIds=newIds+st+",";
								}else{
									isAdded=true;
								}
							}
							if(!isAdded)
								newIds=newIds+id+",";
							if(newIds.length()>0)
								newIds=newIds.substring(0, newIds.length()-1);
							selectedIds=newIds;
						}
					}
				}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void processData() {
		if(kPICustoms!=null && kPICustoms.KPICustomList!=null && kPICustoms.KPICustomList.size()>0){
			for (KPICustom kPICustom : kPICustoms.KPICustomList) {
				if(kPICustom.KPIName.equals("Traffic")){
					tvTCHTrafficValue.setText(String.valueOf(kPICustom.Value));
					rlTCHTraffic.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("CSSR")){
					tvCSSRValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvCSSR.setTargetValue((float)kPICustom.Value);
					rlCSSR.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("DCR")){
					tvDCRValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvDCR.setTargetValue((float)kPICustom.Value);
					rlDCR.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlDCR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlDCR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlDCR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("HOSR")){
					tvHOSRValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvHOSR.setTargetValue((float)kPICustom.Value);
					rlHOSR.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("TCH Congestion")){
					tvTCHCongestionValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvTCHCongestion.setTargetValue((float)kPICustom.Value);
					rlTCHCongestion.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("DL TBF SR")){
					tvDLTBFSRValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvDLTBFSR.setTargetValue((float)kPICustom.Value);
					rlDLTBFSR.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("TCH Availability")){
					tvTCHAVGValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvTCHAVG.setTargetValue((float)kPICustom.Value);
					rlTCHAVG.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}else if(kPICustom.KPIName.equals("NW Availability")){
					tvNWAVGValue.setText(String.valueOf(kPICustom.Value)+"%");
					gvNWAVG.setTargetValue((float)kPICustom.Value);
					rlNWAVG.setTag(kPICustom.SLA);
					if(kPICustom.SLA==1){
						rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
					}else if(kPICustom.SLA==2){
						rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
					}else if(kPICustom.SLA==3){
						rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
					}
				}
			}
		}
	}
	
	private void setSelection(int rlId){
		
		int SLA =Integer.valueOf(rlTCHTraffic.getTag()==null ?"0":String.valueOf(rlTCHTraffic.getTag()));
		if(SLA==1){
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlCSSR.getTag()==null ?"0":rlCSSR.getTag()));
		if(SLA==1){
			rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlDCR.getTag()==null ?"0":rlDCR.getTag()));
		if(SLA==1){
			rlDCR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlDCR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlDCR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlHOSR.getTag()==null ?"0":rlHOSR.getTag()));
		if(SLA==1){
			rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlTCHCongestion.getTag()==null ?"0":rlTCHCongestion.getTag()));
		if(SLA==1){
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlDLTBFSR.getTag()==null ?"0":rlDLTBFSR.getTag()));
		if(SLA==1){
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlTCHAVG.getTag()==null ?"0":rlTCHAVG.getTag()));
		if(SLA==1){
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		SLA =Integer.valueOf(String.valueOf(rlNWAVG.getTag()==null ?"0":rlNWAVG.getTag()));
		if(SLA==1){
			rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_green));
		}else if(SLA==2){
			rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_yellow));
		}else if(SLA==3){
			rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.home_kpi_color_red));
		}
		
		if(rlId==1)
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));
		else if(rlId==2)
			rlCSSR.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));
		else if(rlId==3)
			rlDCR.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));
		else if(rlId==4)
			rlHOSR.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));
		else if(rlId==5)
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));
		else if(rlId==6)
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));
		else if(rlId==7)
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));		
		else if(rlId==8)
			rlNWAVG.setBackgroundColor(this.getResources().getColor(R.color.selected_tab));		
	}
}
