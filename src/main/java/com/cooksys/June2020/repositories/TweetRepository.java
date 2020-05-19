package com.cooksys.June2020.repositories;

import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;
import com.cooksys.June2020.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    List<Tweet> findByRepostOfAndIsDeletedFalse(Tweet repostOf);

    List<Tweet> findByInReplyToAndIsDeletedFalse(Tweet inReplyTo);

    List<Tweet> findByInReplyToOrderByPostedAsc(Tweet inReplyTo);

    @Query("SELECT t FROM Tweet t WHERE t.isDeleted = 'false'")
    List<Tweet> findAllNotDeleted();

    @Query("SELECT t FROM Tweet t WHERE t.id = ?1 AND t.isDeleted = 'false'")
    Optional<Tweet> findByIdAndNotIsDeleted(Integer id);

    @Query("SELECT likes FROM Tweet t JOIN t.likes likes WHERE t.id = ?1")
    List<User> getTweetsLikes(Integer id);

    @Query("SELECT mentions FROM Tweet t JOIN t.mentionedUsers mentions WHERE t.id = ?1")
    List<User> getTweetsMentions(Integer id);

    @Query("SELECT hashtags FROM Tweet t JOIN t.hashtags hashtags WHERE t.id = ?1")
    List<HashTag> getTweetsHashtags(Integer id);
}
