package com.cooksys.June2020.repositories;

import java.util.List;
import java.util.Optional;

import com.cooksys.June2020.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.HashTag;

@Repository
// JpaRepository<Hashtag, Long> to JpaRepository<Hashtable, Long> would remove the error in line 17.
// JpaRepository<HashTag, Long> written to follow syntax of ChatApp.
// JpaRepository<HashTag, Long> and not JpaRepository<Hashtag, Long>
// written to follow syntax in entities package.
public interface HashtagRepository extends JpaRepository<HashTag, Integer> {
	Optional<HashTag> findByLabel(String label);

	// I think that having no results will just give an empty list instead of a null. I could be wrong -- Matthew K.
	// The '#something' owns the tweets, so it belongs in this repo.
	@Query("SELECT tweet FROM HashTag AS tag, IN(tag.tweets) tweet WHERE tag.label = ?1 AND tweet.isDeleted = 'false'")
	List<Tweet> getHashtagTweetsNotDeleted(String label);
}
