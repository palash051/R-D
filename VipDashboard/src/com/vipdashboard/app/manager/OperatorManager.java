package com.vipdashboard.app.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.json.JSONObject;

import android.util.Base64;
import android.webkit.URLUtil;

import com.vipdashboard.app.base.MyNetService;
import com.vipdashboard.app.entities.OperatorLicense;
import com.vipdashboard.app.entities.OperatorLicenseEntityHolder;
import com.vipdashboard.app.entities.OperatorMail;
import com.vipdashboard.app.entities.OperatorMailEntityHolder;
import com.vipdashboard.app.entities.OperatorReview;
import com.vipdashboard.app.entities.OperatorReviewHolder;
import com.vipdashboard.app.entities.OperatorSubscribe;
import com.vipdashboard.app.entities.OperatorSubscribeRoot;
import com.vipdashboard.app.entities.OperatorUnSubscribe;
import com.vipdashboard.app.entities.OperatorUnSubscribeRoot;
import com.vipdashboard.app.entities.TempTableUserRoot;
import com.vipdashboard.app.entities.TempTableUserRoot;
import com.vipdashboard.app.interfaces.IOperatorManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class OperatorManager implements IOperatorManager {

	@Override
	public OperatorSubscribe setPhoneAppsDataPost(int companyID, int phoneID,
			String existingPhone, String countryIsoCodem, String name,
			String email, byte[] byteIdentity, byte[] bytephoto,
			long subscribeAt, String customerFeedback, long customerFeedbackAt) {
		OperatorSubscribe operatorSubscribe = null;
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("companyID", URLEncoder.encode(String.valueOf(companyID), CommonConstraints.EncodingCode));
			jsonObject.put("phoneID", URLEncoder.encode(String.valueOf(phoneID), CommonConstraints.EncodingCode));
			jsonObject.put("existingPhone", URLEncoder.encode(existingPhone,
					CommonConstraints.EncodingCode));
			jsonObject.put("countryIsoCodem", URLEncoder.encode(
					countryIsoCodem, CommonConstraints.EncodingCode));
			jsonObject.put("name",
					URLEncoder.encode(name, CommonConstraints.EncodingCode));
			jsonObject.put("email",
					URLEncoder.encode(email, CommonConstraints.EncodingCode));
			jsonObject.put(
					"byteIdentity",
					byteIdentity != null ? Base64.encodeToString(byteIdentity,
							Base64.NO_WRAP) : "");
			jsonObject.put(
					"bytephoto",
					bytephoto != null ? Base64.encodeToString(bytephoto,
							Base64.NO_WRAP) : "");
			jsonObject.put("subscribeAt",
					URLEncoder.encode("0", CommonConstraints.EncodingCode));
			jsonObject.put("customerFeedback",
					URLEncoder.encode("", CommonConstraints.EncodingCode));
			jsonObject.put("customerFeedbackAt",
					URLEncoder.encode("0", CommonConstraints.EncodingCode));
			OperatorSubscribeRoot operatorSubscribeRoot = (OperatorSubscribeRoot) JSONfunctions
					.retrieveDataFromJsonPost(
							CommonURL.getInstance().SetOperatorSubscribe,
							jsonObject, OperatorSubscribeRoot.class);
			if (operatorSubscribeRoot != null)
				operatorSubscribe = operatorSubscribeRoot.operatorSubscribe;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return operatorSubscribe;
	}

	@Override
	public OperatorUnSubscribe SetOperatorUnSubscribe(String numberToTerminate,
			String name, String email, String requestAt,
			String customerFeedback, String customerFeedbackAt) {
		OperatorUnSubscribe operatorUnSubscribe = null;
		try{
			OperatorUnSubscribeRoot operatorUnSubscribeRoot = (OperatorUnSubscribeRoot) JSONfunctions.retrieveDataFromStream(
					String.format(CommonURL.getInstance().SetOperatorUnSubscribe,
					CommonValues.getInstance().CompanyId,
					MyNetService.phoneId,
					URLEncoder.encode(String.valueOf(numberToTerminate), CommonConstraints.EncodingCode),
					URLEncoder.encode(name, CommonConstraints.EncodingCode),
					URLEncoder.encode(email, CommonConstraints.EncodingCode),
					URLEncoder.encode(requestAt, CommonConstraints.EncodingCode),
					URLEncoder.encode(customerFeedback, CommonConstraints.EncodingCode),
					URLEncoder.encode(customerFeedbackAt, CommonConstraints.EncodingCode)), OperatorUnSubscribeRoot.class);
			if(operatorUnSubscribeRoot != null)
				operatorUnSubscribe = operatorUnSubscribeRoot.operatorUnSubscribe;
		}catch (Exception e) {
			e.printStackTrace();
		}
		return operatorUnSubscribe;
	}


	@Override
	public OperatorReview setProblemTrackingReview(int companyID, int phoneID,
			int networkQuality, int customerCare, int packagesPrice,
			String Remarks, long reviewAt) {

		OperatorReview operatorReview = null;
		try {
			OperatorReviewHolder	operatorReviewHolder = (OperatorReviewHolder) JSONfunctions
					.retrieveDataFromStream(String.format(CommonURL
							.getInstance().setProblemTrackingReview, companyID,
							phoneID, networkQuality, customerCare,
							packagesPrice, URLEncoder.encode(Remarks,
									CommonConstraints.EncodingCode), reviewAt),
									OperatorReviewHolder.class);
			if(operatorReviewHolder!=null && operatorReviewHolder.operatorReview!=null)
				operatorReview=operatorReviewHolder.operatorReview;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return operatorReview;
	}
	
	@Override
	public OperatorLicense getOperatorLicenseByCombinedID(String OperatorName,
			String MCC,
			String MNC,
			String OperatorCountryISO) {

		OperatorLicense operatorLicense= null;
		try {
			OperatorLicenseEntityHolder	operatorLicenseEntityHolder = (OperatorLicenseEntityHolder) JSONfunctions
					.retrieveDataFromStream(String.format(CommonURL
							.getInstance().GetOperatorLicenseByCombinedID,URLEncoder.encode(OperatorName,
									CommonConstraints.EncodingCode),
							MCC,
							MNC,
							OperatorCountryISO),
							OperatorLicenseEntityHolder.class);
			if(operatorLicenseEntityHolder!=null && operatorLicenseEntityHolder.operatorLicense!=null)
				operatorLicense=operatorLicenseEntityHolder.operatorLicense;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return operatorLicense;
	}
	
	@Override
	public OperatorMail setOperatorMail(
			String PhoneId,
			String ToMail,
			String CC,
			String BCC,
			String MailSubject,
			String MailContent,
			String Signature,
			String SIMID,
			String UserNumber,
			String OperatorLicenseID,
			String Name,
			String MobileNumber) {

		OperatorMail operatorMail= null;
		try {
			OperatorMailEntityHolder operatorMailEntityHolder = (OperatorMailEntityHolder) JSONfunctions
					.retrieveDataFromStream(String.format(CommonURL
							.getInstance().SetOperatorMail,
									PhoneId,
									URLEncoder.encode(ToMail,CommonConstraints.EncodingCode),
									URLEncoder.encode(CC,CommonConstraints.EncodingCode),
									URLEncoder.encode(BCC,CommonConstraints.EncodingCode),
									URLEncoder.encode(MailSubject,CommonConstraints.EncodingCode),
									URLEncoder.encode(MailContent,CommonConstraints.EncodingCode),
									URLEncoder.encode(Signature,CommonConstraints.EncodingCode),
											URLEncoder.encode(SIMID,CommonConstraints.EncodingCode),
													URLEncoder.encode(UserNumber,CommonConstraints.EncodingCode),
															URLEncoder.encode(OperatorLicenseID,CommonConstraints.EncodingCode),
																	URLEncoder.encode(Name,CommonConstraints.EncodingCode),
																			URLEncoder.encode(MobileNumber,CommonConstraints.EncodingCode)),
							OperatorMailEntityHolder.class);
			if(operatorMailEntityHolder!=null && operatorMailEntityHolder.operatorMail!=null)
				operatorMail=operatorMailEntityHolder.operatorMail;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return operatorMail;
	}


}
