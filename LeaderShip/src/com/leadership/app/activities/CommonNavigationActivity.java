package com.leadership.app.activities;

import com.leadership.app.R;
import com.leadership.app.asynchronoustask.DownloadableAsyncTask;
import com.leadership.app.entities.CompanyHolder;
import com.leadership.app.entities.CompanySetup;
import com.leadership.app.interfaces.IAsynchronousTask;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.manager.CompanySetUpManager;
import com.leadership.app.utils.CommonConstraints;
import com.leadership.app.utils.CommonTask;
import com.leadership.app.utils.CommonValues;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class CommonNavigationActivity extends Activity implements
		OnClickListener, IAsynchronousTask {

	RelativeLayout rlOperatorNetworkKpiVoice, rlOperatorNetworkKpiData;

	ImageView ivzain, ivZainarbia, ivZainSudan, ivZainBahrain, ivstc, ivmobily,
			ivkorek, ivbatelco, ivwataniya, ivTurkSell, ivavea, ivvodafone,
			ivetisalat;

	String navigationString = "";

	DownloadableAsyncTask downloadableAsyncTask;

	String companyID = "";
	ProgressDialog progress;
	ImageView ivSelectedInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		savedInstanceState = getIntent().getExtras();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_navigation);
		
		if (savedInstanceState != null
				&& savedInstanceState.containsKey("CommonNavigationStr")) {
			navigationString = savedInstanceState
					.getString("CommonNavigationStr");
		}
		Initialization();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		
		if (navigationString.equals("FinancialInfo")) {
			ivSelectedInfo.setImageResource(R.drawable.financial_info);
		} else if (navigationString.equals("NetworkInfo")) {
			ivSelectedInfo.setImageResource(R.drawable.network);
		}
		
	}

	private void Initialization() {
		ivZainarbia = (ImageView) findViewById(R.id.ivZainarbia);
		ivZainSudan = (ImageView) findViewById(R.id.ivZainSudan);
		ivZainBahrain = (ImageView) findViewById(R.id.ivZainBahrain);
		ivstc = (ImageView) findViewById(R.id.ivstc);
		ivmobily = (ImageView) findViewById(R.id.ivmobily);
		ivkorek = (ImageView) findViewById(R.id.ivkorek);
		ivbatelco = (ImageView) findViewById(R.id.ivbatelco);
		ivwataniya = (ImageView) findViewById(R.id.ivwataniya);
		ivTurkSell = (ImageView) findViewById(R.id.ivTurkSell);
		ivavea = (ImageView) findViewById(R.id.ivavea);
		ivvodafone = (ImageView) findViewById(R.id.ivvodafone);
		ivetisalat = (ImageView) findViewById(R.id.ivetisalat);
		ivzain= (ImageView) findViewById(R.id.ivzain);

		ivZainarbia.setOnClickListener(this);
		ivZainSudan.setOnClickListener(this);
		ivZainBahrain.setOnClickListener(this);
		ivstc.setOnClickListener(this);
		ivmobily.setOnClickListener(this);
		ivkorek.setOnClickListener(this);
		ivbatelco.setOnClickListener(this);
		ivwataniya.setOnClickListener(this);
		ivTurkSell.setOnClickListener(this);
		ivavea.setOnClickListener(this);
		ivvodafone.setOnClickListener(this);
		ivetisalat.setOnClickListener(this);
		ivzain.setOnClickListener(this);

		 ivSelectedInfo = (ImageView) findViewById(R.id.ivSelectedInfo);	

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	private void LoadInformation() {
		if (!CommonTask.isOnline(this)) {
			CommonTask.showMessage(this,"Network connection error.\nPlease check your internet connection.");
			return;
		}
		if (downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public Object doBackgroundPorcess() {
		ICompanySetUP companySetUP = new CompanySetUpManager();
		return companySetUP.GetCompanySetupByCompanyID(companyID);
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if (data != null) {
			CompanyHolder companyHolder = (CompanyHolder) data;
			CommonValues.getInstance().SelectedCompany = (CompanySetup) companyHolder.companySetup;
			if (navigationString.equals("FinancialInfo")) {
				Intent intent = new Intent(this, LeadershipFinanceActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			} else if (navigationString.equals("NetworkInfo")) {				
				Intent intent = new Intent(this, LeadershipNetworkKPIVoiceDataActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(intent);
			}
			
		}
		else
		{
		Toast.makeText(this, "Operator information is not available", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.ivZainarbia||
				view.getId() == R.id.ivzain) {
			companyID = String.valueOf(CommonConstraints.ZAIN_IRAQ);
			LoadInformation();

		} else if (view.getId() == R.id.ivZainSudan) {
			companyID = String.valueOf(CommonConstraints.ZAIN_SUDAN);
			LoadInformation();
		} else if (view.getId() == R.id.ivZainBahrain) {
			companyID = String.valueOf(CommonConstraints.ZAIN_BAHRAIN);
			LoadInformation();
		} else if (view.getId() == R.id.ivstc) {
			companyID = String.valueOf(CommonConstraints.STC);
			LoadInformation();
		} else if (view.getId() == R.id.ivmobily) {
			companyID = String.valueOf(CommonConstraints.MOBILY);
			LoadInformation();
		} else if (view.getId() == R.id.ivkorek) {
			companyID = String.valueOf(CommonConstraints.KOREK);
			LoadInformation();
		} else if (view.getId() == R.id.ivbatelco) {
			companyID = String.valueOf(CommonConstraints.BATELCO);
			LoadInformation();
		} else if (view.getId() == R.id.ivwataniya) {
			companyID = String.valueOf(CommonConstraints.WATANYA_KUWAIT);
			LoadInformation();

		} else if (view.getId() == R.id.ivTurkSell) {
			companyID = String.valueOf(CommonConstraints.TURKCELL);
			LoadInformation();

		} else if (view.getId() == R.id.ivavea) {
			companyID = String.valueOf(CommonConstraints.AVEA);
			LoadInformation();

		} else if (view.getId() == R.id.ivvodafone) {
			companyID = String.valueOf(CommonConstraints.VODAFONE_EGYPT);
			LoadInformation();

		} else if (view.getId() == R.id.ivetisalat) {
			companyID = String.valueOf(CommonConstraints.ETISALAT_EGYPT);
			LoadInformation();
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

}
