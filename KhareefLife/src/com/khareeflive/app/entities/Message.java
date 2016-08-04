package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ChatInfo")
public class Message {
	@Element(name = "Msg")
	public String message;
	@Element(name = "From")
	public String sender;
	@Element(name = "MsgTime")
	public String msgTime;
}
