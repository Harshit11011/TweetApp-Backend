package com.tweetapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tweetapp.dto.TweetDto;
import com.tweetapp.dto.TweetDtoEntryDetails;

@Service
public interface TweetService {
	
	//get all tweets 
	List<TweetDto> getAllTweets() throws Exception;
	
	//get all tweets of user	
	List<TweetDto> getAllTweetsByUserName(String userName) throws Exception;
	
	//post new tweet  
	TweetDto saveTweet(TweetDtoEntryDetails tweetDtoEntryDetails, String userName) throws Exception;
	
	//update tweet
	TweetDto updateTweet(TweetDtoEntryDetails tweetDtoEntryDetails, String userName, Long id) throws Exception;
	
	//delete tweet
	String deleteTweet(String userName, Long id) throws Exception;

	//post reply fot the tweet
	TweetDto postReplyForTweets(TweetDtoEntryDetails tweetDtoEntryDetails,String userName, Long id) throws Exception;
	
	//manage likes for Tweets
	TweetDto manageLikesForTweets(String userName, Long id) throws Exception;

}
