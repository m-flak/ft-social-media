package com.cooksys.June2020.repositories;

import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HashtagRepository extends JpaRepository<HashTag, Integer> {
    Optional<HashTag> findByLabel(String label);

    @Query("SELECT tweet FROM HashTag AS tag, IN(tag.tweets) tweet WHERE tag.label = ?1 AND tweet.isDeleted = false")
    List<Tweet> getHashtagTweetsNotDeleted(String label);
}
