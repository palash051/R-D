package com.vipdashboard.app.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.model.LatLng;
import com.vipdashboard.app.R;
import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.ReportProblemAndBadExperience;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ReportProblemAndBadExperienceAdapter extends ArrayAdapter<ReportProblemAndBadExperience>{

	private Context context;
	private ArrayList<ReportProblemAndBadExperience> reportProblemAndBadExperiencesList;
	private ArrayList<ReportProblemAndBadExperience> reportProblemAndBadExperiencesFilterList;
	private ReportProblemAndBadExperience reportProblemAndBadExperience;
	
	public ReportProblemAndBadExperienceAdapter(Context _context,
			int textViewResourceId, ArrayList<ReportProblemAndBadExperience> _objects) {
		super(_context, textViewResourceId, _objects);
		context = _context;
		reportProblemAndBadExperiencesList = _objects;
		reportProblemAndBadExperiencesFilterList = new ArrayList<ReportProblemAndBadExperience>();
		reportProblemAndBadExperiencesFilterList.addAll(reportProblemAndBadExperiencesList);
	}
	
	public int getCount() {
		return reportProblemAndBadExperiencesList.size();
	}
	
	public ReportProblemAndBadExperience getLastItem(){
		return reportProblemAndBadExperiencesList.get(reportProblemAndBadExperiencesList.size()-1);
	}
	public void addItem(ReportProblemAndBadExperience item){
		reportProblemAndBadExperiencesList.add(item);
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		reportProblemAndBadExperience = reportProblemAndBadExperiencesList.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View userItemView = inflater.inflate(R.layout.reportproblemandbadexperience_item, null);
		
		TextView problem = (TextView) userItemView.findViewById(R.id.tvReportproblemandbadexperienceText);
		TextView location = (TextView) userItemView.findViewById(R.id.tvReportproblemandbadexperienceLocation);
		TextView time = (TextView) userItemView.findViewById(R.id.tvReportproblemandbadexperienceTime);
		TextView comments = (TextView) userItemView.findViewById(R.id.tvReportproblemandbadexperienceComments);
		
		problem.setText(reportProblemAndBadExperience.Problem);
		
		String dateTime = (String) DateUtils.getRelativeTimeSpanString(
				CommonTask.convertDatetoLong(reportProblemAndBadExperience.ProblemTime), new Date().getTime(), 0);
		
		
		time.setText(dateTime);
		
		comments.setText(reportProblemAndBadExperience.Comment);
		
		 location.setText(reportProblemAndBadExperience.LocationName);
		
		userItemView.setTag(reportProblemAndBadExperience);
		return userItemView;
	}
	
	public void Filter(String charText){
		charText = charText.toLowerCase(Locale.getDefault());
		reportProblemAndBadExperiencesList.clear();
		if (charText.length() == 0) {
			reportProblemAndBadExperiencesList.addAll(reportProblemAndBadExperiencesFilterList);
		} else {
			for (ReportProblemAndBadExperience reportProblemAndBadExperience : reportProblemAndBadExperiencesFilterList) {				
				if(reportProblemAndBadExperience.Problem.toLowerCase(Locale.getDefault()).contains(charText)){
					reportProblemAndBadExperiencesList.add(reportProblemAndBadExperience);
				}else if(reportProblemAndBadExperience.Comment.toLowerCase(Locale.getDefault()).contains(charText)){
					reportProblemAndBadExperiencesList.add(reportProblemAndBadExperience);
				}
			}
		}
		notifyDataSetChanged();
	}

}
