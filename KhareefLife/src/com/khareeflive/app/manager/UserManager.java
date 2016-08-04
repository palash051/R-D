package com.khareeflive.app.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.khareeflive.app.entities.ArrayOfLoginAuth;
import com.khareeflive.app.entities.LoginAuthentication;
import com.khareeflive.app.entities.LoginAuthenticationRoot;
import com.khareeflive.app.entities.Message;
import com.khareeflive.app.entities.MessageRoot;
import com.khareeflive.app.utils.CommonConstraints;
import com.khareeflive.app.utils.CommonURL;
import com.khareeflive.app.utils.JSONfunctions;


public class UserManager {

	public static boolean isUserAuthenticated(String userName,
			String userPassword) {
		ArrayOfLoginAuth arrayOfLoginAuth = (ArrayOfLoginAuth) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetLoginAthentication,
						userName, userPassword), ArrayOfLoginAuth.class);
		if (arrayOfLoginAuth != null && arrayOfLoginAuth.loginAuth != null
				&& arrayOfLoginAuth.loginAuth.status == 1) {
			return true;
		}
		return false;
	}

	public static ArrayList<LoginAuthentication> GetUserList() {

		LoginAuthenticationRoot arrayOfLoginAuth = (LoginAuthenticationRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetUserList),
						LoginAuthenticationRoot.class);

		if (arrayOfLoginAuth != null && arrayOfLoginAuth.loginAuthList != null) {
			return (ArrayList<LoginAuthentication>) arrayOfLoginAuth.loginAuthList;
		}
		return null;
	}

	public static Message GetMessage(String strfrom, String strto) {

		MessageRoot messageRoot = (MessageRoot) JSONfunctions.getJSONfromURL(
				String.format(CommonURL.GetMessage, strfrom, strto),
				MessageRoot.class);
		if (messageRoot != null) {
			return messageRoot.message;
		}
		return null;
	}
	
	public static boolean SendMessage(String strfrom, String strto,String strMsg) {

		ArrayOfLoginAuth arrayOfLoginAuth;
		try {
			arrayOfLoginAuth = (ArrayOfLoginAuth) JSONfunctions.getJSONfromURL(
					String.format(CommonURL.SendMessage, strfrom, strto,URLEncoder.encode(strMsg,CommonConstraints.EncodingCode)),
					ArrayOfLoginAuth.class);
		
			if (arrayOfLoginAuth != null && arrayOfLoginAuth.loginAuth != null
					&& arrayOfLoginAuth.loginAuth.status == 1) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return false;
	}

}
