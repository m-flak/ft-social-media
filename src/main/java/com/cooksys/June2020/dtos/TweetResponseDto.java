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
public class TweetResponseDto {
	private Integer id;

	private UserResponseDto author;

	private Timestamp timestamp;

	private String content;

	private TweetResponseDto inReplyTo;

	private TweetResponseDto repostOf;
}
