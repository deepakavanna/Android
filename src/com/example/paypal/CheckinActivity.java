package com.example.paypal;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.SessionStore;
import com.facebook.android.Facebook.DialogListener;
import com.triggertrap.seekarc.SeekArc;
import com.triggertrap.seekarc.SeekArc.OnSeekArcChangeListener;

public class CheckinActivity extends Activity {
	
	private Facebook mFacebook;
	private CheckBox mFacebookBtn;
	
	private static final String APP_ID = "635315126575992";
	
	private static final String[] PERMISSIONS = new String[] {"publish_stream", "read_stream", "offline_access"};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.checkin, menu);
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



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_checkin);
		
		
		Intent listIntent = getIntent();
		String location = listIntent.getExtras().getString("name");
		
		TextView textView = (TextView) findViewById(R.id.location1);
//		String str =  new String(textView.getText().toString());
		textView.setText(location);

		textView = (TextView) findViewById(R.id.location2);
//		String str =  new String(textView.getText().toString());
		textView.setText("N 1st street San Jose");

		SeekArc seekArc = (SeekArc)  findViewById(R.id.seekArc);
		
		seekArc.setProgressWidth(10);
		seekArc.setArcWidth(10);
		seekArc.setRotation(315f);
		//seekArc.setStartAngle(270);
		seekArc.setSweepAngle(90);
		
		seekArc.setOnSeekArcChangeListener(new OnSeekArcChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekArc seekArc) {	
			}		
			@Override
			public void onStartTrackingTouch(SeekArc seekArc) {
			}
			
			@Override
			public void onProgressChanged(SeekArc seekArc, int progress,
					boolean fromUser) {
				//mSeekArcProgress.setText(String.valueOf(progress));
				
				System.out.println(String.valueOf(progress));
			}
		});


		
        mFacebookBtn	= (CheckBox) findViewById(R.id.cb_facebook);
        
        mFacebook		= new Facebook(APP_ID);
        
        SessionStore.restore(mFacebook, this);
        
        if (mFacebook.isSessionValid()) {
			mFacebookBtn.setChecked(true);
			
			String name = SessionStore.getName(this);
			name		= (name.equals("")) ? "Unknown" : name;
			
			mFacebookBtn.setText("  Facebook (" + name + ")");
			mFacebookBtn.setTextColor(Color.WHITE);
		}
        
        mFacebookBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onFacebookClick();
			}
		});
		

	}
	
	
	private void fbLogout() {
			
		new Thread() {
			@Override
			public void run() {
				SessionStore.clear(CheckinActivity.this);
		        	   
				int what = 1;
					
		        try {
		        	mFacebook.logout(CheckinActivity.this);
		        		 
		        	what = 0;
		        } catch (Exception ex) {
		        	ex.printStackTrace();
		        }
		        	
		        mHandler.sendMessage(mHandler.obtainMessage(what));
			}
		}.start();
	}

    private void onFacebookClick() {
		if (mFacebook.isSessionValid()) {
//			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			
//			builder.setMessage("Delete current Facebook connection?")
//			       .setCancelable(false)
//			       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			        	   fbLogout();
//			           }
//			       })
//			       .setNegativeButton("No", new DialogInterface.OnClickListener() {
//			           public void onClick(DialogInterface dialog, int id) {
//			                dialog.cancel();
//			                
//			                mFacebookBtn.setChecked(true);
//			           }
//			       });
//			
//			final AlertDialog alert = builder.create();
//			
//			alert.show();
			
			// show different screen
			
			Intent userHome = new Intent(CheckinActivity.this, FaceBookPost.class);			

			startActivity(userHome);

		} else {
			mFacebookBtn.setChecked(false);
			
			mFacebook.authorize(this, PERMISSIONS, -1, new FbLoginDialogListener());
		}
	}


	
	
	   private final class FbLoginDialogListener implements DialogListener {
	        public void onComplete(Bundle values) {
	            SessionStore.save(mFacebook, CheckinActivity.this);
	           
	            mFacebookBtn.setText("  Facebook (No Name)");
	            mFacebookBtn.setChecked(true);
				mFacebookBtn.setTextColor(Color.WHITE);
				 
	            getFbName();
	        }

	        public void onFacebookError(FacebookError error) {
	           Toast.makeText(CheckinActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show();
	           
	           mFacebookBtn.setChecked(false);
	        }
	        
	        public void onError(DialogError error) {
	        	Toast.makeText(CheckinActivity.this, "Facebook connection failed", Toast.LENGTH_SHORT).show(); 
	        	
	        	mFacebookBtn.setChecked(false);
	        }

	        public void onCancel() {
	        	mFacebookBtn.setChecked(false);
	        }
	    }
	    
		private void getFbName() {
			
			new Thread() {
				@Override
				public void run() {
			        String name = "";
			        int what = 1;
			        
			        try {
			        	String me = mFacebook.request("me");
			        	
			        	JSONObject jsonObj = (JSONObject) new JSONTokener(me).nextValue();
			        	name = jsonObj.getString("name");
			        	what = 0;
			        } catch (Exception ex) {
			        	ex.printStackTrace();
			        }
			        
			        mFbHandler.sendMessage(mFbHandler.obtainMessage(what, name));
				}
			}.start();
		}

		private Handler mFbHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				
				if (msg.what == 0) {
					String username = (String) msg.obj;
			        username = (username.equals("")) ? "No Name" : username;
			            
			        SessionStore.saveName(username, CheckinActivity.this);
			        
			        mFacebookBtn.setText("  Facebook (" + username + ")");
			         
			        Toast.makeText(CheckinActivity.this, "Connected to Facebook as " + username, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(CheckinActivity.this, "Connected to Facebook", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
		private Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				
				if (msg.what == 1) {
					Toast.makeText(CheckinActivity.this, "Facebook logout failed", Toast.LENGTH_SHORT).show();
				} else {
					mFacebookBtn.setChecked(false);
		        	mFacebookBtn.setText("  Facebook (Not connected)");
		        	mFacebookBtn.setTextColor(Color.GRAY);
		        	   
					Toast.makeText(CheckinActivity.this, "Disconnected from Facebook", Toast.LENGTH_SHORT).show();
				}
			}
		};
		
}
