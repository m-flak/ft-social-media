package com.cooksys.June2020.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cooksys.June2020.entities.User;

@Repository
public interface ValidateRepository extends JpaRepository<User, Integer> {

}
