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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="tweets")
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
	@Column(name = "posted_at")
	private Timestamp posted;
	
	@Column(nullable=false)
	private User author;

	@Column
	private String content;

	@Column
	private List<User> mentions;
	
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

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(
			name="tweet_hashtags",
			joinColumns = @JoinColumn(name = "tweet_id") ,
			inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private List<HashTag> hashtags;

}
