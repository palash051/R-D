package com.vipdashboard.app.activity.assistance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.widget.LoginButton;
import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonTask;

public class AssistanceAskaFriend extends Fragment {
	
	TextView tvBadExperienceHistry;
	EditText etMessageBox, email;
	ImageView linkedIN, googlePlus;
	LoginButton facebook;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.assistance_aska_friend, container, false);
         Initialization(rootView);
        return rootView;
    }
	private void Initialization(View rootView) {
		/*tvBadExperienceHistry = (TextView) rootView.findViewById(R.id.tvText2);
		CommonTask.makeLinkedTextview(getActivity(), tvBadExperienceHistry, tvBadExperienceHistry.getText().toString());
		etMessageBox = (EditText) rootView.findViewById(R.id.etMessageBox);
		email = (EditText) rootView.findViewById(R.id.etEmailTextbox);
		linkedIN = (ImageView) rootView.findViewById(R.id.linkedIn);
		googlePlus = (ImageView) rootView.findViewById(R.id.google_plus);
		facebook = (LoginButton) rootView.findViewById(R.id.authButton);*/
	} 

}
