package com.example.paypal;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.BaseRequestListener;
import com.facebook.android.Facebook;
import com.facebook.android.SessionStore;

public class FaceBookPost extends Activity {
	private Facebook mFacebook;
	private ProgressDialog mProgress;
	
	private Handler mRunOnUi = new Handler();

	private static final String APP_ID = "635315126575992";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_face_book_post);
		
		final EditText reviewEdit = (EditText) findViewById(R.id.revieew);
		
		mProgress	= new ProgressDialog(this);
		
		mFacebook 	= new Facebook(APP_ID);
		
		SessionStore.restore(mFacebook, this);

		if (mFacebook.isSessionValid()) {
		}
		
		((Button) findViewById(R.id.button1)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String review = reviewEdit.getText().toString();
				
				if (review.equals("")) return;
			
				postToFacebook(review);
			}
		});

	}

	private void postToFacebook(String review) {	
		mProgress.setMessage("Posting ...");
		mProgress.show();
		
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(mFacebook);
		
		Bundle params = new Bundle();
    		
		params.putString("message", review);
		params.putString("name", "Checked at Starbucks");
		params.putString("caption", "Integrated Checkin through Paypal App");
		params.putString("link", "http://www.starbucks.com");
		params.putString("description", "Paypal - People's choice of payment");
		params.putString("picture", "http://blog.litcentral.com/wp-content/uploads/image/Coffee%20Time.jpg");
		
		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener());
	}

	private final class WallPostListener extends BaseRequestListener {
        public void onComplete(final String response) {
        	mRunOnUi.post(new Runnable() {
        		@Override
        		public void run() {
        			mProgress.cancel();
        			
        			Toast.makeText(FaceBookPost.this, "Posted to Facebook", Toast.LENGTH_SHORT).show();
        		}
        	});
        }
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.face_book_post, menu);
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
