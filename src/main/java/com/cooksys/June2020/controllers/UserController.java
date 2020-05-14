package com.cooksys.June2020.controllers;

import com.cooksys.June2020.services.UserService;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

}
