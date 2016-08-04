package com.mobilink.app.activities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.mobilink.app.R;
import com.mobilink.app.adapter.ZoneAdapter;
import com.mobilink.app.asynchronoustask.DownloadableAsyncTask;
import com.mobilink.app.customcontrols.GaugeView;
import com.mobilink.app.entities.CompareListItem;
import com.mobilink.app.entities.KPICustom;
import com.mobilink.app.entities.KPICustoms;
import com.mobilink.app.interfaces.IAsynchronousTask;
import com.mobilink.app.interfaces.INetworkKPIManager;
import com.mobilink.app.manager.NetworkKPIManager;
import com.mobilink.app.utils.CommonTask;
import com.mobilink.app.utils.CommonURL;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener,
		OnMarkerClickListener, IAsynchronousTask, OnItemClickListener {

	RelativeLayout rlHomeCompare, rlHomeRegion, rlTCHTraffic, rlCSSR, rlDCR,
			rlHOSR, rlTCHCongestion, rlDLTBFSR, rlTCHAVG, rlNWAVG;

	private GoogleMap map;
	DownloadableAsyncTask downloadableAsyncTask;
	String companyID;
	ProgressDialog progress;

	GaugeView gvCSSR, gvDCR, gvHOSR, gvTCHCongestion, gvDLTBFSR, gvTCHAVG,
			gvNWAVG;

	TextView tvTitleHome, tvTCHTrafficValue, tvCSSRValue, tvDCRValue,
			tvHOSRValue, tvTCHCongestionValue, tvDLTBFSRValue, tvTCHAVGValue,
			tvNWAVGValue, tvHomeRegion, tvTitleSelectedDate;
	KPICustoms kPICustoms;
	ImageView ivTitleSelectedDate;
	ListView lvZoneList;

	int selectedid = 0;
	boolean isRegionData, isLoadingZoneData;
	String selectedName;
	LinearLayout llHomeCustomer;
	Marker previousMarker;

	Calendar myCalender;
	long selectedDate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_new);

		initalization();

	}

	private void showMessage(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.app_name).setMessage(message)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(HomeActivity.this,
								LoginActivity.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
						startActivity(intent);
						startActivity(new Intent(
								android.provider.Settings.ACTION_SETTINGS));
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isRegionData = true;
		selectedid = 0;
		rlHomeCompare.setEnabled(false);
		isRegionData = true;
		isLoadingZoneData = false;
		tvTitleHome.setText("Network");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		tvTitleSelectedDate.setText(CommonTask
				.convertDateToStringWithCustomFormat(cal.getTimeInMillis()));
		llHomeCustomer.setVisibility(View.VISIBLE);
		lvZoneList.setVisibility(View.GONE);
		tvHomeRegion.setText("Operational Zones");
		if (!CommonTask.isOnline(this)) {
			CommonTask
					.showMessage(this,
							"Network connection error.\nPlease check your internet connection.");
			return;
		}
		previousMarker = null;
		initializeValue();
		initializeMap();
		selectedDate = 0;
		LoadInformation();
	}

	private void initalization() {
		myCalender = Calendar.getInstance();
		rlHomeCompare = (RelativeLayout) findViewById(R.id.rlHomeCompare);
		rlHomeRegion = (RelativeLayout) findViewById(R.id.rlHomeRegion);
		rlTCHTraffic = (RelativeLayout) findViewById(R.id.rlTCHTraffic);
		rlCSSR = (RelativeLayout) findViewById(R.id.rlCSSR);
		rlDCR = (RelativeLayout) findViewById(R.id.rlDCR);
		rlHOSR = (RelativeLayout) findViewById(R.id.rlHOSR);
		rlTCHCongestion = (RelativeLayout) findViewById(R.id.rlTCHCongestion);
		rlDLTBFSR = (RelativeLayout) findViewById(R.id.rlDLTBFSR);
		rlTCHAVG = (RelativeLayout) findViewById(R.id.rlTCHAVG);
		rlNWAVG = (RelativeLayout) findViewById(R.id.rlNWAVG);

		rlTCHTraffic.setOnClickListener(this);
		rlCSSR.setOnClickListener(this);
		rlDCR.setOnClickListener(this);
		rlHOSR.setOnClickListener(this);
		rlTCHCongestion.setOnClickListener(this);
		rlDLTBFSR.setOnClickListener(this);
		rlTCHAVG.setOnClickListener(this);
		rlNWAVG.setOnClickListener(this);
		rlHomeCompare.setOnClickListener(this);
		rlHomeRegion.setOnClickListener(this);

		gvCSSR = (GaugeView) findViewById(R.id.gvCSSR);
		gvDCR = (GaugeView) findViewById(R.id.gvDCR);
		gvHOSR = (GaugeView) findViewById(R.id.gvHOSR);
		gvTCHCongestion = (GaugeView) findViewById(R.id.gvTCHCongestion);
		gvDLTBFSR = (GaugeView) findViewById(R.id.gvDLTBFSR);
		gvTCHAVG = (GaugeView) findViewById(R.id.gvTCHAVG);
		gvNWAVG = (GaugeView) findViewById(R.id.gvNWAVG);

		tvTCHTrafficValue = (TextView) findViewById(R.id.tvTCHTrafficValue);
		tvCSSRValue = (TextView) findViewById(R.id.tvCSSRValue);
		tvDCRValue = (TextView) findViewById(R.id.tvDCRValue);
		tvHOSRValue = (TextView) findViewById(R.id.tvHOSRValue);
		tvTCHCongestionValue = (TextView) findViewById(R.id.tvTCHCongestionValue);
		tvDLTBFSRValue = (TextView) findViewById(R.id.tvDLTBFSRValue);
		tvTCHAVGValue = (TextView) findViewById(R.id.tvTCHAVGValue);
		tvNWAVGValue = (TextView) findViewById(R.id.tvNWAVGValue);
		tvTitleHome = (TextView) findViewById(R.id.tvTitleHome);
		tvHomeRegion = (TextView) findViewById(R.id.tvHomeRegion);
		tvTitleSelectedDate = (TextView) findViewById(R.id.tvTitleSelectedDate);
		ivTitleSelectedDate = (ImageView) findViewById(R.id.ivTitleSelectedDate);

		ivTitleSelectedDate.setOnClickListener(this);

		llHomeCustomer = (LinearLayout) findViewById(R.id.llHomeCustomer);
		lvZoneList = (ListView) findViewById(R.id.lvZoneList);

		lvZoneList.setOnItemClickListener(this);

		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapHomeCustomer)).getMap();

		map.setOnMarkerClickListener(this);
	}

	private void initializeValue() {
		tvTCHTrafficValue.setText("0");
		tvCSSRValue.setText("");
		gvCSSR.setTargetValue(0);
		tvDCRValue.setText("");
		gvDCR.setTargetValue(0);
		tvHOSRValue.setText("");
		gvHOSR.setTargetValue(0);
		tvTCHCongestionValue.setText("");
		gvTCHCongestion.setTargetValue(0);
		tvDLTBFSRValue.setText("");
		gvDLTBFSR.setTargetValue(0);
		tvTCHAVGValue.setText("");
		gvTCHAVG.setTargetValue(0);
		tvNWAVGValue.setText("");
		gvNWAVG.setTargetValue(0);
		rlTCHTraffic.setTag(null);
		rlCSSR.setTag(null);
		rlDCR.setTag(null);
		rlHOSR.setTag(null);
		rlTCHCongestion.setTag(null);
		rlDLTBFSR.setTag(null);
		rlTCHAVG.setTag(null);
		rlNWAVG.setTag(null);

		rlTCHTraffic.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlCSSR.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlDCR.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlHOSR.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlTCHCongestion.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlDLTBFSR.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlTCHAVG.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
		rlNWAVG.setBackgroundColor(this.getResources().getColor(
				R.color.home_kpi_color_gray));
	}

	private ArrayList<CompareListItem> getList(int selectedId) {
		ArrayList<CompareListItem> list = new ArrayList<CompareListItem>();
		CompareListItem item = new CompareListItem(1, "Central 2");
		if (selectedId != 1)
			list.add(item);
		item = new CompareListItem(2, "Central 3");
		if (selectedId != 2)
			list.add(item);
		item = new CompareListItem(3, "South 1");
		if (selectedId != 3)
			list.add(item);
		item = new CompareListItem(4, "South 2");
		if (selectedId != 4)
			list.add(item);
		item = new CompareListItem(5, "South 3");
		if (selectedId != 5)
			list.add(item);
		item = new CompareListItem(6, "South 4");
		if (selectedId != 5)
			list.add(item);

		return list;
	}

	private ArrayList<CompareListItem> getZoneList() {
		ArrayList<CompareListItem> list = new ArrayList<CompareListItem>();
		list.add(new CompareListItem(1, "Z20"));
		list.add(new CompareListItem(2, "Z21"));
		list.add(new CompareListItem(3, "Z22"));
		list.add(new CompareListItem(4, "Z23"));
		list.add(new CompareListItem(5, "Z24"));
		list.add(new CompareListItem(6, "Z25"));
		list.add(new CompareListItem(7, "Z26"));
		list.add(new CompareListItem(8, "Z27"));
		list.add(new CompareListItem(9, "Z28"));
		list.add(new CompareListItem(10, "Z29"));
		list.add(new CompareListItem(11, "Z30"));
		list.add(new CompareListItem(12, "Z31"));
		list.add(new CompareListItem(13, "Z32"));
		list.add(new CompareListItem(14, "Z33"));
		list.add(new CompareListItem(15, "Z34"));
		list.add(new CompareListItem(16, "Z35"));
		list.add(new CompareListItem(17, "Z36"));
		list.add(new CompareListItem(18, "Z37"));
		list.add(new CompareListItem(19, "Z38"));
		list.add(new CompareListItem(20, "Z39"));
		return list;
	}

	@Override
	public void onClick(View view) {
		if (!CommonTask.isOnline(this)) {
			Toast.makeText(
					this,
					"Network connection error.Please check your internet connection.",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (view.getId() == R.id.rlHomeCompare) {
			if (selectedid == 0) {
				Toast.makeText(this, "Please select a region or zone.",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (kPICustoms == null || kPICustoms.KPICustomList.size() == 0) {
				Toast.makeText(this, "No KPI found for this region or zone.",
						Toast.LENGTH_LONG).show();
				return;
			}

			if (isRegionData) {
				KPI_CompareListActivity.ComapareList = getList(selectedid);
			} else {
				KPI_CompareListActivity.ComapareList = ((ZoneAdapter) lvZoneList
						.getAdapter()).getItemListById(selectedid);
			}
			KPI_CompareListActivity.isRegionData = isRegionData;
			KPI_CompareListActivity.selectedid = selectedid;
			KPI_CompareListActivity.selectedHeader = selectedName;
			KPI_CompareListActivity.kPICustoms = kPICustoms;

			Intent intent = new Intent(this, KPI_CompareListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlHomeRegion) {
			initializeValue();
			selectedid = 0;
			tvTitleHome.setText("Network");
			isRegionData = !isRegionData;
			if (isRegionData) {
				llHomeCustomer.setVisibility(View.VISIBLE);
				lvZoneList.setVisibility(View.GONE);
				tvHomeRegion.setText("Operational Zones");
				isLoadingZoneData = false;
				if (previousMarker != null) {
					Bitmap defaultBitmap = BitmapFactory.decodeResource(
							getResources(), R.drawable.mobilink);
					previousMarker.setIcon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap)));
					previousMarker = null;
				}
				selectedDate = 0;
				LoadInformation();
			} else {
				llHomeCustomer.setVisibility(View.GONE);
				lvZoneList.setVisibility(View.VISIBLE);
				tvHomeRegion.setText("Region");
				lvZoneList.setAdapter(new ZoneAdapter(this,
						R.layout.city_item_layout, getZoneList()));
				isLoadingZoneData = false;
				selectedDate = 0;
				LoadInformation();
			}
		} else if (view.getId() == R.id.rlTCHTraffic) {
			KPI_ReportActivity.selectedSubheader = "TCH Traffic";
			setSelection(1);
			if (selectedid > 0) {
				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",1";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",1";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=1";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlCSSR) {
			KPI_ReportActivity.selectedSubheader = "Call Setup Success Rate %";
			setSelection(2);
			if (selectedid > 0) {
				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",2";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",2";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=2";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlDCR) {
			KPI_ReportActivity.selectedSubheader = "Drop Call Rate";

			setSelection(3);
			if (selectedid > 0) {

				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",3";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",3";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=3";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlHOSR) {
			KPI_ReportActivity.selectedSubheader = "Hand Over Success Rate %";
			setSelection(4);
			if (selectedid > 0) {

				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",4";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",4";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=4";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlTCHCongestion) {
			KPI_ReportActivity.selectedSubheader = "TCH Congestion";
			setSelection(5);
			if (selectedid > 0) {

				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",5";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",5";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=5";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlDLTBFSR) {
			KPI_ReportActivity.selectedSubheader = "Downlink Temporary Block Flow Success Rate";
			setSelection(6);
			if (selectedid > 0) {
				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",6";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",6";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=6";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlTCHAVG) {
			KPI_ReportActivity.selectedSubheader = "TCH Availability";
			setSelection(7);
			if (selectedid > 0) {

				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",7";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",7";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=7";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.rlNWAVG) {
			KPI_ReportActivity.selectedSubheader = "Network Availability";
			setSelection(8);
			if (selectedid > 0) {
				KPI_ReportActivity.selectedHeader = selectedName;
				if (isRegionData)
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/RegionWiseNetworkKPI.aspx?reqd="
							+ selectedid + ",8";
				else
					KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
							+ "MLink/ZoneWiseKPI.aspx?reqd="
							+ selectedid
							+ ",8";
			} else {
				KPI_ReportActivity.selectedHeader = "Network";
				KPI_ReportActivity.selectedURL = CommonURL.getInstance().applicationReportUrl
						+ "MLink/NetworkNetworkKPIWise.aspx?reqd=8";
			}
			Intent intent = new Intent(this, KPI_ReportActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (view.getId() == R.id.ivTitleSelectedDate) {
			pickTime();
		}

	}

	private void LoadInformation() {
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	private void initializeMap() {
		try {

			map.clear();

			double defaultLatitude = 0, defaultLongitude = 0;

			defaultLatitude = 31.581432;
			defaultLongitude = 71.225615;
			LatLng Location = new LatLng(31.581432, 71.225615);
			Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(),
					R.drawable.mobilink);

			map.addMarker(new MarkerOptions()
					.position(Location)
					.title("Central 2")					
					.icon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap))));

			Location = new LatLng(29.335095, 70.174595);

			// defaultBitmap =
			// BitmapFactory.decodeResource(getResources(),R.drawable.japan);

			map.addMarker(new MarkerOptions()
					.position(Location)
					.title("Central 3")
					.icon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap))));

			Location = new LatLng(25.135068, 67.213060);
			// defaultBitmap =
			// BitmapFactory.decodeResource(getResources(),R.drawable.pakistan);

			map.addMarker(new MarkerOptions()
					.position(Location)
					.title("South 1")
					.icon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap))));

			Location = new LatLng(28.072635, 68.584657);
			// defaultBitmap =
			// BitmapFactory.decodeResource(getResources(),R.drawable.japan);

			map.addMarker(new MarkerOptions()
					.position(Location)
					.title("South 2")
					.icon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap))));

			Location = new LatLng(29.350402, 67.070281);
			// defaultBitmap =
			// BitmapFactory.decodeResource(getResources(),R.drawable.wataniya);

			map.addMarker(new MarkerOptions()
					.position(Location)
					.title("South 3")					
					.icon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap))));

			Location = new LatLng(25.234844, 68.493109);
			// defaultBitmap =
			// BitmapFactory.decodeResource(getResources(),R.drawable.wataniya);

			map.addMarker(new MarkerOptions()
					.position(Location)
					.title("South 4")
					.icon(BitmapDescriptorFactory
							.fromBitmap(scaleImage(defaultBitmap))));

			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			LatLng Defaultlocation = new LatLng(defaultLatitude,
					defaultLongitude);

			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Defaultlocation,
					14.0f));

			map.animateCamera(CameraUpdateFactory.zoomIn());

			map.animateCamera(CameraUpdateFactory.zoomTo(10));

			CameraPosition cameraPosition = new CameraPosition.Builder()
					.target(Defaultlocation).zoom(4).bearing(0).tilt(30)
					.build();

			map.animateCamera(CameraUpdateFactory
					.newCameraPosition(cameraPosition));

			/*PolygonOptions rectOptions = new PolygonOptions().add(new LatLng(
					37.015, 74.793), new LatLng(36.994, 74.824), new LatLng(
					36.994, 74.825), new LatLng(36.959, 74.853), new LatLng(
					36.947, 74.861), new LatLng(36.917, 74.867), new LatLng(
					36.907, 74.872), new LatLng(36.911, 74.886), new LatLng(
					36.921, 74.892), new LatLng(36.95, 74.901), new LatLng(
					36.963, 74.91), new LatLng(36.971, 74.925), new LatLng(
					36.977, 74.942), new LatLng(36.984, 74.977), new LatLng(
					36.984, 75.014), new LatLng(36.994, 75.1), new LatLng(
					36.991, 75.119), new LatLng(36.985, 75.133), new LatLng(
					36.964, 75.161), new LatLng(36.949, 75.195), new LatLng(
					36.929, 75.313), new LatLng(36.916, 75.351), new LatLng(
					36.894, 75.378), new LatLng(36.864, 75.394), new LatLng(
					36.826, 75.401), new LatLng(36.785, 75.403), new LatLng(
					36.747, 75.411), new LatLng(36.723, 75.432), new LatLng(
					36.721, 75.493), new LatLng(36.728, 75.499), new LatLng(
					36.738, 75.502), new LatLng(36.748, 75.508), new LatLng(
					36.767, 75.571), new LatLng(36.754, 75.65), new LatLng(
					36.723, 75.729), new LatLng(36.688, 75.791), new LatLng(
					36.645, 75.85), new LatLng(36.619, 75.875), new LatLng(
					36.463, 75.977), new LatLng(36.421, 75.987), new LatLng(
					36.342, 75.969), new LatLng(36.302, 75.969), new LatLng(
					36.287, 75.977), new LatLng(36.261, 76.002), new LatLng(
					36.248, 76.012), new LatLng(36.23, 76.014), new LatLng(
					36.222, 76.002), new LatLng(36.215, 75.983), new LatLng(
					36.202, 75.97), new LatLng(36.192, 75.969), new LatLng(
					36.174, 75.977), new LatLng(36.163, 75.977), new LatLng(
					36.155, 75.972), new LatLng(36.15, 75.964), new LatLng(
					36.124, 75.915), new LatLng(36.114, 75.901), new LatLng(
					36.097, 75.894), new LatLng(36.063, 75.9), new LatLng(
					36.032, 75.922), new LatLng(36.007, 75.953), new LatLng(
					35.996, 75.984), new LatLng(35.992, 76.061), new LatLng(
					35.978, 76.09), new LatLng(35.942, 76.115), new LatLng(
					35.903, 76.124), new LatLng(35.862, 76.128), new LatLng(
					35.827, 76.138), new LatLng(35.806, 76.166), new LatLng(
					35.805, 76.19), new LatLng(35.815, 76.237), new LatLng(
					35.818, 76.26), new LatLng(35.814, 76.281), new LatLng(
					35.809, 76.297), new LatLng(35.81, 76.312), new LatLng(
					35.831, 76.349), new LatLng(35.831, 76.366), new LatLng(
					35.828, 76.384), new LatLng(35.831, 76.403), new LatLng(
					35.837, 76.41), new LatLng(35.854, 76.422), new LatLng(
					35.86, 76.432), new LatLng(35.863, 76.443), new LatLng(
					35.865, 76.454), new LatLng(35.865, 76.476), new LatLng(
					35.872, 76.5), new LatLng(35.903, 76.537), new LatLng(
					35.898, 76.555), new LatLng(35.865, 76.565), new LatLng(
					35.784, 76.551), new LatLng(35.751, 76.571), new LatLng(
					35.736, 76.604), new LatLng(35.714, 76.677), new LatLng(
					35.694, 76.709), new LatLng(35.667, 76.733), new LatLng(
					35.655, 76.745), new LatLng(35.647, 76.763), new LatLng(
					35.646, 76.777), new LatLng(35.11, 77.049), new LatLng(
					35.107, 77.043), new LatLng(35.086, 77.023), new LatLng(
					35.062, 77.027), new LatLng(35.035, 77.035), new LatLng(
					35.003, 77.029), new LatLng(34.986, 77.013), new LatLng(
					34.947, 76.953), new LatLng(34.929, 76.934), new LatLng(
					34.921, 76.921), new LatLng(34.923, 76.906), new LatLng(
					34.937, 76.88), new LatLng(34.943, 76.853), new LatLng(
					34.941, 76.817), new LatLng(34.931, 76.782), new LatLng(
					34.915, 76.758), new LatLng(34.892, 76.75), new LatLng(
					34.838, 76.753), new LatLng(34.819, 76.744), new LatLng(
					34.747, 76.653), new LatLng(34.741, 76.64), new LatLng(
					34.726, 76.553), new LatLng(34.726, 76.536), new LatLng(
					34.731, 76.52), new LatLng(34.758, 76.476), new LatLng(
					34.763, 76.438), new LatLng(34.751, 76.4), new LatLng(
					34.685, 76.262), new LatLng(34.662, 76.155), new LatLng(
					34.661, 76.122), new LatLng(34.683, 76.058), new LatLng(
					34.677, 76.023), new LatLng(34.665, 76.007), new LatLng(
					34.636, 75.987), new LatLng(34.622, 75.973), new LatLng(
					34.619, 75.966), new LatLng(34.612, 75.939), new LatLng(
					34.571, 75.874), new LatLng(34.522, 75.816), new LatLng(
					34.508, 75.796), new LatLng(34.504, 75.777), new LatLng(
					34.509, 75.735), new LatLng(34.509, 75.714), new LatLng(
					34.497, 75.656), new LatLng(34.498, 75.612), new LatLng(
					34.557, 75.348), new LatLng(34.574, 75.307), new LatLng(
					34.598, 75.267), new LatLng(34.613, 75.251), new LatLng(
					34.632, 75.237), new LatLng(34.645, 75.213), new LatLng(
					34.645, 75.176), new LatLng(34.634, 75.111), new LatLng(
					34.63, 75.018), new LatLng(34.688, 74.665), new LatLng(
					34.764, 74.412), new LatLng(34.773, 74.349), new LatLng(
					34.769, 74.286), new LatLng(34.748, 74.222), new LatLng(
					34.691, 74.121), new LatLng(34.668, 73.962), new LatLng(
					34.663, 73.949), new LatLng(34.643, 73.919), new LatLng(
					34.619, 73.917), new LatLng(34.593, 73.925), new LatLng(
					34.565, 73.926), new LatLng(34.545, 73.911), new LatLng(
					34.517, 73.863), new LatLng(34.494, 73.847), new LatLng(
					34.494, 73.847), new LatLng(34.456, 73.846), new LatLng(
					34.444, 73.84), new LatLng(34.436, 73.832), new LatLng(
					34.425, 73.807), new LatLng(34.419, 73.796), new LatLng(
					34.396, 73.78), new LatLng(34.371, 73.775), new LatLng(
					34.347, 73.783), new LatLng(34.327, 73.804), new LatLng(
					34.315, 73.829), new LatLng(34.307, 73.853), new LatLng(
					34.304, 73.879), new LatLng(34.307, 73.907), new LatLng(
					34.304, 73.938), new LatLng(34.287, 73.954), new LatLng(
					34.238, 73.977), new LatLng(34.219, 73.991), new LatLng(
					34.197, 73.998), new LatLng(34.176, 73.995), new LatLng(
					34.162, 73.977), new LatLng(34.135, 73.917), new LatLng(
					34.115, 73.893), new LatLng(34.084, 73.884), new LatLng(
					34.054, 73.894), new LatLng(34.028, 73.916), new LatLng(
					34.005, 73.977), new LatLng(34.019, 74.045), new LatLng(
					34.003, 74.118), new LatLng(34.004, 74.154), new LatLng(
					34.012, 74.192), new LatLng(34.013, 74.228), new LatLng(
					33.994, 74.26), new LatLng(33.962, 74.272), new LatLng(
					33.93, 74.262), new LatLng(33.901, 74.24), new LatLng(
					33.878, 74.213), new LatLng(33.858, 74.178), new LatLng(
					33.844, 74.142), new LatLng(33.829, 74.035), new LatLng(
					33.759, 73.977), new LatLng(33.747, 73.971), new LatLng(
					33.722, 73.963), new LatLng(33.692, 73.961), new LatLng(
					33.677, 73.963), new LatLng(33.662, 73.968), new LatLng(
					33.648, 73.977), new LatLng(33.614, 74.024), new LatLng(
					33.585, 74.088), new LatLng(33.549, 74.142), new LatLng(
					33.494, 74.158), new LatLng(33.418, 74.136), new LatLng(
					33.345, 74.105), new LatLng(33.328, 74.096), new LatLng(
					33.314, 74.085), new LatLng(33.302, 74.072), new LatLng(
					33.292, 74.055), new LatLng(33.279, 74.018), new LatLng(
					33.27, 74.002), new LatLng(33.253, 73.992), new LatLng(
					33.209, 73.988), new LatLng(33.178, 74.002), new LatLng(
					33.154, 74.029), new LatLng(33.133, 74.066), new LatLng(
					33.105, 74.098), new LatLng(33.04, 74.154), new LatLng(
					33.022, 74.191), new LatLng(33.009, 74.284), new LatLng(
					32.994, 74.311), new LatLng(32.972, 74.322), new LatLng(
					32.921, 74.324), new LatLng(32.87, 74.337), new LatLng(
					32.849, 74.334), new LatLng(32.831, 74.323), new LatLng(
					32.81, 74.307), new LatLng(32.791, 74.316), new LatLng(
					32.776, 74.329), new LatLng(32.767, 74.346), new LatLng(
					32.764, 74.368), new LatLng(32.768, 74.386), new LatLng(
					32.785, 74.419), new LatLng(32.786, 74.439), new LatLng(
					32.779, 74.455), new LatLng(32.754, 74.484), new LatLng(
					32.746, 74.501), new LatLng(32.745, 74.521), new LatLng(
					32.753, 74.574), new LatLng(32.758, 74.593), new LatLng(
					32.77, 74.614), new LatLng(32.785, 74.622), new LatLng(
					32.803, 74.626), new LatLng(32.822, 74.636), new LatLng(
					32.837, 74.654), new LatLng(32.841, 74.672), new LatLng(
					32.831, 74.685), new LatLng(32.809, 74.689), new LatLng(
					32.787, 74.682), new LatLng(32.745, 74.657), new LatLng(
					32.723, 74.65), new LatLng(32.701, 74.65), new LatLng(
					32.661, 74.657), new LatLng(32.641, 74.656), new LatLng(
					32.624, 74.648), new LatLng(32.606, 74.637), new LatLng(
					32.588, 74.63), new LatLng(32.568, 74.633), new LatLng(
					32.498, 74.663), new LatLng(32.471, 74.689), new LatLng(
					32.461, 74.725), new LatLng(32.463, 74.764), new LatLng(
					32.473, 74.799), new LatLng(32.475, 74.838), new LatLng(
					32.445, 74.907), new LatLng(32.449, 74.948), new LatLng(
					32.463, 74.991), new LatLng(32.466, 75.024), new LatLng(
					32.456, 75.055), new LatLng(32.429, 75.092), new LatLng(
					32.412, 75.126), new LatLng(32.397, 75.196), new LatLng(
					32.38, 75.23), new LatLng(32.312, 75.327), new LatLng(
					32.284, 75.353), new LatLng(32.262, 75.359), new LatLng(
					32.242, 75.349), new LatLng(32.211, 75.317), new LatLng(
					32.194, 75.308), new LatLng(32.16, 75.298), new LatLng(
					32.149, 75.295), new LatLng(32.151, 75.263), new LatLng(
					32.149, 75.25), new LatLng(32.142, 75.226), new LatLng(
					32.129, 75.196), new LatLng(32.11, 75.173), new LatLng(
					32.087, 75.174), new LatLng(32.088, 75.161), new LatLng(
					32.093, 75.138), new LatLng(32.09, 75.13), new LatLng(
					32.081, 75.119), new LatLng(32.078, 75.11), new LatLng(
					32.078, 75.102), new LatLng(32.098, 75.041), new LatLng(
					32.095, 75.023), new LatLng(32.066, 75.03), new LatLng(
					32.066, 75.02), new LatLng(32.067, 74.996), new LatLng(
					32.063, 74.986), new LatLng(32.053, 74.979), new LatLng(
					32.045, 74.974), new LatLng(32.038, 74.968), new LatLng(
					32.032, 74.956), new LatLng(32.03, 74.931), new LatLng(
					32.032, 74.909), new LatLng(32.029, 74.889), new LatLng(
					32.012, 74.873), new LatLng(32.025, 74.829), new LatLng(
					32.004, 74.811), new LatLng(31.969, 74.802), new LatLng(
					31.943, 74.784), new LatLng(31.939, 74.763), new LatLng(
					31.95, 74.698), new LatLng(31.904, 74.67), new LatLng(
					31.896, 74.657), new LatLng(31.891, 74.641), new LatLng(
					31.841, 74.566), new LatLng(31.827, 74.55), new LatLng(
					31.811, 74.541), new LatLng(31.789, 74.537), new LatLng(
					31.776, 74.537), new LatLng(31.771, 74.535), new LatLng(
					31.768, 74.531), new LatLng(31.758, 74.523), new LatLng(
					31.754, 74.521), new LatLng(31.742, 74.518), new LatLng(
					31.737, 74.516), new LatLng(31.735, 74.513), new LatLng(
					31.732, 74.505), new LatLng(31.731, 74.503), new LatLng(
					31.715, 74.493), new LatLng(31.711, 74.489), new LatLng(
					31.7, 74.499), new LatLng(31.612, 74.556), new LatLng(
					31.518, 74.585), new LatLng(31.478, 74.615), new LatLng(
					31.459, 74.617), new LatLng(31.44, 74.612), new LatLng(
					31.424, 74.6), new LatLng(31.407, 74.584), new LatLng(
					31.389, 74.572), new LatLng(31.303, 74.532), new LatLng(
					31.28, 74.525), new LatLng(31.235, 74.525), new LatLng(
					31.196, 74.509), new LatLng(31.175, 74.506), new LatLng(
					31.156, 74.51), new LatLng(31.142, 74.515), new LatLng(
					31.129, 74.524), new LatLng(31.115, 74.537), new LatLng(
					31.109, 74.553), new LatLng(31.116, 74.597), new LatLng(
					31.115, 74.617), new LatLng(31.108, 74.632), new LatLng(
					31.093, 74.65), new LatLng(31.084, 74.659), new LatLng(
					31.057, 74.601), new LatLng(31.043, 74.579), new LatLng(
					31.025, 74.564), new LatLng(31.004, 74.564), new LatLng(
					30.992, 74.549), new LatLng(30.976, 74.535), new LatLng(
					30.962, 74.519), new LatLng(30.957, 74.499), new LatLng(
					30.956, 74.477), new LatLng(30.953, 74.459), new LatLng(
					30.946, 74.442), new LatLng(30.932, 74.424), new LatLng(
					30.915, 74.413), new LatLng(30.901, 74.409), new LatLng(
					30.893, 74.401), new LatLng(30.894, 74.38), new LatLng(
					30.901, 74.354), new LatLng(30.902, 74.342), new LatLng(
					30.9, 74.33), new LatLng(30.893, 74.319), new LatLng(
					30.884, 74.311), new LatLng(30.874, 74.304), new LatLng(
					30.856, 74.311), new LatLng(30.838, 74.3), new LatLng(
					30.825, 74.28), new LatLng(30.819, 74.26), new LatLng(
					30.815, 74.257), new LatLng(30.795, 74.266), new LatLng(
					30.788, 74.267), new LatLng(30.781, 74.259), new LatLng(
					30.771, 74.244), new LatLng(30.764, 74.236), new LatLng(
					30.737, 74.215), new LatLng(30.725, 74.199), new LatLng(
					30.717, 74.191), new LatLng(30.697, 74.185), new LatLng(
					30.687, 74.17), new LatLng(30.679, 74.167), new LatLng(
					30.665, 74.164), new LatLng(30.66, 74.156), new LatLng(
					30.655, 74.133), new LatLng(30.646, 74.111), new LatLng(
					30.637, 74.096), new LatLng(30.626, 74.082), new LatLng(
					30.611, 74.067), new LatLng(30.592, 74.057), new LatLng(
					30.574, 74.058), new LatLng(30.556, 74.061), new LatLng(
					30.531, 74.058), new LatLng(30.519, 74.044), new LatLng(
					30.509, 74.004), new LatLng(30.501, 73.996), new LatLng(
					30.488, 73.99), new LatLng(30.426, 73.939), new LatLng(
					30.419, 73.929), new LatLng(30.417, 73.917), new LatLng(
					30.422, 73.9), new LatLng(30.416, 73.902), new LatLng(
					30.401, 73.904), new LatLng(30.395, 73.906), new LatLng(
					30.397, 73.888), new LatLng(30.387, 73.868), new LatLng(
					30.373, 73.852), new LatLng(30.357, 73.845), new LatLng(
					30.353, 73.842), new LatLng(30.33, 73.892), new LatLng(
					30.304, 73.912), new LatLng(30.261, 73.935), new LatLng(
					30.217, 73.949), new LatLng(30.188, 73.944), new LatLng(
					30.067, 73.778), new LatLng(30.048, 73.74), new LatLng(
					30.013, 73.558), new LatLng(29.942, 73.385), new LatLng(
					29.927, 73.37), new LatLng(29.805, 73.327), new LatLng(
					29.684, 73.285), new LatLng(29.537, 73.233), new LatLng(
					29.443, 73.178), new LatLng(29.36, 73.129), new LatLng(
					29.228, 73.051), new LatLng(29.155, 72.989), new LatLng(
					29.117, 72.963), new LatLng(29.048, 72.93), new LatLng(
					29.033, 72.918), new LatLng(29.023, 72.902), new LatLng(
					28.963, 72.773), new LatLng(28.911, 72.659), new LatLng(
					28.85, 72.526), new LatLng(28.784, 72.382), new LatLng(
					28.767, 72.355), new LatLng(28.687, 72.28), new LatLng(
					28.646, 72.256), new LatLng(28.445, 72.198), new LatLng(
					28.397, 72.178), new LatLng(28.354, 72.15), new LatLng(
					28.318, 72.111), new LatLng(28.228, 71.988), new LatLng(
					28.136, 71.908), new LatLng(28.116, 71.897), new LatLng(
					28.097, 71.892), new LatLng(27.975, 71.88), new LatLng(
					27.96, 71.874), new LatLng(27.95, 71.861), new LatLng(
					27.907, 71.701), new LatLng(27.869, 71.561), new LatLng(
					27.862, 71.477), new LatLng(27.868, 71.398), new LatLng(
					27.862, 71.311), new LatLng(27.845, 71.226), new LatLng(
					27.822, 71.151), new LatLng(27.768, 71.028), new LatLng(
					27.718, 70.914), new LatLng(27.701, 70.832), new LatLng(
					27.71, 70.762), new LatLng(27.741, 70.71), new LatLng(
					27.791, 70.672), new LatLng(27.855, 70.641), new LatLng(
					27.874, 70.638), new LatLng(27.911, 70.642), new LatLng(
					27.932, 70.633), new LatLng(27.944, 70.621), new LatLng(
					27.964, 70.593), new LatLng(27.998, 70.56), new LatLng(
					28.016, 70.535), new LatLng(28.029, 70.507), new LatLng(
					28.037, 70.477), new LatLng(28.04, 70.456), new LatLng(
					28.035, 70.437), new LatLng(28.022, 70.398), new LatLng(
					28.016, 70.36), new LatLng(28.011, 70.342), new LatLng(28,
					70.324), new LatLng(27.945, 70.268), new LatLng(27.901,
					70.199), new LatLng(27.812, 70.102), new LatLng(27.794,
					70.091), new LatLng(27.601, 70.017), new LatLng(27.571,
					69.994), new LatLng(27.497, 69.908), new LatLng(27.41,
					69.848), new LatLng(27.31, 69.731), new LatLng(27.27,
					69.666), new LatLng(27.188, 69.576), new LatLng(27.126,
					69.534), new LatLng(27.05, 69.508), new LatLng(26.927,
					69.486), new LatLng(26.808, 69.465), new LatLng(26.767,
					69.473), new LatLng(26.735, 69.504), new LatLng(26.678,
					69.659), new LatLng(26.653, 69.7), new LatLng(26.595,
					69.772), new LatLng(26.58, 69.816), new LatLng(26.589,
					70.056), new LatLng(26.58, 70.094), new LatLng(26.563,
					70.13), new LatLng(26.53, 70.158), new LatLng(26.493,
					70.163), new LatLng(26.411, 70.157), new LatLng(26.371,
					70.161), new LatLng(26.354, 70.157), new LatLng(26.333,
					70.148), new LatLng(26.314, 70.142), new LatLng(26.294,
					70.144), new LatLng(26.254, 70.152), new LatLng(26.217,
					70.147), new LatLng(26.18, 70.132),
					new LatLng(26.1, 70.078), new LatLng(26.083, 70.074),
					new LatLng(26.047, 70.073), new LatLng(25.996, 70.064),
					new LatLng(25.98, 70.065), new LatLng(25.93, 70.083),
					new LatLng(25.882, 70.115), new LatLng(25.839, 70.154),
					new LatLng(25.807, 70.196), new LatLng(25.786, 70.214),
					new LatLng(25.731, 70.234), new LatLng(25.708, 70.249),
					new LatLng(25.697, 70.265), new LatLng(25.685, 70.304),
					new LatLng(25.673, 70.36), new LatLng(25.676, 70.477),
					new LatLng(25.684, 70.517), new LatLng(25.699, 70.554),
					new LatLng(25.709, 70.592), new LatLng(25.701, 70.632),
					new LatLng(25.674, 70.654), new LatLng(25.634, 70.657),
					new LatLng(25.546, 70.653), new LatLng(25.431, 70.647),
					new LatLng(25.397, 70.654), new LatLng(25.376, 70.67),
					new LatLng(25.336, 70.71), new LatLng(25.311, 70.719),
					new LatLng(25.287, 70.723), new LatLng(25.267, 70.735),
					new LatLng(25.233, 70.768), new LatLng(25.183, 70.831),
					new LatLng(25.163, 70.849), new LatLng(25.139, 70.86),
					new LatLng(25.002, 70.893), new LatLng(24.947, 70.915),
					new LatLng(24.894, 70.943), new LatLng(24.808, 71.003),
					new LatLng(24.721, 71.037), new LatLng(24.683, 71.064),
					new LatLng(24.669, 71.043), new LatLng(24.64, 70.977),
					new LatLng(24.616, 70.963), new LatLng(24.585, 70.955),
					new LatLng(24.556, 70.958), new LatLng(24.54, 70.977),
					new LatLng(24.535, 70.981), new LatLng(24.529, 70.982),
					new LatLng(24.522, 70.981), new LatLng(24.515, 70.977),
					new LatLng(24.495, 70.975), new LatLng(24.487, 70.973),
					new LatLng(24.48, 70.973), new LatLng(24.472, 70.974),
					new LatLng(24.465, 70.977), new LatLng(24.453, 71),
					new LatLng(24.447, 71.04), new LatLng(24.436, 71.075),
					new LatLng(24.412, 71.083), new LatLng(24.402, 71.073),
					new LatLng(24.386, 71.025), new LatLng(24.375, 71.014),
					new LatLng(24.364, 71.007), new LatLng(24.357, 70.997),
					new LatLng(24.357, 70.977), new LatLng(24.366, 70.955),
					new LatLng(24.367, 70.936), new LatLng(24.362, 70.918),
					new LatLng(24.324, 70.857), new LatLng(24.306, 70.841),
					new LatLng(24.288, 70.845), new LatLng(24.272, 70.858),
					new LatLng(24.265, 70.851), new LatLng(24.261, 70.834),
					new LatLng(24.254, 70.814), new LatLng(24.237, 70.776),
					new LatLng(24.231, 70.755), new LatLng(24.241, 70.622),
					new LatLng(24.258, 70.585), new LatLng(24.273, 70.568),
					new LatLng(24.287, 70.56), new LatLng(24.327, 70.555),
					new LatLng(24.362, 70.546), new LatLng(24.373, 70.546),
					new LatLng(24.379, 70.552), new LatLng(24.39, 70.569),
					new LatLng(24.4, 70.575), new LatLng(24.424, 70.563),
					new LatLng(24.425, 70.521), new LatLng(24.402, 70.416),
					new LatLng(24.372, 70.371), new LatLng(24.366, 70.353),
					new LatLng(24.363, 70.299), new LatLng(24.355, 70.279),
					new LatLng(24.331, 70.243), new LatLng(24.327, 70.223),
					new LatLng(24.326, 70.202), new LatLng(24.308, 70.145),
					new LatLng(24.305, 70.11), new LatLng(24.299, 70.098),
					new LatLng(24.283, 70.087), new LatLng(24.22, 70.063),
					new LatLng(24.202, 70.052), new LatLng(24.174, 70.016),
					new LatLng(24.165, 69.972), new LatLng(24.163, 69.769),
					new LatLng(24.169, 69.715), new LatLng(24.189, 69.671),
					new LatLng(24.265, 69.592), new LatLng(24.277, 69.563),
					new LatLng(24.284, 69.281), new LatLng(24.259, 69.206),
					new LatLng(24.253, 69.167), new LatLng(24.257, 69.148),
					new LatLng(24.282, 69.092), new LatLng(24.288, 69.068),
					new LatLng(24.285, 69.048), new LatLng(24.265, 69.008),
					new LatLng(24.255, 68.981), new LatLng(24.257, 68.963),
					new LatLng(24.27, 68.949), new LatLng(24.302, 68.929),
					new LatLng(24.311, 68.922), new LatLng(24.317, 68.913),
					new LatLng(24.321, 68.904), new LatLng(24.319, 68.89),
					new LatLng(24.314, 68.885), new LatLng(24.305, 68.883),
					new LatLng(24.297, 68.88), new LatLng(24.244, 68.849),
					new LatLng(24.236, 68.839), new LatLng(24.25, 68.82),
					new LatLng(24.308, 68.814), new LatLng(24.329, 68.799),
					new LatLng(24.331, 68.747), new LatLng(24.289, 68.726),
					new LatLng(24.209, 68.726), new LatLng(24.104, 68.725),
					new LatLng(23.965, 68.724), new LatLng(23.966, 68.646),
					new LatLng(23.966, 68.548), new LatLng(23.967, 68.432),
					new LatLng(23.96, 68.385), new LatLng(23.939, 68.354),
					new LatLng(23.947, 68.354), new LatLng(23.957, 68.352),
					new LatLng(23.965, 68.349), new LatLng(23.969, 68.343),
					new LatLng(23.967, 68.33), new LatLng(23.958, 68.33),
					new LatLng(23.948, 68.334), new LatLng(23.941, 68.335),
					new LatLng(23.928, 68.326), new LatLng(23.916, 68.314),
					new LatLng(23.911, 68.3), new LatLng(23.916, 68.286),
					new LatLng(23.926, 68.28), new LatLng(23.934, 68.278),
					new LatLng(23.938, 68.274), new LatLng(23.936, 68.264),
					new LatLng(23.929, 68.255), new LatLng(23.92, 68.253),
					new LatLng(23.911, 68.253), new LatLng(23.905, 68.252),
					new LatLng(23.903, 68.254), new LatLng(23.899, 68.258),
					new LatLng(23.896, 68.26), new LatLng(23.892, 68.257),
					new LatLng(23.892, 68.253), new LatLng(23.893, 68.243),
					new LatLng(23.893, 68.24), new LatLng(23.889, 68.233),
					new LatLng(23.882, 68.215), new LatLng(23.877, 68.207),
					new LatLng(23.843, 68.184), new LatLng(23.905, 68.158),
					new LatLng(23.874, 68.157), new LatLng(23.864, 68.158),
					new LatLng(23.858, 68.161), new LatLng(23.845, 68.169),
					new LatLng(23.84, 68.171), new LatLng(23.82, 68.167),
					new LatLng(23.781, 68.149), new LatLng(23.761, 68.144),
					new LatLng(23.743, 68.145), new LatLng(23.714, 68.155),
					new LatLng(23.696, 68.158), new LatLng(23.694, 68.149),
					new LatLng(23.708, 68.131), new LatLng(23.734, 68.103),
					new LatLng(23.709, 68.099), new LatLng(23.708, 68.082),
					new LatLng(23.723, 68.063), new LatLng(23.744, 68.054),
					new LatLng(23.751, 68.058), new LatLng(23.76, 68.072),
					new LatLng(23.765, 68.075), new LatLng(23.783, 68.074),
					new LatLng(23.788, 68.075), new LatLng(23.805, 68.082),
					new LatLng(23.809, 68.089), new LatLng(23.809, 68.103),
					new LatLng(23.816, 68.103), new LatLng(23.826, 68.089),
					new LatLng(23.842, 68.073), new LatLng(23.862, 68.06),
					new LatLng(23.884, 68.054), new LatLng(23.911, 68.062),
					new LatLng(23.923, 68.064), new LatLng(23.921, 68.048),
					new LatLng(23.935, 68.025), new LatLng(23.939, 68.014),
					new LatLng(23.852, 68.042), new LatLng(23.829, 68.054),
					new LatLng(23.792, 68.036), new LatLng(23.774, 68.025),
					new LatLng(23.768, 68.014), new LatLng(23.774, 68.008),
					new LatLng(23.785, 68.004), new LatLng(23.797, 68.001),
					new LatLng(23.806, 68), new LatLng(23.811, 67.995),
					new LatLng(23.809, 67.984), new LatLng(23.805, 67.974),
					new LatLng(23.802, 67.972), new LatLng(23.804, 67.962),
					new LatLng(23.809, 67.951), new LatLng(23.816, 67.942),
					new LatLng(23.826, 67.938), new LatLng(23.833, 67.935),
					new LatLng(23.834, 67.926), new LatLng(23.833, 67.917),
					new LatLng(23.836, 67.91), new LatLng(23.846, 67.905),
					new LatLng(23.859, 67.906), new LatLng(23.867, 67.9),
					new LatLng(23.869, 67.892), new LatLng(23.865, 67.884),
					new LatLng(23.85, 67.869), new LatLng(23.866, 67.865),
					new LatLng(23.903, 67.864), new LatLng(23.912, 67.856),
					new LatLng(23.889, 67.85), new LatLng(23.823, 67.839),
					new LatLng(23.809, 67.832), new LatLng(23.813, 67.803),
					new LatLng(23.821, 67.774), new LatLng(23.843, 67.719),
					new LatLng(23.835, 67.731), new LatLng(23.823, 67.742),
					new LatLng(23.809, 67.744), new LatLng(23.795, 67.733),
					new LatLng(23.791, 67.716), new LatLng(23.793, 67.694),
					new LatLng(23.802, 67.657), new LatLng(23.803, 67.647),
					new LatLng(23.802, 67.638), new LatLng(23.803, 67.632),
					new LatLng(23.812, 67.63), new LatLng(23.825, 67.631),
					new LatLng(23.833, 67.635), new LatLng(23.84, 67.639),
					new LatLng(23.85, 67.643), new LatLng(23.907, 67.651),
					new LatLng(23.919, 67.657), new LatLng(23.925, 67.657),
					new LatLng(23.925, 67.652), new LatLng(23.927, 67.651),
					new LatLng(23.93, 67.652), new LatLng(23.933, 67.651),
					new LatLng(23.916, 67.639), new LatLng(23.874, 67.638),
					new LatLng(23.857, 67.63), new LatLng(23.85, 67.615),
					new LatLng(23.854, 67.6), new LatLng(23.866, 67.59),
					new LatLng(23.884, 67.589), new LatLng(23.872, 67.575),
					new LatLng(23.876, 67.566), new LatLng(23.888, 67.559),
					new LatLng(23.898, 67.547), new LatLng(23.899, 67.533),
					new LatLng(23.891, 67.531), new LatLng(23.88, 67.536),
					new LatLng(23.871, 67.547), new LatLng(23.883, 67.492),
					new LatLng(23.891, 67.479), new LatLng(23.893, 67.489),
					new LatLng(23.892, 67.496), new LatLng(23.893, 67.503),
					new LatLng(23.898, 67.513), new LatLng(23.907, 67.521),
					new LatLng(23.929, 67.533), new LatLng(23.939, 67.541),
					new LatLng(23.935, 67.531), new LatLng(23.928, 67.52),
					new LatLng(23.923, 67.509), new LatLng(23.925, 67.499),
					new LatLng(23.935, 67.497), new LatLng(23.951, 67.5),
					new LatLng(23.974, 67.507), new LatLng(23.982, 67.464),
					new LatLng(23.987, 67.452), new LatLng(24.022, 67.469),
					new LatLng(24.032, 67.479), new LatLng(24.028, 67.493),
					new LatLng(24.045, 67.52), new LatLng(24.055, 67.53),
					new LatLng(24.07, 67.534), new LatLng(24.052, 67.51),
					new LatLng(24.054, 67.49), new LatLng(24.064, 67.468),
					new LatLng(24.07, 67.441), new LatLng(24.067, 67.384),
					new LatLng(24.071, 67.354), new LatLng(24.086, 67.342),
					new LatLng(24.119, 67.337), new LatLng(24.144, 67.327),
					new LatLng(24.186, 67.301), new LatLng(24.199, 67.296),
					new LatLng(24.212, 67.294), new LatLng(24.262, 67.293),
					new LatLng(24.27, 67.289), new LatLng(24.281, 67.28),
					new LatLng(24.288, 67.284), new LatLng(24.298, 67.29),
					new LatLng(24.303, 67.293), new LatLng(24.329, 67.287),
					new LatLng(24.343, 67.306), new LatLng(24.346, 67.337),
					new LatLng(24.344, 67.363), new LatLng(24.354, 67.343),
					new LatLng(24.357, 67.326), new LatLng(24.357, 67.283),
					new LatLng(24.369, 67.272), new LatLng(24.395, 67.274),
					new LatLng(24.439, 67.287), new LatLng(24.432, 67.281),
					new LatLng(24.427, 67.274), new LatLng(24.419, 67.259),
					new LatLng(24.445, 67.257), new LatLng(24.468, 67.247),
					new LatLng(24.489, 67.231), new LatLng(24.508, 67.211),
					new LatLng(24.525, 67.231), new LatLng(24.532, 67.227),
					new LatLng(24.535, 67.205), new LatLng(24.547, 67.195),
					new LatLng(24.561, 67.189), new LatLng(24.576, 67.186),
					new LatLng(24.59, 67.184), new LatLng(24.585, 67.2),
					new LatLng(24.582, 67.219), new LatLng(24.584, 67.237),
					new LatLng(24.59, 67.253), new LatLng(24.607, 67.224),
					new LatLng(24.614, 67.209), new LatLng(24.617, 67.191),
					new LatLng(24.616, 67.173), new LatLng(24.612, 67.16),
					new LatLng(24.616, 67.153), new LatLng(24.635, 67.15),
					new LatLng(24.663, 67.157), new LatLng(24.689, 67.176),
					new LatLng(24.71, 67.2), new LatLng(24.727, 67.225),
					new LatLng(24.74, 67.253), new LatLng(24.748, 67.256),
					new LatLng(24.761, 67.245), new LatLng(24.771, 67.233),
					new LatLng(24.769, 67.224), new LatLng(24.764, 67.213),
					new LatLng(24.758, 67.177), new LatLng(24.76, 67.168),
					new LatLng(24.781, 67.162), new LatLng(24.791, 67.156),
					new LatLng(24.8, 67.149), new LatLng(24.803, 67.14),
					new LatLng(24.801, 67.122), new LatLng(24.798, 67.112),
					new LatLng(24.797, 67.1), new LatLng(24.803, 67.082),
					new LatLng(24.796, 67.074), new LatLng(24.774, 67.083),
					new LatLng(24.781, 67.064), new LatLng(24.81, 67.023),
					new LatLng(24.813, 67.006), new LatLng(24.822, 66.988),
					new LatLng(24.833, 66.973), new LatLng(24.843, 66.966),
					new LatLng(24.843, 66.958), new LatLng(24.825, 66.958),
					new LatLng(24.818, 66.961), new LatLng(24.81, 66.966),
					new LatLng(24.812, 66.942), new LatLng(24.824, 66.922),
					new LatLng(24.839, 66.904), new LatLng(24.851, 66.882),
					new LatLng(24.852, 66.854), new LatLng(24.844, 66.828),
					new LatLng(24.839, 66.803), new LatLng(24.851, 66.78),
					new LatLng(24.847, 66.77), new LatLng(24.841, 66.717),
					new LatLng(24.832, 66.692), new LatLng(24.83, 66.68),
					new LatLng(24.839, 66.655), new LatLng(24.859, 66.657),
					new LatLng(24.879, 66.677), new LatLng(24.892, 66.705),
					new LatLng(24.899, 66.705), new LatLng(24.91, 66.682),
					new LatLng(24.949, 66.682), new LatLng(25.025, 66.698),
					new LatLng(25.085, 66.7), new LatLng(25.104, 66.705),
					new LatLng(25.121, 66.714), new LatLng(25.143, 66.733),
					new LatLng(25.159, 66.738), new LatLng(25.181, 66.738),
					new LatLng(25.212, 66.722), new LatLng(25.231, 66.718),
					new LatLng(25.239, 66.711), new LatLng(25.269, 66.675),
					new LatLng(25.275, 66.66), new LatLng(25.283, 66.649),
					new LatLng(25.33, 66.596), new LatLng(25.349, 66.561),
					new LatLng(25.365, 66.548), new LatLng(25.385, 66.554),
					new LatLng(25.379, 66.58), new LatLng(25.399, 66.592),
					new LatLng(25.426, 66.587), new LatLng(25.439, 66.565),
					new LatLng(25.457, 66.551), new LatLng(25.492, 66.542),
					new LatLng(25.518, 66.531), new LatLng(25.508, 66.513),
					new LatLng(25.513, 66.511), new LatLng(25.517, 66.508),
					new LatLng(25.522, 66.506), new LatLng(25.518, 66.493),
					new LatLng(25.525, 66.487), new LatLng(25.538, 66.483),
					new LatLng(25.549, 66.478), new LatLng(25.582, 66.445),
					new LatLng(25.598, 66.437), new LatLng(25.59, 66.413),
					new LatLng(25.592, 66.388), new LatLng(25.604, 66.373),
					new LatLng(25.625, 66.376), new LatLng(25.614, 66.359),
					new LatLng(25.608, 66.339), new LatLng(25.604, 66.304),
					new LatLng(25.606, 66.292), new LatLng(25.611, 66.271),
					new LatLng(25.612, 66.259), new LatLng(25.609, 66.255),
					new LatLng(25.604, 66.249), new LatLng(25.6, 66.24),
					new LatLng(25.593, 66.204), new LatLng(25.581, 66.184),
					new LatLng(25.566, 66.168), new LatLng(25.549, 66.156),
					new LatLng(25.514, 66.138), new LatLng(25.508, 66.132),
					new LatLng(25.504, 66.121), new LatLng(25.495, 66.11),
					new LatLng(25.484, 66.1), new LatLng(25.475, 66.095),
					new LatLng(25.475, 66.122), new LatLng(25.488, 66.201),
					new LatLng(25.491, 66.214), new LatLng(25.499, 66.224),
					new LatLng(25.516, 66.239), new LatLng(25.518, 66.242),
					new LatLng(25.52, 66.246), new LatLng(25.521, 66.25),
					new LatLng(25.522, 66.255), new LatLng(25.525, 66.259),
					new LatLng(25.531, 66.259), new LatLng(25.538, 66.257),
					new LatLng(25.543, 66.259), new LatLng(25.55, 66.28),
					new LatLng(25.554, 66.31), new LatLng(25.553, 66.337),
					new LatLng(25.546, 66.349), new LatLng(25.535, 66.353),
					new LatLng(25.53, 66.364), new LatLng(25.527, 66.377),
					new LatLng(25.522, 66.389), new LatLng(25.513, 66.398),
					new LatLng(25.491, 66.412), new LatLng(25.481, 66.424),
					new LatLng(25.491, 66.438), new LatLng(25.496, 66.457),
					new LatLng(25.493, 66.476), new LatLng(25.481, 66.493),
					new LatLng(25.454, 66.458), new LatLng(25.43, 66.498),
					new LatLng(25.414, 66.512), new LatLng(25.398, 66.499),
					new LatLng(25.4, 66.484), new LatLng(25.411, 66.466),
					new LatLng(25.433, 66.437), new LatLng(25.465, 66.36),
					new LatLng(25.475, 66.312), new LatLng(25.467, 66.279),
					new LatLng(25.473, 66.252), new LatLng(25.465, 66.22),
					new LatLng(25.453, 66.187), new LatLng(25.447, 66.16),
					new LatLng(25.446, 66.069), new LatLng(25.429, 66.005),
					new LatLng(25.418, 65.925), new LatLng(25.419, 65.882),
					new LatLng(25.417, 65.859), new LatLng(25.402, 65.824),
					new LatLng(25.396, 65.801), new LatLng(25.378, 65.766),
					new LatLng(25.371, 65.725), new LatLng(25.347, 65.672),
					new LatLng(25.344, 65.653), new LatLng(25.347, 65.63),
					new LatLng(25.383, 65.488), new LatLng(25.385, 65.468),
					new LatLng(25.381, 65.446), new LatLng(25.372, 65.423),
					new LatLng(25.367, 65.401), new LatLng(25.371, 65.383),
					new LatLng(25.376, 65.387), new LatLng(25.392, 65.396),
					new LatLng(25.39, 65.363), new LatLng(25.385, 65.337),
					new LatLng(25.377, 65.314), new LatLng(25.365, 65.293),
					new LatLng(25.38, 65.26), new LatLng(25.37, 65.233),
					new LatLng(25.348, 65.211), new LatLng(25.311, 65.188),
					new LatLng(25.303, 65.181), new LatLng(25.299, 65.171),
					new LatLng(25.296, 65.156), new LatLng(25.296, 65.144),
					new LatLng(25.302, 65.118), new LatLng(25.305, 65.09),
					new LatLng(25.314, 65.059), new LatLng(25.316, 65.042),
					new LatLng(25.316, 64.981), new LatLng(25.327, 64.921),
					new LatLng(25.328, 64.89), new LatLng(25.316, 64.861),
					new LatLng(25.325, 64.84), new LatLng(25.328, 64.808),
					new LatLng(25.324, 64.745), new LatLng(25.314, 64.719),
					new LatLng(25.297, 64.696), new LatLng(25.277, 64.679),
					new LatLng(25.255, 64.669), new LatLng(25.229, 64.663),
					new LatLng(25.217, 64.664), new LatLng(25.207, 64.669),
					new LatLng(25.201, 64.68), new LatLng(25.197, 64.697),
					new LatLng(25.19, 64.71), new LatLng(25.179, 64.711),
					new LatLng(25.179, 64.703), new LatLng(25.165, 64.655),
					new LatLng(25.166, 64.641), new LatLng(25.173, 64.616),
					new LatLng(25.173, 64.601), new LatLng(25.211, 64.627),
					new LatLng(25.228, 64.628), new LatLng(25.251, 64.611),
					new LatLng(25.261, 64.594), new LatLng(25.27, 64.566),
					new LatLng(25.276, 64.538), new LatLng(25.275, 64.519),
					new LatLng(25.267, 64.507), new LatLng(25.245, 64.493),
					new LatLng(25.241, 64.481), new LatLng(25.24, 64.468),
					new LatLng(25.236, 64.448), new LatLng(25.234, 64.44),
					new LatLng(25.241, 64.418), new LatLng(25.271, 64.383),
					new LatLng(25.283, 64.361), new LatLng(25.292, 64.319),
					new LatLng(25.296, 64.276), new LatLng(25.298, 64.266),
					new LatLng(25.311, 64.253), new LatLng(25.316, 64.245),
					new LatLng(25.318, 64.184), new LatLng(25.336, 64.095),
					new LatLng(25.35, 64.095), new LatLng(25.368, 64.104),
					new LatLng(25.385, 64.107), new LatLng(25.391, 64.102),
					new LatLng(25.396, 64.093), new LatLng(25.403, 64.084),
					new LatLng(25.416, 64.08), new LatLng(25.427, 64.084),
					new LatLng(25.432, 64.094), new LatLng(25.433, 64.107),
					new LatLng(25.433, 64.118), new LatLng(25.429, 64.141),
					new LatLng(25.432, 64.151), new LatLng(25.447, 64.156),
					new LatLng(25.451, 64.151), new LatLng(25.453, 64.141),
					new LatLng(25.454, 64.128), new LatLng(25.444, 64.082),
					new LatLng(25.441, 64.057), new LatLng(25.45, 64.046),
					new LatLng(25.425, 63.99), new LatLng(25.413, 63.977),
					new LatLng(25.416, 63.992), new LatLng(25.419, 63.997),
					new LatLng(25.402, 64.005), new LatLng(25.397, 64.012),
					new LatLng(25.403, 64.022), new LatLng(25.416, 64.036),
					new LatLng(25.416, 64.049), new LatLng(25.399, 64.062),
					new LatLng(25.365, 64.08), new LatLng(25.346, 64.067),
					new LatLng(25.344, 64.06), new LatLng(25.344, 64.042),
					new LatLng(25.339, 64.005), new LatLng(25.338, 63.987),
					new LatLng(25.344, 63.97), new LatLng(25.335, 63.945),
					new LatLng(25.341, 63.907), new LatLng(25.375, 63.805),
					new LatLng(25.385, 63.738), new LatLng(25.383, 63.67),
					new LatLng(25.374, 63.605), new LatLng(25.365, 63.573),
					new LatLng(25.356, 63.555), new LatLng(25.344, 63.538),
					new LatLng(25.316, 63.511), new LatLng(25.291, 63.496),
					new LatLng(25.284, 63.486), new LatLng(25.289, 63.47),
					new LatLng(25.273, 63.474), new LatLng(25.26, 63.482),
					new LatLng(25.226, 63.512), new LatLng(25.223, 63.517),
					new LatLng(25.221, 63.519), new LatLng(25.214, 63.518),
					new LatLng(25.207, 63.516), new LatLng(25.2, 63.512),
					new LatLng(25.195, 63.507), new LatLng(25.193, 63.501),
					new LatLng(25.199, 63.469), new LatLng(25.222, 63.412),
					new LatLng(25.228, 63.377), new LatLng(25.22, 63.309),
					new LatLng(25.223, 63.292), new LatLng(25.241, 63.245),
					new LatLng(25.258, 63.179), new LatLng(25.261, 63.144),
					new LatLng(25.255, 63.114), new LatLng(25.224, 63.051),
					new LatLng(25.22, 63.028), new LatLng(25.228, 62.943),
					new LatLng(25.262, 62.771), new LatLng(25.264, 62.63),
					new LatLng(25.255, 62.501), new LatLng(25.249, 62.478),
					new LatLng(25.237, 62.465), new LatLng(25.223, 62.468),
					new LatLng(25.214, 62.49), new LatLng(25.207, 62.47),
					new LatLng(25.214, 62.445), new LatLng(25.206, 62.414),
					new LatLng(25.192, 62.386), new LatLng(25.179, 62.366),
					new LatLng(25.169, 62.355), new LatLng(25.155, 62.343),
					new LatLng(25.14, 62.337), new LatLng(25.125, 62.34),
					new LatLng(25.119, 62.35), new LatLng(25.114, 62.382),
					new LatLng(25.111, 62.394), new LatLng(25.096, 62.376),
					new LatLng(25.095, 62.34), new LatLng(25.101, 62.301),
					new LatLng(25.111, 62.278), new LatLng(25.116, 62.281),
					new LatLng(25.12, 62.284), new LatLng(25.122, 62.288),
					new LatLng(25.125, 62.291), new LatLng(25.129, 62.3),
					new LatLng(25.128, 62.309), new LatLng(25.13, 62.316),
					new LatLng(25.142, 62.319), new LatLng(25.163, 62.32),
					new LatLng(25.175, 62.319), new LatLng(25.183, 62.315),
					new LatLng(25.199, 62.296), new LatLng(25.213, 62.275),
					new LatLng(25.223, 62.253), new LatLng(25.228, 62.23),
					new LatLng(25.226, 62.192), new LatLng(25.218, 62.146),
					new LatLng(25.205, 62.103), new LatLng(25.186, 62.079),
					new LatLng(25.177, 62.075), new LatLng(25.163, 62.072),
					new LatLng(25.15, 62.074), new LatLng(25.145, 62.083),
					new LatLng(25.115, 62.107), new LatLng(25.107, 62.098),
					new LatLng(25.109, 62.076), new LatLng(25.118, 62.031),
					new LatLng(25.125, 61.946), new LatLng(25.121, 61.924),
					new LatLng(25.103, 61.882), new LatLng(25.097, 61.86),
					new LatLng(25.084, 61.87), new LatLng(25.075, 61.867),
					new LatLng(25.067, 61.858), new LatLng(25.038, 61.848),
					new LatLng(25.038, 61.836), new LatLng(25.05, 61.812),
					new LatLng(25.048, 61.796), new LatLng(25.038, 61.77),
					new LatLng(25.036, 61.753), new LatLng(25.039, 61.743),
					new LatLng(25.048, 61.731), new LatLng(25.059, 61.726),
					new LatLng(25.069, 61.736), new LatLng(25.083, 61.729),
					new LatLng(25.091, 61.733), new LatLng(25.104, 61.75),
					new LatLng(25.115, 61.76), new LatLng(25.122, 61.763),
					new LatLng(25.135, 61.764), new LatLng(25.145, 61.767),
					new LatLng(25.162, 61.781), new LatLng(25.173, 61.784),
					new LatLng(25.188, 61.784), new LatLng(25.191, 61.78),
					new LatLng(25.188, 61.774), new LatLng(25.186, 61.767),
					new LatLng(25.184, 61.764), new LatLng(25.175, 61.756),
					new LatLng(25.173, 61.75), new LatLng(25.175, 61.745),
					new LatLng(25.184, 61.732), new LatLng(25.186, 61.726),
					new LatLng(25.188, 61.714), new LatLng(25.192, 61.708),
					new LatLng(25.197, 61.703), new LatLng(25.2, 61.695),
					new LatLng(25.201, 61.683), new LatLng(25.2, 61.651),
					new LatLng(25.204, 61.635), new LatLng(25.204, 61.625),
					new LatLng(25.2, 61.613), new LatLng(25.202, 61.589),
					new LatLng(25.285, 61.62), new LatLng(25.37, 61.626),
					new LatLng(25.469, 61.633), new LatLng(25.555, 61.639),
					new LatLng(25.613, 61.643), new LatLng(25.628, 61.647),
					new LatLng(25.637, 61.652), new LatLng(25.65, 61.667),
					new LatLng(25.658, 61.673), new LatLng(25.693, 61.676),
					new LatLng(25.732, 61.665), new LatLng(25.769, 61.66),
					new LatLng(25.795, 61.681), new LatLng(25.8, 61.703),
					new LatLng(25.8, 61.722), new LatLng(25.803, 61.739),
					new LatLng(25.82, 61.755), new LatLng(25.838, 61.76),
					new LatLng(25.907, 61.771), new LatLng(25.991, 61.783),
					new LatLng(26.053, 61.793), new LatLng(26.107, 61.801),
					new LatLng(26.167, 61.81), new LatLng(26.198, 61.818),
					new LatLng(26.225, 61.833), new LatLng(26.242, 61.857),
					new LatLng(26.273, 61.978), new LatLng(26.295, 62.014),
					new LatLng(26.303, 62.032), new LatLng(26.309, 62.053),
					new LatLng(26.315, 62.094), new LatLng(26.325, 62.103),
					new LatLng(26.35, 62.107), new LatLng(26.374, 62.124),
					new LatLng(26.37, 62.161), new LatLng(26.355, 62.206),
					new LatLng(26.351, 62.243), new LatLng(26.361, 62.265),
					new LatLng(26.375, 62.264), new LatLng(26.394, 62.254),
					new LatLng(26.417, 62.249), new LatLng(26.429, 62.256),
					new LatLng(26.457, 62.288), new LatLng(26.474, 62.298),
					new LatLng(26.48, 62.297), new LatLng(26.486, 62.293),
					new LatLng(26.492, 62.29), new LatLng(26.501, 62.292),
					new LatLng(26.509, 62.298), new LatLng(26.514, 62.307),
					new LatLng(26.521, 62.327), new LatLng(26.525, 62.335),
					new LatLng(26.533, 62.348), new LatLng(26.537, 62.356),
					new LatLng(26.538, 62.365), new LatLng(26.537, 62.386),
					new LatLng(26.539, 62.397), new LatLng(26.544, 62.402),
					new LatLng(26.557, 62.411), new LatLng(26.562, 62.418),
					new LatLng(26.565, 62.428), new LatLng(26.581, 62.595),
					new LatLng(26.587, 62.616), new LatLng(26.593, 62.624),
					new LatLng(26.6, 62.632), new LatLng(26.605, 62.641),
					new LatLng(26.603, 62.651), new LatLng(26.598, 62.663),
					new LatLng(26.598, 62.671), new LatLng(26.604, 62.702),
					new LatLng(26.607, 62.711), new LatLng(26.611, 62.718),
					new LatLng(26.615, 62.725), new LatLng(26.632, 62.735),
					new LatLng(26.641, 62.743), new LatLng(26.644, 62.754),
					new LatLng(26.643, 62.822), new LatLng(26.641, 62.9),
					new LatLng(26.639, 62.99), new LatLng(26.634, 63.029),
					new LatLng(26.633, 63.069), new LatLng(26.638, 63.098),
					new LatLng(26.638, 63.108), new LatLng(26.634, 63.116),
					new LatLng(26.629, 63.123), new LatLng(26.626, 63.131),
					new LatLng(26.625, 63.141), new LatLng(26.645, 63.164),
					new LatLng(26.685, 63.174), new LatLng(26.83, 63.182),
					new LatLng(26.846, 63.19), new LatLng(26.854, 63.206),
					new LatLng(26.86, 63.247), new LatLng(26.872, 63.262),
					new LatLng(26.89, 63.265), new LatLng(26.906, 63.256),
					new LatLng(26.922, 63.244), new LatLng(26.941, 63.235),
					new LatLng(27.054, 63.23), new LatLng(27.066, 63.231),
					new LatLng(27.076, 63.236), new LatLng(27.089, 63.246),
					new LatLng(27.1, 63.25), new LatLng(27.116, 63.261),
					new LatLng(27.123, 63.281), new LatLng(27.123, 63.302),
					new LatLng(27.117, 63.32), new LatLng(27.185, 63.286),
					new LatLng(27.203, 63.271), new LatLng(27.214, 63.261),
					new LatLng(27.219, 63.252), new LatLng(27.227, 63.227),
					new LatLng(27.233, 63.215), new LatLng(27.254, 63.19),
					new LatLng(27.258, 63.177), new LatLng(27.259, 63.167),
					new LatLng(27.243, 63.112), new LatLng(27.243, 63.099),
					new LatLng(27.244, 63.083), new LatLng(27.243, 63.056),
					new LatLng(27.229, 63.004), new LatLng(27.225, 62.978),
					new LatLng(27.215, 62.92), new LatLng(27.213, 62.892),
					new LatLng(27.22, 62.861), new LatLng(27.231, 62.837),
					new LatLng(27.234, 62.829), new LatLng(27.232, 62.822),
					new LatLng(27.225, 62.809), new LatLng(27.225, 62.801),
					new LatLng(27.232, 62.786), new LatLng(27.267, 62.742),
					new LatLng(27.268, 62.758), new LatLng(27.274, 62.768),
					new LatLng(27.286, 62.774), new LatLng(27.3, 62.779),
					new LatLng(27.316, 62.781), new LatLng(27.327, 62.776),
					new LatLng(27.348, 62.756), new LatLng(27.453, 62.809),
					new LatLng(27.495, 62.815), new LatLng(27.547, 62.809),
					new LatLng(27.639, 62.8), new LatLng(27.689, 62.795),
					new LatLng(27.77, 62.786), new LatLng(27.83, 62.78),
					new LatLng(27.926, 62.754), new LatLng(27.995, 62.736),
					new LatLng(28.109, 62.752), new LatLng(28.211, 62.766),
					new LatLng(28.246, 62.762), new LatLng(28.258, 62.739),
					new LatLng(28.229, 62.576), new LatLng(28.24, 62.555),
					new LatLng(28.301, 62.492), new LatLng(28.322, 62.478),
					new LatLng(28.34, 62.471), new LatLng(28.35, 62.464),
					new LatLng(28.356, 62.454), new LatLng(28.36, 62.437),
					new LatLng(28.369, 62.421), new LatLng(28.381, 62.411),
					new LatLng(28.394, 62.402), new LatLng(28.406, 62.391),
					new LatLng(28.419, 62.363), new LatLng(28.442, 62.231),
					new LatLng(28.475, 62.148), new LatLng(28.481, 62.116),
					new LatLng(28.486, 62.049), new LatLng(28.495, 62.019),
					new LatLng(28.519, 61.971), new LatLng(28.534, 61.911),
					new LatLng(28.543, 61.893), new LatLng(28.575, 61.861),
					new LatLng(28.627, 61.798), new LatLng(28.674, 61.756),
					new LatLng(28.756, 61.653), new LatLng(28.788, 61.624),
					new LatLng(28.843, 61.595), new LatLng(28.871, 61.567),
					new LatLng(28.886, 61.561), new LatLng(28.903, 61.559),
					new LatLng(28.922, 61.553), new LatLng(29.007, 61.511),
					new LatLng(29.018, 61.502), new LatLng(29.025, 61.491),
					new LatLng(29.031, 61.478), new LatLng(29.036, 61.47),
					new LatLng(29.042, 61.468), new LatLng(29.048, 61.471),
					new LatLng(29.054, 61.478), new LatLng(29.063, 61.487),
					new LatLng(29.072, 61.491), new LatLng(29.081, 61.488),
					new LatLng(29.087, 61.478), new LatLng(29.094, 61.461),
					new LatLng(29.108, 61.453), new LatLng(29.125, 61.448),
					new LatLng(29.14, 61.438), new LatLng(29.145, 61.427),
					new LatLng(29.146, 61.418), new LatLng(29.149, 61.409),
					new LatLng(29.159, 61.398), new LatLng(29.17, 61.393),
					new LatLng(29.195, 61.39), new LatLng(29.207, 61.387),
					new LatLng(29.224, 61.374), new LatLng(29.255, 61.342),
					new LatLng(29.275, 61.333), new LatLng(29.294, 61.333),
					new LatLng(29.33, 61.345), new LatLng(29.349, 61.347),
					new LatLng(29.374, 61.337), new LatLng(29.385, 61.32),
					new LatLng(29.393, 61.299), new LatLng(29.407, 61.279),
					new LatLng(29.492, 61.197), new LatLng(29.574, 61.119),
					new LatLng(29.652, 61.043), new LatLng(29.721, 60.978),
					new LatLng(29.775, 60.925), new LatLng(29.858, 60.844),
					new LatLng(29.846, 60.876), new LatLng(29.826, 60.947),
					new LatLng(29.802, 61.031), new LatLng(29.779, 61.114),
					new LatLng(29.755, 61.197), new LatLng(29.732, 61.28),
					new LatLng(29.709, 61.364), new LatLng(29.685, 61.447),
					new LatLng(29.662, 61.53), new LatLng(29.639, 61.613),
					new LatLng(29.615, 61.697), new LatLng(29.592, 61.78),
					new LatLng(29.568, 61.863), new LatLng(29.545, 61.947),
					new LatLng(29.522, 62.03), new LatLng(29.498, 62.113),
					new LatLng(29.475, 62.196), new LatLng(29.451, 62.279),
					new LatLng(29.425, 62.374), new LatLng(29.408, 62.478),
					new LatLng(29.42, 62.631), new LatLng(29.424, 62.68),
					new LatLng(29.428, 62.729), new LatLng(29.433, 62.778),
					new LatLng(29.437, 62.827), new LatLng(29.441, 62.876),
					new LatLng(29.445, 62.926), new LatLng(29.449, 62.975),
					new LatLng(29.453, 63.024), new LatLng(29.457, 63.073),
					new LatLng(29.461, 63.122), new LatLng(29.465, 63.171),
					new LatLng(29.469, 63.22), new LatLng(29.473, 63.269),
					new LatLng(29.477, 63.318), new LatLng(29.481, 63.367),
					new LatLng(29.485, 63.416), new LatLng(29.497, 63.569),
					new LatLng(29.461, 63.788), new LatLng(29.43, 63.972),
					new LatLng(29.387, 64.086), new LatLng(29.396, 64.113),
					new LatLng(29.458, 64.15), new LatLng(29.484, 64.173),
					new LatLng(29.5, 64.208), new LatLng(29.57, 64.478),
					new LatLng(29.569, 64.684), new LatLng(29.568, 64.82),
					new LatLng(29.542, 64.987), new LatLng(29.54, 65.036),
					new LatLng(29.577, 65.181), new LatLng(29.592, 65.24),
					new LatLng(29.607, 65.298), new LatLng(29.621, 65.356),
					new LatLng(29.636, 65.414), new LatLng(29.651, 65.472),
					new LatLng(29.666, 65.53), new LatLng(29.681, 65.588),
					new LatLng(29.695, 65.646), new LatLng(29.71, 65.704),
					new LatLng(29.725, 65.762), new LatLng(29.74, 65.82),
					new LatLng(29.754, 65.878), new LatLng(29.769, 65.936),
					new LatLng(29.784, 65.994), new LatLng(29.799, 66.052),
					new LatLng(29.814, 66.11), new LatLng(29.835, 66.196),
					new LatLng(29.885, 66.275), new LatLng(29.916, 66.302),
					new LatLng(29.947, 66.322), new LatLng(29.95, 66.328),
					new LatLng(29.952, 66.337), new LatLng(29.957, 66.341),
					new LatLng(29.966, 66.332), new LatLng(29.987, 66.302),
					new LatLng(30.023, 66.26), new LatLng(30.044, 66.225),
					new LatLng(30.058, 66.219), new LatLng(30.074, 66.222),
					new LatLng(30.112, 66.236), new LatLng(30.226, 66.301),
					new LatLng(30.245, 66.305), new LatLng(30.305, 66.303),
					new LatLng(30.437, 66.322), new LatLng(30.458, 66.319),
					new LatLng(30.478, 66.314), new LatLng(30.491, 66.306),
					new LatLng(30.518, 66.282), new LatLng(30.558, 66.265),
					new LatLng(30.601, 66.268), new LatLng(30.923, 66.366),
					new LatLng(30.937, 66.375), new LatLng(30.945, 66.392),
					new LatLng(30.968, 66.527), new LatLng(30.977, 66.55),
					new LatLng(31.06, 66.644), new LatLng(31.083, 66.663),
					new LatLng(31.196, 66.697), new LatLng(31.21, 66.721),
					new LatLng(31.215, 66.76), new LatLng(31.232, 66.785),
					new LatLng(31.255, 66.809), new LatLng(31.277, 66.838),
					new LatLng(31.306, 66.906), new LatLng(31.315, 66.943),
					new LatLng(31.316, 67.002), new LatLng(31.309, 67.017),
					new LatLng(31.295, 67.024), new LatLng(31.273, 67.026),
					new LatLng(31.265, 67.023), new LatLng(31.259, 67.018),
					new LatLng(31.253, 67.014), new LatLng(31.245, 67.015),
					new LatLng(31.239, 67.023), new LatLng(31.236, 67.035),
					new LatLng(31.232, 67.058), new LatLng(31.232, 67.078),
					new LatLng(31.24, 67.117), new LatLng(31.241, 67.137),
					new LatLng(31.236, 67.157), new LatLng(31.219, 67.193),
					new LatLng(31.212, 67.214), new LatLng(31.211, 67.23),
					new LatLng(31.213, 67.282), new LatLng(31.208, 67.346),
					new LatLng(31.211, 67.365), new LatLng(31.236, 67.434),
					new LatLng(31.243, 67.493), new LatLng(31.257, 67.53),
					new LatLng(31.265, 67.584), new LatLng(31.271, 67.602),
					new LatLng(31.306, 67.666), new LatLng(31.317, 67.679),
					new LatLng(31.325, 67.693), new LatLng(31.329, 67.709),
					new LatLng(31.328, 67.749), new LatLng(31.334, 67.765),
					new LatLng(31.353, 67.775), new LatLng(31.395, 67.77),
					new LatLng(31.405, 67.734), new LatLng(31.395, 67.651),
					new LatLng(31.4, 67.631), new LatLng(31.411, 67.611),
					new LatLng(31.426, 67.597), new LatLng(31.465, 67.591),
					new LatLng(31.482, 67.578), new LatLng(31.497, 67.563),
					new LatLng(31.512, 67.556), new LatLng(31.53, 67.569),
					new LatLng(31.53, 67.601), new LatLng(31.518, 67.665),
					new LatLng(31.521, 67.696), new LatLng(31.531, 67.727),
					new LatLng(31.564, 67.781), new LatLng(31.623, 67.843),
					new LatLng(31.635, 67.871), new LatLng(31.636, 67.884),
					new LatLng(31.631, 67.915), new LatLng(31.633, 67.955),
					new LatLng(31.638, 67.966), new LatLng(31.652, 67.977),
					new LatLng(31.663, 67.994), new LatLng(31.688, 68.047),
					new LatLng(31.695, 68.052), new LatLng(31.717, 68.056),
					new LatLng(31.726, 68.06), new LatLng(31.752, 68.093),
					new LatLng(31.769, 68.105), new LatLng(31.811, 68.126),
					new LatLng(31.825, 68.139), new LatLng(31.826, 68.159),
					new LatLng(31.818, 68.185), new LatLng(31.808, 68.207),
					new LatLng(31.79, 68.232), new LatLng(31.777, 68.243),
					new LatLng(31.766, 68.255), new LatLng(31.764, 68.277),
					new LatLng(31.765, 68.317), new LatLng(31.763, 68.356),
					new LatLng(31.73, 68.461), new LatLng(31.725, 68.498),
					new LatLng(31.73, 68.515), new LatLng(31.741, 68.537),
					new LatLng(31.754, 68.55), new LatLng(31.763, 68.541),
					new LatLng(31.765, 68.521), new LatLng(31.764, 68.461),
					new LatLng(31.767, 68.44), new LatLng(31.773, 68.422),
					new LatLng(31.783, 68.419), new LatLng(31.796, 68.438),
					new LatLng(31.816, 68.481), new LatLng(31.823, 68.505),
					new LatLng(31.823, 68.527), new LatLng(31.812, 68.561),
					new LatLng(31.783, 68.619), new LatLng(31.775, 68.676),
					new LatLng(31.769, 68.688), new LatLng(31.756, 68.695),
					new LatLng(31.716, 68.698), new LatLng(31.701, 68.705),
					new LatLng(31.676, 68.731), new LatLng(31.619, 68.777),
					new LatLng(31.603, 68.804), new LatLng(31.606, 68.844),
					new LatLng(31.634, 68.907), new LatLng(31.644, 68.941),
					new LatLng(31.641, 68.977), new LatLng(31.651, 69.004),
					new LatLng(31.673, 69.04), new LatLng(31.698, 69.072),
					new LatLng(31.716, 69.085), new LatLng(31.724, 69.1),
					new LatLng(31.738, 69.114), new LatLng(31.882, 69.224),
					new LatLng(31.907, 69.251), new LatLng(31.938, 69.299),
					new LatLng(31.947, 69.305), new LatLng(31.96, 69.302),
					new LatLng(32.036, 69.271), new LatLng(32.131, 69.251),
					new LatLng(32.152, 69.252), new LatLng(32.195, 69.26),
					new LatLng(32.237, 69.26), new LatLng(32.28, 69.267),
					new LatLng(32.301, 69.268), new LatLng(32.322, 69.264),
					new LatLng(32.378, 69.239), new LatLng(32.421, 69.228),
					new LatLng(32.463, 69.233), new LatLng(32.501, 69.252),
					new LatLng(32.533, 69.282), new LatLng(32.544, 69.302),
					new LatLng(32.556, 69.344), new LatLng(32.568, 69.361),
					new LatLng(32.635, 69.414), new LatLng(32.655, 69.426),
					new LatLng(32.667, 69.429), new LatLng(32.7, 69.419),
					new LatLng(32.718, 69.417), new LatLng(32.726, 69.415),
					new LatLng(32.73, 69.408), new LatLng(32.733, 69.399),
					new LatLng(32.738, 69.39), new LatLng(32.744, 69.383),
					new LatLng(32.752, 69.378), new LatLng(32.772, 69.376),
					new LatLng(32.785, 69.387), new LatLng(32.807, 69.421),
					new LatLng(32.836, 69.446), new LatLng(32.852, 69.471),
					new LatLng(32.857, 69.477), new LatLng(32.886, 69.487),
					new LatLng(32.994, 69.469), new LatLng(33.028, 69.488),
					new LatLng(33.057, 69.514), new LatLng(33.075, 69.548),
					new LatLng(33.08, 69.588), new LatLng(33.079, 69.608),
					new LatLng(33.084, 69.641), new LatLng(33.082, 69.65),
					new LatLng(33.078, 69.659), new LatLng(33.077, 69.668),
					new LatLng(33.081, 69.686), new LatLng(33.109, 69.733),
					new LatLng(33.115, 69.772), new LatLng(33.087, 69.839),
					new LatLng(33.089, 69.881), new LatLng(33.128, 69.956),
					new LatLng(33.129, 69.966), new LatLng(33.127, 69.995),
					new LatLng(33.132, 70.007), new LatLng(33.141, 70.015),
					new LatLng(33.194, 70.048), new LatLng(33.198, 70.06),
					new LatLng(33.191, 70.084), new LatLng(33.19, 70.105),
					new LatLng(33.199, 70.125), new LatLng(33.319, 70.294),
					new LatLng(33.352, 70.302), new LatLng(33.383, 70.286),
					new LatLng(33.433, 70.233), new LatLng(33.44, 70.228),
					new LatLng(33.456, 70.22), new LatLng(33.461, 70.213),
					new LatLng(33.469, 70.185), new LatLng(33.473, 70.178),
					new LatLng(33.489, 70.164), new LatLng(33.507, 70.155),
					new LatLng(33.526, 70.151), new LatLng(33.545, 70.153),
					new LatLng(33.608, 70.173), new LatLng(33.632, 70.174),
					new LatLng(33.639, 70.17), new LatLng(33.643, 70.163),
					new LatLng(33.65, 70.146), new LatLng(33.656, 70.137),
					new LatLng(33.661, 70.133), new LatLng(33.677, 70.127),
					new LatLng(33.717, 70.118), new LatLng(33.727, 70.108),
					new LatLng(33.721, 70.066), new LatLng(33.74, 70.012),
					new LatLng(33.745, 69.973), new LatLng(33.753, 69.958),
					new LatLng(33.772, 69.947), new LatLng(33.791, 69.94),
					new LatLng(33.806, 69.931), new LatLng(33.836, 69.908),
					new LatLng(33.852, 69.899), new LatLng(33.889, 69.885),
					new LatLng(33.903, 69.876), new LatLng(33.927, 69.847),
					new LatLng(33.942, 69.841), new LatLng(33.956, 69.854),
					new LatLng(33.972, 69.872), new LatLng(33.986, 69.874),
					new LatLng(34.001, 69.87), new LatLng(34.017, 69.873),
					new LatLng(34.031, 69.889), new LatLng(34.039, 69.916),
					new LatLng(34.046, 69.969), new LatLng(34.044, 70.003),
					new LatLng(33.981, 70.219), new LatLng(33.976, 70.276),
					new LatLng(33.962, 70.31), new LatLng(33.957, 70.328),
					new LatLng(33.954, 70.409), new LatLng(33.94, 70.491),
					new LatLng(33.939, 70.522), new LatLng(33.955, 70.656),
					new LatLng(33.954, 70.791), new LatLng(33.965, 70.862),
					new LatLng(33.994, 70.88), new LatLng(34.007, 70.884),
					new LatLng(34.009, 70.894), new LatLng(34.002, 70.921),
					new LatLng(34.001, 70.94), new LatLng(34.005, 70.954),
					new LatLng(34.014, 70.966), new LatLng(34.027, 70.977),
					new LatLng(34.033, 70.998), new LatLng(34.042, 71.047),
					new LatLng(34.054, 71.063), new LatLng(34.073, 71.067),
					new LatLng(34.089, 71.064), new LatLng(34.105, 71.063),
					new LatLng(34.125, 71.074), new LatLng(34.152, 71.102),
					new LatLng(34.165, 71.108), new LatLng(34.189, 71.109),
					new LatLng(34.209, 71.107), new LatLng(34.244, 71.097),
					new LatLng(34.262, 71.097), new LatLng(34.332, 71.126),
					new LatLng(34.357, 71.122), new LatLng(34.39, 71.051),
					new LatLng(34.414, 71.02), new LatLng(34.443, 70.992),
					new LatLng(34.469, 70.971), new LatLng(34.488, 70.961),
					new LatLng(34.51, 70.956), new LatLng(34.532, 70.957),
					new LatLng(34.55, 70.969), new LatLng(34.556, 70.986),
					new LatLng(34.556, 71.007), new LatLng(34.551, 71.048),
					new LatLng(34.558, 71.066), new LatLng(34.579, 71.077),
					new LatLng(34.603, 71.08), new LatLng(34.624, 71.076),
					new LatLng(34.646, 71.069), new LatLng(34.661, 71.07),
					new LatLng(34.673, 71.081), new LatLng(34.72, 71.151),
					new LatLng(34.737, 71.19), new LatLng(34.748, 71.203),
					new LatLng(34.81, 71.256), new LatLng(34.844, 71.271),
					new LatLng(34.875, 71.289), new LatLng(34.898, 71.325),
					new LatLng(34.943, 71.46), new LatLng(34.95, 71.47),
					new LatLng(34.96, 71.477), new LatLng(34.983, 71.485),
					new LatLng(34.994, 71.493), new LatLng(34.994, 71.493),
					new LatLng(34.994, 71.493), new LatLng(34.994, 71.493),
					new LatLng(35.001, 71.504), new LatLng(35.009, 71.511),
					new LatLng(35.018, 71.513), new LatLng(35.028, 71.508),
					new LatLng(35.072, 71.509), new LatLng(35.099, 71.535),
					new LatLng(35.138, 71.604), new LatLng(35.17, 71.629),
					new LatLng(35.19, 71.638), new LatLng(35.203, 71.634),
					new LatLng(35.223, 71.603), new LatLng(35.276, 71.548),
					new LatLng(35.301, 71.53), new LatLng(35.328, 71.532),
					new LatLng(35.361, 71.562), new LatLng(35.395, 71.611),
					new LatLng(35.41, 71.621), new LatLng(35.43, 71.622),
					new LatLng(35.443, 71.614), new LatLng(35.455, 71.601),
					new LatLng(35.471, 71.588), new LatLng(35.493, 71.582),
					new LatLng(35.512, 71.586), new LatLng(35.53, 71.593),
					new LatLng(35.549, 71.593), new LatLng(35.564, 71.584),
					new LatLng(35.574, 71.568), new LatLng(35.596, 71.513),
					new LatLng(35.61, 71.493), new LatLng(35.627, 71.483),
					new LatLng(35.647, 71.496), new LatLng(35.666, 71.514),
					new LatLng(35.684, 71.52), new LatLng(35.701, 71.515),
					new LatLng(35.719, 71.503), new LatLng(35.725, 71.496),
					new LatLng(35.734, 71.481), new LatLng(35.741, 71.476),
					new LatLng(35.752, 71.471), new LatLng(35.761, 71.471),
					new LatLng(35.77, 71.472), new LatLng(35.78, 71.471),
					new LatLng(35.795, 71.464), new LatLng(35.844, 71.431),
					new LatLng(35.859, 71.417), new LatLng(35.885, 71.371),
					new LatLng(35.9, 71.361), new LatLng(35.933, 71.356),
					new LatLng(35.947, 71.342), new LatLng(35.957, 71.313),
					new LatLng(35.962, 71.284), new LatLng(35.972, 71.257),
					new LatLng(35.994, 71.232), new LatLng(36.003, 71.218),
					new LatLng(36.019, 71.182), new LatLng(36.027, 71.171),
					new LatLng(36.046, 71.166), new LatLng(36.061, 71.175),
					new LatLng(36.088, 71.208), new LatLng(36.097, 71.213),
					new LatLng(36.118, 71.218), new LatLng(36.125, 71.223),
					new LatLng(36.146, 71.262), new LatLng(36.158, 71.293),
					new LatLng(36.163, 71.302), new LatLng(36.173, 71.309),
					new LatLng(36.194, 71.314), new LatLng(36.201, 71.319),
					new LatLng(36.219, 71.382), new LatLng(36.231, 71.403),
					new LatLng(36.301, 71.479), new LatLng(36.31, 71.496),
					new LatLng(36.315, 71.515), new LatLng(36.319, 71.539),
					new LatLng(36.328, 71.558), new LatLng(36.341, 71.553),
					new LatLng(36.356, 71.542), new LatLng(36.372, 71.547),
					new LatLng(36.392, 71.572), new LatLng(36.402, 71.58),
					new LatLng(36.418, 71.589), new LatLng(36.449, 71.601),
					new LatLng(36.458, 71.61), new LatLng(36.46, 71.629),
					new LatLng(36.453, 71.649), new LatLng(36.421, 71.707),
					new LatLng(36.396, 71.736), new LatLng(36.391, 71.751),
					new LatLng(36.392, 71.767), new LatLng(36.397, 71.783),
					new LatLng(36.408, 71.794), new LatLng(36.421, 71.792),
					new LatLng(36.448, 71.775), new LatLng(36.48, 71.773),
					new LatLng(36.491, 71.794), new LatLng(36.494, 71.859),
					new LatLng(36.499, 71.875), new LatLng(36.508, 71.888),
					new LatLng(36.518, 71.9), new LatLng(36.528, 71.913),
					new LatLng(36.55, 71.961), new LatLng(36.563, 71.977),
					new LatLng(36.572, 71.998), new LatLng(36.58, 72.039),
					new LatLng(36.593, 72.055), new LatLng(36.602, 72.056),
					new LatLng(36.61, 72.052), new LatLng(36.619, 72.05),
					new LatLng(36.627, 72.059), new LatLng(36.632, 72.071),
					new LatLng(36.639, 72.096), new LatLng(36.645, 72.154),
					new LatLng(36.654, 72.172), new LatLng(36.67, 72.165),
					new LatLng(36.689, 72.15), new LatLng(36.702, 72.153),
					new LatLng(36.711, 72.17), new LatLng(36.726, 72.214),
					new LatLng(36.743, 72.302), new LatLng(36.745, 72.346),
					new LatLng(36.756, 72.387), new LatLng(36.753, 72.433),
					new LatLng(36.758, 72.454), new LatLng(36.801, 72.517),
					new LatLng(36.821, 72.565), new LatLng(36.833, 72.63),
					new LatLng(36.837, 72.696), new LatLng(36.827, 72.779),
					new LatLng(36.83, 72.868), new LatLng(36.837, 72.897),
					new LatLng(36.847, 72.921), new LatLng(36.852, 72.946)

			);
			rectOptions.fillColor(getResources().getColor(
					R.color.home_kpi_color_trans_green));
			Polygon polygon = map.addPolygon(rectOptions);
			polygon.setStrokeWidth((float) 3.5);
			polygon.setStrokeColor(getResources().getColor(
					R.color.home_kpi_color_green));

			polygon.setVisible(true);*/

		} catch (Exception e) {

		}
	}

	private Bitmap scaleImage(Bitmap originalImage) {

		return scaleImage(originalImage, 40, 40);
	}

	private Bitmap scaleImage(Bitmap originalImage, int width, int height) {
		Bitmap background = Bitmap
				.createBitmap(width, height, Config.ARGB_8888);
		float originalWidth = originalImage.getWidth(), originalHeight = originalImage
				.getHeight();
		Canvas canvas = new Canvas(background);
		float scale = width / originalWidth;
		float xTranslation = 0.0f, yTranslation = (height - originalHeight
				* scale) / 2.0f;
		Matrix transformation = new Matrix();
		transformation.postTranslate(xTranslation, yTranslation);
		transformation.preScale(scale, scale);
		Paint paint = new Paint();
		paint.setFilterBitmap(true);
		canvas.drawBitmap(originalImage, transformation, paint);
		return background;
	}

	@Override
	public boolean onMarkerClick(Marker marker) {
		initializeValue();
		Bitmap defaultBitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.mobilink);
		marker.setIcon(BitmapDescriptorFactory.fromBitmap(scaleImage(
				defaultBitmap, 60, 60)));		
		if (previousMarker != null) {
			previousMarker.setIcon(BitmapDescriptorFactory
					.fromBitmap(scaleImage(defaultBitmap)));
		}
		previousMarker = marker;
		isLoadingZoneData = false;
		tvTitleHome.setText(marker.getTitle());
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		tvTitleSelectedDate.setText(CommonTask
				.convertDateToStringWithCustomFormat(cal.getTimeInMillis()));
		selectedName = marker.getTitle();
		selectedid = 0;
		if (marker.getTitle().equals("Central 2")) {
			selectedid = 1;
			rlHomeCompare.setEnabled(true);
		} else if (marker.getTitle().equals("Central 3")) {
			selectedid = 2;
			rlHomeCompare.setEnabled(true);
		} else if (marker.getTitle().equals("South 1")) {
			selectedid = 3;
			rlHomeCompare.setEnabled(true);
		} else if (marker.getTitle().equals("South 2")) {
			selectedid = 4;
			rlHomeCompare.setEnabled(true);
		} else if (marker.getTitle().equals("South 3")) {
			selectedid = 5;
			rlHomeCompare.setEnabled(true);
		} else if (marker.getTitle().equals("South 4")) {
			selectedid = 6;
			rlHomeCompare.setEnabled(true);
		}
		if (selectedid > 0) {
			selectedDate = 0;
			LoadInformation();
		}
		return false;
	}

	@Override
	public void showProgressLoader() {
		/*
		 * progress= ProgressDialog.show(this, "","Please wait", true);
		 * progress.setCancelable(false); progress.setIcon(null);
		 */

	}

	@Override
	public void hideProgressLoader() {
		// progress.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		INetworkKPIManager manager = new NetworkKPIManager();
		if (selectedid == 0) {
			return manager.GetMLNetworkKPI(selectedDate);
		} else {
			if (isRegionData) {
				return manager.GetMLRegionWiseKPI(selectedid, selectedDate);
			} else {
				return manager.GetMLZoneWiseKPI(selectedid, selectedDate);
			}
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			kPICustoms = (KPICustoms) data;
			processData();
		} else {
			selectedid = 0;
			Toast.makeText(this, "KPI not available", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void processData() {
		if (kPICustoms != null && kPICustoms.KPICustomList != null
				&& kPICustoms.KPICustomList.size() > 0) {
			tvTitleSelectedDate.setText(CommonTask
					.convertDateToStringWithCustomFormat(CommonTask
							.convertJsonDateToLong(kPICustoms.KPICustomList
									.get(0).ReportDate)));
			for (KPICustom kPICustom : kPICustoms.KPICustomList) {
				if (kPICustom.KPIName.equals("Traffic")) {
					tvTCHTrafficValue.setText(String
							.valueOf((int) kPICustom.Value));
					rlTCHTraffic.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlTCHTraffic.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlTCHTraffic.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlTCHTraffic.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("CSSR")) {
					tvCSSRValue.setText(String.format("%.1f", kPICustom.Value)
							+ "%");
					gvCSSR.setTargetValue((float) kPICustom.Value);
					rlCSSR.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlCSSR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlCSSR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlCSSR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("DCR")) {
					tvDCRValue.setText(String.format("%.1f", kPICustom.Value)
							+ "%");
					gvDCR.setTargetValue((float) kPICustom.Value);
					rlDCR.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlDCR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlDCR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlDCR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("HOSR")) {
					tvHOSRValue.setText(String.format("%.1f", kPICustom.Value)
							+ "%");
					gvHOSR.setTargetValue((float) kPICustom.Value);
					rlHOSR.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlHOSR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlHOSR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlHOSR.setBackgroundColor(this.getResources().getColor(
								R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("TCH Congestion")) {
					tvTCHCongestionValue.setText(String.format("%.2f",
							kPICustom.Value) + "%");
					gvTCHCongestion.setTargetValue((float) kPICustom.Value);
					rlTCHCongestion.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlTCHCongestion.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlTCHCongestion.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlTCHCongestion.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("DL TBF SR")) {
					tvDLTBFSRValue.setText(String.format("%.1f",
							kPICustom.Value) + "%");
					gvDLTBFSR.setTargetValue((float) kPICustom.Value);
					rlDLTBFSR.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlDLTBFSR.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlDLTBFSR.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlDLTBFSR.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("TCH Availability")) {
					tvTCHAVGValue.setText(String
							.format("%.2f", kPICustom.Value) + "%");
					gvTCHAVG.setTargetValue((float) kPICustom.Value);
					rlTCHAVG.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlTCHAVG.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlTCHAVG.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlTCHAVG.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_red));
					}
				} else if (kPICustom.KPIName.equals("NW Availability")) {
					tvNWAVGValue.setText(String.format("%.2f", kPICustom.Value)
							+ "%");
					gvNWAVG.setTargetValue((float) kPICustom.Value);
					rlNWAVG.setTag(kPICustom.SLA);
					if (kPICustom.SLA == 1) {
						rlNWAVG.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_green));
					} else if (kPICustom.SLA == 2) {
						rlNWAVG.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_yellow));
					} else if (kPICustom.SLA == 3) {
						rlNWAVG.setBackgroundColor(this.getResources()
								.getColor(R.color.home_kpi_color_red));
					}
				}
			}
		}
	}

	private void setSelection(int rlId) {

		int SLA = Integer.valueOf(rlTCHTraffic.getTag() == null ? "0" : String
				.valueOf(rlTCHTraffic.getTag()));
		if (SLA == 1) {
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer.valueOf(String.valueOf(rlCSSR.getTag() == null ? "0"
				: rlCSSR.getTag()));
		if (SLA == 1) {
			rlCSSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlCSSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlCSSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer.valueOf(String.valueOf(rlDCR.getTag() == null ? "0"
				: rlDCR.getTag()));
		if (SLA == 1) {
			rlDCR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlDCR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlDCR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer.valueOf(String.valueOf(rlHOSR.getTag() == null ? "0"
				: rlHOSR.getTag()));
		if (SLA == 1) {
			rlHOSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlHOSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlHOSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer
				.valueOf(String.valueOf(rlTCHCongestion.getTag() == null ? "0"
						: rlTCHCongestion.getTag()));
		if (SLA == 1) {
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer.valueOf(String.valueOf(rlDLTBFSR.getTag() == null ? "0"
				: rlDLTBFSR.getTag()));
		if (SLA == 1) {
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer.valueOf(String.valueOf(rlTCHAVG.getTag() == null ? "0"
				: rlTCHAVG.getTag()));
		if (SLA == 1) {
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}
		SLA = Integer.valueOf(String.valueOf(rlNWAVG.getTag() == null ? "0"
				: rlNWAVG.getTag()));
		if (SLA == 1) {
			rlNWAVG.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_green));
		} else if (SLA == 2) {
			rlNWAVG.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_yellow));
		} else if (SLA == 3) {
			rlNWAVG.setBackgroundColor(this.getResources().getColor(
					R.color.home_kpi_color_red));
		}

		if (rlId == 1)
			rlTCHTraffic.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 2)
			rlCSSR.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 3)
			rlDCR.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 4)
			rlHOSR.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 5)
			rlTCHCongestion.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 6)
			rlDLTBFSR.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 7)
			rlTCHAVG.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
		else if (rlId == 8)
			rlNWAVG.setBackgroundColor(this.getResources().getColor(
					R.color.selected_tab));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3) {
		CompareListItem item = (CompareListItem) v.getTag();
		selectedid = item.ItemId;
		tvTitleHome.setText(item.ItemName);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		tvTitleSelectedDate.setText(CommonTask
				.convertDateToStringWithCustomFormat(cal.getTimeInMillis()));
		selectedName = item.ItemName;
		rlHomeCompare.setEnabled(true);
		isLoadingZoneData = false;
		initializeValue();
		selectedDate = 0;
		LoadInformation();
	}

	private void pickTime() {
		DatePickerDialog datePickerDialog = new DatePickerDialog(this,
				DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT, kpiDate,
				myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH),
				myCalender.get(Calendar.DAY_OF_MONTH));
		Calendar minDate = Calendar.getInstance();
		minDate.add(Calendar.DAY_OF_YEAR, -1);
		datePickerDialog.getDatePicker().setMaxDate(minDate.getTimeInMillis());
		minDate.add(Calendar.DAY_OF_YEAR, -13);
		datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
		datePickerDialog.setTitle("Select Date");
		datePickerDialog.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				dialog.cancel();
			}
		});
		datePickerDialog.show();

	}

	public DatePickerDialog.OnDateSetListener kpiDate = new OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = new GregorianCalendar(years, monthOfYear, dayOfMonth);
			tvTitleSelectedDate
					.setText(CommonTask.convertDateToStringWithCustomFormat(cal
							.getTimeInMillis()));
			selectedDate = cal.getTimeInMillis();
			initializeValue();
			LoadInformation();
		}
	};
}
