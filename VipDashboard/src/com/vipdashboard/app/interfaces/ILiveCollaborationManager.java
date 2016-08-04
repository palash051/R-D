package com.vipdashboard.app.interfaces;

import com.vipdashboard.app.entities.Collaboration;
import com.vipdashboard.app.entities.Collaborations;
import com.vipdashboard.app.entities.UserGroupUnion;
import com.vipdashboard.app.entities.UserGroupUnions;

public interface ILiveCollaborationManager {
	Collaborations GetLiveCollaborationsByGroupId(int groupId);
	Collaboration SendLiveCollaboration(Collaboration collaboration);
	UserGroupUnions GetCollaborationGroupUserListByMsgFrom();
	Collaborations GetLiveCollaborationsByMsgTo(UserGroupUnion userGroupUnion,long longDate, int LoadEarlierPress);
}
