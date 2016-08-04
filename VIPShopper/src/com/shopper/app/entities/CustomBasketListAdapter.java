package com.shopper.app.entities;

import java.math.BigDecimal;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shopper.app.R;
import com.shopper.app.utils.CommonBasketValues;
import com.shopper.app.utils.CommonTask;

public class CustomBasketListAdapter extends ArrayAdapter<OrderLine> {
	OrderLine orderLine;
	ArrayList<OrderLine> orderLineList;
	Context context;
	int width;
	int _position = -1;
	View oldView = null;
	private LayoutInflater mInflater;
	
	public CustomBasketListAdapter(Context context, int resource,
			ArrayList<OrderLine> _orderLineList) {
		super(context, resource, _orderLineList);
		this.context = context;
		orderLineList = _orderLineList;		
		this.mInflater = LayoutInflater.from( context );
	}	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final BasketViewHolder vh;
		orderLine = (OrderLine) orderLineList.get(position);
		orderLine.RowId = position;
		if ( convertView == null ) {
			convertView = mInflater.inflate( R.layout.basket_item_view, parent, false );
			vh = BasketViewHolder.create( (LinearLayout)convertView , orderLine.RowId);
			convertView.setTag( vh );
			convertView.setPadding(4, 3, 4, 3);
			convertView.setBackgroundColor(Color.WHITE);
			vh.llBasketItemDetails.setGravity(Gravity.TOP);
			width = 170;
			width = CommonTask.convertToDimensionValue(context, width);
		} else {
			vh = (BasketViewHolder)convertView.getTag();
		}
		

		
		vh.rowID = position;
		
		vh.tvBasketItemImage.setTag(orderLine.Id);
		if(null != CommonTask.getArticleImageFromCache(orderLine.Id)){
			vh.tvBasketItemImage.setBackgroundDrawable(CommonTask.getArticleImageFromCache(orderLine.Id));
		}else{
			AsyncImageLoader asyncImageLoader = new AsyncImageLoader();
			asyncImageLoader.execute(vh.tvBasketItemImage);
		}
		
		vh.tvBasketItemQty.setText(String.valueOf(orderLine.quantity));
		vh.tvBasketItemText.setText(CommonTask.toCamelCase(orderLine.Text, " "));
		vh.tvBasketItemPricePer.setText("a " + CommonTask.getString(orderLine.Price));
		vh.rlBasketItemPrice.setVisibility(View.VISIBLE);
		double price = CommonTask.round(orderLine.TotalPrice, 2,
				BigDecimal.ROUND_HALF_UP);
		String[] total = String.valueOf(price).replace(".", ":").split(":");
		vh.tvBasketItemTotalPrice.setText(total[0]);
		vh.tvBasketItemTotalPriceFraction.setText(total[1].length() < 2 ? total[1] + "0" : total[1]);
		if (orderLine.Discount != null) {
			vh.tvBasketItemDiscount.setText(orderLine.Discount.Text + " "
					+ CommonTask.getString(orderLine.Discount.Amount));
			vh.tvBasketItemDiscount.setVisibility(View.VISIBLE);
		} else {
			vh.tvBasketItemDiscount.setText("");
			vh.tvBasketItemDiscount.setVisibility(View.GONE);
		}
		
		
		convertView.setOnTouchListener(touchListener);

