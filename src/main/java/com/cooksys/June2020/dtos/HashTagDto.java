package com.cooksys.June2020.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@Getter
@Setter
public class HashTagDto {
    private String label;

    private Timestamp firstUsed;

    private Timestamp lastUsed;
}
