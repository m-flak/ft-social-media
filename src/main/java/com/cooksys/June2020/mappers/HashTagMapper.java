package com.cooksys.June2020.mappers;

import java.util.List;

import org.mapstruct.Mapper;

import com.cooksys.June2020.dtos.HashTagDto;
import com.cooksys.June2020.entities.HashTag;
import com.cooksys.June2020.entities.Tweet;

@Mapper(componentModel = "spring")
public interface HashTagMapper {
	HashTagDto entityToDto(HashTag entity);

	List<Tweet> entityToDto(List<Tweet> tweets);

	List<HashTagDto> entitiesToDtos(List<HashTag> entities);

	List<HashTag> dtosToEntities(List<HashTag> dtos);

	HashTag dtoToEntity(HashTagDto dto);
}
