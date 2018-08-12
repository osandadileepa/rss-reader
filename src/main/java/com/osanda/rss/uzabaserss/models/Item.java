package com.osanda.rss.uzabaserss.models;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Item extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5135084090635578205L;
	
	private String pubDate;
	private String guid;
	private String enclosure;

}
