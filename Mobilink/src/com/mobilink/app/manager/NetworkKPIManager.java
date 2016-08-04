package com.mobilink.app.manager;

import com.mobilink.app.entities.FinancialDatas;
import com.mobilink.app.entities.KPICustoms;
import com.mobilink.app.entities.MobilinkZones;
import com.mobilink.app.entities.NetworkKPIs;
import com.mobilink.app.interfaces.INetworkKPIManager;
import com.mobilink.app.utils.CommonURL;
import com.mobilink.app.utils.CommonValues;
import com.mobilink.app.utils.JSONfunctions;

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
	
	@Override
	public KPICustoms GetMLNetworkKPI(long selectedDate) {
		KPICustoms companySetups = (KPICustoms) JSONfunctions
				.retrieveDataFromStream(String.format(CommonURL.getInstance().GetMLNetworkKPI,selectedDate),KPICustoms.class);
		return companySetups;
	}
	
	@Override
	public KPICustoms GetMLRegionWiseKPI(int regionid,long selectedDate) {
		KPICustoms companySetups = (KPICustoms) JSONfunctions
				.retrieveDataFromStream(String.format(CommonURL.getInstance().GetMLRegionWiseKPI,regionid,selectedDate),KPICustoms.class);
		return companySetups;
	}
	
	@Override
	public KPICustoms GetMLZoneWiseKPI(int zoneid,long selectedDate) {
		KPICustoms companySetups = (KPICustoms) JSONfunctions
				.retrieveDataFromStream(String.format(CommonURL.getInstance().GetMLZoneKPI,zoneid,selectedDate),KPICustoms.class);
		return companySetups;
	}
	
	@Override
	public MobilinkZones MobilinkZones() {
		return (MobilinkZones) JSONfunctions
				.retrieveDataFromStream(String.format(CommonURL.getInstance().GetallZone),MobilinkZones.class);
	}
}
