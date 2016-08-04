package com.vipdashboard.app.interfaces;

import com.vipdashboard.app.entities.OperatorLicense;
import com.vipdashboard.app.entities.OperatorMail;
import com.vipdashboard.app.entities.OperatorReview;
import com.vipdashboard.app.entities.OperatorSubscribe;
import com.vipdashboard.app.entities.OperatorUnSubscribe;

public interface IOperatorManager {
	OperatorSubscribe setPhoneAppsDataPost(int companyID, int phoneID,
			String existingPhone, String countryIsoCodem, String name,
			String email, byte[] byteIdentity, byte[] bytephoto,
			long subscribeAt, String customerFeedback, long customerFeedbackAt);

	OperatorUnSubscribe SetOperatorUnSubscribe(String numberToTerminate,
			String name, String email, String requestAt,
			String customerFeedback, String customerFeedbackAt);

	OperatorReview setProblemTrackingReview(int companyID, int phoneID,
			int networkQuality, int customerCare, int packagesPrice,
			String Remarks, long reviewAt);

	OperatorLicense getOperatorLicenseByCombinedID(String OperatorName,
			String MCC, String MNC, String OperatorCountryISO);

	OperatorMail setOperatorMail(String PhoneId, String ToMail, String CC,
			String BCC, String MailSubject, String MailContent,
			String Signature, String SIMID, String UserNumber,
			String OperatorLicenseID, String Name, String MobileNumber);
}
