package com.cooksys.June2020.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {

	List<Tweet> findAllAndNotIsDeleted();

	Optional<Tweet> findByIdAndNotIsDeleted(Integer id);
}
