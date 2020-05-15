package com.cooksys.June2020.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usertable")
@NoArgsConstructor
@Getter
@Setter
public class User {

// Below I have two forms of setting up the User entity method. The first, (commented out), is
// coming from the example given in the June2020 assignment. I assume this is what we will use. 
// The second is from the chat app example. Right now I'm going back and reviewing the videos and notes.
// However, I did want to post this as a branch for the time being.
// Here, I also left in the (name = "usertable"), added a time-stamp and made relationships between other tables.
// Please feel free to destroy or adjust with any thoughts. I'm sure there are issues.

//	username: 'string',
//	profile: 'Profile',
//	joined: 'timestamp'

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column
	private Boolean isDeleted = false;

	@CreationTimestamp
	private Timestamp timestamp;

	@Embedded
	private Credentials credentials;

	@Embedded
	private Profile profile;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;

	@OneToMany
	private List<User> followers;
}