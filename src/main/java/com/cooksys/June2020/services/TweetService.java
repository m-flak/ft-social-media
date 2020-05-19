package com.cooksys.June2020.services;

import com.cooksys.June2020.dtos.*;
import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;
import com.cooksys.June2020.entities.User;
import com.cooksys.June2020.exception.InvalidUserCredentialsException;
import com.cooksys.June2020.exception.TweetNotFoundException;
import com.cooksys.June2020.mappers.HashTagMapper;
import com.cooksys.June2020.mappers.TweetMapper;
import com.cooksys.June2020.mappers.UserMapper;
import com.cooksys.June2020.repositories.HashtagRepository;
import com.cooksys.June2020.repositories.TweetRepository;
import com.cooksys.June2020.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class TweetService {

    private final HashtagRepository hashtagRepository;
    private final HashTagMapper hashTagMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final TweetRepository tweetRepository;
    private final TweetMapper tweetMapper;
    private final ValidateService validateService;

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
        User authoringUser = validateService.validateUserCredentials(tweetRequest.getCredentials());

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
        User requestingUser = validateService.validateUserCredentials(userCredentials);

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
        User repostingUser = validateService.validateUserCredentials(userCredentials);

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
        User authoringUser = validateService.validateUserCredentials(tweetRequest.getCredentials());

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

        return new ResponseEntity<>(tweetMapper.entitiesToDtos(reposts), HttpStatus.OK);
    }

    public ResponseEntity<List<TweetResponseDto>> getRepliesOfTweet(Integer id) {
        Tweet parentTweet = validateTweet(id);
        List<Tweet> replies = tweetRepository.findByInReplyToAndIsDeletedFalse(parentTweet);

        return new ResponseEntity<>(tweetMapper.entitiesToDtos(replies), HttpStatus.OK);
    }

    public ResponseEntity<ContextDto> getTweetContext(Integer id) {
        Tweet targetTweet = validateTweet(id);

        // Get all replies for a tweet and its replies.
        // This moves downwards to get all possible CHILDREN.
        List<Tweet> afterTweets = new ArrayList<>();
        List<Tweet> tweetReplies = tweetRepository.findByInReplyToOrderByPostedAsc(targetTweet);
        while (!tweetReplies.isEmpty()) {
            final Tweet top = tweetReplies.remove(0);
            afterTweets.add(top);
            tweetReplies.addAll(tweetRepository.findByInReplyToOrderByPostedAsc(top));
        }

        if (!afterTweets.isEmpty()) {
            // Removes any deleted replies and/or references to deleted replies
            int afterTweetSz = afterTweets.size();
            for (int i = 0; i < afterTweetSz; i++) {
                try {
                    if (afterTweets.get(i).getIsDeleted()) {
                        afterTweets.remove(i);
                        afterTweetSz -= 1;
                        // don't change the index if we removed the top element
                        i = i == 0 ? 0 : i - 1;
                        continue;
                    }
                    // Reference to a deleted reply. If it's deleted, remove it from the 'inReplyTo' field.
                    Tweet inReplyToFieldContents = afterTweets.get(i).getInReplyTo();
                    if (Objects.nonNull(inReplyToFieldContents)) {
                        if (inReplyToFieldContents.getIsDeleted()) {
                            afterTweets.get(i).setInReplyTo(null);
                        }
                    }
                }
                catch (NullPointerException npe) {
                    continue;
                }
            }
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

    public ResponseEntity<Object> likeTweet(Integer id, CredentialsDto userCredentials) {
        Tweet tweetToLike = validateTweet(id);
        User likingUser = validateService.validateUserCredentials(userCredentials);

        Optional<List<User>> currentTweetLikes = Optional.ofNullable(tweetToLike.getLikes());

        if (currentTweetLikes.isPresent()) {
            currentTweetLikes.get().add(likingUser);
        }
        else {
            tweetToLike.setLikes(Arrays.asList(likingUser));
        }

        tweetRepository.saveAndFlush(tweetToLike);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<HashTagDto>> getTagsOfTweet(Integer id) {
       validateTweet(id);

       return new ResponseEntity<>(hashTagMapper.entitiesToDtos(tweetRepository.getTweetsHashtags(id)), HttpStatus.OK);
    }

    public ResponseEntity<List<UserResponseDto>> getLikesOfTweet(Integer id) {
        validateTweet(id);

        List<User> likingUsers = tweetRepository.getTweetsLikes(id);
        if (!likingUsers.isEmpty()) {
            likingUsers.removeIf((user) -> user.getIsDeleted());
        }

        return new ResponseEntity<>(userMapper.entitiesToDtos(likingUsers), HttpStatus.OK);
    }

    public ResponseEntity<List<UserResponseDto>> getMentionsOfTweet(Integer id) {
        validateTweet(id);

        List<User> mentionedUsers = tweetRepository.getTweetsMentions(id);
        if (!mentionedUsers.isEmpty()) {
            mentionedUsers.removeIf((user) -> user.getIsDeleted());
        }

        return new ResponseEntity<>(userMapper.entitiesToDtos(mentionedUsers), HttpStatus.OK);
    }
}
