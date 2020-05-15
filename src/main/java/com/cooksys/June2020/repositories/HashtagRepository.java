package com.cooksys.June2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.HashTag;

@Repository
// JpaRepository<Hashtag, Long> to JpaRepository<Hashtable, Long> would remove the error in line 17.
// JpaRepository<HashTag, Long> written to follow syntax of ChatApp.
// JpaRepository<HashTag, Long> and not JpaRepository<Hashtag, Long>
// written to follow syntax in entities package.
public interface HashtagRepository extends JpaRepository<HashTag, Integer> {

}