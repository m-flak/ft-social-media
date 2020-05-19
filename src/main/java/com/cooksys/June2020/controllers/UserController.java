package com.cooksys.June2020.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.June2020.dtos.UserRequestDto;
import com.cooksys.June2020.dtos.UserResponseDto;
import com.cooksys.June2020.services.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	@PostMapping
	public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUser(userRequestDto);
	}

	@GetMapping
	public List<UserResponseDto> getUsers() {
		return userService.getUsers();
	}

	@GetMapping("/@{username}")
	public UserResponseDto getUser(@PathVariable String username) {
		return userService.getUser(username);
	}

	@PatchMapping("/@{username}")
	public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
		return userService.updateUser(username, userRequestDto);
	}

	@DeleteMapping("/@{username}")
	public UserResponseDto deleteByUsername(@PathVariable String username) {
		return userService.deleteByUsername(username);
	}

	@GetMapping("/@{username}/tweets")
	public UserResponseDto getUserByTweets(@PathVariable String tweet) {
		return userService.getUserByTweets(tweet);
	}

	// should feed be in the form of a list?
	@GetMapping("/@{username}/feed")
	public UserResponseDto getUserByFeed(@PathVariable String feed) {
		return userService.getUserByFeed(feed);
	}

	// should mentions be in the form of a list?
	@GetMapping("/@{username}/mentions")
	public UserResponseDto getUserByMentions(@PathVariable String mentions, String mention) {
		return userService.getUserByMentions(mention);
	}

	@PostMapping("/@{username}/follow")
	public UserResponseDto createUserByFollow(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUserByFollow(userRequestDto);
	}

	@PostMapping("/@{username}/unfollow")
	public UserResponseDto createUserByUnfollow(@RequestBody UserRequestDto userRequestDto) {
		return userService.createUserByUnfollow(userRequestDto);
	}

	// should followers be in the form of a list?
	@GetMapping("/@{username}/followers")
	public UserResponseDto getUserByFollowers(@PathVariable String followers) {
		return userService.getUserByFollowers(followers);
	}

	@GetMapping("/@{username}/followering")
	public UserResponseDto getUserByFollowering(@PathVariable String followering) {
		return userService.getUserByFollowering(followering);
	}

}
