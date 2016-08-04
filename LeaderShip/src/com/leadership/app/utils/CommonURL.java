package com.leadership.app.utils;

/**
 * Singleton Class Use for initializing common URL values
 */

public class CommonURL {
	//private String baseUrl="http://192.168.1.9:9000/";
	private String applicationBaseUrl="http://120.146.188.232:9090/";
	//private String applicationBaseUrl="http://192.168.1.8:9090/";
	
	public String GoogleBarChartUrl = "http://chart.apis.google.com/chart?cht=bvg&chxt=x,y&chf=b&chs=400x500&chbh=a,2,15&chdlp=b&chg=0,10,1,0,10&";
	public String GetAllOperators = applicationBaseUrl+"OperatorService/GetAllOperators?companyID=%s";
	public String GetAllNetworkKPIByType =applicationBaseUrl+"DataService/GetAllNetworkKPIByType?companyID=%s&type=%s";
	public String GetAllFinancialDataByType =applicationBaseUrl+"DataService/GetAllFinancialDataByType?companyID=%s&type=%s";
	public String GetCompanySetupByCompanyID = applicationBaseUrl+"OperatorService/GetCompanySetupByCompanyID?companyID=%s";
	public String LoginAuthentication =applicationBaseUrl+"OperatorService/LoginAuthentication?userID=%s&password=%s";
	public String GetCitySetups = applicationBaseUrl+"DataService/GetCitySetups";

	public String getImageServer="http://120.146.188.232:9100/LeadershipApps/";

	
	static CommonURL commonURLInstance;
	
	
	/**
	 * Return Instance
	 * 
	 * @return
	 */
	public static CommonURL getInstance() { 
		return commonURLInstance;
	}

	/**
	 * Create instance
	 */
	public static void initializeInstance() {
		if (commonURLInstance == null)
			commonURLInstance = new CommonURL();
	}

	// Constructor hidden because of singleton
	private CommonURL() {

	}
}
