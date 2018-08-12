package com.osanda.rss.uzabaserss.contollers;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.osanda.rss.uzabaserss.models.dto.Feed;
import com.osanda.rss.uzabaserss.services.RssFeedParserService;
import com.osanda.rss.uzabaserss.utils.ResponseMessage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("${spring.data.rest.base-path}")
@RequiredArgsConstructor
public class RssReadUrlAddContoller {
	
	private final RssFeedParserService rssFeedParserService; 
	
	/***
	 * @author Osanda Wedamulla
	 * 
	 * @param urlList
	 * @return
	 */
	@GetMapping(value = "read")
	public ResponseEntity<?> addUrlToRead (@RequestParam(value = "urlList", required = false) List<String> urlList) {
		System.err.println("List " + urlList);
		return rssFeedParserService.readRssFeed(urlList);
	}// addUrlToRead()
	
	
	/***
	 * printout converted details to console
	 * 
	 * @author Osanda Wedamulla
	 * 
	 * @return
	 */
	@GetMapping(value = "print")
	public ResponseEntity<?> printRssDataToConsole () {
		
		 ResponseEntity<Map<String, Feed>> readRssFeed = (ResponseEntity<Map<String, Feed>>) rssFeedParserService.readRssFeed(null);
		 
		 readRssFeed.getBody().forEach((key, value) -> {
			 
			 log.info("RSS Feed URL : " + key);
			 log.info("Content " + value.toString());
			 
		 });
		 
		 return ResponseEntity.ok(ResponseMessage.createResponse("Print Successfull."));
	}// printRssDataToConsole()

}