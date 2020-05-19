package com.cooksys.June2020.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(nullable = false)
	private Boolean isDeleted = false;

	@CreationTimestamp
	private Timestamp joined;

	@Embedded
	private Credentials credentials;

	@Embedded
	private Profile profile;

	@OneToMany(mappedBy = "author")
	private List<Tweet> tweets;

	@ManyToMany(mappedBy = "mentionedUsers")
	private List<Tweet> mentions;

	@ManyToMany
	private List<User> followers;

	@ManyToMany(mappedBy = "followers")
	private List<User> following;

	@ManyToMany
	private List<Tweet> likedTweets;

}
