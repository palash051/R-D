package com.vipdashboard.app.classes;

public class SearchAlarmItem {
	private String Name;
	
	public SearchAlarmItem(String name){
		this.Name = name;
	}
	
	public String getName(){
		return this.Name;
	}
	
	public void setName(String name){
		this.Name = name;
	}
}
