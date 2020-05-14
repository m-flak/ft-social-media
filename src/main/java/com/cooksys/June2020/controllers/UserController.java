package com.cooksys.June2020.controllers;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

}
