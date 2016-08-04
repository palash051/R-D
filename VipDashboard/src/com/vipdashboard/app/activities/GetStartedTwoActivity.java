package com.vipdashboard.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;

public class GetStartedTwoActivity extends SherlockFragment implements
		OnClickListener {
	Button gpbtnsign, gpbtnjoin;
	ImageView getstartedtwo;

	Context context;
	public static boolean IsNeedtoSkipLogin = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.getstartedpagetwo, container,
				false);

		gpbtnsign = (Button) rootView.findViewById(R.id.gpbtnsign);
		gpbtnjoin = (Button) rootView.findViewById(R.id.gpbtnjoin);
		getstartedtwo = (ImageView) rootView.findViewById(R.id.getstartedtwo);
		gpbtnsign.setOnClickListener(this);
		gpbtnjoin.setOnClickListener(this);
		context = getActivity();

		if (IsNeedtoSkipLogin) {
			float scale = getResources().getDisplayMetrics().density;
			int dpAsPixels = (int) (60 * scale + 0.5f);

			// View positiveButton = findViewById(R.id.positiveButton);
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getstartedtwo
					.getLayoutParams();
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
			getstartedtwo.setLayoutParams(layoutParams);
			gpbtnsign.setVisibility(View.GONE);
			gpbtnjoin.setVisibility(View.GONE);

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
