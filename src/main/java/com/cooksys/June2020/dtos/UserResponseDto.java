package com.cooksys.June2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class UserResponseDto {
    private String username;

    private ProfileDto profile;

    private Timestamp joined;
}
