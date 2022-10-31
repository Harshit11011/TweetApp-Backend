package com.tweetapp.dto;

import java.util.Date;

public class TweetRepliesDto {

	private String replyId;
	private long parentTweetId;
	private String message;
	private String tag;
	private String userName;
	private Date replyDate;

	public TweetRepliesDto() {
	}

	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}

	public long getParentTweetId() {
		return parentTweetId;
	}

	public void setParentTweetId(long parentTweetId) {
		this.parentTweetId = parentTweetId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Date replyDate) {
		this.replyDate = replyDate;
	}

	@Override
	public String toString() {
		return "TweetRepliesDto [replyId=" + replyId + ", parentTweetId=" + parentTweetId + ", message=" + message
				+ ", tag=" + tag + ", userName=" + userName + ", replyDate=" + replyDate + "]";
	}

	
}
