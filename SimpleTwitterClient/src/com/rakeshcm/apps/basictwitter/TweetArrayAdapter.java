package com.rakeshcm.apps.basictwitter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rakeshcm.apps.basictwitter.models.Tweet;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	public TweetArrayAdapter(Context context, ArrayList<Tweet> tweets) {
	    super(context, R.layout.tweet_item, tweets);
	}
	
	 @Override
	 public View getView(int position, View convertView, ViewGroup parent) {
		// Get the data item for this position
		Tweet tweet = this.getItem(position); 
		
		// Find or inflate the template
		View v;
		if (convertView == null) {
		   LayoutInflater inflater = LayoutInflater.from(getContext());
		   v = inflater.inflate(R.layout.tweet_item, parent, false);
		}
		else {
			v = convertView;
		}
		
		// Find the views within template
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvScreenName = (TextView) v.findViewById(R.id.tvScreenName);
		TextView tvProfileName = (TextView) v.findViewById(R.id.tvProfleName);
		TextView tvTweetBody = (TextView) v.findViewById(R.id.tvTweetBody);
		TextView tvTimeAgo = (TextView) v.findViewById(R.id.tvTimeAgo);
		ivProfileImage.setImageResource(android.R.color.transparent);
		
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvScreenName.setText("@" + tweet.getUser().getScreenName());
		tvProfileName.setText(tweet.getUser().getName());
		tvTweetBody.setText(tweet.getBody());
		tvTimeAgo.setText(tweet.getRelativeTimeAgo(tweet.getCreatedAt()));

		return v;
	}
}
