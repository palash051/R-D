package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfChatInfo")
public class ChatGroupRoot {
	@ElementList(inline=true, name="ChatInfo")
	public List<ChatGroup> chatGroupList;

}
