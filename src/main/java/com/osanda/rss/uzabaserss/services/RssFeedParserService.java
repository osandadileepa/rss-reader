package com.osanda.rss.uzabaserss.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.osanda.rss.uzabaserss.models.dto.Feed;
import com.osanda.rss.uzabaserss.models.dto.FeedMessage;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RssFeedParserService {

	public static final String CHANNEL = "channel";
	public static final String TITLE = "title";
	public static final String LINK = "link";
	public static final String DESCRIPTION = "description";
	public static final String LAST_BUILD_DATE = "lastbuildDate";
	public static final String DOCS = "docs";
	public static final String GENERATOR = "generator";
	
	public static final String ITEM = "item";
	
	public static final String ENCLOSURE = "enclosure";
	public static final String PUB_DATE = "pubDate";
	public static final String GUID = "guid";

	/***
	 * get relevant feed from urls. Read the rss feed and convert it into readable
	 * format
	 * 
	 * @author Osanda Wedamulla
	 * @param feedUrl
	 * @return ResponseEntity
	 */
	//@PostConstruct
	public ResponseEntity<?> readRssFeed(List<String> rssUrls) {

		Map<String, Feed> feedMap = new HashMap<>();

		if (rssUrls == null) {
			rssUrls = new ArrayList<>();
			rssUrls.add("http://tech.uzabase.com/rss");
		}

		rssUrls.forEach(s -> {

			URL url = null;

			try {
				url = new URL(s);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}

			try {

				Boolean isFeedHeader = true;

				// Set header values intial to the empty string
				String title = "";
				String link = "";
				String description = "";
				String lastbuildDate = "";
				String docs = "";
				String generator = "";
				
				String enclosure = "";
				String pubdate = "";
				String guid = "";
				
				Feed feed = null;

				// First create a new XMLInputFactory
				XMLInputFactory inputFactory = XMLInputFactory.newInstance();
				// Setup a new eventReader
				InputStream in = this.getStreamToRead(url);

				XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
				// read the XML document
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (event.isStartElement()) {
						String localPart = event.asStartElement().getName().getLocalPart();
						
						System.err.println("Local" + localPart);
						
//						switch (localPart) {
//						
//						case ITEM:
//							if (isFeedHeader) {
//								isFeedHeader = false;
//								feed = new Feed(title, link, description, lastbuildDate, docs, generator);
//							}
//							event = eventReader.nextEvent();
//							break;
//						case TITLE:
//							title = getCharacterData(event, eventReader);
//							break;
//						case DESCRIPTION:
//							description = getCharacterData(event, eventReader);
//							break;
//						case LINK:
//							link = getCharacterData(event, eventReader);
//							break;
//						case GUID:
//							guid = getCharacterData(event, eventReader);
//							break;
//						case ENCLOSURE:
//							language = getCharacterData(event, eventReader);
//							break;
//						case PUB_DATE:
//							pubdate = getCharacterData(event, eventReader);
//							break;
//						}
						
					} else if (event.isEndElement()) {
						
						System.err.println("Event Element" + event.asEndElement().getName().getLocalPart());
						
//						if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
//							FeedMessage message = new FeedMessage();
//							
//							message.setTitle(title);
//							message.setLink(link);
//							message.setDescription(description);
//							message.setPubDate(pubdate);
//							message.setGuid(guid);
//							message.setEnclosure(enclosure);
//							
//							feed.getItems().add(message);
//							event = eventReader.nextEvent();
//							continue;
//						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}

		});// End url list iteration

		return ResponseEntity.ok(feedMap);
	}// readRssFeed()

	/***
	 * get input stream from url feed
	 * 
	 * @author Osanda Wedamulla
	 * @return InputStream
	 * @throws Exception 
	 */
	private InputStream getStreamToRead(URL url) throws Exception {

		if (url == null) {
			throw new Exception("URL not available.");
		}

		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}// getStreamToRead()
	
	
	/**
	 * get string characters from XML events 
	 * 
	 * @author Osanda Wedamulla
	 * 
	 * @param event
	 * @param eventReader
	 * @return
	 * @throws XMLStreamException
	 */
	private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }// getCharacterData()

}// class {} RssFeedParserService