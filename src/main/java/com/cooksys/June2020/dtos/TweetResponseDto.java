package com.cooksys.June2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TweetResponseDto {
    private Long id;

    private UserResponseDto author;

    private Timestamp timestamp;

    private String content;

    private TweetResponseDto inReplyTo;

    private TweetResponseDto repostOf;
}
