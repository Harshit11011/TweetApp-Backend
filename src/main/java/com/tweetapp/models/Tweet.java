package com.tweetapp.models;

import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.dto.TweetRepliesDto;

@Document(collection = "Tweet")
public class Tweet {

	@Id
	private Long tweetId;
	private String tweetMessage;
	private String tweetTag;
	private Date createdDate;
	private Integer likes;
	private List<TweetRepliesDto> reply;
	private String userName;

	public Tweet() {
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

	public List<TweetRepliesDto> getReply() {
		return reply;
	}

	public void setReply(List<TweetRepliesDto> reply) {
		this.reply = reply;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "Tweet [tweetId=" + tweetId + ", tweetMessage=" + tweetMessage + ", tweetTag=" + tweetTag
				+ ", createdDate=" + createdDate + ", likes=" + likes + ", reply=" + reply + ", userName=" + userName
				+ "]";
	}

}
