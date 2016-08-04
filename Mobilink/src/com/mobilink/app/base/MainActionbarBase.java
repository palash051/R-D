package com.mobilink.app.base;

import android.os.Bundle;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.mobilink.app.R;


public class MainActionbarBase extends SherlockFragmentActivity {
	public ActionBar mSupportActionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme);
		super.onCreate(savedInstanceState);
		createMenuBar();

	}

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		//mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.tool_bar));
		// mSupportActionBar.setTitle("MyNet");
		/*
		 * if(CommonValues.getInstance().LoginUser != null){
		 * mSupportActionBar.setSubtitle("Welcome " +
		 * CommonValues.getInstance().LoginUser.Name.toString()); }
		 */
		mSupportActionBar.setDisplayShowTitleEnabled(false);
		mSupportActionBar.setDisplayShowHomeEnabled(false);
		mSupportActionBar.setIcon(null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		//menuInflater.inflate(R.menu.menuhome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		} 
		return true;
	}

	public void backTohome() {
		/*Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);*/
	}

	public void LoadDataLogOut() {		
		/*Intent intent = new Intent(MainActionbarBase.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		LoginActivity.isCallFromLogout=true;
		startActivity(intent);*/
	}
}
