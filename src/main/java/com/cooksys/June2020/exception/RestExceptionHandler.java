package com.cooksys.June2020.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<Object> handleHashtagNotFoundException(HashtagNotFoundException hashtagNotFoundException,
                                                                 WebRequest webRequest) {
        return handleExceptionInternal(hashtagNotFoundException, hashtagNotFoundException.getMessage(), null,
                HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleInvalidUserCredentialsException(InvalidUserCredentialsException invalidUserCredentialsException,
                                                                        WebRequest webRequest) {
        return handleExceptionInternal(invalidUserCredentialsException, invalidUserCredentialsException.getMessage(),
                null, HttpStatus.UNAUTHORIZED, webRequest);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleTweetNotFoundException(TweetNotFoundException tweetNotFoundException,
                                                               WebRequest webRequest) {
        return handleExceptionInternal(tweetNotFoundException, tweetNotFoundException.getMessage(), null,
                HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException userNotFoundException,
                                                              WebRequest webRequest) {
        return handleExceptionInternal(userNotFoundException, userNotFoundException.getMessage(), null,
                HttpStatus.NOT_FOUND, webRequest);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleBadRequestException(BadRequestException badRequestException,
                                                            WebRequest webRequest) {
        return handleExceptionInternal(badRequestException, badRequestException.getMessage(), null,
                HttpStatus.BAD_REQUEST, webRequest);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleUserExistsException(UserExistsException userExistsException,
                                                            WebRequest webRequest) {
        return handleExceptionInternal(userExistsException, userExistsException.getMessage(), null,
                HttpStatus.BAD_REQUEST, webRequest);
    }
}
