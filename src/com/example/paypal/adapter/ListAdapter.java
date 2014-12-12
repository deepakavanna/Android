package com.example.paypal.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.paypal.R;



public class ListAdapter  extends ArrayAdapter<Location> {

	private List<Location> locationList;
	private Context context;
	
	
	
	public ListAdapter(List<Location> locationList, Context ctx) {
		super(ctx, R.layout.img_row_layout, locationList);
		this.locationList = locationList;
		this.context = ctx;
	}
	
	public int getCount() {
		return locationList.size();
	}

	public Location getItem(int position) {
		return locationList.get(position);
	}

	public long getItemId(int position) {
		return locationList.get(position).hashCode();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		LocationHolder holder = new LocationHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.img_row_layout, null);
			// Now we can fill the layout with the right values
			TextView tv = (TextView) v.findViewById(R.id.name);
			TextView distView = (TextView) v.findViewById(R.id.dist);

			
			holder.planetNameView = tv;
			holder.distView = distView;
			
			v.setTag(holder);
		}
		else 
			holder = (LocationHolder) v.getTag();
		
		Location p = locationList.get(position);
		holder.planetNameView.setText(p.getName());
		holder.distView.setText("" + p.getLocation());
		
		
		return v;
	}
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class LocationHolder {
		public TextView planetNameView;
		public TextView distView;
	}
	

}
