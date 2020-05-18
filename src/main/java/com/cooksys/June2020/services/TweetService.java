package com.cooksys.June2020.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cooksys.June2020.dtos.CredentialsDto;
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

	private User validateUserCredentials(CredentialsDto credentialsToValidate) {
		Optional<User> authoringUser = userRepository.findByCredentialsUsernameAndCredentialsPassword(
				credentialsToValidate.getUsername(), credentialsToValidate.getPassword());

		// User Credentials are bogus, abort!
		if (!authoringUser.isPresent()) {
			throw new InvalidUserCredentialsException("Invalid Username/Password combination supplied.");
		}

		return authoringUser.get();
	}

	private Tweet validateTweet(Integer id) {
		Optional<Tweet> optionalTweet = tweetRepository.findByIdAndNotIsDeleted(id);

		if (!optionalTweet.isPresent()) {
			throw new TweetNotFoundException("The specified tweet does not exist.");
		}
		return optionalTweet.get();
	}

	/* (IN) tweetContent - A tweet's contents to parse
	 * (OUT) mentionedUsers - A list to store mentioned users in
	 * (OUT) foundHashTags - A list to store found hashtags in
	 */
	private void parseForLikesMentions(String tweetContent, final ArrayList<User> mentionedUsers,
									   final ArrayList<HashTag> foundHashTags) {
		// Create regex to pilfer the tweet's body.
		Matcher likeMentionMatcher = Pattern.compile("([#|@]\\w+)", Pattern.MULTILINE)
				.matcher(tweetContent);

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
						foundHashTags.add(referencedHashTag.get());
					} else {
						// new hashtag
						HashTag newHashTag = new HashTag();
						newHashTag.setLabel(label);
						foundHashTags.add(newHashTag);
					}
				}
			}
		}
	}

	public ResponseEntity<TweetResponseDto> postNewTweet(TweetRequestDto tweetRequest) {
		// Validate credentials
		User authoringUser = validateUserCredentials(tweetRequest.getCredentials());

		// Found (if any) mentioned users and/or hashtags
		ArrayList<User> mentionedUsers = new ArrayList<>();
		ArrayList<HashTag> hashTags = new ArrayList<>();
		parseForLikesMentions(tweetRequest.getContent(), mentionedUsers, hashTags);

		Tweet tweetToPost = tweetMapper.dtoToEntity(tweetRequest);
		tweetToPost.setAuthor(authoringUser);

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

	public ResponseEntity<TweetResponseDto> deleteTweetById(Integer id, CredentialsDto userCredentials) {
		// Get tweet if it exists
		Tweet tweetToDelete = validateTweet(id);

		// Check credentials of request
		User requestingUser = validateUserCredentials(userCredentials);

		// requestingUser has been authenticated, so only usernames need to be compared.
		final String requesterUsername = requestingUser.getCredentials().getUsername();
		final String ownerUsername = tweetToDelete.getAuthor().getCredentials().getUsername();

		// the requesting user does not own the tweet so abort
		if (!ownerUsername.equals(requesterUsername)) {
			throw new InvalidUserCredentialsException("Cannot delete tweets by other users.");
		}

		tweetToDelete.setIsDeleted(true);
		// save changes
		tweetToDelete = tweetRepository.saveAndFlush(tweetToDelete);

		return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToDto(tweetToDelete), HttpStatus.OK);
	}

	public ResponseEntity<TweetResponseDto> getTweetById(Integer id) {
		Tweet tweetToGet = validateTweet(id);
		return new ResponseEntity<TweetResponseDto>(tweetMapper.entityToDto(tweetToGet), HttpStatus.OK);
	}

	public ResponseEntity<TweetResponseDto> repostTweet(Integer id, CredentialsDto userCredentials) {
		Tweet originalTweet = validateTweet(id);
		User repostingUser = validateUserCredentials(userCredentials);

		Tweet repostedTweet = new Tweet();
		repostedTweet.setAuthor(repostingUser);
		repostedTweet.setContent(null);
		repostedTweet.setRepostOf(originalTweet);
		// Give the repost the same mentions & likes
		repostedTweet.setMentions(tweetRepository.getTweetsMentions(id));
		repostedTweet.setLikes(tweetRepository.getTweetsLikes(id));

		return new ResponseEntity<>(tweetMapper.entityToDto(tweetRepository.saveAndFlush(repostedTweet)), HttpStatus.OK);
	}

	public ResponseEntity<TweetResponseDto> replyToTweet(Integer id, TweetRequestDto tweetRequest) {
		Tweet replyingTo = validateTweet(id);
		User authoringUser = validateUserCredentials(tweetRequest.getCredentials());

		// Found (if any) mentioned users and/or hashtags
		ArrayList<User> mentionedUsers = new ArrayList<>();
		ArrayList<HashTag> hashTags = new ArrayList<>();
		parseForLikesMentions(tweetRequest.getContent(), mentionedUsers, hashTags);

		Tweet theReply = tweetMapper.dtoToEntity(tweetRequest);
		theReply.setInReplyTo(replyingTo);
		theReply.setAuthor(authoringUser);

		if (mentionedUsers.size() > 0) {
			theReply.setMentions(mentionedUsers);
		}
		if (hashTags.size() > 0) {
			theReply.setHashtags(hashTags);
		}

		return new ResponseEntity<>(tweetMapper.entityToDto(tweetRepository.saveAndFlush(theReply)), HttpStatus.OK);
	}
}
