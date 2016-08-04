package com.vipdashboard.app.fragments;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.CriticalAlarmStatusAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.entities.SiteInformation;
import com.vipdashboard.app.entities.SiteInformations;
import com.vipdashboard.app.entities.TTStatus;
import com.vipdashboard.app.entities.TTStatuses;
import com.vipdashboard.app.entities.TTrequest;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.ILiveAlarmManager;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.LiveAlarmManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;

public class TTRequestDetailsFragment extends Activity implements
		IAsynchronousTask, OnClickListener {
	public static TTrequest TTrequest;
	TTStatus TTstatus;
	DownloadableAsyncTask downloadAsyne;
	ProgressBar pbAlarm;
	// ListView alarm_StatusList;
	private GoogleMap map;
	private LatLng Location;
	CriticalAlarmStatusAdapter criticalAlaramAdapter;
	RelativeLayout statusLayout;
	LinearLayout linear;

	/*
	 * TextView ttNumber, siteid, eventTime, ticketTitle, createdBY,
	 * workingUser, status, type, priority, alarmText, impact, details;
	 */
	/*
	 * public static Fragment newInstance(Context context) {
	 * TTRequestDetailsFragment f = new TTRequestDetailsFragment(); return f; }
	 * 
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) { ViewGroup root = (ViewGroup)
	 * inflater.inflate(R.layout.alarm_details, container, false); TextView
	 * tvTTNumberDetails=(TextView) root.findViewById(R.id.tvTTNumberDetails);
	 * TextView tvTTProblemDescription=(TextView)
	 * root.findViewById(R.id.tvTTProblemDescription); TextView
	 * tvTTProblemText=(TextView) root.findViewById(R.id.tvTTProblemText);
	 * tvTTNumberDetails.setText(TTrequest.TTNumber);
	 * tvTTProblemDescription.setText(TTrequest.ProblemDescription);
	 * tvTTProblemText.setText(TTrequest.ProblemText); return root; }
	 */
	
	String latestTTStatus;

	TextView siteID, position, statusReport, showAllReports;
	private boolean isCallFromShowAllUpdates;
	TextView severity, alarmtext, eventtimer, responsible, ttnumber;
	EditText etPost;
	Button bPost;
	boolean isCallFromSetTTStatusComments, isCallFromNotifyToManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alarm_details);

		// mSupportActionBar.setDisplayHomeAsUpEnabled(true);

		showAllReports = (TextView) findViewById(R.id.tvstatusReport);

		severity = (TextView) findViewById(R.id.tvSeverityText);
		alarmtext = (TextView) findViewById(R.id.tvAlarmText);
		eventtimer = (TextView) findViewById(R.id.tvEventTimeText);
		responsible = (TextView) findViewById(R.id.tvResponsibleText);
		ttnumber = (TextView) findViewById(R.id.tvTTNumberText);

		etPost = (EditText) findViewById(R.id.etAlarmTTStatusCommentPost);
		bPost = (Button) findViewById(R.id.bPostTTStatusComment);

		ttnumber.setText(TTrequest.TTNumber);

		// ttNumber = (TextView) findViewById(R.id.tvTTNumberText);
		/*
		 * siteid = (TextView) findViewById(R.id.tvSiteIdText); eventTime =
		 * (TextView) findViewById(R.id.tvEventTimeText); ticketTitle =
		 * (TextView) findViewById(R.id.tvTicketTitleText); createdBY =
		 * (TextView) findViewById(R.id.tvCreatorText); workingUser = (TextView)
		 * findViewById(R.id.tvWorkingUserText); status = (TextView)
		 * findViewById(R.id.tvStatusText); type = (TextView)
		 * findViewById(R.id.tvTicketTypeText); priority = (TextView)
		 * findViewById(R.id.tvPriorityText); alarmText = (TextView)
		 * findViewById(R.id.tvAlarmTitleText); impact = (TextView)
		 * findViewById(R.id.tvImpactTitle); details = (TextView)
		 * findViewById(R.id.tvDetailsTitletext);
		 */

		statusLayout = (RelativeLayout) findViewById(R.id.rlStatusList);
		linear = new LinearLayout(getApplicationContext());
		linear.setOrientation(LinearLayout.VERTICAL);

		// statusReport = (TextView) findViewById(R.id.statusReport);

		// siteID = (TextView) findViewById(R.id.siteid);
		// position = (TextView) findViewById(R.id.position);

		pbAlarm = (ProgressBar) findViewById(R.id.pbAlarm);
		// alarm_StatusList = (ListView) findViewById(R.id.lvStatusAlarm);

		// Log.e("size of TTstatus", String.valueOf(TTrequest.TTstatus.size()));

		CommonTask.makeLinkedTextview(this, showAllReports, showAllReports
				.getText().toString());

		showAllReports.setOnClickListener(this);
		bPost.setOnClickListener(this);

	}

	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		this.isCallFromShowAllUpdates = false;
		this.isCallFromNotifyToManager = false;
		initializeControl();
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflate = getMenuInflater();
		inflate.inflate(R.menu.menuttrequest, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(android.view.MenuItem item) {
		super.onOptionsItemSelected(item);
		if (item.getItemId() == R.id.menu_CallNOC) {
			Intent calltonoc = new Intent(Intent.ACTION_CALL);
			calltonoc.setData(Uri.parse("tel:" + "+8801610004316"));
			startActivity(calltonoc);
			/*
			 * Intent intent = new Intent(this,DemoScreenActivity.class);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			 * startActivity(intent);
			 */
		} else if (item.getItemId() == R.id.menu_CallManager) {
			Intent calltomanager = new Intent(Intent.ACTION_CALL);
			calltomanager.setData(Uri.parse("tel:" + "+8801610004316"));
			startActivity(calltomanager);
			/*
			 * Intent intent = new Intent(this,DemoScreenActivity.class);
			 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			 * startActivity(intent);
			 */
		} else if (item.getItemId() == R.id.menu_NotifyToManager) {
			this.isCallFromNotifyToManager = true;
			runDownloaded();
			/*Intent intent = new Intent(this, DemoScreenActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);*/
		} 
		/*else if (item.getItemId() == R.id.menu_UpdateStatus) {
			TTStatusUpdateActivity.TTrequest = TTrequest;
			CommonValues.getInstance().isCallFromTTUpdateSet = true;
			CommonValues.getInstance().isCallFromTTStatusSet = false;
			Intent intent = new Intent(this, TTStatusUpdateActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			
			 * Toast.makeText(getApplicationContext(),getResources().getString(R.
			 * string.apllication_default_msg) ,Toast.LENGTH_SHORT).show();
			 
		} else if (item.getItemId() == R.id.menu_SetStatus) {
			TTStatusUpdateActivity.TTrequest = TTrequest;
			CommonValues.getInstance().isCallFromTTStatusSet = true;
			CommonValues.getInstance().isCallFromTTUpdateSet = false;
			Intent intent = new Intent(this, TTStatusUpdateActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		} */
		return true;
	}

	private void initializeControl() {
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.mapAlarmStatus)).getMap();
		runDownloaded();
	}

	private void runDownloaded() {
		// Log.e("downloadAsync Task", "insert");
		if (downloadAsyne != null) {
			downloadAsyne.cancel(true);
		}
		downloadAsyne = new DownloadableAsyncTask(TTRequestDetailsFragment.this);
		downloadAsyne.execute();
	}

	// @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		}
		return true;
	}

	@Override
	public void showProgressLoader() {
		pbAlarm.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		pbAlarm.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		ILiveAlarmManager liveAlaramManger = new LiveAlarmManager();
		if (isCallFromSetTTStatusComments) {
			return liveAlaramManger.SetTTStatusCommend(String.valueOf(TTrequest.TTCode), String.valueOf(latestTTStatus), etPost.getText().toString(), TTrequest.WorkingUserName);
		}else if(this.isCallFromNotifyToManager){
			return userManager.GetManagerInformatoinByUserID();
		}else {
			return liveAlaramManger.GetTTStatusByTTCodeLatestUpdate(
					TTrequest.TTCode, TTrequest.CompanyID, TTrequest.SiteID);
		}

	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			if (isCallFromSetTTStatusComments) {
				isCallFromSetTTStatusComments = false;
				etPost.setText("");
				runDownloaded();
			} else if(this.isCallFromNotifyToManager){
				
			}else {
				ArrayList<Object> complexData = (ArrayList<Object>) data;
				if (complexData.size() > 0) {
					TTStatuses TTstatuses = (TTStatuses) complexData.get(0);
					SiteInformations siteInformations = (SiteInformations) complexData
							.get(1);
					if (TTstatuses.TTstatusList.size() > 0) {
						TTStatus ttStatus = TTstatuses.TTstatusList.get(TTstatuses.TTstatusList.size()-1);
						latestTTStatus = ttStatus.Status;
					}
					try {
						if (this.isCallFromShowAllUpdates) {
							String sbMessage = "";
							TTStatus ttstatus;
							for (int i = TTstatuses.TTstatusList.size() - 1; i >= 0; i--) {
								TextView messageTextView = new TextView(this);
								TextView comments = new TextView(this);
								TextView separator = new TextView(this);
								messageTextView
										.setLayoutParams(new LayoutParams(
												LayoutParams.WRAP_CONTENT,
												LayoutParams.WRAP_CONTENT));
								comments.setLayoutParams(new LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
								separator.setLayoutParams(new LayoutParams(
										LayoutParams.WRAP_CONTENT, 1));
								/*
								 * messageTextView
								 * .setBackgroundResource(R.drawable
								 * .notification_list_item_bac);
								 */
								messageTextView
										.setBackgroundResource(R.color.TTStatus_text);
								comments.setBackgroundResource(R.color.TTStatus_text);
								messageTextView.setTextColor(Color.rgb(0, 176,
										245));
								comments.setTextColor(Color.rgb(0, 0, 0));
								messageTextView.setPadding(10, 5, 10, 0);
								comments.setPadding(10, 5, 10, 0);
								messageTextView.setTextSize(15);
								comments.setTextSize(15);
								ttstatus = TTstatuses.TTstatusList.get(i);
								sbMessage = ttstatus.UserName
										+ " ( "
										+ CommonTask
												.convertJsonDateToTTStatusTime(ttstatus.StatusUpdateTime)
										+ " )";

								messageTextView.setText(sbMessage);
								sbMessage = "";
								sbMessage = ttstatus.Comments;
								comments.setText(sbMessage);
								linear.addView(messageTextView);
								linear.addView(comments);
								linear.addView(separator);

							}
							statusLayout.addView(linear);
							this.isCallFromShowAllUpdates = false;
						} else {
							String sbMessage = "";
							TTStatus ttstatus;
							for (int i = TTstatuses.TTstatusList.size() - 1; i > (TTstatuses.TTstatusList
									.size() - 4); i--) {
								TextView messageTextView = new TextView(this);
								TextView comments = new TextView(this);
								TextView separator = new TextView(this);
								messageTextView
										.setLayoutParams(new LayoutParams(
												LayoutParams.WRAP_CONTENT,
												LayoutParams.WRAP_CONTENT));
								comments.setLayoutParams(new LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
								separator.setLayoutParams(new LayoutParams(
										LayoutParams.WRAP_CONTENT, 1));
								/*
								 * messageTextView
								 * .setBackgroundResource(R.drawable
								 * .notification_list_item_bac);
								 */
								messageTextView
										.setBackgroundResource(R.color.TTStatus_text);
								comments.setBackgroundResource(R.color.TTStatus_text);
								messageTextView.setTextColor(Color.rgb(0, 176,
										245));
								comments.setTextColor(Color.rgb(0, 0, 0));
								messageTextView.setPadding(10, 5, 10, 0);
								comments.setPadding(10, 5, 10, 0);
								messageTextView.setTextSize(15);
								comments.setTextSize(15);
								ttstatus = TTstatuses.TTstatusList.get(i);
								sbMessage = ttstatus.UserName
										+ " ( "
										+ CommonTask
												.convertJsonDateToTTStatusTime(ttstatus.StatusUpdateTime)
										+ " )";

								messageTextView.setText(sbMessage);
								sbMessage = "";
								sbMessage = ttstatus.Comments;
								comments.setText(sbMessage);
								linear.addView(messageTextView);
								linear.addView(comments);
								linear.addView(separator);

							}
							statusLayout.addView(linear);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
					try {
						severity.setText(TTrequest.Severity);
						alarmtext.setText(TTrequest.AlarmText);
						eventtimer.setText(TTrequest.Eventtime);
						responsible.setText(TTrequest.WorkingUserName);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
					// map 
					
					
					try {
						double defaultLatitude = 0, defaultLongitude = 0, latitude = 0, longitude = 0;
						Geocoder geocoder = new Geocoder(this,
								Locale.getDefault());
						List<Address> addresses;
						String addressText = "";
						for (int i = 0; i <= 0; i++) {
							latitude = TTstatuses.TTstatusList
									.get(TTstatuses.TTstatusList.size() - 1).WULat;
							longitude = TTstatuses.TTstatusList
									.get(TTstatuses.TTstatusList.size() - 1).WULong;
							addresses = geocoder.getFromLocation(latitude,
									longitude, 1);
							if (addresses != null && addresses.size() > 0) {
								Address address = addresses.get(0);
								for (int lineIndex = 0; lineIndex < address
										.getMaxAddressLineIndex(); lineIndex++) {
									addressText = addressText
											+ address.getAddressLine(lineIndex)
											+ ", ";
								}
								addressText = addressText
										+ address.getLocality() + ", "
										+ address.getCountryName();
							}
							Location = new LatLng(latitude, longitude);
							map.addMarker(new MarkerOptions()
									.position(Location).title(addressText));
							if (i == 0) {
								defaultLatitude = TTstatuses.TTstatusList
										.get(TTstatuses.TTstatusList.size() - 1).WULat;
								defaultLongitude = TTstatuses.TTstatusList
										.get(TTstatuses.TTstatusList.size() - 1).WULong;
							}
						}
						for (SiteInformation siteInformation : siteInformations.siteNotificationsList) {
							latitude = siteInformation.Latitude;
							longitude = siteInformation.Longitude;
							addresses = geocoder.getFromLocation(latitude,
									longitude, 1);
							if (addresses != null && addresses.size() > 0) {
								Address address = addresses.get(0);
								for (int lineIndex = 0; lineIndex < address
										.getMaxAddressLineIndex(); lineIndex++) {
									addressText = addressText
											+ address.getAddressLine(lineIndex)
											+ ", ";
								}
								addressText = addressText
										+ address.getLocality() + ", "
										+ address.getCountryName();
							}
							Location = new LatLng(latitude, longitude);
							map.addMarker(new MarkerOptions()
									.position(Location).title(addressText));
						}

						map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

						LatLng Defaultlocation = new LatLng(defaultLatitude,
								defaultLongitude);

						map.moveCamera(CameraUpdateFactory.newLatLngZoom(
								Defaultlocation, 14.0f));

						map.animateCamera(CameraUpdateFactory.zoomIn());

						map.animateCamera(CameraUpdateFactory.zoomTo(10));

						CameraPosition cameraPosition = new CameraPosition.Builder()
								.target(Defaultlocation).zoom(17).bearing(90)
								.tilt(30).build();

						map.animateCamera(CameraUpdateFactory
								.newCameraPosition(cameraPosition));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.tvstatusReport) {
			this.isCallFromShowAllUpdates = true;
			showAllReports.setVisibility(View.GONE);
			runDownloaded();
		}else if (view.getId() == R.id.bPostTTStatusComment) {
			if(!etPost.getText().toString().equals("")){
				isCallFromSetTTStatusComments = true;
				runDownloaded();
			}
		}
	}

	/*
	 * private void crateTextView() { for(int i=0;i<10;i++){ TextView view = new
	 * TextView(this); view.setText("The Value of i is :"+ i + "\r\n\n");
	 * view.setTextSize(12); view.setGravity(Gravity.LEFT);
	 * view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
	 * 
	 * LayoutParams.WRAP_CONTENT)); linear.addView(view); } }
	 */
}
