package com.mobilink.app.manager;

import com.mobilink.app.entities.CityRoot;
import com.mobilink.app.entities.CompanyHolder;
import com.mobilink.app.entities.CompanySetups;
import com.mobilink.app.interfaces.ICompanySetUP;
import com.mobilink.app.utils.CommonURL;
import com.mobilink.app.utils.CommonValues;
import com.mobilink.app.utils.JSONfunctions;

public class CompanySetUpManager implements ICompanySetUP {

	@Override
	public CompanySetups GetAllOperators() {
		CompanySetups companySetups = (CompanySetups) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetAllOperators,CommonValues.getInstance().SelectedCompany.CompanyID),
						CompanySetups.class);
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
	public CityRoot GetCity() {
		CityRoot cityRoot= (CityRoot) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetCitySetups),
								CityRoot.class);
		return cityRoot;
	}

}
