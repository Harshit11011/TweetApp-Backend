package com.tweetapp.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweetapp.dto.TweetDto;
import com.tweetapp.dto.TweetDtoEntryDetails;
import com.tweetapp.dto.TweetRepliesDto;
import com.tweetapp.dto.UserDto;
import com.tweetapp.exceptions.InvalidCredentialsException;
import com.tweetapp.exceptions.TweetNotFoundException;
import com.tweetapp.exceptions.UserNotFoundException;
import com.tweetapp.models.Tweet;
import com.tweetapp.models.TweetLikes;
import com.tweetapp.repositories.TweetLikesRepository;
import com.tweetapp.repositories.TweetRepository;
import com.tweetapp.sequences.TweetSequenceGeneratorService;

@Service
public class TweetServiceImpl implements TweetService {

	private static final Logger logger = LogManager.getLogger(TweetServiceImpl.class);

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserService userService;

	@Autowired
	private TweetRepository tweetRepository;

	@Autowired
	private TweetSequenceGeneratorService tweetSequenceGeneratorService;

	@Autowired
	private TweetLikesRepository tweetLikesRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	@Transactional(readOnly = true)
	public List<TweetDto> getAllTweets() throws Exception {
		// TODO Auto-generated method stub
		List<Tweet> tweets = this.tweetRepository.findAll();
		if (tweets == null) {
			throw new TweetNotFoundException("There are no tweets available in the application");
		}
		List<TweetDto> tweetDtos = tweets.stream().map(userTweet -> this.modelMapper.map(userTweet, TweetDto.class))
				.collect(Collectors.toList());
		logger.info("All the tweets available in the application are : "+tweetDtos);
		return tweetDtos;
	}

	@Override
	@Transactional(readOnly = true)
	public List<TweetDto> getAllTweetsByUserName(String userName) throws Exception {
		UserDto searchedUser = this.userService.getUserByName(userName);
		if (searchedUser == null) {
			throw new UserNotFoundException(
					String.format("User with username %s is not found in the application", userName));

		}
		List<Tweet> tweets = this.tweetRepository.findByUserName(userName);
		if (tweets == null) {
			throw new TweetNotFoundException(
					"There are no tweets available for the " + userName + " in the application");
		}

		List<TweetDto> tweetDtos = tweets.stream().map(userTweet -> this.modelMapper.map(userTweet, TweetDto.class))
				.collect(Collectors.toList());
		logger.info("All the tweets available for the user "+userName +" in the application are : "+tweetDtos);
		return tweetDtos;
	}

	@Override
	@Transactional
	public TweetDto saveTweet(TweetDtoEntryDetails tweetDtoEntryDetails, String userName) throws Exception {
		UserDto searchedUser = this.userService.getUserByName(userName);
		if (searchedUser == null) {
			throw new UserNotFoundException(
					String.format("User with username %s is not found in the application", userName));

		}

		TweetDto tempTweetDto = new TweetDto();
		tempTweetDto.setTweetId(tweetSequenceGeneratorService.generateSequence(TweetDto.SEQUENCE_NAME));
		tempTweetDto.setTweetMessage(tweetDtoEntryDetails.getTweetMessage());
		if (tweetDtoEntryDetails.getTweetTag() != null) {
			tempTweetDto.setTweetTag(tweetDtoEntryDetails.getTweetTag());
		}

		tempTweetDto.setCreatedDate(new Date());

		tempTweetDto.setLikes(0);
		tempTweetDto.setReply(null);
		tempTweetDto.setUserName(userName);

		Tweet userTweet = this.modelMapper.map(tempTweetDto, Tweet.class);
		this.tweetRepository.save(userTweet);
		logger.info("Saving the tweet with details "+userTweet +" for the user "+userName);
		return tempTweetDto;
	}

	@Override
	@Transactional
	public TweetDto updateTweet(TweetDtoEntryDetails tweetDtoEntryDetails, String userName, Long id) throws Exception {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("tweetId").is(id));
			query.addCriteria(Criteria.where("userName").is(userName));

			Tweet queryResultTweet = mongoTemplate.findOne(query, Tweet.class);
			if (queryResultTweet == null) {
				throw new InvalidCredentialsException("Invalid Details");
			}

