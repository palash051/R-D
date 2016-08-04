package com.vipdashboard.app.activity.assistance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonTask;

public class AssistanceNetworkDoctor extends Fragment {
	
	ImageView imageView;
	TextView tvPerformance, tvInternet, tvMakeCall, tvCallDrop, tvConnectToInternet, tvGettingDisconnected,
			tvInternetSpeed, tvWrongBilling, tvPerformancePoor;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.assistance_network_doctor, container, false);
        Initialization(rootView);
        return rootView;
    }
	private void Initialization(View rootView) {
		imageView = (ImageView) rootView.findViewById(R.id.image);
		
		
		
	} 

}
