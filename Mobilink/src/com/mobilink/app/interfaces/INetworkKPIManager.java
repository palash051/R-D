package com.mobilink.app.interfaces;

import com.mobilink.app.entities.FinancialDatas;
import com.mobilink.app.entities.KPICustoms;
import com.mobilink.app.entities.MobilinkZones;
import com.mobilink.app.entities.NetworkKPIs;

public interface INetworkKPIManager {
	
	NetworkKPIs GetAllNetworkKPIByType(String kpiType);

	FinancialDatas GetAllFinancialDataByType(String reportType);


	KPICustoms GetMLRegionWiseKPI(int regionid,long selectedDate);


	MobilinkZones MobilinkZones();

	KPICustoms GetMLZoneWiseKPI(int zoneid,long selectedDate);

	KPICustoms GetMLNetworkKPI(long selectedDate);

}
