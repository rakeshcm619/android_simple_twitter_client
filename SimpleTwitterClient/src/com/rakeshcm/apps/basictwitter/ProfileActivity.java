package com.rakeshcm.apps.basictwitter;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rakeshcm.apps.basictwitter.models.User;

public class ProfileActivity extends FragmentActivity {
	private TwitterClient client;
	ImageView ivMyProfileImage;
	TextView tvMyProfileName;
	TextView tvMyScreenName;
	TextView tvFollowers;
	TextView tvFollowing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		ivMyProfileImage = (ImageView) findViewById(R.id.ivMyProfileImage);
		tvMyProfileName = (TextView) findViewById(R.id.tvMyProfileName);
		tvMyScreenName = (TextView) findViewById(R.id.tvMyScreenName);
		tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		
		client = TwitterApplication.getRestClient();
		populateUser();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }
	
	public void populateUser() {
		client.getUserInfo(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User authUser = User.fromJson(json);
				getActionBar().setTitle("@" + authUser.getScreenName());
				ivMyProfileImage.setImageResource(android.R.color.transparent);
				
				ImageLoader imageLoader = ImageLoader.getInstance();
				imageLoader.displayImage(authUser.getProfileImageUrl(), ivMyProfileImage);
				tvMyScreenName.setText("@" + authUser.getScreenName());
				tvMyProfileName.setText(authUser.getName());
				tvFollowers.setText(String.valueOf(authUser.getFollowersCount()) + " followers");
				tvFollowing.setText(String.valueOf(authUser.getFriendsCount()) + " following");
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
