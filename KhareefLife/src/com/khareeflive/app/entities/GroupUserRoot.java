package com.khareeflive.app.entities;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="ArrayOfChatInfo")
public class GroupUserRoot {
	@ElementList(inline=true, name="ChatInfo")
	public List< GroupUser> groupUserList;
}