		return convertView;
	}

	public OrderLine getItemAtPosition(int position) {
		orderLine = (OrderLine) orderLineList.get(position);
		return orderLine;
	}

	public int getSize() {
		return orderLineList.size();
	}

	public void setSelection(int pos) {
		_position = pos;
	}
	
	public void updateList(){
		clear();
		addAll(CommonBasketValues.getInstance().Basket.Lines);		
		notifyDataSetChanged();
	}
	
	RelativeLayout.OnTouchListener touchListener = new RelativeLayout.OnTouchListener() {
		public boolean onTouch(View v, MotionEvent event) {
			try {
				if (oldView != null) {
					oldView.setBackgroundResource(R.color.white);
				}
//				setSelection(((OrderLine) v.getTag()).RowId);
				setSelection(((BasketViewHolder)v.getTag()).rowID);
				v.setBackgroundResource(R.drawable.list_pressed);
				v.setPadding(4, 3, 4, 3);
				oldView = v;

			} catch (Exception e) {

			}
			return false;
		}
	};
	
	/**
	 * ViewHolder class for layout.<br />
	 * <br />
	 * Auto-created on 2013-05-27 14:53:37 by Android Layout Finder
	 * (http://www.buzzingandroid.com/tools/android-layout-finder)
	 */
	public static class BasketViewHolder {
		public final RelativeLayout rlBasketItemImage;
		public final ImageView tvBasketItemImage;
		public final RelativeLayout rlBasketItemQty;
		public final TextView tvBasketItemQty;
		public final RelativeLayout llBasketItemDetails;
		public final TextView tvBasketItemText;
		public final TextView tvBasketItemPricePer;
		public final TextView tvBasketItemDiscount;
		public final RelativeLayout rlBasketItemPrice;
		public final TextView tvBasketItemTotalPrice;
		public final TextView tvBasketItemTotalPriceFraction;
		public int rowID;
	
		private BasketViewHolder(RelativeLayout rlBasketItemImage, ImageView tvBasketItemImage, RelativeLayout rlBasketItemQty, TextView tvBasketItemQty, RelativeLayout llBasketItemDetails, TextView tvBasketItemText, TextView tvBasketItemPricePer, TextView tvBasketItemDiscount, RelativeLayout rlBasketItemPrice, TextView tvBasketItemTotalPrice, TextView tvBasketItemTotalPriceFraction, int rowID) {
			this.rlBasketItemImage = rlBasketItemImage;
			this.tvBasketItemImage = tvBasketItemImage;
			this.rlBasketItemQty = rlBasketItemQty;
			this.tvBasketItemQty = tvBasketItemQty;
			this.llBasketItemDetails = llBasketItemDetails;
			this.tvBasketItemText = tvBasketItemText;
			this.tvBasketItemPricePer = tvBasketItemPricePer;
			this.tvBasketItemDiscount = tvBasketItemDiscount;
			this.rlBasketItemPrice = rlBasketItemPrice;
			this.tvBasketItemTotalPrice = tvBasketItemTotalPrice;
			this.tvBasketItemTotalPriceFraction = tvBasketItemTotalPriceFraction;
			this.rowID = rowID;
		}
	
		public static BasketViewHolder create(LinearLayout bBasketRowItem, int rowId) {
			RelativeLayout rlBasketItemImage = (RelativeLayout)bBasketRowItem.findViewById( R.id.rlBasketItemImage );
			ImageView tvBasketItemImage = (ImageView)bBasketRowItem.findViewById( R.id.tvBasketItemImage );
			RelativeLayout rlBasketItemQty = (RelativeLayout)bBasketRowItem.findViewById( R.id.rlBasketItemQty );
			TextView tvBasketItemQty = (TextView)bBasketRowItem.findViewById( R.id.tvBasketItemQty );
			RelativeLayout llBasketItemDetails = (RelativeLayout)bBasketRowItem.findViewById( R.id.llBasketItemDetails );
			TextView tvBasketItemText = (TextView)bBasketRowItem.findViewById( R.id.tvBasketItemText );
			TextView tvBasketItemPricePer = (TextView)bBasketRowItem.findViewById( R.id.tvBasketItemPricePer );
			TextView tvBasketItemDiscount = (TextView)bBasketRowItem.findViewById( R.id.tvBasketItemDiscount );
			RelativeLayout rlBasketItemPrice = (RelativeLayout)bBasketRowItem.findViewById( R.id.rlBasketItemPrice );
			TextView tvBasketItemTotalPrice = (TextView)bBasketRowItem.findViewById( R.id.tvBasketItemTotalPrice );
			TextView tvBasketItemTotalPriceFraction = (TextView)bBasketRowItem.findViewById( R.id.tvBasketItemTotalPriceFraction );
			return new BasketViewHolder( rlBasketItemImage, tvBasketItemImage, rlBasketItemQty, tvBasketItemQty, llBasketItemDetails, tvBasketItemText, tvBasketItemPricePer, tvBasketItemDiscount, rlBasketItemPrice, tvBasketItemTotalPrice, tvBasketItemTotalPriceFraction, rowId );
		}
	}
	
}
