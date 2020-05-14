package com.cooksys.June2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class ContextDto {
    private TweetResponseDto target;

    private List<TweetResponseDto> before;

    private List<TweetResponseDto> after;
}
