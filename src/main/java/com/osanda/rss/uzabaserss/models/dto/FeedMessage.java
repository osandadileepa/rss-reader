package com.osanda.rss.uzabaserss.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class FeedMessage {
	
	private String title; 
	private String link;
	private String description;
	
	private String pubDate;
	private String guid;
	private String enclosure;

}