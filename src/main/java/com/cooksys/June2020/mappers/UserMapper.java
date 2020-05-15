package com.cooksys.June2020.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cooksys.June2020.entities.User;
import com.cooksys.June2020.dtos.UserRequestDto;
import com.cooksys.June2020.dtos.UserResponseDto;

//IMPORTANT This mapper will also need to handle mappings
//between a User entity and CredentialsDto as well as a User entity and ProfileDto
//Split into UserRequestDto & UserResponseDto

@Mapper(componentModel = "spring")
public interface UserMapper {

	@Mapping(source = "credentials.username", target = "username")
	UserResponseDto entityToDto(User user);

	List<UserResponseDto> entitiesToDtos(List<User> users);

	User dtoToEntity(UserRequestDto dto);

}
