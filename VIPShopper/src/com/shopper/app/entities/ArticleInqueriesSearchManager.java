package com.shopper.app.entities;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.shopper.app.utils.CommonConstraints;
import com.shopper.app.utils.CommonURL;
import com.shopper.app.utils.JSONfunctions;

/**
 * @author Tac
 * use for loading article information for search these informations
 * are fetched through JSon functions from server.
 */

public class ArticleInqueriesSearchManager {
	public SearchArticleInquiryRoot searchArticleInquiryRoot;

	public ArticleInqueriesSearchManager(String searchstring) {	
		try {
			searchArticleInquiryRoot =(SearchArticleInquiryRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonURL.getInstance().articlesearchURL, URLEncoder.encode(searchstring, CommonConstraints.EncodingCode)),SearchArticleInquiryRoot.class);			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

}
