package com.cooksys.June2020.controllers;

import java.util.List;

import com.cooksys.June2020.dtos.TweetResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cooksys.June2020.dtos.HashTagDto;
import com.cooksys.June2020.services.TagService;

@RestController
@RequestMapping("/tags")
public class TagController {

	private TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

	@GetMapping
	public List<HashTagDto> getTags() {
		return tagService.getTags();
	}

	@GetMapping("/{label}")
	public ResponseEntity<List<TweetResponseDto>> getTagsByLabel(@PathVariable String label) {
		return tagService.getTagsByLabel(label);
	}

}
