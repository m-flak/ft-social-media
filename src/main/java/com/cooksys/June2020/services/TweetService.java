package com.cooksys.June2020.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cooksys.June2020.dtos.ContextDto;
import com.cooksys.June2020.dtos.CredentialsDto;
import com.cooksys.June2020.exception.TweetNotFoundException;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class TweetService {

	private final HashtagRepository hashtagRepository;
	private final UserRepository userRepository;
	private final TweetRepository tweetRepository;
	private final TweetMapper tweetMapper;
	
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
			tweetToPost.setMentionedUsers(mentionedUsers);
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
		repostedTweet.setMentionedUsers(tweetRepository.getTweetsMentions(id));
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
			theReply.setMentionedUsers(mentionedUsers);
		}
		if (hashTags.size() > 0) {
			theReply.setHashtags(hashTags);
		}

		return new ResponseEntity<>(tweetMapper.entityToDto(tweetRepository.saveAndFlush(theReply)), HttpStatus.OK);
	}

	public ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(Integer id) {
		Tweet parentTweet = validateTweet(id);
		List<Tweet> reposts = tweetRepository.findByRepostOfAndIsDeletedFalse(parentTweet);

		// Return HTTP204 if there are no reposts.
		if (reposts.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(tweetMapper.entitiesToDtos(reposts), HttpStatus.OK);
	}

	public ResponseEntity<List<TweetResponseDto>> getRepliesOfTweet(Integer id) {
		Tweet parentTweet = validateTweet(id);
		List<Tweet> replies = tweetRepository.findByInReplyToAndIsDeletedFalse(parentTweet);

		if (replies.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<>(tweetMapper.entitiesToDtos(replies), HttpStatus.OK);
	}

	public ResponseEntity<ContextDto> getTweetContext(Integer id) {
		Tweet targetTweet = validateTweet(id);

		// Get all replies for a tweet and its replies.
		// This moves downwards to get all possible CHILDREN.
		List<Tweet> afterTweets = new ArrayList<>();
		List<Tweet> tweetReplies = tweetRepository.findByInReplyToOrderByPostedAsc(targetTweet);
		while(!tweetReplies.isEmpty()) {
			final Tweet top = tweetReplies.remove(0);
			afterTweets.add(top);
			tweetReplies.addAll(tweetRepository.findByInReplyToOrderByPostedAsc(top));
		}

		if (!afterTweets.isEmpty()) {
			// Removes any deleted replies
			afterTweets.removeIf((reply) -> reply.getIsDeleted());
			// Removes any references to deleted replies
			afterTweets.forEach((reply) -> {
				if (Objects.nonNull(reply.getInReplyTo())) {
					if (reply.getInReplyTo().getIsDeleted()) {
						reply.setInReplyTo(null);
					}
				}
			});
		}

		// Gets all replies leading to the original tweet
		// Move upwards to get all possible PARENTS
		List<Tweet> beforeTweets = new ArrayList<>();
		Tweet parentTweet = targetTweet.getInReplyTo();
		while (Objects.nonNull(parentTweet)) {
			beforeTweets.add(parentTweet);
			parentTweet = parentTweet.getInReplyTo();
		}

		// Remove all deleted replies. No need to worry about refs to deleted posts when moving upward.
		if (!beforeTweets.isEmpty()) {
			beforeTweets.removeIf((reply) -> reply.getIsDeleted());
		}

		ContextDto tweetContext = new ContextDto(tweetMapper.entityToDto(targetTweet),
			tweetMapper.entitiesToDtos(beforeTweets), tweetMapper.entitiesToDtos(afterTweets));

		return new ResponseEntity<>(tweetContext, HttpStatus.OK);
	}
}
