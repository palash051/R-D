package com.vipdashboard.app.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.vipdashboard.app.R;
import com.vipdashboard.app.base.MainActionbarBase;
import com.vipdashboard.app.base.MyNetApplication;

public class MyNetEmailActivity extends MainActionbarBase implements OnClickListener{
	
	EditText etTo, etSubject, etMessage;
	Button bSendEmail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mynet_email);
		Initilization();
	}
	
	@Override
	protected void onPause() {
		MyNetApplication.activityPaused();
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		MyNetApplication.activityResumed();
		super.onResume();
	}

	private void Initilization() {
		etTo = (EditText) findViewById(R.id.etMyNetEmailTo);
		etSubject = (EditText) findViewById(R.id.etMyNetEmailSubject);
		etMessage = (EditText) findViewById(R.id.etMyNetEmailBody);
		bSendEmail = (Button) findViewById(R.id.bMyNetEmailSendEmail);
		bSendEmail.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.bMyNetEmailSendEmail){
			
		}
	}

}
