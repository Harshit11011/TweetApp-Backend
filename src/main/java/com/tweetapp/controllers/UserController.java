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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.JwtImplementation.JwtAuthResponse;
import com.tweetapp.configurations.GlobalConstants;
import com.tweetapp.dto.UserDto;
import com.tweetapp.dto.UserDtoDisplay;
import com.tweetapp.dto.UserDtoForgotPassword;
import com.tweetapp.dto.UserDtoLogin;
import com.tweetapp.dto.UserRegisterDto;
import com.tweetapp.services.RegistrationService;
import com.tweetapp.services.UserService;

@RestController
@RequestMapping("/api/v1.0/tweets")
@CrossOrigin(origins="*")
public class UserController {

	private static final Logger logger = LogManager.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@Autowired
	private RegistrationService registrationService;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	// register user
	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody @Valid UserRegisterDto userRegisterDto) throws Exception {
		String informativeMassage = "User Details provided for Registration are: " + userRegisterDto;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY, informativeMassage);
		UserDto temp = this.registrationService.signUpUser(userRegisterDto);
		String message = String.format("User with userName %s is saved.", temp.getUserName());
		return new ResponseEntity<>(message, HttpStatus.CREATED);
	}

	@PostMapping("/login")
	public ResponseEntity<JwtAuthResponse> createToken(@RequestBody UserDtoLogin userDtoLogin) throws Exception {
		String informativeMassage = "User Login Details provided are: " + userDtoLogin;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY, informativeMassage);
		JwtAuthResponse token = this.registrationService.loginCreateToken(userDtoLogin);
		return new ResponseEntity<JwtAuthResponse>(token, HttpStatus.CREATED);

	}

	@PostMapping("/forgot/{userName}")
	public ResponseEntity<String> forgotPassword(@PathVariable String userName,
			@RequestBody UserDtoForgotPassword userDtoForgotPassword) throws Exception {
		String informativeMassage = "User Details for using forget password feature are: " + userDtoForgotPassword;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY, informativeMassage);
		String message = this.registrationService.forgotUserPassword(userName, userDtoForgotPassword);
		return new ResponseEntity<>(message, HttpStatus.OK);
	}

	// Get Users
	@GetMapping("/users/all")
	public ResponseEntity<List<UserDtoDisplay>> getUsers() {
		List<UserDtoDisplay> allUsers = this.userService.getAllUsers();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

	@GetMapping("/valid/{userName}")
	public ResponseEntity<Boolean> validUser(@PathVariable String userName) throws Exception {
		boolean result = false;
		UserDto user = this.userService.getUserByName(userName);
		if (user == null) {
			result = true;
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/user/search/{userName}")
	public ResponseEntity<List<UserDtoDisplay>> getUsersByName(@PathVariable String userName) throws Exception {
		String informativeMassage = "Searching the users with userName *it may be partial : " + userName;
		rabbitTemplate.convertAndSend(GlobalConstants.EXCHANGE, GlobalConstants.ROUTING_KEY, informativeMassage);
		List<UserDtoDisplay> allUsers = this.userService.getAllUsersByName(userName);
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
	}

}
