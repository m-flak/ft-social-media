package com.cooksys.June2020.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(nullable = false)
	private Boolean isDeleted;
	
	@CreationTimestamp
	private Timestamp posted;

//	@Column(nullable=false)
//	private User author;

	@Column
	private String content;

	@Column
	private List<Tweet> mentions;
	
	@Column
	private List<Tweet> likes;
	
	@OneToMany(mappedBy = "tweet")
	private List<Tweet> inReplyTos;

	@OneToMany(mappedBy = "tweet")
	private List<Tweet> repostOfs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tweet_id")
	private Tweet inReplyTo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tweet_id")
	private Tweet repostOf;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "hashtags")
	@JoinTable(
			name="tweet_hashtag",
			joinColumns = @JoinColumn(name="tweet_id"),
			inverseJoinColumns = @JoinColumn(name="hashtag_id"))
	private List<HashTag> hashtags;

}
