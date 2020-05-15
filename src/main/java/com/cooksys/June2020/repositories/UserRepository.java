package com.cooksys.June2020.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	Optional<User> findByCredentialsUsername(String username);

	Optional<User> findByCredentialsUsernameAndCredentialsPassword(String username, String password);

	Optional<User> findByProfileEmail(String email);

	@Query("SELECT u FROM User u WHERE u.credentials.username = ?1")
	User get(String username);

	Optional<User> findByUsernameAndNotIsDelete(String username);

}