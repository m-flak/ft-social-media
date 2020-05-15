package com.cooksys.June2020.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.June2020.dtos.UserResponseDto;
import com.cooksys.June2020.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@DeleteMapping("/@{username}")
	public ResponseEntity<UserResponseDto> deleteByUsername(@PathVariable String username) {
		return userService.deleteByUsername(username);
	}
}
