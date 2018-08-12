package com.osanda.rss.uzabaserss.models;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Channel extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8682483674714593890L;

	private String lastBuildDate;
	private String docs;
	private String generator;
	
	@OneToMany(fetch = FetchType.EAGER, orphanRemoval = false)
	private List<Item> items; 

}
