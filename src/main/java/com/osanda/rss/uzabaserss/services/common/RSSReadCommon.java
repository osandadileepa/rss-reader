package com.osanda.rss.uzabaserss.services.common;

import java.util.List;

import org.springframework.http.ResponseEntity;

public interface RSSReadCommon {
	
	public ResponseEntity<?> readRssFeed(List<String> url);

}
