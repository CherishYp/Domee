package com.domee.manager;

import com.domee.activity.*;
import com.domee.model.DMComment;

public class DMUIManager {

	private static DMUIManager dmUIManager = new DMUIManager();
	
	private DMUIManager() {
		// TODO Auto-generated constructor stub
	}
	
	public static DMUIManager getInstance() {
		return dmUIManager;
	}
	
	public DMFriendsTimelineActivity friendsTimelineActivity;
    public DMAtActivity mAtActivity;
    public DMCommentActivity mCommentActivity;
    public DMProfileActivity mProfileActivity;
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

    public DMAtActivity getmAtActivity() {
        return mAtActivity;
    }

    public void setmAtActivity(DMAtActivity mAtActivity) {
        this.mAtActivity = mAtActivity;
    }

    public DMCommentActivity getmCommentActivity() {
        return mCommentActivity;
    }

    public void setmCommentActivity(DMCommentActivity mCommentActivity) {
        this.mCommentActivity = mCommentActivity;
    }

    public DMProfileActivity getmProfileActivity() {
        return mProfileActivity;
    }

    public void setmProfileActivity(DMProfileActivity mProfileActivity) {
        this.mProfileActivity = mProfileActivity;
    }
}
