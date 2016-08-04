package com.vipdashboard.app.manager;

import java.util.ArrayList;

import com.vipdashboard.app.entities.NetworkAvailabilitys;
import com.vipdashboard.app.entities.NetworkDataTraffics;
import com.vipdashboard.app.entities.Statistics2Gs;
import com.vipdashboard.app.entities.Statistics3Gs;
import com.vipdashboard.app.entities.StatisticsLTEKPIs;
import com.vipdashboard.app.interfaces.INetworkManager;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class NetworkManager implements INetworkManager {

	@Override
	public ArrayList<Object> getComplesData() {
		ArrayList<Object> ComplexData=new ArrayList<Object>();
		
		NetworkAvailabilitys networkAvailabilitys = (NetworkAvailabilitys) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetNetworkAvailability), NetworkAvailabilitys.class);
		
		ComplexData.add(networkAvailabilitys);
		
		NetworkDataTraffics networkDataTraffics = (NetworkDataTraffics) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetNetworkDataTraffic), NetworkDataTraffics.class);
		ComplexData.add(networkDataTraffics);
		
		Statistics2Gs statistics2Gs=(Statistics2Gs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().Get2GByCompanyID,
						CommonValues.getInstance().CompanyId), Statistics2Gs.class);
		ComplexData.add(statistics2Gs);
		
		Statistics3Gs satistics3Gs=(Statistics3Gs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().Get3GByCompanyID,
						CommonValues.getInstance().CompanyId), Statistics3Gs.class);
		
		ComplexData.add(satistics3Gs);
		
		StatisticsLTEKPIs statisticsLTEKPIs= (StatisticsLTEKPIs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLTEByCompanyID,
						CommonValues.getInstance().CompanyId), StatisticsLTEKPIs.class);
		ComplexData.add(statisticsLTEKPIs);
		
		return ComplexData;
	}

}
