package com.mobilink.app.interfaces;

import com.mobilink.app.entities.CityRoot;
import com.mobilink.app.entities.CompanyHolder;
import com.mobilink.app.entities.CompanySetups;

public interface ICompanySetUP {
	
	CompanySetups GetAllOperators();

	CompanyHolder GetCompanySetupByCompanyID(String companyID);

	CityRoot GetCity();
}
