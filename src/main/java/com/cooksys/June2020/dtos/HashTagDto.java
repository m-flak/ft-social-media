package com.cooksys.June2020.dtos;

import java.sql.Timestamp;
import java.util.List;

import com.cooksys.June2020.entities.Tweet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HashTagDto {
	private String label;

	private Timestamp firstUsed;

	private Timestamp lastUsed;

	private List<TweetResponseDto> tweets;
}
