package com.cooksys.June2020.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TweetNotFoundException extends RuntimeException {
    private String message;
}
