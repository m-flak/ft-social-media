package com.cooksys.June2020.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@Getter
@Setter
public class Profile {

// Below I have two forms of setting up the Profile entity method. The first,
// (commented out), is coming from the example given in the June2020 assignment. I assume this is
// what we will use.
// The second is from the chat app example. Right now I'm going back and
// reviewing the videos and notes.
// However, I did want to post this as a branch for the time being.
// Please feel free to destroy or adjust with any thoughts. I'm sure there are
// issues.

//	firstName?: 'string',
//	lastName?: 'string',
//	email: 'string',
//	phone?: 'string'

	private String firstName;

	private String lastName;

	@Column(unique = true, nullable = false)
	private String email;

	private String phoneNumber;

}