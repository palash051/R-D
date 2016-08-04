package com.vipdashboard.app.classes;

public class Report {
	
	private String Name;
	private int Id;
	
	public Report(String name, int id){
		this.Name = name;
		this.Id = id;
	}
	
	public String getName(){
		return this.Name;
	}
	
	public int getID(){
		return this.Id;
	}
	
	public void setName(String name){
		this.Name = name;
	}
	
	public void setName(int id){
		this.Id = id;
	}

}
