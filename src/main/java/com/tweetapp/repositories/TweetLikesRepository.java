package com.tweetapp.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.models.TweetLikes;

@Repository
public interface TweetLikesRepository extends MongoRepository<TweetLikes,String>{

}
