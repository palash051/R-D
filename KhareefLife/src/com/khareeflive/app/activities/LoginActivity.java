package com.khareeflive.app.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import com.khareeflive.app.R;
import com.khareeflive.app.asynchronoustask.LoginAuthenticationTask;
import com.khareeflive.app.base.KhareefLiveApplication;
import com.khareeflive.app.base.KhareefLiveService;
import com.khareeflive.app.entities.UserInformation;
import com.khareeflive.app.manager.UserManager;
import com.khareeflive.app.utils.CommonValues;

public class LoginActivity extends Activity implements OnClickListener {

	public EditText etUserName,etUserPassword;
	Button bLogin;
	ProgressBar pbLogin;	
	
	LoginAuthenticationTask loginAuthenticationTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		initializeControls();		
		//CommonValues.getInstance().lastWarMessageTime="2013-08-29 10:00:06";
	}
	private void initializeControls() {
		etUserName=(EditText)findViewById(R.id.etUserName);
		etUserPassword=(EditText)findViewById(R.id.etUserPassword);
		pbLogin=(ProgressBar)findViewById(R.id.pbLogin);
		bLogin=(Button)findViewById(R.id.bLogin);
		bLogin.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		int id=v.getId();
		
		if(id==R.id.bLogin){
			if (loginAuthenticationTask != null) {
				loginAuthenticationTask.cancel(true);
			}			
			loginAuthenticationTask = new LoginAuthenticationTask(this);
			loginAuthenticationTask.execute();
		}
	}
	
	public boolean isLoginSuccess(){
		return UserManager.isUserAuthenticated(etUserName.getText().toString(), etUserPassword.getText().toString());
	}
	
	public void doLogin(){
		CommonValues.getInstance().LoginUser=new UserInformation();
		CommonValues.getInstance().LoginUser.userName=etUserName.getText().toString();
		CommonValues.getInstance().LoginUser.userPassword=etUserPassword.getText().toString();
/*		Intent intent = new Intent(this,MenuActivity.class);	*/
		Intent intent = new Intent(this,SplashActivity.class);		
		startActivity(intent);	
		startService(new Intent(this, KhareefLiveService.class));
	}
	
	public void showLoader(){
		pbLogin.setVisibility(View.VISIBLE);
	}
	public void hideLoader(){
		pbLogin.setVisibility(View.GONE);
	}
	@Override
	protected void onPause() {
		KhareefLiveApplication.activityPaused();
		super.onPause();
	}

	@Override
	protected void onResume() {
		KhareefLiveApplication.activityResumed();
		super.onResume();
	}
}
