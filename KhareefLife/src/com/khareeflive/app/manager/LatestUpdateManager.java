package com.khareeflive.app.manager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import com.khareeflive.app.entities.ArrayOfLatestUpdate;
import com.khareeflive.app.entities.ArrayOfLoginAuth;
import com.khareeflive.app.entities.ArrayOfSiteDownGoogleMap;
import com.khareeflive.app.entities.CSSR;
import com.khareeflive.app.entities.CSSRRoot;
import com.khareeflive.app.entities.ChatGroup;
import com.khareeflive.app.entities.ChatGroupRoot;
import com.khareeflive.app.entities.DROPData;
import com.khareeflive.app.entities.DROPDataRoot;
import com.khareeflive.app.entities.DataVolume;
import com.khareeflive.app.entities.DataVolumeRoot;
import com.khareeflive.app.entities.GroupUser;
import com.khareeflive.app.entities.GroupUserRoot;
import com.khareeflive.app.entities.Latest2GUpdate;
import com.khareeflive.app.entities.Latest2GUpdateRoot;
import com.khareeflive.app.entities.Latest3GUpdate;
import com.khareeflive.app.entities.Latest3GUpdateRoot;
import com.khareeflive.app.entities.LatestLteUpdate;
import com.khareeflive.app.entities.LatestLteUpdateRoot;
import com.khareeflive.app.entities.LatestUpdate;
import com.khareeflive.app.entities.LoginAuthentication;
import com.khareeflive.app.entities.LoginAuthenticationRoot;
import com.khareeflive.app.entities.NOCRoster;
import com.khareeflive.app.entities.NOCRosterRoot;
import com.khareeflive.app.entities.RoomWiseChat;
import com.khareeflive.app.entities.RoomWiseChatRoot;
import com.khareeflive.app.entities.TrafficReport;
import com.khareeflive.app.entities.TrafficReportRoot;
import com.khareeflive.app.utils.CommonConstraints;
import com.khareeflive.app.utils.CommonURL;
import com.khareeflive.app.utils.JSONfunctions;

public class LatestUpdateManager {

	private LatestUpdate _latestUpdate;
	private ArrayOfSiteDownGoogleMap _listgooglemapdata;
	private ArrayOfLatestUpdate _listnetworkupdats;
	
	public LatestUpdateManager() {
		_latestUpdate = new LatestUpdate();
		_listgooglemapdata = new ArrayOfSiteDownGoogleMap();
		_listnetworkupdats=new ArrayOfLatestUpdate();
		SetLatestNews();
	}

