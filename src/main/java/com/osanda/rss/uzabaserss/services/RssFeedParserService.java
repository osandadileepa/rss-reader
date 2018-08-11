package com.osanda.rss.uzabaserss.services;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.stereotype.Service;

import com.osanda.rss.uzabaserss.models.dto.Feed;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RssFeedParserService {
	
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String CHANNEL = "channel";
	public static final String LANGUAGE = "language";
	public static final String COPYRIGHT = "copyright";
	public static final String LINK = "link";
	public static final String AUTHOR = "author";
	public static final String ITEM = "item";
	public static final String PUB_DATE = "pubDate";
	public static final String GUID = "guid";

    public URL url = null;
   
    public Feed readRssFeed (String feedUrl) {
    	
    	if (feedUrl == null) {
    		feedUrl = "http://tech.uzabase.com/rss";
    	}
    	
    	try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    	
    	return null;
    }// readRssFeed()

}// class {} RssFeedParserService
