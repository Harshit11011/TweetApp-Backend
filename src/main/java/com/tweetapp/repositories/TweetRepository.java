package com.tweetapp.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.models.Tweet;


@Repository
public interface TweetRepository extends MongoRepository<Tweet,Long> {
	
	List<Tweet>findByUserName(String userName);
	
	Tweet findByTweetId(Long id);
	
	void deleteByTweetId(long id);
	
	
	
}

