package com.khareeflive.app.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Latest3GUpdate")
public class Latest3GUpdate {
	@Element(name="Msg")
	public String message;
	@Element(name="MsgTitle")
	public String messageTitle;
	@Element(name="UploadedBy")
	public String uploadedBy;
	@Element(name="UploadDate")
	public String uploadDate;
}
