package com.osanda.rss.uzabaserss;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.osanda.rss.uzabaserss.models.dto.Feed;
import com.osanda.rss.uzabaserss.services.RssFeedParserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class UzabaseRssApplicationTests {
	
	@Inject private RssFeedParserService rssFeedParserService;
	
	@Test
	public void rssFeedTest() {
		
		List<String> urlList = new ArrayList<>();
		urlList.add("http://tech.uzabase.com/rss");
		
		ResponseEntity<Map<String, Feed>> readRssFeed = (ResponseEntity<Map<String, Feed>>) rssFeedParserService
				.readRssFeed(urlList);

		readRssFeed.getBody().forEach((key, value) -> {
			log.info(value.toString());
		});
		
		
	}

}
