package com.cooksys.June2020.controllers;

import com.cooksys.June2020.services.TagService;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tags")
public class TagController {

	private TagService tagService;

	public TagController(TagService tagService) {
		this.tagService = tagService;
	}

}
