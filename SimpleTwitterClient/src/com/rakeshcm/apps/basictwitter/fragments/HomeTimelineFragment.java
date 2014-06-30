package com.rakeshcm.apps.basictwitter.fragments;

import com.rakeshcm.apps.basictwitter.FragmentType;

import android.os.Bundle;

public class HomeTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFragmentType(FragmentType.Home);
		populateHomeTimeline(false, Long.MAX_VALUE);
	}
}
