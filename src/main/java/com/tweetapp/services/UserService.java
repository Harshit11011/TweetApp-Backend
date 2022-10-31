package com.tweetapp.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tweetapp.dto.UserDto;
import com.tweetapp.dto.UserDtoDisplay;

@Service
public interface UserService {


	UserDto saveUser(UserDto userDto);

	UserDto getUserByName(String name) throws Exception;
	
	List<UserDtoDisplay> getAllUsersByName(String name) throws Exception;
	
	String updateUser(UserDto userDto,String password) throws Exception;
	
	List<UserDtoDisplay> getAllUsers();


}
