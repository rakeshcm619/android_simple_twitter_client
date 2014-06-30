package com.rakeshcm.apps.basictwitter.fragments;

import com.rakeshcm.apps.basictwitter.FragmentType;

import android.os.Bundle;

public class UserTimelineFragment extends TweetsListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setFragmentType(FragmentType.User);
		populateUserTimeline(false, Long.MAX_VALUE);
	}
}
