package com.shopper.app.entities;

import com.google.gson.annotations.SerializedName;

public class CustomerRoot {
	@SerializedName("Customer")
	public Customer customer;
	public CustomerRoot() {
		customer=new Customer();
	}

}
