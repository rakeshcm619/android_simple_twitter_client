package com.rakeshcm.apps.basictwitter.fragments;

import com.rakeshcm.apps.basictwitter.FragmentType;

import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchHomeTimelineTweets();
	}
	
	public void fetchHomeTimelineTweets() {
		setFragmentType(FragmentType.Home);
		populateHomeTimeline(false, 0L);
	}
}
