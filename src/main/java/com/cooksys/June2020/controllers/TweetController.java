package com.cooksys.June2020.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tweets")
public class TweetController {

	private TweetService tweetService;

	public TweetController(TweetService tweetService) {
		this.tweetService = tweetService;
	}

}
