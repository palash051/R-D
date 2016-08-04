package com.khareeflive.app.asynchronoustask;
import com.khareeflive.app.activities.LoginActivity;
import android.os.AsyncTask;
import android.widget.Toast;

public class LoginAuthenticationTask extends AsyncTask<Void, Void, Boolean>{
	LoginActivity loginActivity;
	public LoginAuthenticationTask(LoginActivity _loginActivity) {
		loginActivity = _loginActivity;
	}
	@Override
	protected void onPreExecute() {
		loginActivity.showLoader();
	}
	@Override
	protected Boolean doInBackground(Void... arg0) {		
		return loginActivity.isLoginSuccess();
	}
	@Override
	protected void onPostExecute(Boolean result) {
		loginActivity.hideLoader();
		if(result){
			loginActivity.doLogin();
		}else{
			Toast.makeText(loginActivity, "Invalid User or Password", Toast.LENGTH_LONG).show();
		}
	}
}
