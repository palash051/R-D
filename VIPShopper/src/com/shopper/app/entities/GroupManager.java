package com.shopper.app.entities;

import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.JSONfunctions;

public class GroupManager {
	public GroupInquieryRoot groupInquieryRoot;

	public GroupManager(int familyno) {
		
		groupInquieryRoot = (GroupInquieryRoot)JSONfunctions.retrieveDataFromStream(String.format(
				CommonURL.getInstance().groupURL, familyno),GroupInquieryRoot.class);	
	}	
}
