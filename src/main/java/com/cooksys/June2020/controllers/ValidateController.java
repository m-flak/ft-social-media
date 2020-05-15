package com.cooksys.June2020.controllers;

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

}
