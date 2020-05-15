package com.cooksys.June2020.mappers;

import com.cooksys.June2020.dtos.HashTagDto;
import com.cooksys.June2020.entities.HashTag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashTagMapper {
    HashTagDto entityToDto(HashTag entity);

    List<HashTagDto> entitiesToDtos(List<HashTag> entities);
    List<HashTag> dtosToEntities(List<HashTag> dtos);

    HashTag dtoToEntity(HashTagDto dto);
}
