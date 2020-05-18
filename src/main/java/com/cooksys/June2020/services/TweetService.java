package com.cooksys.June2020.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cooksys.June2020.exception.TweetNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cooksys.June2020.dtos.TweetRequestDto;
import com.cooksys.June2020.dtos.TweetResponseDto;
import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;
import com.cooksys.June2020.entities.User;
import com.cooksys.June2020.exception.InvalidUserCredentialsException;
import com.cooksys.June2020.mappers.TweetMapper;
import com.cooksys.June2020.repositories.HashtagRepository;
import com.cooksys.June2020.repositories.TweetRepository;
import com.cooksys.June2020.repositories.UserRepository;

@Service
public class TweetService {
	private HashtagRepository hashtagRepository;
	private UserRepository userRepository;
	private TweetRepository tweetRepository;
	private TweetMapper tweetMapper;

	public TweetService(UserRepository userRepository, TweetRepository tweetRepository, TweetMapper tweetMapper,
			HashtagRepository hashtagRepository) {
		this.userRepository = userRepository;
		this.tweetRepository = tweetRepository;
		this.tweetMapper = tweetMapper;
		this.hashtagRepository = hashtagRepository;
	}

	public ResponseEntity<TweetResponseDto> postNewTweet(TweetRequestDto tweetRequest) {
		// Validate credentials
		Optional<User> authoringUser = userRepository.findByCredentialsUsernameAndCredentialsPassword(
				tweetRequest.getCredentials().getUsername(), tweetRequest.getCredentials().getPassword());

		// User Credentials are bogus, abort!
		if (!authoringUser.isPresent()) {
			throw new InvalidUserCredentialsException("Invalid Username/Password combination supplied.");
		}

		// Create regex to pilfer the tweet's body.
		Matcher likeMentionMatcher = Pattern.compile("([#|@]\\w+)", Pattern.MULTILINE)
				.matcher(tweetRequest.getContent());

		// Found (if any) mentioned users and/or hashtags
		ArrayList<User> mentionedUsers = new ArrayList<>();
		ArrayList<HashTag> hashTags = new ArrayList<>();

		// does the contents have any mentions/hashtags?
		if (likeMentionMatcher.find()) {
			// move the matcher back to the beginning
			likeMentionMatcher.reset();

			while (likeMentionMatcher.find()) {
				final String match = likeMentionMatcher.group();

				if (match.startsWith("@")) {
					Optional<User> mentionedUser = userRepository.findByCredentialsUsername(match.substring(1));
					// Add a user to be mentioned, if they exist.
					mentionedUser.ifPresent(mentionedUsers::add);
				} else if (match.startsWith("#")) {
					// lowercase label, so #TaG is still #tag
					String label = match.substring(1).toLowerCase();
					Optional<HashTag> referencedHashTag = hashtagRepository.findByLabel(label);

					if (referencedHashTag.isPresent()) {
						// existing hashtag
						hashTags.add(referencedHashTag.get());
					} else {
						// new hashtag
						HashTag newHashTag = new HashTag();
						newHashTag.setLabel(label);
						hashTags.add(newHashTag);
					}
				}
			}
		}

		Tweet tweetToPost = tweetMapper.dtoToEntity(tweetRequest);
		tweetToPost.setAuthor(authoringUser.get());

		if (mentionedUsers.size() > 0) {
			tweetToPost.setMentions(mentionedUsers);
		}
		if (hashTags.size() > 0) {
			tweetToPost.setHashtags(hashTags);
		}

		return new ResponseEntity<>(tweetMapper.entityToDto(tweetRepository.saveAndFlush(tweetToPost)), HttpStatus.OK);
	}

	public List<TweetResponseDto> getTweets() {
		return tweetMapper.entitiesToDtos(tweetRepository.findAllNotDeleted());
	}

	public ResponseEntity<TweetResponseDto> deleteTweetById(Integer id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndNotIsDeleted(id);
		// Validate credentials
		Optional<User> authoringUser = userRepository.findByCredentialsUsernameAndCredentialsPassword(
				optionalTweet.get().getAuthor().getCredentials().getUsername(),
				optionalTweet.get().getAuthor().getCredentials().getPassword());

		// User Credentials are bogus, abort!
		if (!authoringUser.isPresent()) {
			throw new InvalidUserCredentialsException("Invalid Username/Password combination supplied.");
		}
		if (!optionalTweet.isPresent()) {
			throw new TweetNotFoundException("The specified tweet does not exist.");
		}
		Tweet tweetToDelete = optionalTweet.get();
		tweetToDelete.setIsDeleted(true);
		// save changes
		tweetToDelete = tweetRepository.saveAndFlush(tweetToDelete);

		return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToDto(tweetToDelete), HttpStatus.OK);
	}

	public ResponseEntity<TweetResponseDto> getTweetById(Integer id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndNotIsDeleted(id);
		if (!optionalTweet.isPresent()) {
			throw new TweetNotFoundException("The specified tweet does not exist.");
		}
		return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToDto(optionalTweet.get()), HttpStatus.OK);
	}

}
