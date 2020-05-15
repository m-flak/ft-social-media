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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tweets")
@NoArgsConstructor
@Getter
@Setter
public class Tweet {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(nullable = false)
	private Boolean isDeleted = false;

	@CreationTimestamp
	@Column(name = "posted_at")
	private Timestamp posted;

	@ManyToOne
	private User author;

	@Column
	private String content;

	@ManyToMany
	private List<User> mentions;

	@ManyToMany
	private List<User> likes;

	@OneToMany(mappedBy = "inReplyTo")
	private List<Tweet> inReplyTos;

	@OneToMany(mappedBy = "repostOf")
	private List<Tweet> repostOfs;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reply_id")
	private Tweet inReplyTo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "repost_id")
	private Tweet repostOf;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<HashTag> hashtags;

}
