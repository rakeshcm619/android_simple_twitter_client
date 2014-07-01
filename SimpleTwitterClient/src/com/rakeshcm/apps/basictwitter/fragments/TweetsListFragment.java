package com.rakeshcm.apps.basictwitter.fragments;

import java.util.ArrayList;

import org.json.JSONArray;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.rakeshcm.apps.basictwitter.FragmentType;
import com.rakeshcm.apps.basictwitter.R;
import com.rakeshcm.apps.basictwitter.TweetArrayAdapter;
import com.rakeshcm.apps.basictwitter.TwitterApplication;
import com.rakeshcm.apps.basictwitter.TwitterClient;
import com.rakeshcm.apps.basictwitter.models.Tweet;

public class TweetsListFragment extends Fragment {
	
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private TweetArrayAdapter aTweets;
	private ListView lvTweets;
	
	private long minUid = Long.MAX_VALUE;
	private long minMentionsUid = Long.MAX_VALUE;
	private long minUserUid = Long.MAX_VALUE;
	private long currentUserId = 0L;
	private boolean isFetchComplete = false;
	private FragmentType fragmentType;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);		
		//Assign view references
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		
		setItemClickListener();
		setScrollListener();
		
		return v;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();	
		
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
		setFragmentType(FragmentType.Home);
	}
	
	// Private and public methods.
	public void setFragmentType(FragmentType argFragmentType) {
		fragmentType = argFragmentType;
	}
	
	private void setItemClickListener() {
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	private void setScrollListener() {
		lvTweets.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if ((firstVisibleItem + visibleItemCount + 1 >= totalItemCount) && isFetchComplete) {
					Log.d("debug", "Scroll " + ", " + firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount  + ", " + (minUid-1) + ", " + isFetchComplete);
					isFetchComplete = false;
					
					if(fragmentType == FragmentType.Home) {
						populateHomeTimeline(true, minUid-1);
					}
					else if(fragmentType == FragmentType.Mentions) {
						populateMentionsTimeline(true, minMentionsUid-1);
					}
					else if(fragmentType == FragmentType.User) {
						populateUserTimeline(true, minUserUid-1, currentUserId);
					}
				}
			}
		});
	}
	
	public void populateHomeTimeline(final boolean isPagination, final long maxId) {
		isFetchComplete = false;
		client.getPaginatedHomeTimeline(maxId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				commonOnSuccess(json, isPagination);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}
	
	public void populateMentionsTimeline(final boolean isPagination, final long maxId) {
		isFetchComplete = false;
		client.getPaginatedMentionsTimeline(maxId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				commonOnSuccess(json, isPagination);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	}
	
	public void populateUserTimeline(final boolean isPagination, final Long maxId, final Long userId) {
		isFetchComplete = false;
		currentUserId = userId;
		client.getPaginatedUserTimeline(userId, maxId, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray json) {
				commonOnSuccess(json, isPagination);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}
		});
	} 
	
	private void commonOnSuccess(JSONArray json, boolean isPagination) {
		ArrayList<Tweet> allReturnedTweets = Tweet.fromJsonArray(json);
		if(fragmentType == FragmentType.Home) {
			if(!isPagination){	
				minUid = Long.MAX_VALUE;
			}
			minUid = Tweet.findMinUid(minUid, allReturnedTweets);
			Log.d("debug", "Home: max_id: " + minUid + ", " + isFetchComplete + ", pagination: " + isPagination);
		}
		else if(fragmentType == FragmentType.Mentions) {
			if(!isPagination){	
				minMentionsUid = Long.MAX_VALUE;
			}
			minMentionsUid = Tweet.findMinUid(minMentionsUid, allReturnedTweets);
			Log.d("debug", "Mentions: max_id: " + minMentionsUid + ", " + isFetchComplete + ", pagination: " + isPagination);
		}
		else if(fragmentType == FragmentType.User) {
			if(!isPagination){	
				minUserUid = Long.MAX_VALUE;
			}
			minUserUid = Tweet.findMinUid(minUserUid, allReturnedTweets);
			Log.d("debug", "User: " + allReturnedTweets + ", max_id: " + minUserUid + ", " + isFetchComplete + ", pagination: " + isPagination);
		}
		if(!isPagination){	
			tweets.clear();
			Log.d("debug", "tweets Cleared");
		}
		tweets.addAll(allReturnedTweets);
		aTweets.notifyDataSetChanged();
		Log.d("debug", "tweetsCount: " + tweets.size() + ", allReturnedTweets: " + allReturnedTweets.size());
		isFetchComplete = true;
	}
}
