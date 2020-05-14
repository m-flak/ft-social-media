package com.cooksys.June2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TweetRequestDto {
    private String content;

    private CredentialsDto credentials;
}
