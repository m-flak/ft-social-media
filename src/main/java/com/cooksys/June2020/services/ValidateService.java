package com.cooksys.June2020.services;

import com.cooksys.June2020.dtos.CredentialsDto;
import com.cooksys.June2020.dtos.UserRequestDto;
import com.cooksys.June2020.entities.User;
import com.cooksys.June2020.exception.BadRequestException;
import com.cooksys.June2020.exception.InvalidUserCredentialsException;
import com.cooksys.June2020.repositories.HashtagRepository;
import com.cooksys.June2020.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ValidateService {

    private final UserRepository userRepository;
    private final HashtagRepository hashtagRepository;

    public User validateUserCredentials(CredentialsDto credentialsToValidate) {
        Optional<User> potentialUser = userRepository.findByCredentialsUsernameAndCredentialsPasswordAndIsDeletedIsFalse(
                credentialsToValidate.getUsername(), credentialsToValidate.getPassword());
        if (!potentialUser.isPresent()) {
            throw new InvalidUserCredentialsException("Invalid Username/Password combination supplied.");
        }
        return potentialUser.get();
    }

    public void validateRequest(UserRequestDto userRequestDto) {
        if (userRequestDto.getCredentials() == null) {
            throw new BadRequestException("You must pass in a username and password");
        }
        if (userRequestDto.getCredentials().getUsername() == null) {
            throw new BadRequestException("A username must be provided");
        }
        if (userRequestDto.getCredentials().getPassword() == null) {
            throw new BadRequestException("A password must be provided");
        }
        if (userRequestDto.getProfile() == null || userRequestDto.getProfile().getEmail() == null) {
            throw new BadRequestException("An email must be provided");
        }
    }


}
