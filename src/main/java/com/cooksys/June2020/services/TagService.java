package com.cooksys.June2020.services;

import java.util.List;
import java.util.Optional;

import com.cooksys.June2020.dtos.TweetResponseDto;
import com.cooksys.June2020.exception.HashtagNotFoundException;
import com.cooksys.June2020.mappers.TweetMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.June2020.dtos.HashTagDto;
import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;
import com.cooksys.June2020.mappers.HashTagMapper;
import com.cooksys.June2020.repositories.HashtagRepository;

@Service
@AllArgsConstructor
public class TagService {

	private final HashtagRepository hashtagRepository;
	private final HashTagMapper hashtagMapper;
	private final TweetMapper tweetMapper;

	public List<HashTagDto> getTags() {
		return hashtagMapper.entitiesToDtos(hashtagRepository.findAll());
	}

	public ResponseEntity<List<TweetResponseDto>> getTagsByLabel(String label) {
		// It's best to use our repositories.
		List<Tweet> tweets = hashtagRepository.getHashtagTweetsNotDeleted(label);

		if (tweets.isEmpty()) {
			throw new HashtagNotFoundException("The specified hashtag does not exist.");
		}

		return new ResponseEntity<>(tweetMapper.entitiesToDtos(tweets), HttpStatus.OK);

	}
}
