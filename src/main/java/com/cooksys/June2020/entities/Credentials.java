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
public class Credentials {

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

}
