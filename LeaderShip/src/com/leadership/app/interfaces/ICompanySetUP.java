package com.leadership.app.interfaces;

import com.leadership.app.entities.CityRoot;
import com.leadership.app.entities.CompanyHolder;
import com.leadership.app.entities.CompanySetups;
import com.leadership.app.entities.UserInfo;

public interface ICompanySetUP {
	
	CompanySetups GetAllOperators();

	CompanyHolder GetCompanySetupByCompanyID(String companyID);

	CityRoot GetCity();

	UserInfo LoginAuthentication(String userId, String userPass);
}
