package com.tweetapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "TweetLikes")
public class TweetLikes {

	@Id
	private String likesId;
	private Long tweetId;
	private String userName;

	public TweetLikes() {
	}

	public String getLikesId() {
		return likesId;
	}

	public void setLikesId(String likesId) {
		this.likesId = likesId;
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
