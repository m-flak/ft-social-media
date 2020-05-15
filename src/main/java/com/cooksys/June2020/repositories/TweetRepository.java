package com.cooksys.June2020.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
	@Query("SELECT t FROM Tweet t WHERE t.isDeleted = 'false'")
	List<Tweet> findAllNotDeleted();

	@Query("SELECT t FROM Tweet t WHERE t.id = ?1 AND t.isDeleted = 'false'")
	Optional<Tweet> findByIdAndNotIsDeleted(Integer id);
}
