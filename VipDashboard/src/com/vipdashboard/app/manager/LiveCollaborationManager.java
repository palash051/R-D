package com.vipdashboard.app.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.CollaborationRoot;
import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;
import com.vipdashboard.app.interfaces.ILiveCollaborationManager;
import com.vipdashboard.app.utils.CommonConstraints;
import com.vipdashboard.app.utils.CommonURL;
import com.vipdashboard.app.utils.CommonValues;
import com.vipdashboard.app.utils.JSONfunctions;

public class LiveCollaborationManager implements ILiveCollaborationManager {
	@Override
	public Collaborations GetLiveCollaborationsByGroupId(int groupId) {
		return (Collaborations) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLiveCollaborationsByGroupId,
						groupId), Collaborations.class);
	}
	@Override
	public Collaborations GetLiveCollaborationsByMsgTo(UserGroupUnion userGroupUnion,long longDate, int LoadEarlierPress) {
		return (Collaborations) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetLiveCollaborationsByMsgFrom,
						CommonValues.getInstance().LoginUser.UserNumber,userGroupUnion.ID,userGroupUnion.Type,longDate,LoadEarlierPress), Collaborations.class);
	}
	@Override
	public Collaboration SendLiveCollaboration(Collaboration collaboration) {		
		CollaborationRoot collaborationRoot;
		try {
			collaborationRoot = (CollaborationRoot) JSONfunctions.retrieveDataFromStream(String.format(CommonURL.getInstance().SetLiveCollaborations,collaboration.MsgFrom,URLEncoder.encode(collaboration.MsgText,CommonConstraints.EncodingCode),collaboration.UserType.equals("G")? collaboration.GroupID:collaboration.MsgTo,collaboration.Latitude,collaboration.Longitude,collaboration.UserType), CollaborationRoot.class);
		
			if(collaborationRoot!=null){
				collaboration=collaborationRoot.collaboration;
			}else{
				collaboration=null;
			}
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}
		return collaboration;
	}
	@Override
	public UserGroupUnions GetCollaborationGroupUserListByMsgFrom() {
		return (UserGroupUnions) JSONfunctions.retrieveDataFromStream(String
				.format(CommonURL.getInstance().GetCollaborationGroupUserListByMsgFrom,
						CommonValues.getInstance().CompanyId,CommonValues.getInstance().LoginUser.UserNumber), UserGroupUnions.class);
	}
}
