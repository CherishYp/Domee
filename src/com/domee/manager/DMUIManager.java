package com.domee.manager;

import com.domee.activity.DMFriendsTimelineActivity;
import com.domee.activity.MainActivity;

public class DMUIManager {

	private static DMUIManager dmUIManager = new DMUIManager();
	
	private DMUIManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static DMUIManager getInstance() {
		return dmUIManager;
	}
	
	public DMFriendsTimelineActivity friendsTimelineActivity;
	public MainActivity mainActivity;
	public DMFriendsTimelineActivity getFriendsTimelineActivity() {
		return friendsTimelineActivity;
	}

	public void setFriendsTimelineActivity(
			DMFriendsTimelineActivity friendsTimelineActivity) {
		this.friendsTimelineActivity = friendsTimelineActivity;
	}
	public MainActivity getMainActivity() {
		return mainActivity;
	}

	public void setMainActivity(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
	}
	
	
}
