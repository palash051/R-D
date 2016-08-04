package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfChatInfo")
public class RoomWiseChatRoot {
	@ElementList(inline=true, name="ChatInfo")
	public List<RoomWiseChat> chatInfoList;

}
