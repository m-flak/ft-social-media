package com.cooksys.June2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ProfileDto {
    private String firstName;

    private String lastName;

    private String email;

    private String phone;
}
