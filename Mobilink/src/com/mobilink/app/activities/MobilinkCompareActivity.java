package com.mobilink.app.activities;

import com.mobilink.app.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

public class MobilinkCompareActivity extends Activity implements OnClickListener {
	
	RelativeLayout rlNetworkKPI,rlFinance;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobilink_compare);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
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