	public void SetLatestNews() {
		// http://188.75.104.141/ENOC/service.asmx?op=GetLatestNews

		String xml = "";

		ArrayOfLatestUpdate arrayoflatestupdate = (ArrayOfLatestUpdate) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetLatestNews),
						ArrayOfLatestUpdate.class);

		if (arrayoflatestupdate != null
				&& arrayoflatestupdate.latestupdate != null
				&& arrayoflatestupdate.latestupdate.size() > 0) {
			_latestUpdate = arrayoflatestupdate.latestupdate.get(0);
			_listnetworkupdats=arrayoflatestupdate;

		}

		ArrayOfSiteDownGoogleMap listgooglemapdata = (ArrayOfSiteDownGoogleMap) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GoogleMap),
						ArrayOfSiteDownGoogleMap.class);
		if (listgooglemapdata != null
				&& listgooglemapdata.siteDownGoogleMap.size() > 0) {
			_listgooglemapdata= listgooglemapdata;
		}

	}

	public LatestUpdate GetLatestNews() {
		return _latestUpdate;

	}

	public ArrayOfSiteDownGoogleMap GetGoogleMapData() {
		if (_listgooglemapdata != null
				&& _listgooglemapdata.siteDownGoogleMap.size() > 0) {
			return _listgooglemapdata;
		}
		return null;
	}
	
	public static Latest2GUpdate Get2GUpdate() {

		Latest2GUpdateRoot latest2GUpdateRoot = (Latest2GUpdateRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.Get2GUpdate),
						Latest2GUpdateRoot.class);

		if (latest2GUpdateRoot != null) {
			return (Latest2GUpdate) latest2GUpdateRoot.latest2GUpdate;
		}
		return null;
	}
	public static Latest3GUpdate Get3GUpdate() {

		Latest3GUpdateRoot latest3GUpdateRoot = (Latest3GUpdateRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.Get3GUpdate),
						Latest3GUpdateRoot.class);

		if (latest3GUpdateRoot != null) {
			return (Latest3GUpdate) latest3GUpdateRoot.latest3GUpdate;
		}
		return null;
	}
	public static LatestLteUpdate GetLTEUpdate() {

		LatestLteUpdateRoot latestLteUpdateRoot = (LatestLteUpdateRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetLTEUpdate),
						LatestLteUpdateRoot.class);

		if (latestLteUpdateRoot != null) {
			return (LatestLteUpdate) latestLteUpdateRoot.latestLteUpdate;
		}
		return null;
	}
	
	public static ArrayList<NOCRoster> GetLatestNOCRoster() {

		NOCRosterRoot arrayOfNOCRoster = (NOCRosterRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetLatestNOCRoster),
						NOCRosterRoot.class);

		if (arrayOfNOCRoster != null && arrayOfNOCRoster.nocRosterList != null) {
			return (ArrayList<NOCRoster>) arrayOfNOCRoster.nocRosterList;
		}
		return null;
	}
	
	public ArrayOfLatestUpdate GetNetworkUpdates()
	{
		if (_listnetworkupdats != null
				&& _listnetworkupdats.latestupdate.size() > 0) {
			return _listnetworkupdats;
		}
		return null;
	}
	
	
	
	public static ArrayList<TrafficReport> GetTraffic() {

		TrafficReportRoot trafficReportRoot = (TrafficReportRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetTraffic),
						TrafficReportRoot.class);

		if (trafficReportRoot != null && trafficReportRoot.trafficReportList != null) {
			return (ArrayList<TrafficReport>) trafficReportRoot.trafficReportList;
		}
		return null;
	}
	
	public static ArrayList<DROPData> GetDROPData() {

		DROPDataRoot dROPDataRoot = (DROPDataRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetDROPData),
						DROPDataRoot.class);

		if (dROPDataRoot != null && dROPDataRoot.dropDataList != null) {
			return (ArrayList<DROPData>) dROPDataRoot.dropDataList;
		}
		return null;
	}
	public static ArrayList<CSSR> GetCSSRData() {

		CSSRRoot cSSRRootRoot = (CSSRRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetCSSR),
						CSSRRoot.class);

		if (cSSRRootRoot != null && cSSRRootRoot.cssrList != null) {
			return (ArrayList<CSSR>) cSSRRootRoot.cssrList;
		}
		return null;
	}
	public static ArrayList<DataVolume> GetDataVolume() {

		DataVolumeRoot dataVolumeRoot = (DataVolumeRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetDataVolume),
						DataVolumeRoot.class);

		if (dataVolumeRoot != null && dataVolumeRoot.dataVolumeList != null) {
			return (ArrayList<DataVolume>) dataVolumeRoot.dataVolumeList;
		}
		return null;
	}
	
	public static ArrayList<ChatGroup> GetChatRoom() {

		ChatGroupRoot chatGroupRoot = (ChatGroupRoot) JSONfunctions
				.getJSONfromURL(String.format(CommonURL.GetChatRoom),
						ChatGroupRoot.class);

		if (chatGroupRoot != null && chatGroupRoot.chatGroupList != null) {
			return (ArrayList<ChatGroup>) chatGroupRoot.chatGroupList;
		}
		return null;
	}
	
	public static ArrayList<RoomWiseChat> GetRoomWiseChat(String strRoom) {

		try {
			RoomWiseChatRoot roomWiseChatRoot = (RoomWiseChatRoot) JSONfunctions
					.getJSONfromURL(String.format(CommonURL.GetRoomWiseChat,URLEncoder.encode(strRoom,CommonConstraints.EncodingCode)),
							RoomWiseChatRoot.class);
			if (roomWiseChatRoot != null && roomWiseChatRoot.chatInfoList != null) {
				return (ArrayList<RoomWiseChat>) roomWiseChatRoot.chatInfoList;
			}
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}

		
		return null;
	}
	
	public static ArrayList<GroupUser> GetChatRoomUserID(String strRoom) {

		GroupUserRoot groupUserRoot;
		try {
			groupUserRoot = (GroupUserRoot) JSONfunctions
					.getJSONfromURL(String.format(CommonURL.GetChatRoomUserID,URLEncoder.encode(strRoom,CommonConstraints.EncodingCode)),
							GroupUserRoot.class);
			if (groupUserRoot != null && groupUserRoot.groupUserList != null) {
				return (ArrayList<GroupUser>) groupUserRoot.groupUserList;
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}

		
		return null;
	}
	
	public static boolean LoadRoomChat(String strRoom,String strFrom,String strMessage) {
		ArrayOfLoginAuth arrayOfLoginAuth;
		try {
			arrayOfLoginAuth = (ArrayOfLoginAuth) JSONfunctions
					.getJSONfromURL(String.format(CommonURL.LoadRoomChat,
							URLEncoder.encode(strRoom,CommonConstraints.EncodingCode), URLEncoder.encode(strFrom,CommonConstraints.EncodingCode),URLEncoder.encode(strMessage,CommonConstraints.EncodingCode)), ArrayOfLoginAuth.class);
			if (arrayOfLoginAuth != null && arrayOfLoginAuth.loginAuth != null
					&& arrayOfLoginAuth.loginAuth.status == 1) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			
			e.printStackTrace();
		}
		
		return false;
	}
	
	public static String isNewMessageFound(String strDate) {

		try {
			RoomWiseChatRoot roomWiseChatRoot = (RoomWiseChatRoot) JSONfunctions
					.getJSONfromURL(String.format(CommonURL.GetRoomWiseUnreadChat,URLEncoder.encode(strDate,CommonConstraints.EncodingCode)),RoomWiseChatRoot.class);
			if (roomWiseChatRoot != null && roomWiseChatRoot.chatInfoList != null) {
				return "CHAT";
			}else{
				ArrayOfLatestUpdate arrayoflatestupdate = (ArrayOfLatestUpdate) JSONfunctions
						.getJSONfromURL(String.format(CommonURL.GetLatestUnreadNews,URLEncoder.encode(strDate,CommonConstraints.EncodingCode)),ArrayOfLatestUpdate.class);

				if (arrayoflatestupdate != null
						&& arrayoflatestupdate.latestupdate != null
						&& arrayoflatestupdate.latestupdate.size() > 0) {
					return "LATESTNEWS";
				}
			}
			
		} catch (UnsupportedEncodingException e) {			
			e.printStackTrace();
		}

		
		return "";
	}

}
