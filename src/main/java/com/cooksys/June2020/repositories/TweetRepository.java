package com.cooksys.June2020.repositories;

import java.util.List;
import java.util.Optional;

import com.cooksys.June2020.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.Tweet;

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

	@Query("SELECT mentions FROM Tweet t JOIN t.mentions mentions WHERE t.id = ?1")
	List<User> getTweetsMentions(Integer id);
}
