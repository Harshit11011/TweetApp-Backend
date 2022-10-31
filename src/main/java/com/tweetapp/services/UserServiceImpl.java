package com.tweetapp.services;

import java.util.ArrayList;
import java.util.List;
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

import com.tweetapp.configurations.GlobalConstants;
import com.tweetapp.dto.UserDto;
import com.tweetapp.dto.UserDtoDisplay;
import com.tweetapp.exceptions.InvalidCredentialsException;
import com.tweetapp.exceptions.UserNotFoundException;
import com.tweetapp.models.User;
import com.tweetapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger logger = LogManager.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	private ModelMapper modelMapper;

	private User dtoToUser(UserDto userDto) {
		User user = new User();
		user.setId(userDto.getId());
		user.setFirstName(userDto.getFirstName());
		user.setLastName(userDto.getLastName());
		user.setEmail(userDto.getEmail());
		user.setUserName(userDto.getUserName());
		user.setContactNumber(userDto.getContactNumber());
		user.setPassword(userDto.getPassword());
		user.setRoles(userDto.getRoles());
		return user;
	}

	private UserDto userToDto(User user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setFirstName(user.getFirstName());
		userDto.setLastName(user.getLastName());
		userDto.setEmail(user.getEmail());
		userDto.setUserName(user.getUserName());
		userDto.setContactNumber(user.getContactNumber());
		userDto.setPassword(user.getPassword());
		userDto.setRoles(user.getRoles());
		return userDto;
	}

	@Override
	@Transactional
	public UserDto saveUser(UserDto userDto) {
		User user = this.dtoToUser(userDto);
		User savedUser = this.userRepository.save(user);
		logger.info("Saving the user with details " + userDto);
		return userToDto(savedUser);
	}

	@Override
	public UserDto getUserByName(String name) {
		User searchedUser = this.userRepository.findByUserName(name);
		if (searchedUser != null) {
			logger.info("Getting the user with username " + name);
			return this.userToDto(searchedUser);
		}
		return null;

	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDtoDisplay> getAllUsers() {
		List<User> users = this.userRepository.findAll();
		if (users != null) {
			List<UserDto> userDtoList = users.stream().map(user -> this.userToDto(user)).collect(Collectors.toList());
			List<UserDtoDisplay> userDtoDisplayList = userDtoList.stream()
					.map(uservalue -> this.modelMapper.map(uservalue, UserDtoDisplay.class))
					.collect(Collectors.toList());
			logger.info("Getting All the users in the application ,The details are as follows :" + userDtoDisplayList);
			return userDtoDisplayList;
		}
		return null;
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserDtoDisplay> getAllUsersByName(String name) throws UserNotFoundException {
		Query query = new Query();
		query.addCriteria(Criteria.where("userName").regex("^" + name));
		List<User> users = mongoTemplate.find(query, User.class);
		if (users != null) {
			List<UserDtoDisplay> userDtoList = new ArrayList<UserDtoDisplay>();
			for (User user : users) {
				userDtoList.add(new UserDtoDisplay(user.getFirstName(), user.getLastName(),user.getEmail(),
						user.getContactNumber()));
			}
			logger.info("Getting All the users in the application with userName " + name
					+ " The details are as follows " + userDtoList);
			return userDtoList;
		} else {
			throw new UserNotFoundException(String.format(GlobalConstants.USER_NOT_FOUND, name));
		}
	}

	@Override
	@Transactional
	public String updateUser(UserDto userDto, String password) throws Exception {
		Query query = new Query();
		query.addCriteria(Criteria.where("email").is(userDto.getEmail()));
		User user = mongoTemplate.findOne(query, User.class);
		if (user == null) {
			throw new InvalidCredentialsException(
					"The email " + userDto.getEmail() + " for the user" + userDto.getEmail() + " is not valid.");
		}
		Update update = new Update();
		update.set("password", password);
		mongoTemplate.updateFirst(query, update, User.class);
		logger.info("Updating the user userName " + userDto.getUserName());
		return "User Updated Successfully";
	}

}
