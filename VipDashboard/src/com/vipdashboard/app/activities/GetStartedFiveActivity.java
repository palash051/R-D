package com.vipdashboard.app.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;
import com.vipdashboard.app.R;

public class GetStartedFiveActivity extends SherlockFragment implements OnClickListener{
Button gpbtnsign,gpbtnjoin;
	
	Context context;
	public static boolean IsNeedtoSkipLogin=false;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.getstartedpagefive, container, false);
        
        gpbtnsign = (Button)rootView.findViewById(R.id.gpbtnsign);
        gpbtnjoin= (Button)rootView.findViewById(R.id.gpbtnjoin);
        
        gpbtnsign.setOnClickListener(this);
        gpbtnjoin.setOnClickListener(this);
		context=getActivity();
		
		if(IsNeedtoSkipLogin)
		{
			gpbtnsign.setVisibility(View.GONE);
			gpbtnjoin.setVisibility(View.GONE);
		}
		
        return rootView;
    }
	
	@Override
	public void onClick(View v) {
		int id = v.getId();
		
		if (id == R.id.gpbtnsign) {
			Intent intent = new Intent(context , LoginWithPasswordActivity.class);
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
