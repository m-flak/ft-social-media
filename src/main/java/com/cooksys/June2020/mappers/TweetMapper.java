package com.cooksys.June2020.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.June2020.dtos.TweetRequestDto;
import com.cooksys.June2020.dtos.TweetResponseDto;
import com.cooksys.June2020.entities.Tweet;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface TweetMapper {

	TweetResponseDto entityToDto(Tweet tweet);

	List<TweetResponseDto> entitiesToDtos(List<Tweet> tweets);

	Tweet dtoToEntity(TweetRequestDto dto);

}
