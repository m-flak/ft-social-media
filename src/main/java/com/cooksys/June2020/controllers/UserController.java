package com.cooksys.June2020.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cooksys.June2020.dtos.CredentialsDto;
import com.cooksys.June2020.dtos.TweetResponseDto;
import com.cooksys.June2020.dtos.UserRequestDto;
import com.cooksys.June2020.dtos.UserResponseDto;
import com.cooksys.June2020.services.UserService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
@CrossOrigin(origins = "*")
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
}
