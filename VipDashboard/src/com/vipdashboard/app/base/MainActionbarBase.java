package com.vipdashboard.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
//import com.mynet.app.vipDashboard.VIPD_ServiceUsages;
import com.vipdashboard.app.R;
import com.vipdashboard.app.activities.CareIMActivity;
import com.vipdashboard.app.activities.ExperinceLiveActivity;
import com.vipdashboard.app.activities.GetStarted;
import com.vipdashboard.app.activities.GetStartedOneActivity;
import com.vipdashboard.app.activities.HomeActivity;
import com.vipdashboard.app.activities.LoginActivity;
import com.vipdashboard.app.activities.MyProfileActivity;
import com.vipdashboard.app.activities.VIPDEngineeringModeActivity;
import com.vipdashboard.app.activities.VIPDManualSyncActivity;
import com.vipdashboard.app.activities.VIPDMapsActivity;
import com.vipdashboard.app.activities.VIPD_SettingActivity;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.fragments.CollaborationMainFragment;
import com.vipdashboard.app.utils.CommonTask;

public class MainActionbarBase extends SherlockFragmentActivity {
	public ActionBar mSupportActionBar;
	DownloadableAsyncTask downloadableAsyncTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setTheme(R.style.AppTheme);
		super.onCreate(savedInstanceState);
		createMenuBar();
	}

	private void createMenuBar() {
		mSupportActionBar = getSupportActionBar();
		mSupportActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.tool_bar));
		mSupportActionBar.setTitle("CARE");
		
		/*
		 * if(CommonValues.getInstance().LoginUser != null){
		 * mSupportActionBar.setSubtitle("Welcome " +
		 * CommonValues.getInstance().LoginUser.Name.toString()); }
		 */
		mSupportActionBar.setDisplayShowTitleEnabled(true);
		mSupportActionBar.setIcon(R.drawable.application_logo);
		// mSupportActionBar.setDisplayShowHomeEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		com.actionbarsherlock.view.MenuInflater menuInflater = getSupportMenuInflater();
		menuInflater.inflate(R.menu.menuhome, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			super.onBackPressed();
		}
		/*
		 * else if (item.getItemId() == R.id.menu_homesubmenu) { Intent intent =
		 * new Intent(this, HomeActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		 * startActivity(intent); }
		 */
		/*else if (item.getItemId() == R.id.menu_upto_date) {
			Intent intent = new Intent(this, VIPDMapsActivity.class);
			intent.putExtra("isVisibleRequired", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		 else if (item.getItemId() == R.id.menu_settings) {
				Intent intent = new Intent(this, VIPD_SettingActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
		 else if (item.getItemId() == R.id.menu_engineering_mode) {
				Intent intent = new Intent(this, VIPDEngineeringModeActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			else if (item.getItemId() == R.id.menu_collaboration) {
				Intent intent = new Intent(this, CareIMActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}*/
		
		else if (item.getItemId() == R.id.menu_my_profile) {
			if (!CommonTask.isOnline(this)) {
				if (!isFinishing()) 
				{
				Toast.makeText(
						this,
						"No Internet Connection.\nPlease enable your connection first",
						Toast.LENGTH_SHORT).show();
				return true;
				}
			}
			
			Intent intent = new Intent(this, MyProfileActivity.class);
			intent.putExtra("isVisibleRequired", true);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		
		/*
		 * else if(item.getItemId() == R.id.menu_dashboard){
		 * com.vipdashboard.app.activities.DailyKPIActivity.name = "Dashboard";
		 * com.vipdashboard.app.activities.DailyKPIActivity.URL =
		 * "http://120.146.188.232:9001?uid="
		 * +CommonValues.getInstance().LoginUser
		 * .Mobile+"&pwd="+CommonValues.getInstance().LoginUser.Password+"";
		 * Intent intent = new
		 * Intent(this,com.vipdashboard.app.activities.DailyKPIActivity.class);
		 * intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 * startActivity(intent); }
		 */
		else if (item.getItemId() == R.id.manual_sync_menu) {
			Intent intent = new Intent(this, VIPDManualSyncActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		else if (item.getItemId() == R.id.menu_applicaton_features) {
		
			Intent intent = new Intent(this, GetStarted.class);
			GetStarted.IsNeedtoskipLogin=true;
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
		
		

		
		/*else if (item.getItemId() == R.id.menu_feed) {
			Intent intent = new Intent(this, ExperinceLiveActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}*/
		
		
		/*else if (item.getItemId() == R.id.menu_servicetest) {
			Intent intent = new Intent(this, VIPD_ServiceTestActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else if (item.getItemId() == R.id.menu_speedtest) {
			Intent intent = new Intent(this, VIPD_SpeedTestActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}*/

		return true;
	}

	public void backTohome() {
		Intent intent = new Intent(this, HomeActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);
	}

	public void LoadDataLogOut() {
		Intent intent = new Intent(MainActionbarBase.this, LoginActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		LoginActivity.isCallFromLogout = true;
		startActivity(intent);
	}
}
