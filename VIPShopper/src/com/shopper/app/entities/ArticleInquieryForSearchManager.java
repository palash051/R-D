package com.shopper.app.entities;

import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.JSONfunctions;

/**
 * @author Tanvir Ahmed CHowdhury use for loading article information these
 *         informations are fetched through JSon functions from server.
 */

public class ArticleInquieryForSearchManager {
	public ArticleInquiryForSearchRoot articleInquiryRoot;

	public ArticleInquieryForSearchManager(int groupno, int familyno) {
		articleInquiryRoot= (ArticleInquiryForSearchRoot)JSONfunctions.retrieveDataFromStream(String.format(
				CommonURL.getInstance().articleURL, familyno, groupno),ArticleInquiryForSearchRoot.class);			
	}
}