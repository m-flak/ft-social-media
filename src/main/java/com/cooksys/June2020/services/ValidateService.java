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

    public boolean hashtagExists(String hashtagLabel) {
        return (hashtagRepository.countByLabel(hashtagLabel) == 1);
    }

    public boolean usernameAvailable(String username) {
        return (userRepository.countByCredentialsUsername(username) == 0);
    }

    public boolean usernameExists(String username) {
        return (userRepository.countByCredentialsUsernameAndIsDeletedIsFalse(username) == 1);
    }
}
