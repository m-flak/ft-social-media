package com.cooksys.June2020.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.June2020.services.ValidateService;

@RestController
@RequestMapping("/validate")
public class ValidateController {

	ValidateService validateService;

	public ValidateController(ValidateService validateService) {
		this.validateService = validateService;
	}

	@GetMapping("/tag/exists/{label}")
	public boolean hashtagExists(@PathVariable String label) {
		return validateService.hashtagExists(label);
	}

	@GetMapping("/username/available/@{username}")
	public boolean usernameAvailable(@PathVariable String username) {
		return validateService.usernameAvailable(username);
	}

	@GetMapping("/username/exists/@{username}")
	public boolean usernameExists(@PathVariable String username) {
		return validateService.usernameExists(username);
	}
}
