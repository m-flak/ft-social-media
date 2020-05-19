package com.cooksys.June2020.repositories;

import com.cooksys.June2020.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByCredentialsUsername(String username);

    Optional<User> findByCredentialsUsernameAndCredentialsPasswordAndIsDeletedIsFalse(String username, String password);

    Optional<User> findByCredentialsUsernameAndIsDeletedIsFalse(String username);

    List<User> findAllByIsDeletedIsFalse();

    int countByCredentialsUsername(String username);

    int countByCredentialsUsernameAndIsDeletedIsFalse(String username);
}
