package com.rakeshcm.apps.basictwitter.fragments;

import com.rakeshcm.apps.basictwitter.FragmentType;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchUserTimelineTweets(0L);
	}
	
	public void fetchUserTimelineTweets(long userId) {
		setFragmentType(FragmentType.User);
		populateUserTimeline(false, 0L, userId);
	}
}
