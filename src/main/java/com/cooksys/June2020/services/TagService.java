package com.cooksys.June2020.services;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.June2020.dtos.HashTagDto;
import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;
import com.cooksys.June2020.mappers.HashTagMapper;
import com.cooksys.June2020.repositories.HashtagRepository;

@Service
public class TagService {

	private HashtagRepository hashtagRepository;
	private HashTagMapper hashtagMapper;

	public TagService(HashtagRepository hashtagRepository) {
		this.hashtagRepository = hashtagRepository;
	}

	public TagService(HashtagRepository hashtagRepository, HashTagMapper hashtagMapper) {
		this.hashtagRepository = hashtagRepository;
		this.hashtagMapper = hashtagMapper;
	}

	public List<HashTagDto> getTags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	public ResponseEntity<List<Tweet>> getTagsByLabel(String label) {
		Optional<HashTag> optionalHashtag = hashtagRepository.findByLabel(label);
		List<Tweet> tweets = optionalHashtag.get().getTweets();
		if (tweets.isEmpty()) {
			return new ResponseEntity<List<Tweet>>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<List<Tweet>>(hashtagMapper.entityToDto(tweets), HttpStatus.OK);

	}
}
