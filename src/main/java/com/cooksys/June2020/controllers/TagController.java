package com.cooksys.June2020.controllers;

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
