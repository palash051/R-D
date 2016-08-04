package com.shopper.app.entities;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Departments {
	@SerializedName("Dept")
	public List<Department> departmentList;
	public Departments() {
		departmentList=new ArrayList<Department>();
	}

}
