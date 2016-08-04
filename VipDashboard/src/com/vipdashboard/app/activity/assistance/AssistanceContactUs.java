package com.vipdashboard.app.activity.assistance;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.TextView;

import com.vipdashboard.app.R;
import com.vipdashboard.app.utils.CommonTask;

public class AssistanceContactUs extends Fragment {
	
	EditText etMessageBox, etSubject;
	TextView tvSendEmail, tvCallCustomerCare;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.assistance_contact_us, container, false);
         Initialization(rootView);
        return rootView;
    }

	private void Initialization(View rootView) {
		etMessageBox = (EditText) rootView.findViewById(R.id.etMessageBox);
		etSubject = (EditText) rootView.findViewById(R.id.etSubjectTextBox);
		tvSendEmail = (TextView) rootView.findViewById(R.id.tvSendEmail);
		CommonTask.makeLinkedTextview(getActivity(), tvSendEmail, tvSendEmail.getText().toString());
		tvCallCustomerCare = (TextView) rootView.findViewById(R.id.tvContactTOHotLine);
		CommonTask.makeLinkedTextview(getActivity(), tvCallCustomerCare, tvCallCustomerCare.getText().toString());
	} 

}
