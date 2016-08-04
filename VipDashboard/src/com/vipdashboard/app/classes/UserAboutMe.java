package com.vipdashboard.app.classes;

import java.util.ArrayList;

import com.vipdashboard.app.R;
import com.vipdashboard.app.asynchronoustask.DownloadableAsyncTask;
import com.vipdashboard.app.base.MyNetDatabase;
import com.vipdashboard.app.entities.FacebokPerson;
import com.vipdashboard.app.entities.FacebookPersons;
import com.vipdashboard.app.entities.FacebookQualificationExperience;
import com.vipdashboard.app.entities.User;
import com.vipdashboard.app.interfaces.IAsynchronousTask;
import com.vipdashboard.app.interfaces.IUserManager;
import com.vipdashboard.app.manager.UserManager;
import com.vipdashboard.app.utils.CommonTask;
import com.vipdashboard.app.utils.CommonValues;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class UserAboutMe implements IAsynchronousTask {
	
	Context context;
	TextView tvBirthDayInfo,tvHomtownInfo,tvRelationshipStatusInfo,tvLookingForInfo,tvFacebookLinksInfo,tvEducationInfo,tvWorkInfo;
	RelativeLayout rlEducationTitle;
	RelativeLayout rlWorkTitle;
	LinearLayout linearLayoutEducation, LinearLayoutWork;
	DownloadableAsyncTask downloadableAsyncTask;
	ProgressDialog progressDialog;
	TextView education,profession;
	RelativeLayout rlAboutMeBasicInfo,rlAboutMeFamily;
	TextView tvBioHeader,
	tvLinks,
	tvEducation,
	tvWorkHeader;
	ScrollView svBasicInfo;
	LinearLayout llFamilyMember; 
	ListView lvFamilyMemberList;
	TextView tvFamilyFooter;
	TextView tvAboutMeBasicInfo, tvAboutMeFamily;
	public static int SHOW_BASIC_INFO = 0;
	public static int SHOW_FAMILY_MEMBER = 1;
	public static int selectedDownloadProcess = SHOW_BASIC_INFO;
	
	public UserAboutMe(Context _context, TextView _tvBirthDayInfo, TextView _tvHomtownInfo, TextView _tvRelationshipStatusInfo, 
			TextView _tvLookingForInfo, TextView _tvFacebookLinksInfo, TextView _tvEducationInfo, TextView _tvWorkInfo, 
			RelativeLayout _rlEducationTitle, RelativeLayout _rlWorkTitle,
			TextView _tvBioHeader,
			TextView _tvLinks,
			TextView _tvEducation,
			TextView _tvWorkHeader, 
			RelativeLayout _rlAboutMeBasicInfo, 
			RelativeLayout _rlAboutMeFamily, 
			TextView _tvAboutMeBasicInfo, TextView _tvAboutMeFamily, 
			ScrollView _svBasicInfo, 
			LinearLayout _llFamilyMember, 
			ListView _lvFamilyMemberList, 
			TextView _tvFamilyFooter) {
		context = _context;
		tvBirthDayInfo = _tvBirthDayInfo;
		tvHomtownInfo = _tvHomtownInfo;
		tvRelationshipStatusInfo = _tvRelationshipStatusInfo;
		tvLookingForInfo = _tvLookingForInfo;
		tvFacebookLinksInfo = _tvFacebookLinksInfo;
		tvEducationInfo = _tvEducationInfo;
		tvWorkInfo = _tvWorkInfo;
		rlEducationTitle = _rlEducationTitle;
		rlWorkTitle = _rlWorkTitle;
		
		tvBioHeader=_tvBioHeader;
		tvLinks=_tvLinks;
		tvEducation=_tvEducation;
		tvWorkHeader=_tvWorkHeader;
		tvAboutMeBasicInfo = _tvAboutMeBasicInfo; 
		tvAboutMeFamily = _tvAboutMeFamily;
		rlAboutMeBasicInfo = _rlAboutMeBasicInfo;
		rlAboutMeFamily = _rlAboutMeFamily;
		
		svBasicInfo = _svBasicInfo;
		llFamilyMember = _llFamilyMember;
		lvFamilyMemberList = _lvFamilyMemberList;
		tvFamilyFooter = _tvFamilyFooter;
	}
	
	public void showAboutMe(){	
		rlEducationTitle.removeAllViews();
		rlWorkTitle.removeAllViews();
		
		linearLayoutEducation = new LinearLayout(context);
		linearLayoutEducation.setOrientation(LinearLayout.VERTICAL);
		LinearLayoutWork = new LinearLayout(context);
		LinearLayoutWork.setOrientation(LinearLayout.VERTICAL);
		MyNetDatabase database = new MyNetDatabase(context);
		database.open();			
		FacebokPerson facebokPerson = database.getFacebokPerson();
		ArrayList<FacebookQualificationExperience> facebookQualificationExperienceList = database.getFacebook_Qualification_Experience();
		database.close();
		String UserContactName= CommonTask.getContentName(context,
				CommonValues.getInstance().LoginUser.FullName).toUpperCase();
		if(!UserContactName.equals(""))
		{
			tvBioHeader.setText(UserContactName+"'s Bio");
		}
		else
		{	
		tvBioHeader.setText(String.valueOf(CommonValues.getInstance().LoginUser.FullName)+"'s Bio");
		}
		if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
			if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
				tvBioHeader.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name+"'s Bio");
		}else{
			if(facebokPerson != null && facebokPerson.Name != null){
				tvBioHeader.setText(facebokPerson.Name+"'s Bio");
			}
		}
		
		try{
			
			User user = CommonValues.getInstance().LoginUser;
				
			
			if(facebokPerson.DateOfBirth != null)
				tvBirthDayInfo.setText(CommonTask.convertJsonDateToDateofBirth(facebokPerson.DateOfBirth.toString()));
			if(facebokPerson.Pages != null)
				tvHomtownInfo.setText(facebokPerson.Pages.toString());
			if(facebokPerson.Relationship_Status != null)
				tvRelationshipStatusInfo.setText(facebokPerson.Relationship_Status.toString());
			
			for(int i=0;i<facebookQualificationExperienceList.size();i++){
				if(facebookQualificationExperienceList.get(i).QualificationExperienceType.
						toString().equals("Education")){
					education = new TextView(context);
					education
					.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					education.setTextColor(context.getResources().getColor(R.color.drop_down_back));
					education.setTextSize(13);
					education.setPadding(0, 5, 5, 5);
					education.setText(facebookQualificationExperienceList.get(i).QualificationExperience.
							toString());						
					linearLayoutEducation.addView(education);
				}
			}
			rlEducationTitle.addView(linearLayoutEducation);
			for(int i=0;i<facebookQualificationExperienceList.size();i++){
				if(facebookQualificationExperienceList.get(i).QualificationExperienceType.
						toString().equals("Profession")){
					profession = new TextView(context);
					profession
					.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					profession.setTextColor(context.getResources().getColor(R.color.drop_down_back));
					profession.setTextSize(13);
					profession.setPadding(0, 5, 5, 5);
					profession.setText(facebookQualificationExperienceList.get(i).QualificationExperience.toString());
					LinearLayoutWork.addView(profession);
				}
			}
			rlWorkTitle.addView(LinearLayoutWork);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void LoadInformation() {
		if(downloadableAsyncTask != null)
			downloadableAsyncTask.cancel(true);
		downloadableAsyncTask = new DownloadableAsyncTask(this);
		downloadableAsyncTask.execute();
	}

	@Override
	public void showProgressLoader() {
		progressDialog = new ProgressDialog(context,ProgressDialog.THEME_HOLO_LIGHT);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Please Wait...");
		progressDialog.show();
	}

	@Override
	public void hideProgressLoader() {
		progressDialog.dismiss();
	}

	@Override
	public Object doBackgroundPorcess() {
		IUserManager userManager = new UserManager();
			//return userManager.GetFacebookInfoByUserNumber(String.valueOf(CommonValues.getInstance().LoginUser.UserNumber));
		
		return null;
	}

	@Override
	public void processDataAfterDownload(Object data) {
		if(data != null){
			if(selectedDownloadProcess == SHOW_BASIC_INFO){
				try{
					rlEducationTitle.removeAllViews();
					rlWorkTitle.removeAllViews();
					
					linearLayoutEducation = new LinearLayout(context);
					linearLayoutEducation.setOrientation(LinearLayout.VERTICAL);
					LinearLayoutWork = new LinearLayout(context);
					LinearLayoutWork.setOrientation(LinearLayout.VERTICAL);
					
					User user = (User) data;
					
					tvBioHeader.setText(user.Name+"'s Bio");
					
					if(CommonValues.getInstance().LoginUser.Facebook_Person != null){
						if(CommonValues.getInstance().LoginUser.Facebook_Person.Name != null)
							tvBioHeader.setText(CommonValues.getInstance().LoginUser.Facebook_Person.Name+"'s Bio");
					}
						
					
					if(user.Facebook_Person.DateOfBirth != null)
						tvBirthDayInfo.setText(CommonTask.convertJsonDateToDateofBirth(user.Facebook_Person.DateOfBirth.toString()));
					if(user.Facebook_Person.FacebookContact.size()>0)
						tvHomtownInfo.setText(user.Facebook_Person.FacebookContact.get(0).Country.toString());
					if(user.Facebook_Person.Relationship_Status != null)
						tvRelationshipStatusInfo.setText(user.Facebook_Person.Relationship_Status.toString());
					
					for(int i=0;i<user.Facebook_Person.FacebookQualificationExperience.size();i++){
						if(user.Facebook_Person.FacebookQualificationExperience.get(i).QualificationExperienceType.
								toString().equals("Education")){
							education = new TextView(context);
							education
							.setLayoutParams(new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
							education.setTextColor(context.getResources().getColor(R.color.bottom_hometext_color));
							education.setTextSize(13);
							education.setPadding(0, 5, 5, 5);
							education.setText(user.Facebook_Person.FacebookQualificationExperience.get(i).QualificationExperience.
									toString());						
							linearLayoutEducation.addView(education);
						}
					}
					rlEducationTitle.addView(linearLayoutEducation);
					for(int i=0;i<user.Facebook_Person.FacebookQualificationExperience.size();i++){
						if(user.Facebook_Person.FacebookQualificationExperience.get(i).QualificationExperienceType.
								toString().equals("Profession")){
							profession = new TextView(context);
							profession
							.setLayoutParams(new LayoutParams(
									LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT));
							profession.setTextColor(context.getResources().getColor(R.color.bottom_hometext_color));
							profession.setTextSize(13);
							profession.setPadding(0, 5, 5, 5);
							profession.setText(user.Facebook_Person.FacebookQualificationExperience.get(i).QualificationExperience.toString());
							LinearLayoutWork.addView(profession);
						}
					}
					rlWorkTitle.addView(LinearLayoutWork);
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
