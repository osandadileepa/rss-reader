package com.osanda.rss.uzabaserss.services.common;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;

public interface RSSReadCommon {
	
	public ResponseEntity<?> readRssFeed(List<String> url);
	
	public void createTextFileRss(HttpServletRequest request, HttpServletResponse response);

}
