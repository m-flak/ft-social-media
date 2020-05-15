package com.cooksys.June2020.entities;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hashtags")
@NoArgsConstructor
@Getter
@Setter
public class HashTag {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	// HashTag parsing from a tweet's contents should lowercase the tags when
	// tagging
	// Since all hashtags will be converted to lowercase when contents are parsed,
	// they'll be unique!
	@Column(unique = true, nullable = false)
	private String label;

	@CreationTimestamp
	@Column(nullable = false)
	private Timestamp firstUsed;

	@UpdateTimestamp
	@Column(nullable = false)
	private Timestamp lastUsed;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "hashtags")
	private List<Tweet> tweets;
}
