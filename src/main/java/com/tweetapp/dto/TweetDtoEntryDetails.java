package com.tweetapp.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class TweetDtoEntryDetails {
	
	@NotBlank(message="Tweet message Should not be blank")
	@Size(max=144 ,message="Tweet message should not be greater than 144 characters.")
	private String tweetMessage;
	@Size(max=50, message="Tag should not be greater than 50 characters.")
	private String tweetTag;
	
	
	public TweetDtoEntryDetails() {
	}
	public String getTweetMessage() {
		return tweetMessage;
	}
	public void setTweetMessage(String tweetMessage) {
		this.tweetMessage = tweetMessage;
	}
	public String getTweetTag() {
		return tweetTag;
	}
	public void setTweetTag(String tweetTag) {
		this.tweetTag = tweetTag;
	}
	@Override
	public String toString() {
		return "TweetDtoEntryDetails [tweetMessage=" + tweetMessage + ", tweetTag=" + tweetTag + "]";
	}
	
	
}
