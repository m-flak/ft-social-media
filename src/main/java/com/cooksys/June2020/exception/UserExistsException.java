package com.cooksys.June2020.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
public class UserExistsException extends RuntimeException {

    private final String message = "The username provided is not available";

}
