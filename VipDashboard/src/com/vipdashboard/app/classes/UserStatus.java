package com.vipdashboard.app.classes;

public class UserStatus {
	
	public int resourceID;
	public String Name;
	public int ID;

	public UserStatus(int userStatusOnline, String name, int id) {
		this.resourceID = userStatusOnline;
		this.Name = name;
		this.ID = id;
	}
	
	public int getResourceID(){
		return this.resourceID;
	}
	
	public String getName(){
		return this.Name;
	}
	
	public int getID(){
		return this.ID;
	}
	
}
