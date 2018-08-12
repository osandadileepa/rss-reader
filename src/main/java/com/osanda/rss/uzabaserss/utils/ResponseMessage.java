package com.osanda.rss.uzabaserss.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseMessage {
	
	public static Map<String, String> createResponse (String message) {
		
		 Map<String, String> data = new HashMap<>();
		 data.put("message", message);
		
		return data;
	}

}
