package com.osanda.rss.uzabaserss.contollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.osanda.rss.uzabaserss.services.RssFeedParserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("${spring.data.rest.base-path}")
@RequiredArgsConstructor
public class RssReadUrlAddContoller {
	
	private final RssFeedParserService rssFeedParserService; 
	
	@GetMapping("read")
	public ResponseEntity<?> addUrlToRead () {
		return rssFeedParserService.readRssFeed(null);
	}

}
