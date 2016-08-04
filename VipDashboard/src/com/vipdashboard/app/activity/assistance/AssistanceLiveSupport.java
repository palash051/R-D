package com.vipdashboard.app.activity.assistance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vipdashboard.app.R;

public class AssistanceLiveSupport extends Fragment {
	
	EditText etMessageBox;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.assistance_live_support, container, false);
         Initilization(rootView);
        return rootView;
    }
	private void Initilization(View rootView) {
		etMessageBox = (EditText) rootView.findViewById(R.id.etChatInCustomarCare);
	} 

}
