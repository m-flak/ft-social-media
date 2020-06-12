package com.cooksys.June2020.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.June2020.dtos.*;
import com.cooksys.June2020.entities.*;
import com.cooksys.June2020.mappers.*;
import com.cooksys.June2020.repositories.*;
import com.cooksys.June2020.exception.BadRequestException;
import com.cooksys.June2020.exception.UserExistsException;
import com.cooksys.June2020.exception.UserNotFoundException;


import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final TweetRepository tweetRepository;
    private final UserMapper userMapper;
    private final TweetMapper tweetMapper;
    private final ValidateService validateService;

	private void validateRequest(UserRequestDto userRequestDto) {
		if (userRequestDto.getCredentials() == null) {
			throw new BadRequestException("You must pass in a username and password");
		}
		if (userRequestDto.getCredentials().getUsername() == null) {
			throw new BadRequestException("A username must be provided");
		}
		if (userRequestDto.getCredentials().getPassword() == null) {
			throw new BadRequestException("A password must be provided");
		}
		if (userRequestDto.getProfile() == null || userRequestDto.getProfile().getEmail() == null) {
			throw new BadRequestException("An email must be provided");
		}
	}

	public UserResponseDto createUser(UserRequestDto userRequestDto) {
		validateRequest(userRequestDto);
		if (userRepository.findByCredentialsUsername(userRequestDto.getCredentials().getUsername()).isPresent()) {
			throw new UserExistsException();
		}
		return userMapper.entityToDto(userRepository.saveAndFlush(userMapper.dtoToEntity(userRequestDto)));
	}

	public List<UserResponseDto> getUsers() {
		return userMapper.entitiesToDtos(userRepository.findAllByIsDeletedIsFalse());
	}

	public UserResponseDto getUser(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndIsDeletedIsFalse(username);
		if (optionalUser.isPresent()) {
			return userMapper.entityToDto(optionalUser.get());
		}
		throw new UserNotFoundException("No user with the provided username was found");
	}

	public UserResponseDto updateUser(String username, UserRequestDto userRequestDto) {
		validateRequest(userRequestDto);
		User userToUpdate = validateService.validateUserCredentials(userRequestDto.getCredentials());
		if (!userToUpdate.getCredentials().getUsername().equals(username)) {
			throw new BadRequestException("The provided username must match the username in the provided credentials");
		}
		userToUpdate.getProfile().setFirstName(userRequestDto.getProfile().getFirstName());
		userToUpdate.getProfile().setLastName(userRequestDto.getProfile().getLastName());
		userToUpdate.getProfile().setEmail(userRequestDto.getProfile().getEmail());
		userToUpdate.getProfile().setPhone(userRequestDto.getProfile().getPhone());
		return userMapper.entityToDto(userRepository.saveAndFlush(userToUpdate));
	}

	public UserResponseDto deleteByUsername(String username) {
		Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndIsDeletedIsFalse(username);
		if (!optionalUser.isPresent()) {
			throw new UserNotFoundException("The specified user does not exist.");
		}
		User userToDelete = optionalUser.get();
		userToDelete.setIsDeleted(true);
		return userMapper.entityToDto(userRepository.saveAndFlush(userToDelete));
	}

    public ResponseEntity<Boolean> loginUser(CredentialsDto credentialsDto) {
        Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsDeletedIsFalse(credentialsDto.getUsername(), credentialsDto.getPassword());
		if (optionalUser.isPresent()) {
			return new ResponseEntity<Boolean>(true, HttpStatus.OK);
		}
		return new ResponseEntity<Boolean>(false, HttpStatus.UNAUTHORIZED);
    }

	public List<TweetResponseDto> getUserTweets(String username) {
    	Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    	if (optionalUser.isPresent()) {
    		List<Tweet> tweets = tweetRepository.findByAuthor(optionalUser.get());
    		return tweetMapper.entitiesToDtos(tweets);
		}
    	throw new UserNotFoundException("The username provided could not be found");
	}
}
