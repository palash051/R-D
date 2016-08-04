package com.shopper.app.entities;

import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.JSONfunctions;

public class FamilyInq {
	public FamilyInquieryRoot familyInquieryResponse;

	public FamilyInq() {
		familyInquieryResponse = (FamilyInquieryRoot)JSONfunctions.retrieveDataFromStream(String.format(CommonURL.getInstance().familyURL),FamilyInquieryRoot.class);		
	}
}
