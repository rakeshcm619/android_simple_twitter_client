package com.rakeshcm.apps.basictwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
	private long uid;
	private String name;
	private String screenName;
	private String profileImageUrl;
	private int followersCount;
	private int friendsCount;
	
	public int getFollowersCount() {
		return followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public long getUid() {
		return uid;
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static User fromJson(JSONObject json) {
		User u = new User();
		//Extract values form json to populate the member variables
		try {
			u.name = json.getString("name");
			u.uid = json.getLong("id");
			u.screenName = json.getString("screen_name");
			u.profileImageUrl = json.getString("profile_image_url");
			u.followersCount = json.getInt("followers_count");
			u.friendsCount = json.getInt("friends_count");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return u;
	}
	
	@Override
	public String toString() {
		return getName() + ", " + getScreenName();
	}
}
