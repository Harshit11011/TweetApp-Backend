package com.tweetapp.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;

public class TweetDto {

	public static final String SEQUENCE_NAME="tweet_sequence";

	@Id
	private Long tweetId;
	@Size(max=144 ,message="Tweet should not be greater than 144 characters.")
	private String tweetMessage;
	@Size(max=50, message="Tag should not be greater than 50 characters.")
	private String tweetTag;
	private Date createdDate;
	private Integer likes;
	private String userName;

	private List<TweetRepliesDto> reply;
	
	

	public TweetDto() {
	}

	public Long getTweetId() {
		return tweetId;
	}

	public void setTweetId(Long tweetId) {
		this.tweetId = tweetId;
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

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getLikes() {
		return likes;
	}

	public void setLikes(Integer likes) {
		this.likes = likes;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<TweetRepliesDto> getReply() {
		return reply;
	}

	public void setReply(List<TweetRepliesDto> reply) {
		this.reply = reply;
	}

	@Override
	public String toString() {
		return "TweetDto [tweetId=" + tweetId + ", tweetMessage=" + tweetMessage + ", tweetTag=" + tweetTag
				+ ", createdDate=" + createdDate + ", likes=" + likes + ", userName=" + userName + ", reply=" + reply
				+ "]";
	}

	
	
}
