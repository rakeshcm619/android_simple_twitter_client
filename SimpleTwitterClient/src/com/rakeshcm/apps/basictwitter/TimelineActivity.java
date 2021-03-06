package com.rakeshcm.apps.basictwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.rakeshcm.apps.basictwitter.fragments.HomeTimelineFragment;
import com.rakeshcm.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.rakeshcm.apps.basictwitter.listeners.FragmentTabListener;

public class TimelineActivity extends FragmentActivity {
	
	private HomeTimelineFragment fragmentHomeTimelineList;
	private MentionsTimelineFragment fragmentMentionsTimelineList;
	private int TWEETS_REQUEST_CODE = 23;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		setupTabs();
		getSupportFragmentManager().executePendingTransactions();
		fragmentHomeTimelineList = (HomeTimelineFragment) getSupportFragmentManager().findFragmentByTag("home");
		fragmentMentionsTimelineList = (MentionsTimelineFragment) getSupportFragmentManager().findFragmentByTag("mentions");
		//fragmentHomeTimelineList.populateHomeTimeline(false, 0L);
		Log.d("debug", "TimelineActivity: fragmentHomeTimelineList: " + fragmentHomeTimelineList + ", fragmentMentionsTimelineList: " + fragmentMentionsTimelineList);
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home",
						HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions",
			    			MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeline_tweet, menu);
        return true;
    }
	
	public void onNewTweet(MenuItem mi) {
		Intent i = new Intent(this, ComposeTweetActivity.class);
		startActivityForResult(i, TWEETS_REQUEST_CODE);
	}
	
	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		i.putExtra("myprofile", true);
		i.putExtra("userid", 0L);
		startActivity(i);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.d("debug", "Tweet Succesfully posted" + resultCode + ", " + requestCode + "{" + RESULT_OK + "," + RESULT_CANCELED + "}");
		if (requestCode == TWEETS_REQUEST_CODE) {
			Log.d("debug", "Tweet Succesfully posted");
			if (fragmentHomeTimelineList != null) {
				fragmentHomeTimelineList.fetchHomeTimelineTweets();
			}
			if (fragmentMentionsTimelineList != null) {
				fragmentMentionsTimelineList.fetchMentionsTimelineTweets();
			}
		}
	}
}
