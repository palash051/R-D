package com.leadership.app.activities;

import com.leadership.app.R;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
import com.leadership.app.entities.UserInfo;
import com.leadership.app.interfaces.IAsynchronousTask;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.manager.CompanySetUpManager;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener ,IAsynchronousTask{
	
	EditText etUserName, etUserPassword;
	ImageView bLogin;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progress;
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
			if(etUserName.getText().toString().isEmpty()){
				Toast.makeText(this, "Please Enter Valid User Name", Toast.LENGTH_SHORT).show();
				return;
			}
			if(etUserPassword.getText().toString().isEmpty()){
				Toast.makeText(this, "Please Enter passcode", Toast.LENGTH_SHORT).show();
				return;
			}
			if (!CommonTask.isOnline(this)) {
				CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
				return;
			}
			if (downloadableAsyncTask != null)
				downloadableAsyncTask.cancel(true);
			downloadableAsyncTask = new DownloadableAsyncTask(this);
			downloadableAsyncTask.execute();
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
	public Object doBackgroundPorcess() {
		
		ICompanySetUP companySetUP = new CompanySetUpManager();
		return companySetUP.LoginAuthentication(etUserName.getText().toString(), etUserPassword.getText().toString());
	}

	@Override
	public void processDataAfterDownload(Object data) {
		
		if(data!=null){
			CommonValues.getInstance().LoginUser=(UserInfo) data;
			CommonValues.getInstance().LoginUser.UserMode=CommonValues.getInstance().LoginUser.UserRoleID;
			Intent intent = new Intent(this, HomeActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else{
			Toast.makeText(this, "Please Enter Valid User Name and Passcode", Toast.LENGTH_SHORT).show(); 
		}
	}
}
