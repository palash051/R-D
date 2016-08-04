package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ChatInfo")
public class RoomWiseChat {
	@Element(name = "Msg")
	public String message;
	@Element(name = "From")
	public String from;
	@Element(name = "MsgTime")
	public String msgTime;
}
