package com.example.paypal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;


public class MainActivity extends Activity {

	public class PayPalLoginTask extends AsyncTask<User, Void, Boolean>{
		User user;
		boolean cardPresent = false;
		
//		private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
		
		@Override
		protected Boolean doInBackground(User...params){
			user = params[0];
			
			if(user != null){
			
				
				try {
					org.apache.http.client.HttpClient client = new DefaultHttpClient();
					
					
					HttpPost post = new HttpPost("https://www.sandbox.paypal.com/v1/oauth2/login");
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				    nameValuePairs.add(new BasicNameValuePair("user",
				          " deepak.avanna-buyer@gmail.com"));
				    nameValuePairs.add(new BasicNameValuePair("password",
					          "buyer1234"));
				    
				    nameValuePairs.add(new BasicNameValuePair("response_type", "token"));
				    nameValuePairs.add(new BasicNameValuePair("grant_type", "password"));
				    nameValuePairs.add(new BasicNameValuePair("scope", "address"));
				    

				    post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				 			        
			        
			        // execute method and handle any error responses.
			        
			        HttpResponse response = client.execute(post);
			        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			        String line = "";
			        while ((line = rd.readLine()) != null) {
			          System.out.println(line);
			        }
			
			      } catch (IOException e) {
			        e.printStackTrace();
			      } 
				
			}
			return Boolean.TRUE;

		}
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        ImageButton submitButton 	= (ImageButton) findViewById(R.id.imageButton1);
        
        submitButton.setOnClickListener(new OnClickListener() {
        	
        	public void onClick(View v) {
        		
        		EditText  password = (EditText) findViewById(R.id.editText2);
        		EditText  userName = (EditText) findViewById(R.id.editText1);
        		User user = new User();
        		
        		user.setPassword(password.getText().toString());
        		user.setUserName(userName.getText().toString());
        		
        	
        		try {
        			Boolean loginTrue = new PayPalLoginTask().execute(user).get();
        			
        			if (loginTrue) {
						Intent userHome = new Intent(MainActivity.this, ListActivity.class);
						startActivity(userHome);

        				
        			}
        		}
        		catch (Exception e) {
        			e.printStackTrace();
        		}
        		
        	}
        });
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
