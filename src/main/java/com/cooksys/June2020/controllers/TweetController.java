package com.cooksys.June2020.controllers;

import com.cooksys.June2020.dtos.TweetRequestDto;
import com.cooksys.June2020.dtos.TweetResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.June2020.services.TweetService;

@RestController
@RequestMapping("/tweets")
public class TweetController {

	private TweetService tweetService;

	public TweetController(TweetService tweetService) {
		this.tweetService = tweetService;
	}

	@PostMapping
	public ResponseEntity<TweetResponseDto> postNewTweet(@RequestBody TweetRequestDto tweetRequestBody) {
		return tweetService.postNewTweet(tweetRequestBody);
	}
}
