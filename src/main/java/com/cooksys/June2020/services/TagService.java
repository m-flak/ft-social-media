package com.cooksys.June2020.services;

import org.springframework.stereotype.Service;

import com.cooksys.June2020.repositories.HashtagRepository;

@Service
public class TagService {

	private HashtagRepository hashtagRepository;

	public TagService(HashtagRepository hashtagRepository) {
		this.hashtagRepository = hashtagRepository;
	}

}
