package com.tweetapp.helpers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tweetapp.models.User;
import com.tweetapp.repositories.UserRepository;

@Service
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// load username from database
		User user = this.userRepository.findByUserName(userName);
		if (user == null) {
			throw new UsernameNotFoundException("The user with userName " + userName + " is not available.");
		}
		return this.modelMapper.map(user, MyUserDetails.class);

	}

}
