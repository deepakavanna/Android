package com.example.paypal;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.example.paypal.adapter.ListAdapter;
import com.example.paypal.adapter.Location;

public class ListActivity extends Activity {
	
	List<Location> locationList = new ArrayList<Location>();
	ListAdapter aAdpt;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
        initList();
        
        // We get the ListView component from the layout
        ListView lv = (ListView) findViewById(R.id.listView);
        
        
        // This is a simple adapter that accepts as parameter
        // Context
        // Data list
        // The row layout that is used during the row creation
        // The keys used to retrieve the data
        // The View id used to show the data. The key number and the view id must match
        //aAdpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, planetsList);
        aAdpt = new ListAdapter(locationList, this);
        lv.setAdapter(aAdpt);
        
        // React to user clicks on item
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
					long id) {
				
				
				// We know the View is a <extView so we can cast it
//				TextView clickedView = (TextView) view;

				//Toast.makeText(ListActivity.this, "Item with id ["+id+"] - Position ["+position+"] - Planet ["+"]", Toast.LENGTH_SHORT).show();

				Intent userHome = new Intent(ListActivity.this, CheckinActivity.class);
				userHome.putExtra("name", locationList.get(position).getName());
				

				startActivity(userHome);
			

			}
		   });
        
          // we register for the contextmneu        
          registerForContextMenu(lv);

	}

	
	private void initList() {
        // We populate the planets
       
        locationList.add(new Location("Paypal Cafe 17", "0.1 mile"));
        locationList.add(new Location("Paypal Cafe 10", "0.1 mile"));
        locationList.add(new Location("Starbucks", "0.1 mile"));
        locationList.add(new Location("Chipotle", "1 mile"));
        locationList.add(new Location("Subway", "1 mile"));
        locationList.add(new Location("Fantazia", "2 miles"));
        locationList.add(new Location("PF Chang", "5 miles"));
     
    	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
