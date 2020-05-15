package com.cooksys.June2020.services;

import org.springframework.stereotype.Service;

import com.cooksys.June2020.repositories.ValidateRepository;

@Service
public class ValidateService {

	private ValidateRepository validateRepository;

	public ValidateService(ValidateRepository validateRepository) {
		this.validateRepository = validateRepository;
	}

}
