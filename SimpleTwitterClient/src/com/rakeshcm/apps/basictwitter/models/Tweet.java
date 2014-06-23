package com.rakeshcm.apps.basictwitter.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

public class Tweet {
	private long uid;
	private String body;
	private String createdAt;
	private User user;
	
	public long getUid() {
		return uid;
	}

	public String getBody() {
		return body;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}
	
	public static Tweet fromJson(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		//Extract values form json to populate the member variables
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = jsonObject.getString("created_at");
			tweet.user = User.fromJson(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return tweet;
	}

	public static ArrayList<Tweet> fromJsonArray(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		for(int x=0; x < jsonArray.length(); x++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(x);
			} catch (JSONException ex) {
				ex.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJson(tweetJson);
			if(tweet != null) {
				tweets.add(tweet);
			}
		}
		return tweets;
	}
	
	public static long findMinUid(long minUid, ArrayList<Tweet> resultListTweets) {
		long newMinUid = minUid;
		for(Tweet eachTweet : resultListTweets) {
			if(eachTweet.getUid() < newMinUid) {
				newMinUid = eachTweet.getUid();
			}
		}
		return newMinUid;
	}
	
	// getRelativeTimeAgo("Mon Apr 01 21:16:23 +0000 2014");
	public String getRelativeTimeAgo(String rawJsonDate) {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(rawJsonDate).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return shortenRelativeTimeString(relativeDate);
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getBody() + ", " + getUser().getScreenName();
	}
	
	private String shortenRelativeTimeString(String relativeDate) {
		String shortnedRelativeDate = ""; 
		if(relativeDate.contains(" second ago")) shortnedRelativeDate = relativeDate.replaceAll(" second ago", "s");
		else if(relativeDate.contains(" seconds ago")) shortnedRelativeDate = relativeDate.replaceAll(" seconds ago", "s");
		else if(relativeDate.contains(" seconds")) shortnedRelativeDate = relativeDate.replaceAll(" seconds", "s");
		else if(relativeDate.contains(" minute ago")) shortnedRelativeDate = relativeDate.replaceAll(" minute ago", "m");
		else if(relativeDate.contains(" minutes ago")) shortnedRelativeDate = relativeDate.replaceAll(" minutes ago", "m");
		else if(relativeDate.contains(" hour ago")) shortnedRelativeDate = relativeDate.replaceAll(" hour ago", "h");
		else if(relativeDate.contains(" hours ago")) shortnedRelativeDate = relativeDate.replaceAll(" hours ago", "h");
		else if(relativeDate.contains(" day ago")) shortnedRelativeDate = relativeDate.replaceAll(" day ago", "d");
		else if(relativeDate.contains(" days ago")) shortnedRelativeDate = relativeDate.replaceAll(" days ago", "d");
		else shortnedRelativeDate = relativeDate;
		return shortnedRelativeDate;
	}
}
