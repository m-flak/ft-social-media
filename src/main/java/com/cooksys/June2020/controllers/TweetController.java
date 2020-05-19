package com.cooksys.June2020.controllers;

import java.util.List;

import com.cooksys.June2020.dtos.*;
import com.cooksys.June2020.entities.HashTag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/{id}/repost")
    public ResponseEntity<TweetResponseDto> repostTweet(@PathVariable Integer id,
                                                        @RequestBody CredentialsDto credentialsRequestBody) {
        return tweetService.repostTweet(id, credentialsRequestBody);
    }

    @PostMapping("/{id}/reply")
    public ResponseEntity<TweetResponseDto> replyToTweet(@PathVariable Integer id,
                                                         @RequestBody TweetRequestDto tweetRequestBody) {
        return tweetService.replyToTweet(id, tweetRequestBody);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Object> likeTweet(@PathVariable Integer id,
                                            @RequestBody CredentialsDto credentialsRequestBody) {
        return tweetService.likeTweet(id, credentialsRequestBody);
    }

    @GetMapping
    public List<TweetResponseDto> getTweets() {
        return tweetService.getTweets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TweetResponseDto> getTweetById(@PathVariable Integer id) {
        return tweetService.getTweetById(id);
    }

    @GetMapping("/{id}/reposts")
    public ResponseEntity<List<TweetResponseDto>> getRepostsOfTweet(@PathVariable Integer id) {
        return tweetService.getRepostsOfTweet(id);
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<TweetResponseDto>> getRepliesOfTweet(@PathVariable Integer id) {
        return tweetService.getRepliesOfTweet(id);
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<List<HashTagDto>> getTagsOfTweet(@PathVariable Integer id) {
        return tweetService.getTagsOfTweet(id);
    }

    @GetMapping("/{id}/likes")
    public ResponseEntity<List<UserResponseDto>> getLikesOfTweet(@PathVariable Integer id) {
        return tweetService.getLikesOfTweet(id);
    }

    @GetMapping("/{id}/mentions")
    public ResponseEntity<List<UserResponseDto>> getMentionsOfTweet(@PathVariable Integer id) {
        return tweetService.getMentionsOfTweet(id);
    }

    @GetMapping("/{id}/context")
    public ResponseEntity<ContextDto> getTweetContext(@PathVariable Integer id) {
        return tweetService.getTweetContext(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TweetResponseDto> deleteTweetById(@PathVariable Integer id,
                                                            @RequestBody CredentialsDto credentialsRequestBody) {
        return tweetService.deleteTweetById(id, credentialsRequestBody);
    }

}
