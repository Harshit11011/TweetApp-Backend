package com.tweetapp.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.tweetapp.JwtImplementation.JwtAuthResponse;
import com.tweetapp.JwtImplementation.JwtTokenHelper;
import com.tweetapp.configurations.GlobalConstants;
import com.tweetapp.dto.UserDto;
import com.tweetapp.dto.UserDtoForgotPassword;
import com.tweetapp.dto.UserDtoLogin;
import com.tweetapp.dto.UserRegisterDto;
import com.tweetapp.exceptions.UserAlreadyExistException;
import com.tweetapp.exceptions.UserNotFoundException;
import com.tweetapp.helpers.MyUserDetailsService;
import com.tweetapp.sequences.SequenceGeneratorService;

@Service
public class RegistrationService {

	private static final Logger logger = LogManager.getLogger(RegistrationService.class);

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private SequenceGeneratorService sequenceGeneratorService;

	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private JwtTokenHelper jwtTokenHelper;

	@Autowired
	private AuthenticationManager authenticationManager;

	public UserDto signUpUser(UserRegisterDto userRegisterDto) throws Exception {
		UserDto user = userService.getUserByName(userRegisterDto.getUserName());
		if (user != null) {
			throw new UserAlreadyExistException(String.format(GlobalConstants.USER_ALREADY_EXIST,userRegisterDto.getUserName()));
		}
		UserDto userDto = new UserDto();
		userDto.setId(sequenceGeneratorService.generateSequence(UserDto.USER_SEQUENCE_NAME));
		userDto.setFirstName(userRegisterDto.getFirstName());
		userDto.setLastName(userRegisterDto.getLastName());
		userDto.setEmail(userRegisterDto.getEmail());
		userDto.setUserName(userRegisterDto.getUserName());
		String encodedPassword = bCryptPasswordEncoder.encode(userRegisterDto.getPassword());
		userDto.setPassword(encodedPassword);
		userDto.setContactNumber(userRegisterDto.getContactNumber());
		userDto.setRoles("App_User");

		UserDto savedUser = userService.saveUser(userDto);
		return savedUser;
	}

	public String forgotUserPassword(String userName, UserDtoForgotPassword userDtoForgotPassword) throws Exception {
		UserDto user = userService.getUserByName(userName);
		if (user == null) {
			throw new UserNotFoundException(
					String.format("User with username %s is not found in the application", userName));
		}
		String encodedPassword = bCryptPasswordEncoder.encode(userDtoForgotPassword.getPassword());
		user.setEmail(userDtoForgotPassword.getEmail());
		return userService.updateUser(user, encodedPassword);

	}

	public JwtAuthResponse loginCreateToken(UserDtoLogin userDtoLogin) throws Exception {
		try {
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(userDtoLogin.getUserName(), userDtoLogin.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password");
		}
		UserDetails user = this.myUserDetailsService.loadUserByUsername(userDtoLogin.getUserName());
		String token = this.jwtTokenHelper.generateToken(user);
		JwtAuthResponse response = new JwtAuthResponse(token);

		return response;
	}

}
