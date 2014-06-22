package com.rakeshcm.apps.basictwitter;

import java.util.ArrayList;

import org.json.JSONArray;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rakeshcm.apps.basictwitter.models.Tweet;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;
	
	private long minUid = 1;
	private boolean isFetchComplete = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		client = TwitterApplication.getRestClient();	
		populateTimeline();
		Log.d("debug", "TimelineActivity");
		lvTweets = (ListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
		
		lvTweets.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount + 1 >= totalItemCount) && isFetchComplete) {
					Log.d("debug", "Scroll " + ", " + firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount  + ", " + (minUid-1) + ", " + isFetchComplete);
					isFetchComplete = false;
					Log.d("debug", "Scroll " + ", " + firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount  + ", " + (minUid-1) + ", " + isFetchComplete);
					client.getPaginatedHomeTimeline(minUid-1, new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(JSONArray json) {
							ArrayList<Tweet> allReturnedTweets = Tweet.fromJsonArray(json);
							minUid = Tweet.findMinUid(minUid, allReturnedTweets);
							tweets.addAll(allReturnedTweets);
							aTweets.notifyDataSetChanged();
							isFetchComplete = true;
							Log.d("debug", "Paginated Scroll " + (minUid-1) + ", " + isFetchComplete);
						}
						
						@Override
						public void onFailure(Throwable e, String s) {
							Log.d("debug", e.toString());
							Log.d("debug", s.toString());
						}
					});
				}
			}
		});
	}
	
	public void populateTimeline() {
		isFetchComplete = false;
		client.getHomeTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				ArrayList<Tweet> allReturnedTweets = Tweet.fromJsonArray(json);
				minUid = Tweet.findMinUid(minUid, allReturnedTweets);
				tweets.clear();
				tweets.addAll(allReturnedTweets);
				aTweets.notifyDataSetChanged();
				isFetchComplete = true;
				Log.d("debug", "Scroll " + minUid + ", " + isFetchComplete);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}
}
