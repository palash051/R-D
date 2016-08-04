package com.vipdashboard.app.activities;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.ViewFlipper;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.AllCallsAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.classes.StatisticsSocialDetails;
import com.vipdashboard.app.classes.TopContent;
import com.vipdashboard.app.entities.PhoneCallInformation;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.JSONfunctions;

public class StatisticsActivity extends MainActionbarBase implements
		OnClickListener, IAsynchronousTask, OnItemClickListener,
		OnQueryTextListener {

	TextView tvUserExperinceStart, tvUserExperinceExperience,
			tvUserExperinceAssistance;
	TextView tvStatisticsOverview, tvStatisticsChart, tvStatisticsSocial;
	TextView tvStatisticsTotalOutgoingCallDuration,
			tvStatisticsTotalOutgoingCallCount,
			tvStatisticsTotalIncomingCallDuration,
			tvStatisticsTotalIncomingCallCount,
			tvStatisticsTotalMissedCallCount,
			tvStatisticsTotalOutgoingCallDurationForProgress,
			tvStatisticsTotalIncomingCallDurationForProgress,
			tvStatisticsTotalCallDuration, tvStatisticsTotalCallCount;
	TextView tvOverviewFilterAll, tvOverviewFilterToday, tvOverviewFilterWeek,
			tvOverviewFilterMonth;
	ProgressBar progressOutgoing, progressIncoming;

	DownloadableAsyncTask downloadableAsyncTask;

	ImageView ivStatisticsChartOverview, ivStatisticsChart;

	ArrayList<PhoneCallInformation> phoneCallSummeryListByTotal,
			phoneCallSummeryListNyNumber;

	ViewFlipper vfStatistics;

	private static final int DOWNLOAD_TOTAL_CALL_SUMMERY = 0,
			DOWNLOAD_CALL_SUMMERY_BY_NUMBER = 1;
	int DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;

	int incommingCallCount = 0, outgoingCallCount = 0, missedCallCount = 0,
			totalCallCount = 0, dropCallCount = 0, incommingCallDuaration = 0,
			outgoingCallDuaration = 0, totalCallDuaration = 0;
	ListView socailListView;
	SearchView svStatisticsSocial;
	AllCallsAdapter adapter;
	StatisticsSocialDetails statisticsSocialDetails;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_main);

		initialization();
		AutoCompleteTextView search_text = (AutoCompleteTextView) svStatisticsSocial
				.findViewById(svStatisticsSocial
						.getContext()
						.getResources()
						.getIdentifier("android:id/search_src_text", null, null));
		search_text.setTextColor(Color.WHITE);
		search_text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
	}

	private void initialization() {
		socailListView = (ListView) findViewById(R.id.lvStatisticsListView);

		socailListView.setOnItemClickListener(this);

		svStatisticsSocial = (SearchView) findViewById(R.id.svStatisticsSocial);

		tvUserExperinceExperience = (TextView) findViewById(R.id.tvUserExperinceExperience);
		tvUserExperinceStart = (TextView) findViewById(R.id.tvUserExperinceStart);
		tvUserExperinceAssistance = (TextView) findViewById(R.id.tvUserExperinceAssistance);

		vfStatistics = (ViewFlipper) findViewById(R.id.vfStatistics);

		tvStatisticsOverview = (TextView) findViewById(R.id.tvStatisticsOverview);
		tvStatisticsChart = (TextView) findViewById(R.id.tvStatisticsChart);
		tvStatisticsSocial = (TextView) findViewById(R.id.tvStatisticsSocial);

		tvStatisticsTotalOutgoingCallDuration = (TextView) findViewById(R.id.tvStatisticsTotalOutgoingCallDuration);
		tvStatisticsTotalOutgoingCallCount = (TextView) findViewById(R.id.tvStatisticsTotalOutgoingCallCount);
		tvStatisticsTotalIncomingCallDuration = (TextView) findViewById(R.id.tvStatisticsTotalIncomingCallDuration);
		tvStatisticsTotalIncomingCallCount = (TextView) findViewById(R.id.tvStatisticsTotalIncomingCallCount);
		tvStatisticsTotalMissedCallCount = (TextView) findViewById(R.id.tvStatisticsTotalMissedCallCount);
		tvStatisticsTotalCallDuration = (TextView) findViewById(R.id.tvStatisticsTotalCallDuration);
		tvStatisticsTotalCallCount = (TextView) findViewById(R.id.tvStatisticsTotalCallCount);

		tvStatisticsTotalOutgoingCallDurationForProgress = (TextView) findViewById(R.id.tvStatisticsTotalOutgoingCallDurationForProgress);
		tvStatisticsTotalIncomingCallDurationForProgress = (TextView) findViewById(R.id.tvStatisticsTotalIncomingCallDurationForProgress);

		tvOverviewFilterAll = (TextView) findViewById(R.id.tvOverviewFilterAll);
		tvOverviewFilterToday = (TextView) findViewById(R.id.tvOverviewFilterToday);
		tvOverviewFilterWeek = (TextView) findViewById(R.id.tvOverviewFilterWeek);
		tvOverviewFilterMonth = (TextView) findViewById(R.id.tvOverviewFilterMonth);

		tvUserExperinceExperience.setOnClickListener(this);
		tvUserExperinceStart.setOnClickListener(this);
		tvUserExperinceAssistance.setOnClickListener(this);

		tvStatisticsOverview.setOnClickListener(this);
		tvStatisticsChart.setOnClickListener(this);
		tvStatisticsSocial.setOnClickListener(this);

		tvOverviewFilterAll.setOnClickListener(this);
		tvOverviewFilterToday.setOnClickListener(this);
		tvOverviewFilterWeek.setOnClickListener(this);
		tvOverviewFilterMonth.setOnClickListener(this);

		svStatisticsSocial.setOnQueryTextListener(this);

		progressOutgoing = (ProgressBar) findViewById(R.id.progressOutgoing);
		progressIncoming = (ProgressBar) findViewById(R.id.progressIncoming);

		ivStatisticsChartOverview = (ImageView) findViewById(R.id.ivStatisticsChartOverview);
		ivStatisticsChart = (ImageView) findViewById(R.id.ivStatisticsChart);
		ivStatisticsChart.setOnClickListener(this);

		/*
		 * vfStatistics.setOnTouchListener(new OnSwipeTouchListener(this) {
		 * public void onSwipeTop() {
		 * 
		 * }
		 * 
		 * public void onSwipeRight() { arrangeRightSwipe(); }
		 * 
		 * public void onSwipeLeft() { arrangeLeftSwipe(); }
		 * 
		 * public void onSwipeBottom() {
		 * 
		 * } });
		 * 
		 * socailListView.setOnTouchListener(new OnSwipeTouchListener(this) {
		 * public void onSwipeTop() {
		 * 
		 * }
		 * 
		 * public void onSwipeRight() { arrangeRightSwipe(); }
		 * 
		 * public void onSwipeLeft() {
		 * 
		 * }
		 * 
		 * public void onSwipeBottom() {
		 * 
		 * } });
		 */
	}

	private void arrangeCallInfoList() {
		MyNetDatabase database = new MyNetDatabase(this);
		database.open();
		ArrayList<PhoneCallInformation> callList = database
				.getTotalCallInfoByPhoneNumber();
		database.close();
		adapter = new AllCallsAdapter(this, R.layout.all_call_item_layout,
				callList);
		socailListView.setAdapter(adapter);
	}

	private void arrangeRightSwipe() {
		if (vfStatistics.getDisplayedChild() != 0) {

			switch (vfStatistics.getDisplayedChild()) {
			case 1:
				arrangeOverviewTab();
				break;
			case 2:
				arrangeChartTab();
				break;
			default:
				break;
			}
			vfStatistics.showPrevious();
		} else {
			Intent intent = new Intent(StatisticsActivity.this,
					NetworkSelfcareMyExperinceShowMoreActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	private void arrangeLeftSwipe() {
		if (vfStatistics.getDisplayedChild() != vfStatistics.getChildCount() - 1) {
			switch (vfStatistics.getDisplayedChild()) {
			case 0:
				arrangeChartTab();
				break;
			case 1:
				arrangeSocialTab();
				break;
			default:
				break;
			}
			vfStatistics.showNext();
		} else {
			Intent intent = new Intent(StatisticsActivity.this,
					NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}
		vfStatistics.setDisplayedChild(0);
		arrangeCallInfoList();
		/*arrangeOverviewTab();
		vfStatistics.setDisplayedChild(1);*/
		arrangeSocialTab();
		vfStatistics.setDisplayedChild(2);
		super.onResume();
	}

	private void arrangeMonthTab() {
		tvOverviewFilterAll.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterMonth.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		getCallInfo(CommonConstraints.INFO_TYPE_MONTH);
		downloadableAsyncTask = new DownloadableAsyncTask(
				StatisticsActivity.this);
		downloadableAsyncTask.execute();
	}

	private void arrangeWeekTab() {
		tvOverviewFilterAll.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvOverviewFilterMonth.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		getCallInfo(CommonConstraints.INFO_TYPE_WEEK);
		downloadableAsyncTask = new DownloadableAsyncTask(
				StatisticsActivity.this);
		downloadableAsyncTask.execute();

	}

	private void arrangeTodayTab() {
		tvOverviewFilterAll.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterToday.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvOverviewFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterMonth.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		getCallInfo(CommonConstraints.INFO_TYPE_TODAY);
		downloadableAsyncTask = new DownloadableAsyncTask(
				StatisticsActivity.this);
		downloadableAsyncTask.execute();
	}

	private void arrangeAllTab() {
		tvOverviewFilterAll.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvOverviewFilterToday.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterWeek.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvOverviewFilterMonth.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		getCallInfo(CommonConstraints.INFO_TYPE_ALL);
		downloadableAsyncTask = new DownloadableAsyncTask(
				StatisticsActivity.this);
		downloadableAsyncTask.execute();
	}

	private void arrangeOverviewTab() {
		tvStatisticsOverview.setBackgroundColor(getResources().getColor(
				R.color.header_text));
		tvStatisticsChart.setBackgroundColor(getResources().getColor(
				R.color.value_text));

		tvStatisticsSocial.setBackgroundColor(getResources().getColor(
				R.color.value_text));

		DOWNLOAD_TYPE = DOWNLOAD_TOTAL_CALL_SUMMERY;
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		arrangeAllTab();

	}

	private void arrangeChartTab() {
		tvStatisticsOverview.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvStatisticsChart.setBackgroundColor(getResources().getColor(
				R.color.header_text));

		tvStatisticsSocial.setBackgroundColor(getResources().getColor(
				R.color.value_text));

		DOWNLOAD_TYPE = DOWNLOAD_CALL_SUMMERY_BY_NUMBER;
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(
				StatisticsActivity.this);
		downloadableAsyncTask.execute();
	}

	private void arrangeSocialTab() {
		tvStatisticsOverview.setBackgroundColor(getResources().getColor(
				R.color.value_text));
		tvStatisticsChart.setBackgroundColor(getResources().getColor(
				R.color.value_text));

		tvStatisticsSocial.setBackgroundColor(getResources().getColor(
				R.color.header_text));
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvUserExperinceExperience) {
			Intent intent = new Intent(this,
					NetworkSelfCareMyExperienceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.tvUserExperinceAssistance) {
			Intent intent = new Intent(this, AssistanceActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.tvUserExperinceStart) {
			Intent intent = new Intent(this, ExperinceLiveActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (v.getId() == R.id.tvStatisticsOverview) {
			if (vfStatistics.getDisplayedChild() != 0) {
				arrangeOverviewTab();
				vfStatistics.setDisplayedChild(0);
			}
		} else if (v.getId() == R.id.tvStatisticsChart) {
			if (vfStatistics.getDisplayedChild() != 1) {
				arrangeChartTab();
				vfStatistics.setDisplayedChild(1);
			}
		} else if (v.getId() == R.id.tvStatisticsSocial) {
			TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
				if (!isFinishing()) 
				CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
			}
			else if (!CommonTask.isOnline(this)) {
				if (!isFinishing()) 
				CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
			}else{
				if (vfStatistics.getDisplayedChild() != 2) {
					arrangeSocialTab();
					vfStatistics.setDisplayedChild(2);
				}
			}
		} else if (v.getId() == R.id.tvOverviewFilterAll) {
			arrangeAllTab();
		} else if (v.getId() == R.id.tvOverviewFilterToday) {
			arrangeTodayTab();
		} else if (v.getId() == R.id.tvOverviewFilterWeek) {
			arrangeWeekTab();
		} else if (v.getId() == R.id.tvOverviewFilterMonth) {
			arrangeMonthTab();
		} else if (v.getId() == R.id.ivStatisticsChart) {
			TopContent topContent = new TopContent(this);
			topContent.showTopContentList();
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

		try {
			if (DOWNLOAD_TYPE == DOWNLOAD_TOTAL_CALL_SUMMERY) {
				String urlRqs3DPie = String.format(
						CommonURL.getInstance().GoogleChartTotalCalls,
						URLEncoder.encode("Outgoing",
								CommonConstraints.EncodingCode), URLEncoder
								.encode("Incoming",
										CommonConstraints.EncodingCode),
						URLEncoder.encode("Missed",
								CommonConstraints.EncodingCode),
						incommingCallCount, outgoingCallCount, missedCallCount,
						incommingCallCount, outgoingCallCount, missedCallCount);

				return JSONfunctions.LoadChart(urlRqs3DPie);
			} else if (DOWNLOAD_TYPE == DOWNLOAD_CALL_SUMMERY_BY_NUMBER) {
				String urlRqs3DPie = CommonURL.getInstance().GoogleChartUrl
						+ "chdlp=bv|l&chds=a&chs=450x470&";
				String strchdl = "chdl=", strchl = "chl=", strchd = "chd=t:";
				int index = 0, otherDuration = 0, hour = 0, min = 0, sec = 0, percent;
				String durationText = "";
				NumberFormat formatter = new DecimalFormat("00");
				for (PhoneCallInformation phoneCallInformation : phoneCallSummeryListNyNumber) {
					if (index < 10) {
						sec = phoneCallInformation.DurationInSec % 60;
						min = phoneCallInformation.DurationInSec / 60;
						if (min > 59) {
							hour = min / 60;
							min = min % 60;
						}
						durationText = formatter.format(hour) + ":"
								+ formatter.format(min) + ":"
								+ formatter.format(sec);

						percent = (int) (phoneCallInformation.DurationInSec * 100)
								/ totalCallDuaration;
						strchdl = strchdl
								+ URLEncoder
										.encode(quickCallerId(phoneCallInformation.Number)
												+ "(" + percent + "%)",
												CommonConstraints.EncodingCode)
								+ "|";
						strchl = strchl + durationText + "|";
						strchd = strchd + phoneCallInformation.DurationInSec
								+ ",";
					} else {
						otherDuration = otherDuration
								+ phoneCallInformation.DurationInSec;

					}
					index++;
				}

				if (otherDuration > 0) {

					sec = otherDuration % 60;
					min = otherDuration / 60;
					if (min > 59) {
						hour = min / 60;
						min = min % 60;
					}
					durationText = formatter.format(hour) + ":"
							+ formatter.format(min) + ":"
							+ formatter.format(sec);
					percent = (int) (otherDuration * 100) / totalCallDuaration;
					strchdl = strchdl
							+ URLEncoder.encode("Others(" + percent + "%)",
									CommonConstraints.EncodingCode);
					strchl = strchl + durationText;
					strchd = strchd + otherDuration;
				} else {
					strchdl = strchdl.substring(0, strchdl.length() - 1);
					strchl = strchl.substring(0, strchl.length() - 1);
					strchd = strchd.substring(0, strchd.length() - 1);
				}

				urlRqs3DPie = urlRqs3DPie + strchdl + "&" + strchl + "&"
						+ strchd;

				return JSONfunctions.LoadChart(urlRqs3DPie);
			}
		} catch (Exception e) {
			String ss = e.getMessage();
			ss = ss + e.getMessage();
		}
		return null;
	}

	private String quickCallerId(String phoneNumber) {
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		ContentResolver resolver = getContentResolver();
		Cursor cur = resolver.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cur != null && cur.moveToFirst()) {
			String value = cur.getString(cur
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
			if (value != null) {
				cur.close();
				return value;
			}
		}
		cur.close();
		return phoneNumber;
	}

	private void getCallInfo(int type) {
		incommingCallCount = 0;
		outgoingCallCount = 0;
		missedCallCount = 0;
		totalCallCount = 0;
		dropCallCount = 0;
		incommingCallDuaration = 0;
		outgoingCallDuaration = 0;
		totalCallDuaration = 0;

		MyNetDatabase myNetDatabase = new MyNetDatabase(this);

		myNetDatabase.open();
		phoneCallSummeryListByTotal = myNetDatabase.getTotalCallInfo(type);
		phoneCallSummeryListNyNumber = myNetDatabase
				.getTotalCallInfoByPhoneNumber();
		myNetDatabase.close();

		for (PhoneCallInformation phoneCallInformation : phoneCallSummeryListByTotal) {
			if (phoneCallInformation.CallType == CallLog.Calls.INCOMING_TYPE) {
				incommingCallCount = phoneCallInformation.CallCount;
				incommingCallDuaration = phoneCallInformation.DurationInSec;
				totalCallDuaration = totalCallDuaration
						+ incommingCallDuaration;
			} else if (phoneCallInformation.CallType == CallLog.Calls.OUTGOING_TYPE) {
				outgoingCallCount = phoneCallInformation.CallCount;
				outgoingCallDuaration = phoneCallInformation.DurationInSec;
				totalCallDuaration = totalCallDuaration + outgoingCallDuaration;
			} else {
				missedCallCount = phoneCallInformation.CallCount;
			}
			totalCallCount = totalCallCount + phoneCallInformation.CallCount;
		}
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (DOWNLOAD_TYPE == DOWNLOAD_TOTAL_CALL_SUMMERY) {
				NumberFormat formatter = new DecimalFormat("00");

				int hour = 0, min = 0, sec = 0;
				String durationText = "";

				sec = incommingCallDuaration % 60;
				min = incommingCallDuaration / 60;
				if (min > 59) {
					hour = min / 60;
					min = min % 60;
				}

				durationText = formatter.format(hour) + ":"
						+ formatter.format(min) + ":" + formatter.format(sec);

				ivStatisticsChartOverview.setImageBitmap((Bitmap) data);
				progressIncoming.setMax(totalCallDuaration);
				progressIncoming.setProgress(incommingCallDuaration);
				tvStatisticsTotalIncomingCallDurationForProgress
						.setText(durationText);
				tvStatisticsTotalIncomingCallDuration.setText(durationText);
				tvStatisticsTotalIncomingCallCount.setText(String
						.valueOf(incommingCallCount));

				hour = 0;
				min = 0;
				sec = 0;
				durationText = "";

				sec = outgoingCallDuaration % 60;
				min = outgoingCallDuaration / 60;
				if (min > 59) {
					hour = min / 60;
					min = min % 60;
				}

				durationText = formatter.format(hour) + ":"
						+ formatter.format(min) + ":" + formatter.format(sec);

				progressOutgoing.setMax(totalCallDuaration);
				progressOutgoing.setProgress(outgoingCallDuaration);
				tvStatisticsTotalOutgoingCallDurationForProgress
						.setText(durationText);
				tvStatisticsTotalOutgoingCallDuration.setText(durationText);
				tvStatisticsTotalOutgoingCallCount.setText(String
						.valueOf(outgoingCallCount));

				hour = 0;
				min = 0;
				sec = 0;
				durationText = "";

				sec = totalCallDuaration % 60;
				min = totalCallDuaration / 60;
				if (min > 59) {
					hour = min / 60;
					min = min % 60;
				}

				durationText = formatter.format(hour) + ":"
						+ formatter.format(min) + ":" + formatter.format(sec);

				tvStatisticsTotalCallDuration.setText(durationText);
				tvStatisticsTotalCallCount.setText(String
						.valueOf(totalCallCount));

				tvStatisticsTotalMissedCallCount.setText(String
						.valueOf(missedCallCount));

			} else if (DOWNLOAD_TYPE == DOWNLOAD_CALL_SUMMERY_BY_NUMBER) {
				ivStatisticsChart.setImageBitmap((Bitmap) data);
			}
		}

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int arg2, long arg3) {
		PhoneCallInformation phoneCallInformation = (PhoneCallInformation) view
				.getTag();
		TelephonyManager tMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		if (TelephonyManager.SIM_STATE_ABSENT == tMgr.getSimState()) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this, "Mobile SIM card is not installed.\nPlease install it.");
		}
		else if (!CommonTask.isOnline(this)) {
			if (!isFinishing()) 
			CommonTask.DryConnectivityMessage(this,"No Internet Connection.\nPlease enable your connection first.");
		}else{
			if (phoneCallInformation != null) {
				if (statisticsSocialDetails == null)
					statisticsSocialDetails = new StatisticsSocialDetails(this);
				statisticsSocialDetails
						.showDetailsInformation(phoneCallInformation);
			}
		}
	}

	@Override
	public boolean onQueryTextChange(String value) {
		adapter.HistrySocialSearchItem(value);
		return true;
	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		return false;
	}

	/*
	 * private void PostStatusInFacebook() { try{ com.facebook.Session session =
	 * com.facebook.Session.getActiveSession(); if(session != null &&
	 * session.isOpened()){ com.facebook.Session.openActiveSession(this,
	 * falsString name = CommonTask.getContentName(this,
	 * phoneCallInformation.Number); if(name != ""){ number.setText(name);
	 * }else{ number.setText(phoneCallInformation.Number); }e, new
	 * StatusCallback() {
	 * 
	 * @Override public void call(final com.facebook.Session session,
	 * SessionState state, Exception exception) { if(session.isOpened()){
	 * Request.newMeRequest(session, new GraphUserCallback() {
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) {
	 * publishFeedDialog(); } }).executeAsync(); } } }); }else{ try{
	 * com.facebook.Session.openActiveSession(this, true, new
	 * com.facebook.Session.StatusCallback() {
	 * 
	 * @Override public void call(final com.facebook.Session session,
	 * SessionState state, Exception exception) { if(session.isOpened()){
	 * Request.newMeRequest(session, new GraphUserCallback() {
	 * 
	 * @Override public void onCompleted(GraphUser user, Response response) {
	 * publishFeedDialog(); } }).executeAsync(); } } }); }catch (Exception e) {
	 * Toast.makeText(this, e.getMessage().toString(),
	 * Toast.LENGTH_LONG).show(); onBackPressed(); } } }catch (Exception e) {
	 * e.printStackTrace(); }
	 * 
	 * }
	 * 
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { super.onActivityResult(requestCode,
	 * resultCode, data);
	 * com.facebook.Session.getActiveSession().onActivityResult(this,
	 * requestCode, resultCode, data); }
	 * 
	 * 
	 * private void publishFeedDialog() { Bundle postParams = new Bundle();
	 * postParams.putString("name", "MyNet Apps");
	 * postParams.putString("caption", "Call Details");
	 * postParams.putString("picture",
	 * "http://oss-net.com/wp-content/uploads/2013/12/Oss-Net-Logo-3033.png");
	 * postParams.putString("description",
	 * "Hi,\nCheck this summary of our phone conversation :)");
	 * postParams.putString("link", "www.oss-net.com");
	 * 
	 * WebDialog feedDialog = (new
	 * WebDialog.FeedDialogBuilder(this,com.facebook.
	 * Session.getActiveSession(),postParams)) .setOnCompleteListener(new
	 * OnCompleteListener() {
	 * 
	 * @Override public void onComplete(Bundle values,FacebookException error) {
	 * if (error == null) { final String postId = values.getString("post_id");
	 * if (postId != null) {
	 * Toast.makeText(StatisticsActivity.this,"Posted story, id: "
	 * +postId,Toast.LENGTH_SHORT).show(); } else {
	 * Toast.makeText(StatisticsActivity
	 * .this,"Publish cancelled",Toast.LENGTH_SHORT).show(); } } else if (error
	 * instanceof FacebookOperationCanceledException) {
	 * Toast.makeText(StatisticsActivity
	 * .this,"Publish cancelled",Toast.LENGTH_SHORT).show(); } else {
	 * Toast.makeText
	 * (StatisticsActivity.this,"Error posting story",Toast.LENGTH_SHORT
	 * ).show(); } } }) .build(); feedDialog.show(); }
	 */
}
