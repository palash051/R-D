package com.vipdashboard.app.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

public class GetStartedOneActivity extends SherlockFragment implements
		OnClickListener {
	Button gpbtnsign, gpbtnjoin;
	TextView tvOne;

	Context context;
	AssetManager mgr;
	
	ImageView getstartedone;

	public static boolean IsNeedtoSkipLogin = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.getstartedpageone, container,
				false);

		gpbtnsign = (Button) rootView.findViewById(R.id.gpbtnsign);
		gpbtnjoin = (Button) rootView.findViewById(R.id.gpbtnjoin);
		getstartedone = (ImageView) rootView.findViewById(R.id.getstartedone);
//		tvOne = (TextView) rootView.findViewById(R.id.textView1);
//		Typeface font = Typeface.createFromAsset(mgr, "tempsitc.ttf");
//		tvOne.setTypeface(font);

		gpbtnsign.setOnClickListener(this);
		gpbtnjoin.setOnClickListener(this);
		context = getActivity();

		if (IsNeedtoSkipLogin) {
			float scale = getResources().getDisplayMetrics().density;
		    int dpAsPixels = (int) (60*scale + 0.5f);
			
			//View positiveButton = findViewById(R.id.positiveButton);
			RelativeLayout.LayoutParams layoutParams = 
			    (RelativeLayout.LayoutParams)getstartedone.getLayoutParams();
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			getstartedone.setLayoutParams(layoutParams);
			
			gpbtnsign.setVisibility(View.GONE);
			gpbtnjoin.setVisibility(View.GONE);
		//	getstartedone.setPadding(0, dpAsPixels, 0, 0);
			
		}

		return rootView;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id == R.id.gpbtnsign) {
			Intent intent = new Intent(context, LoginWithPasswordActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}

		if (id == R.id.gpbtnjoin) {
			Intent intent = new Intent(context, LoginActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			// intent.putExtra("LOGIN", true);
			startActivity(intent);
		}
	}

}
