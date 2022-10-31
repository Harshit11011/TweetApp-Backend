package com.tweetapp.controllers;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.configurations.GlobalConstants;
import com.tweetapp.dto.TweetDto;
import com.tweetapp.dto.TweetDtoEntryDetails;
import com.tweetapp.services.TweetService;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins="*")
public class TweetController {

	private static final Logger logger = LogManager.getLogger(TweetController.class);

	@Autowired
	private TweetService TweetService;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;


	// getting all Tweets
	@GetMapping("/all")
	public ResponseEntity<List<TweetDto>> findAllTweetsOfAllUsers() throws Exception {
		List<TweetDto> tweetsList = this.TweetService.getAllTweets();
		return new ResponseEntity<>(tweetsList, HttpStatus.OK);
	}

	// getting all Tweets of User
	@GetMapping("/{userName}")
	public ResponseEntity<List<TweetDto>> findAllTweetsOfUser(@PathVariable String userName) throws Exception {
		String informativeMassage= "Searching the tweets of user : "+userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY,informativeMassage);
		List<TweetDto> tweetsList = this.TweetService.getAllTweetsByUserName(userName);
		return new ResponseEntity<>(tweetsList, HttpStatus.OK);
	}

	// Adding new Tweet
	@PostMapping("/{userName}/add")
	public ResponseEntity<TweetDto> addTweet(@RequestBody @Valid TweetDtoEntryDetails tweetDtoEntryDetails,
			@PathVariable String userName) throws Exception {
		String informativeMassage= "Adding the tweet : "+ tweetDtoEntryDetails +" For the user : "+userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY,informativeMassage);
		TweetDto temp = this.TweetService.saveTweet(tweetDtoEntryDetails, userName);
		String message = String.format("Tweet is added for the user with userName %s.", userName);
		return new ResponseEntity<TweetDto>(temp, HttpStatus.CREATED);
	}

	// updating the users tweet
	@PutMapping("/{userName}/update/{id}")
	public ResponseEntity<TweetDto> updateUsersTweet(@RequestBody @Valid TweetDtoEntryDetails tweetDtoEntryDetails,
			@PathVariable String userName, @PathVariable Long id) throws Exception {
		
		String informativeMassage= "Updating the tweet : "+ tweetDtoEntryDetails +" For the user : "+userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY,informativeMassage);
		TweetDto temp = this.TweetService.updateTweet(tweetDtoEntryDetails, userName, id);
		String message = String.format("Tweet is update for the user with userName %s.", userName);
		return new ResponseEntity<TweetDto>(temp, HttpStatus.CREATED);
	}

	// deleting the tweet
	@DeleteMapping("/{userName}/delete/{id}")
	public ResponseEntity<String> deleteUsersTweet(@PathVariable String userName, @PathVariable Long id)
			throws Exception {
		String informativeMassage= "Deleting the tweet with tweetId : "+ id +" For the user : "+userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY,informativeMassage);
		this.TweetService.deleteTweet(userName, id);
		String message = String.format("Tweet is deleted for the user with userName %s.", userName);

		return new ResponseEntity<String>(message, HttpStatus.CREATED);
	}

	// managing the likes of users tweets
	@PutMapping("/{userName}/like/{id}")
	public ResponseEntity<TweetDto> updateLikesOfUsersTweet(@PathVariable String userName, @PathVariable Long id)
			throws Exception {
		String informativeMassage= "Liking the tweet with tweetId : "+ id +" For the user : "+userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY,informativeMassage);
		TweetDto temp = this.TweetService.manageLikesForTweets(userName, id);
		String message = String.format("Tweet is update for the user with userName %s.", userName);
		return new ResponseEntity<TweetDto>(temp, HttpStatus.CREATED);
	}

	// Posting the replies of users tweets
	@PostMapping("/{userName}/reply/{id}")
	public ResponseEntity<TweetDto> addRepliesOftweets(@RequestBody @Valid TweetDtoEntryDetails tweetDtoEntryDetails,
			@PathVariable String userName, @PathVariable Long id) throws Exception {
		String informativeMassage= "Replying the tweet with details : "+ tweetDtoEntryDetails +" For the user : "+userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY,informativeMassage);
		TweetDto temp = this.TweetService.postReplyForTweets(tweetDtoEntryDetails, userName, id);
		String message = String.format("Tweet is update for the user with userName %s.", userName);
		return new ResponseEntity<TweetDto>(temp, HttpStatus.CREATED);
	}

}
