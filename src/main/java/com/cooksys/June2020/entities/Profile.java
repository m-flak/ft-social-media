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

	private String firstName;

	private String lastName;

	@Column(nullable = false)
	private String email;

	private String phone;

}
