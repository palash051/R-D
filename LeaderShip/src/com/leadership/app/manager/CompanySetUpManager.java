package com.leadership.app.manager;

import com.leadership.app.entities.CityRoot;
import com.leadership.app.entities.CompanyHolder;
import com.leadership.app.entities.CompanySetup;
import com.leadership.app.entities.CompanySetups;
import com.leadership.app.entities.UserInfo;
import com.leadership.app.entities.UserInfoRoot;
import com.leadership.app.interfaces.ICompanySetUP;
import com.leadership.app.utils.CommonURL;
import com.leadership.app.utils.CommonValues;
import com.leadership.app.utils.JSONfunctions;

public class CompanySetUpManager implements ICompanySetUP {

	@Override
	public CompanySetups GetAllOperators() {
		CompanySetups companySetups = (CompanySetups) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetAllOperators,CommonValues.getInstance().SelectedCompany.CompanyID),
						CompanySetups.class);
		if(CommonValues.getInstance().LoginUser.UserMode==3 && companySetups!=null && companySetups.companySetupList!=null){
			for (CompanySetup companySetup : companySetups.companySetupList) {				
				companySetup.CompanyName=companySetup.CompanyDescription;
			}
		}
		
		return companySetups;
	}

	@Override
	public CompanyHolder GetCompanySetupByCompanyID(String companyID) {
		CompanyHolder companyHolder= (CompanyHolder) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetCompanySetupByCompanyID,
								companyID),
						CompanyHolder.class);
		return companyHolder;
	}
	
	@Override
	public UserInfo LoginAuthentication(String userId,String userPass) {
		UserInfoRoot userInfoRoot= (UserInfoRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().LoginAuthentication,
								userId,userPass),
								UserInfoRoot.class);
		if(userInfoRoot!=null && userInfoRoot.UserInfo!=null)
			return userInfoRoot.UserInfo;
		return null;
	}
	
	@Override
	public CityRoot GetCity() {
		CityRoot cityRoot= (CityRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetCitySetups),
								CityRoot.class);
		return cityRoot;
	}

}
