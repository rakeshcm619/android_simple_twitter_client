package com.rakeshcm.apps.basictwitter;

import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rakeshcm.apps.basictwitter.models.User;

public class ComposeTweetActivity extends Activity {
	private TwitterClient client;
	ImageView ivUserProfileImage;
	TextView tvUserProfileName;
	TextView tvUserScreenName;
	EditText etTweetText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_compose_tweet);
		
		ivUserProfileImage = (ImageView) findViewById(R.id.ivUserProfileImage);
		tvUserProfileName = (TextView) findViewById(R.id.tvUserProfileName);
		tvUserScreenName = (TextView) findViewById(R.id.tvUserScreenName);
		etTweetText = (EditText) findViewById(R.id.etTweetText);
		
		client = TwitterApplication.getRestClient();
		populateUser();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_tweet, menu);
        return true;
    }
	
	public void onPostTweet(MenuItem mi) {
		String tweetBodyText = etTweetText.getText().toString();
		
		client.postTweetInfo(tweetBodyText, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				setResult(RESULT_OK);
				Log.d("debug", "RESULT_OK = " + RESULT_OK);
				finish();
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
				Toast.makeText(getBaseContext(), "Something went wrong! Try again.", Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				finish();
			}
		});
		
		finish();
	}
	
	public void populateUser() {
		client.getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User authUser = User.fromJson(json);
				ivUserProfileImage.setImageResource(android.R.color.transparent);
				
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(authUser.getProfileImageUrl(), ivUserProfileImage);
				tvUserScreenName.setText("@" + authUser.getScreenName());
				tvUserProfileName.setText(authUser.getName());
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
				Toast.makeText(getBaseContext(), "Something went wrong! Try again.", Toast.LENGTH_SHORT).show();
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}
}
