package com.vipdashboard.app.manager;

import com.vipdashboard.app.entities.DailyKPIs;
import com.vipdashboard.app.interfaces.IReportManager;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.JSONfunctions;

public class ReportManager implements IReportManager{

	@Override
	public DailyKPIs getDailyKPI() {
		return (DailyKPIs) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetKPIReport), DailyKPIs.class);
	}

}
