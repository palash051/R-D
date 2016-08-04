package com.vipdashboard.app.activities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import com.vipdashboard.app.R;
import com.vipdashboard.app.adapter.ReportMainAdapter;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;
import com.vipdashboard.app.classes.Report;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;

public class MyProfileEditActivity extends MainActionbarBase implements OnClickListener,IAsynchronousTask, OnItemSelectedListener{
	
	Spinner spGender;
	ImageView ivCalender;
	TextView tvName, tvPhone, tvDateofBirth, tvWorkingFor, tvPosition, tvManagerEmail;
	Button bSave;
	int year, month,date;
	ArrayList<Report> report;
	ReportMainAdapter adapter;
	Calendar myCalender;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressBar progressBar;
	private String GenderText;
	public static String DateTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		savedInstanceState  = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup_layout);
		//mSupportActionBar.setDisplayHomeAsUpEnabled(true);
		
		Initalization();
		report = new ArrayList<Report>();
		report.add(new Report("Select...", 0));
		report.add(new Report("Male", 1));
		report.add(new Report("Female", 2));
		adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, report);
		spGender.setAdapter(adapter);
		
		setIntentValue(savedInstanceState);
		
	}

	private void setIntentValue(Bundle savedInstanceState) {
		if(savedInstanceState != null){
			if(savedInstanceState.containsKey("USER_NAME") && savedInstanceState.containsKey("USER_PHONE") && savedInstanceState.containsKey("USER_GENDER")
					&& savedInstanceState.containsKey("USER_DATE_OF_BIRTH") && savedInstanceState.containsKey("USER_WORKING_USER")
					&& savedInstanceState.containsKey("USER_POSITION") && savedInstanceState.containsKey("USER_MANAGER_EMAIL")){
				tvName.setText(savedInstanceState.getString("USER_NAME").toString());
				tvPhone.setText(savedInstanceState.getString("USER_PHONE").toString());
				tvDateofBirth.setText(savedInstanceState.getString("USER_DATE_OF_BIRTH").toString());
				tvWorkingFor.setText(savedInstanceState.getString("USER_WORKING_USER").toString());
				tvPosition.setText(savedInstanceState.getString("USER_POSITION").toString());
				tvManagerEmail.setText(savedInstanceState.getString("USER_MANAGER_EMAIL").toString());
				if(savedInstanceState.getString("USER_GENDER").equals("Male")){
					report.clear();
					report = new ArrayList<Report>();
					report.add(new Report("Male", 1));
					report.add(new Report("Select...", 0));
					report.add(new Report("Female", 2));
					adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, report);
					spGender.setAdapter(adapter);
				}else if(savedInstanceState.getString("USER_GENDER").equals("Female")){
					report.clear();
					report = new ArrayList<Report>();
					report.add(new Report("Female", 2));
					report.add(new Report("Male", 1));
					report.add(new Report("Select...", 0));					
					adapter = new ReportMainAdapter(this, R.layout.label_item_lauout, report);
					spGender.setAdapter(adapter);
				}
			}
		}
	}

	private void Initalization() {
		tvName = (TextView) findViewById(R.id.etNameText);
		tvPhone = (TextView) findViewById(R.id.etPhoneText);
		tvDateofBirth = (TextView) findViewById(R.id.etDateofBirthText);
		tvWorkingFor = (TextView) findViewById(R.id.etWorkingForText);
		tvPosition = (TextView) findViewById(R.id.etPositionText);
		tvManagerEmail = (TextView) findViewById(R.id.etManagerEmailText);
		spGender = (Spinner) findViewById(R.id.spGender);
		ivCalender = (ImageView) findViewById(R.id.ivCalender);
		bSave = (Button) findViewById(R.id.bSave);
		myCalender = Calendar.getInstance();
		progressBar = (ProgressBar) findViewById(R.id.pbSignUpEdit);
		tvWorkingFor.setEnabled(false);
		tvWorkingFor.setClickable(false);
		
		ivCalender.setOnClickListener(this);
		bSave.setOnClickListener(this);
		spGender.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivCalender){
			
			DatePickerDialog datePickerDialog = new DatePickerDialog(this, dateofbirth, myCalender.get(Calendar.YEAR), 
					myCalender.get(Calendar.MONTH),myCalender.get(Calendar.DAY_OF_MONTH));
			datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
			datePickerDialog.setTitle("Date of Birth");
			datePickerDialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					dialog.cancel();
				}
			});
			datePickerDialog.show();
		}else if(view.getId() == R.id.bSave){
			if(tvManagerEmail.getText().toString().equals("")){
				if(GenderText == null)
					GenderText = "";
				if(DateTime == null)
					DateTime = "0";
				SaveInformaiton();
			}
			else{
				if(!tvManagerEmail.getText().toString().equals("") && 
						android.util.Patterns.EMAIL_ADDRESS.matcher(tvManagerEmail.getText().toString()).matches()){
					if(GenderText == null)
						GenderText = "";
					if(DateTime == null)
						DateTime = "0";
					SaveInformaiton();
				}else{
					tvManagerEmail.setError("Email not match");
				}
			}
		}
	}
	
	@Override
	 protected void onPause() {
	  MyNetApplication.activityPaused();
	  super.onPause();
	 }

	 @Override
	 protected void onResume() {
		 super.onResume();
	  MyNetApplication.activityResumed();
	 
	 }

	public DatePickerDialog.OnDateSetListener dateofbirth = new OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int years, int monthOfYear,
				int dayOfMonth) {
			Calendar cal = new GregorianCalendar(years, monthOfYear, dayOfMonth);
			showResult(cal);
		}
	};

	private void showResult(Calendar cal) {
		String value = DateUtils.formatDateTime(this, cal.getTimeInMillis(), DateUtils.FORMAT_ABBREV_MONTH | DateUtils.FORMAT_SHOW_YEAR);
		DateTime = String.valueOf(cal.getTimeInMillis());
		tvDateofBirth.setText(value);
	}
	
	private void SaveInformaiton() {
		if (downloadableAsyncTask != null) {
			downloadableAsyncTask.cancel(true);
		}
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();		
	}

	@Override
	public void showProgressLoader() {
		progressBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void hideProgressLoader() {
		progressBar.setVisibility(View.GONE);
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
		return userManager.setuserInformation(tvName.getText().toString(), tvPhone.getText().toString(), GenderText, 
				DateTime, tvPosition.getText().toString(),tvManagerEmail.getText().toString());
		
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			onBackPressed();
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View view, int arg2,
			long arg3) {
		int id = (Integer) view.getTag();
		if(id == 1){
			GenderText = "Male";
		}else if(id == 2){
			GenderText = "Female";
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
