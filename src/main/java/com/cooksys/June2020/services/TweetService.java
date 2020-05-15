package com.cooksys.June2020.services;

import org.springframework.stereotype.Service;

import com.cooksys.June2020.repositories.TweetRepository;

@Service
public class TweetService {

	private TweetRepository tweetRepository;

	public TweetService(TweetRepository tweetRepository) {
		this.tweetRepository = tweetRepository;
	}

}
