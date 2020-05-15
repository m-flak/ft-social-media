package com.cooksys.June2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.Tweet;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    @Query("SELECT t FROM Tweet t WHERE t.id = ?1")
    Tweet get(Integer id);
}
