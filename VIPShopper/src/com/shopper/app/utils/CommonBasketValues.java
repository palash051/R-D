package com.shopper.app.utils;

import java.util.Observable;

import com.shopper.app.entities.OrderReply;

/**
 * Singleton Class
 * use for initializing common basket values
 * @author 
 */
public class CommonBasketValues extends Observable{
	public OrderReply Basket = new OrderReply();
	static CommonBasketValues commonBasketValuesInstance;

	/**
	 * Return Instance
	 * @return
	 */
	public static CommonBasketValues getInstance() {		
		return commonBasketValuesInstance;
	}

	/**
	 * Create instance 
	 */
	public static void initializeInstance() {
		if (commonBasketValuesInstance == null)
			commonBasketValuesInstance = new CommonBasketValues();
	}
	// Constructor hidden because of singleton
	private CommonBasketValues(){
		
	}
	
	public void updateBasket(){
		setChanged();
		notifyObservers();
	}
}
