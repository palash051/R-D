package com.mobilink.app.activities;

import com.mobilink.app.R;
import com.mobilink.app.utils.CommonTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	
	EditText etUserName, etUserPassword;
	ImageView bLogin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		Initialization();
	
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}

	private void Initialization() {
		etUserName = (EditText) findViewById(R.id.etUserName);
		etUserPassword = (EditText) findViewById(R.id.etUserPassword);
		bLogin = (ImageView) findViewById(R.id.ivNext);
		
		bLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.ivNext){
			Boolean isValidLogedInUser=false;
			
			if (!CommonTask.isOnline(this)) {
				Toast.makeText(this, "Network connection error.Please check your internet connection.", Toast.LENGTH_SHORT).show();
				return;	
			}
			
			if(etUserName.getText().toString().equals("ferdi.widianto@ericsson.com") && 
					etUserPassword.getText().toString().equals("ferdi123"))
			{
				isValidLogedInUser=true;
			}
			else if(etUserName.getText().toString().equals("123") && 
					etUserPassword.getText().toString().equals("123"))
			{
				isValidLogedInUser=true;
			}
			else if(etUserName.getText().toString().equals("imtiaz.rizvi@ericsson.com") && 
					etUserPassword.getText().toString().equals("imtiaz123"))
			{
				isValidLogedInUser=true;
			}
			
			if(isValidLogedInUser)
			{
				Intent intent = new Intent(this, HomeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
				
			}else{
				Toast.makeText(this, "Please Enter Valid User Name and Passcode", Toast.LENGTH_SHORT).show();
			}
				
		}
	}
}
