package com.leadership.app.activities;

import com.leadership.app.R;
import com.leadership.app.utils.CommonTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class LeadershipCompareActivity extends Activity implements OnClickListener {
	
	RelativeLayout rlNetworkKPI,rlFinance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.leadership_compare);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		initializaiton();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private void initializaiton(){
		rlNetworkKPI = (RelativeLayout) findViewById(R.id.rlNetworkKPI);
		rlFinance = (RelativeLayout) findViewById(R.id.rlFinance);
		
		rlNetworkKPI.setOnClickListener(this);
		rlFinance.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if(view.getId() == R.id.rlNetworkKPI){
			Intent intent = new Intent(this, NetworkKpiCompare.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}else if(view.getId() == R.id.rlFinance){
			Intent intent = new Intent(this, DemoActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
	}

}
