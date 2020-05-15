package com.cooksys.June2020.services;

import org.springframework.stereotype.Service;

import com.cooksys.June2020.repositories.UserRepository;

@Service
public class UserService {

	private UserRepository userRepository;

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

}
