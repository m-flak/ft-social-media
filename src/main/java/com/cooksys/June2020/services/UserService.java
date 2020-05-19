package com.cooksys.June2020.services;

import java.util.Optional;

import com.cooksys.June2020.exception.TweetNotFoundException;
import com.cooksys.June2020.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.June2020.dtos.UserResponseDto;
import com.cooksys.June2020.entities.User;
import com.cooksys.June2020.mappers.UserMapper;
import com.cooksys.June2020.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;

	public UserService(UserRepository userRepository, UserMapper userMapper) {
		this.userRepository = userRepository;
		this.userMapper = userMapper;
	}

	public ResponseEntity<UserResponseDto> deleteByUsername(String username) {
		Optional<User> optionalUser = userRepository.findByUsernameAndNotIsDelete(username);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("The specified user does not exist.");
		}
		User userToDelete = optionalUser.get();
		userToDelete.setIsDeleted(true);
		//save changes
		userToDelete = userRepository.saveAndFlush(userToDelete);

		return new ResponseEntity<UserResponseDto>(userMapper.entityToDto(userToDelete), HttpStatus.OK);
	}

}
