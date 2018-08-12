package com.osanda.rss.uzabaserss.models.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Feed {
	
	private String title; 
	private String link;
	private String description; 
	private String lastBuildDate;
	private String docs; 
	private String generator;
	
	private List<FeedMessage> items = new ArrayList<>();

	public Feed(String title, String link, String description, String lastbuildDate, String docs, String generator) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.lastBuildDate = lastbuildDate;
		this.docs = docs;
		this.generator = generator;
	}
	
}