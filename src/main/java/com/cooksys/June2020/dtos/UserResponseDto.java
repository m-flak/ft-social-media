package com.cooksys.June2020.dtos;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserResponseDto {

	private Integer id;

	private String username;

	private ProfileDto profile;

	private Timestamp joined;
}
