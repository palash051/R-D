package com.vipdashboard.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;

public class GetStartedThreeActivity extends SherlockFragment implements
		OnClickListener {
	Button gpbtnsign, gpbtnjoin;
	ImageView getstartedthree;

	Context context;
	public static boolean IsNeedtoSkipLogin = false;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.getstartedpagethree,
				container, false);

		gpbtnsign = (Button) rootView.findViewById(R.id.gpbtnsign);
		gpbtnjoin = (Button) rootView.findViewById(R.id.gpbtnjoin);
		getstartedthree = (ImageView) rootView
				.findViewById(R.id.getstartedthree);

		gpbtnsign.setOnClickListener(this);
		gpbtnjoin.setOnClickListener(this);
		context = getActivity();

		if (IsNeedtoSkipLogin) {
			float scale = getResources().getDisplayMetrics().density;
			int dpAsPixels = (int) (60 * scale + 0.5f);

			// View positiveButton = findViewById(R.id.positiveButton);
			RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) getstartedthree
					.getLayoutParams();
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,
					RelativeLayout.TRUE);
			getstartedthree.setLayoutParams(layoutParams);
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