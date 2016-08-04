package com.vipdashboard.app.classes;

import android.graphics.Bitmap;

public class ContactList {
	
	public String Number;
	public String Name;
	public Bitmap Image;
	
	public ContactList(String _number, String _name, Bitmap _image) {
		this.Name = _name;
		this.Number = _number;
		this.Image = _image;
	}
	
	public String getNumber(){
		return this.Number;
	}
	
	public String getName(){
		return this.Name;
	}
	
	public Bitmap getPicture(){
		return this.Image;
	}
	
	public void SetName(String _name){
		this.Name = _name;
	}
	
	public void SetNumber(String _number){
		this.Number = _number;
	}
	
	public void SetImage(Bitmap _image){
		this.Image = _image;
	}

}
