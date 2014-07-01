package com.rakeshcm.apps.basictwitter.fragments;

import com.rakeshcm.apps.basictwitter.FragmentType;

import android.os.Bundle;

public class MentionsTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fetchMentionsTimelineTweets();
	}
	
	public void fetchMentionsTimelineTweets() {
		setFragmentType(FragmentType.Mentions);
		populateMentionsTimeline(false, 0L);
	}
}
