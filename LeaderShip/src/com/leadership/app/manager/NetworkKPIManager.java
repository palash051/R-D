package com.leadership.app.manager;

import com.leadership.app.entities.FinancialDatas;
import com.leadership.app.entities.NetworkKPIs;
import com.leadership.app.interfaces.INetworkKPIManager;
import com.leadership.app.utils.CommonURL;
import com.leadership.app.utils.CommonValues;
import com.leadership.app.utils.JSONfunctions;

public class NetworkKPIManager implements INetworkKPIManager {
	@Override
	public NetworkKPIs GetAllNetworkKPIByType(String kpiType) {
		NetworkKPIs companySetups = (NetworkKPIs) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetAllNetworkKPIByType,CommonValues.getInstance().SelectedCompany.CompanyID,kpiType),NetworkKPIs.class);
		return companySetups;
	}
	
	@Override
	public FinancialDatas GetAllFinancialDataByType(String reportType) {
		FinancialDatas companySetups = (FinancialDatas) JSONfunctions
				.retrieveDataFromStream(
						String.format(CommonURL.getInstance().GetAllFinancialDataByType,CommonValues.getInstance().SelectedCompany.CompanyID,reportType),FinancialDatas.class);
		return companySetups;
	}
}
