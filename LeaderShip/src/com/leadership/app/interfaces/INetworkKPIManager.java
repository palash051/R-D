package com.leadership.app.interfaces;

import com.leadership.app.entities.FinancialDatas;
import com.leadership.app.entities.NetworkKPIs;

public interface INetworkKPIManager {
	
	NetworkKPIs GetAllNetworkKPIByType(String kpiType);

	FinancialDatas GetAllFinancialDataByType(String reportType);

}