			Update update = new Update();
			if (tweetDtoEntryDetails.getTweetMessage() != null) {
				update.set("tweetMessage", tweetDtoEntryDetails.getTweetMessage());
			}
			if (tweetDtoEntryDetails.getTweetTag() != null && (tweetDtoEntryDetails.getTweetTag().length() > 0)) {
				update.set("tweetTag", tweetDtoEntryDetails.getTweetTag());
			}
			update.set("createdDate", new Date());
			mongoTemplate.updateFirst(query, update, Tweet.class);
		} catch (Exception e) {
			throw new TweetNotFoundException("There are no tweets available for the " + userName + " with the tweetId "
					+ id + " in the application");
		}
		Tweet updatedTweet = this.tweetRepository.findByTweetId(id);
		logger.info("Updating the tweet with details "+updatedTweet +" for the user "+userName);
		return this.modelMapper.map(updatedTweet, TweetDto.class);

	}

	@Override
	@Transactional
	public String deleteTweet(String userName, Long id) throws Exception {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("tweetId").is(id));
			query.addCriteria(Criteria.where("userName").is(userName));

			Tweet queryResultTweet = mongoTemplate.findOne(query, Tweet.class);
			if (queryResultTweet == null) {
				throw new InvalidCredentialsException("Invalid Details");
			}
		} catch (Exception e) {
			throw new TweetNotFoundException("There are no tweets available for the " + userName + " with the tweetId "
					+ id + " in the application");
		}
		this.tweetRepository.deleteByTweetId(id);
		logger.info("Deleting the tweet with tweetId "+id +" for the user "+userName);
		return "Tweet deleted Successfully";

	}

	@Override
	@Transactional
	public TweetDto postReplyForTweets(TweetDtoEntryDetails tweetDtoEntryDetails, String userName, Long id)
			throws Exception {
		UserDto searchedUser = this.userService.getUserByName(userName);
		if (searchedUser == null) {
			throw new UserNotFoundException(
					String.format("User with username %s is not found in the application", userName));

		}
		Tweet tweet = this.tweetRepository.findByTweetId(id);
		if (tweet == null) {
			throw new TweetNotFoundException("There are no tweets with the tweetId " + id + " in the application");
		}
		String message = tweetDtoEntryDetails.getTweetMessage();
		String tag = tweetDtoEntryDetails.getTweetTag();

		TweetRepliesDto tweetReply = new TweetRepliesDto();
		UUID uuid = UUID.randomUUID();
		tweetReply.setReplyId(uuid.toString());
		tweetReply.setParentTweetId(id);
		tweetReply.setUserName(userName);
		tweetReply.setReplyDate(new Date());
		tweetReply.setMessage(message);
		if (tag != null) {
			tweetReply.setTag(tag);
		}
		if (tweet.getReply() != null) {
			tweet.getReply().add(tweetReply);
		} else {
			tweet.setReply(Arrays.asList(tweetReply));
		}

		Query query = new Query();
		query.addCriteria(Criteria.where("tweetId").is(id));
		Update update = new Update();
		update.set("reply", tweet.getReply());
		mongoTemplate.updateFirst(query, update, Tweet.class);
		Tweet tweetupdated = this.tweetRepository.findByTweetId(id);
		logger.info("Updating the replies of the tweet with details "+tweetupdated +" for the user "+userName);
		return this.modelMapper.map(tweetupdated, TweetDto.class);

	}

	@Override
	@Transactional
	public TweetDto manageLikesForTweets(String userName, Long id) throws Exception {
		UserDto searchedUser = this.userService.getUserByName(userName);
		if (searchedUser == null) {
			throw new UserNotFoundException(
					String.format("User with username %s is not found in the application", userName));

		}
		Tweet tweet = this.tweetRepository.findByTweetId(id);
		if (tweet == null) {
			throw new TweetNotFoundException("There are no tweets with the tweetId " + id + " in the application");
		}
		boolean userAlreadyLiked = false;
		TweetLikes tweetLikesCount = new TweetLikes();
		UUID uuid = UUID.randomUUID();
		tweetLikesCount.setLikesId(uuid.toString());
		tweetLikesCount.setTweetId(id);
		tweetLikesCount.setUserName(userName);
		List<TweetLikes> tweetLikeList = this.tweetLikesRepository.findAll();
		if (tweetLikeList != null) {
			for (TweetLikes likes : tweetLikeList) {
				if (likes.getTweetId().equals(id) && likes.getUserName().equalsIgnoreCase(userName)) {
					userAlreadyLiked = true;
				}
			}
		}
		if (!userAlreadyLiked) {
			this.tweetLikesRepository.save(tweetLikesCount);
			tweet.setLikes(tweet.getLikes() + 1);
		}
		Query query = new Query();
		query.addCriteria(Criteria.where("tweetId").is(id));
		Update update = new Update();
		update.set("likes", tweet.getLikes());
		mongoTemplate.updateFirst(query, update, Tweet.class);
		Tweet tweetupdated = this.tweetRepository.findByTweetId(id);
		logger.info("Updating the likes of the tweet with details "+tweetupdated +" for the user "+userName);
		return this.modelMapper.map(tweetupdated, TweetDto.class);

	}

}
