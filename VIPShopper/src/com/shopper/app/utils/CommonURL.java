package com.shopper.app.utils;

/**
 * Singleton Class
 * 
 * @author use for initializing common URL values
 */

public class CommonURL {	
	public String ProductImageURL = "";
	public String ProductDetailsURL = "";
	public String familyURL = "";
	public String BasketURL = "";
	public String CheckoutURL = "";
	public String BarcodeURL = "";
	public String groupURL = "";
	public String articleURL = "";
	public String BasketItemWithSameFamilyURL = "";
	public String articlesearchURL = "";
	// Using by temporary base url...
	public String UserInformationByCustomerIdURL = "";
	public String LoginCustomerURL = "";
	public String SaveNewCustomerURL = "";
	public String UpdateCustomerURL = "";
	// Users aternate address from Online payment SaveUserAlternateAddress
	public String SaveUserAlternateAddress = "";
	public String UpdateUserAlternateAddress = "";
	public final String NewServerUrlValidation =  "/agetor/vikingproxy/ArticleInq?Store=%s";

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

	public void assignValues(String baseURL,String shopNumber) {		
		ProductImageURL = "http://vikingmobile.bording.dk/agetor/tfx/articleImage?ean=%s";
		// ProductImageURL = BaseURL + "agetor/tfx/articleImage?ean=%s";
		ProductDetailsURL = baseURL	+ "/agetor/vikingproxy/ArticleInq?Store=%s&ClientId=%s&ArtNo=%s";
		familyURL = baseURL + "/agetor/vikingproxy/ArticleInq?Store="+ shopNumber;
		BasketURL = baseURL + "/agetor/vikingproxy/WebOrder?Method=%s&Store=%s&Order=%s&ArtNo=%s&Qty=%s";
		CheckoutURL = baseURL + "/agetor/vikingproxy/WebOrder?Method=%s&Store=%s&Order=%s";
		BarcodeURL = baseURL + "/agetor/tfx/generateBarcode?ean=%s";
		groupURL = familyURL + "&DeptNo=%s";
		articleURL = familyURL + "&DeptNo=%s&GroupNo=%s";
		BasketItemWithSameFamilyURL = baseURL+ "/agetor/vikingproxy/ArticleInq?Store=%s&ClientId=%s&fam=%s";		
		articlesearchURL = baseURL + "/agetor/vikingproxy/ArticleInq?Store="+ shopNumber + "&Search=%s";
		// Using by temporary base url...
		UserInformationByCustomerIdURL = baseURL+ "/agetor/vikingproxy/customer?CustomerId=%s";
		LoginCustomerURL = baseURL	+ "/agetor/vikingproxy/customer?Email=%s&Password=%s";
		SaveNewCustomerURL = baseURL + "/agetor/vikingproxy/customer?Name=%s&Address1=%s&Address2=%s&Zipcode=%s&City=%s&Phone=%s&Email=%s&Password=%s";
		UpdateCustomerURL = baseURL	+ "/agetor/vikingproxy/customer?CustomerId=%s&Name=%s&Address1=%s&Address2=%s&Zipcode=%s&City=%s&Phone=%s&Email=%s&Password=%s";
		// Users aternate address from Online payment SaveUserAlternateAddress
		SaveUserAlternateAddress = baseURL	+ "/agetor/vikingproxy/address?CustomerId=%s&Address1=%s&Address2=%s&Zipcode=%s&City=%s&Phone=%s&";
		UpdateUserAlternateAddress = baseURL + "/agetor/vikingproxy/address?CustomerId=%s&Address1=%s&Address2=%s&Zipcode=%s&City=%s&Phone=%s&";		
	}
}
