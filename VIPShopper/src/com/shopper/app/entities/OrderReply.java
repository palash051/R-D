package com.shopper.app.entities;

import java.util.ArrayList;

import com.google.gson.annotations.SerializedName;

public class OrderReply {
	@SerializedName("customerId")
	public String CustomerId;
	public String Barcode;
	@SerializedName("OrderNo")
	public int OrderNo;
	@SerializedName("total")
	public double TotalPrice;	
	@SerializedName("isClosed")
	public boolean IsClosed;
	@SerializedName("line")
	public ArrayList<OrderLine> Lines;

	public OrderReply() {
		Barcode = "";
		OrderNo = 0;
		TotalPrice = 0;
		Lines = new ArrayList<OrderLine>();
	}

	public boolean hasChanged() {
		for (OrderLine line : Lines) {
			if (line.hasChanged)
				return true;
		}
		return false;
	}

	public void resetHasChanged() {
		for (OrderLine line : Lines) {
			line.hasChanged = false;
		}
	}

	public int getTotalItemCount() {
		int count = 0;
		for (OrderLine line : Lines) {
			count += line.quantity;
		}
		return count;
	}
	
	public int getItemQuantity(String Id) {		
		for (OrderLine line : Lines) {
			if(line.Id.equals(Id))
			return line.quantity;
		}
		return 0;
	}
	
	public OrderLine getOrderLineById(String Id) {
		
		for (OrderLine line : Lines) {
			if(line.Id.equals(Id))
				return line;
		}
		return null;
	}

	// Return true if there have currently any working order on the basket
	public boolean hasWorkingOrder() {
		boolean hasOrder = false;
		if (OrderNo > 0 || Lines.size() > 0)
			hasOrder = true;
		return hasOrder;
	}

}
