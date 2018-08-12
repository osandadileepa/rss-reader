package com.osanda.rss.uzabaserss.services;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.XMLEvent;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.osanda.rss.uzabaserss.models.dto.Feed;
import com.osanda.rss.uzabaserss.models.dto.FeedMessage;
import com.osanda.rss.uzabaserss.services.common.RSSReadCommon;
import com.osanda.rss.uzabaserss.utils.ResponseMessage;

import lombok.extern.slf4j.Slf4j;

/***
 * Implementation of rss feed reading service for multiple rss feeds
 * 
 * @author Osanda Wedamulla
 *
 */

@Slf4j
@Service
public class RssFeedParserService implements RSSReadCommon {

	public static final String CHANNEL = "channel";
	public static final String TITLE = "title";
	public static final String LINK = "link";
	public static final String DESCRIPTION = "description";
	public static final String LAST_BUILD_DATE = "lastBuildDate";
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
	 * @return ResponseEntity<?>
	 */
	@Override
	public ResponseEntity<?> readRssFeed(List<String> rssUrls) {

		Map<String, Feed> feedMap = new HashMap<>();

		if (rssUrls == null) {
			rssUrls = new ArrayList<>();
			rssUrls.add("http://tech.uzabase.com/rss");
		}

		for (String s : rssUrls) {

			log.info("Reading URL : " + s);

			URL url = null;
			Feed feed = null;

			try {
				url = new URL(s);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
						.body(ResponseMessage.createResponse("Invalid RSS fedd."));
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

						switch (localPart) {

						case ITEM:
							if (isFeedHeader) {
								isFeedHeader = false;
								feed = new Feed(title, link, description, lastbuildDate, docs, generator);
							}
							event = eventReader.nextEvent();
							break;
						case TITLE:
							title = getCharacterData(event, eventReader).replaceAll("[NewsPicks]", "");
							break;
						case DESCRIPTION:
							description = getCharacterData(event, eventReader).replaceAll("[NewsPicks]", "");
							break;
						case LINK:
							link = getCharacterData(event, eventReader);
							break;
						case GUID:
							guid = getCharacterData(event, eventReader);
							break;
						case ENCLOSURE:
							enclosure = getElementData(event, eventReader, "url");
							break;
						case LAST_BUILD_DATE:
							lastbuildDate = getCharacterData(event, eventReader);
							break;
						case DOCS:
							docs = getCharacterData(event, eventReader);
							break;
						case PUB_DATE:
							pubdate = getCharacterData(event, eventReader);
							break;
						case GENERATOR:
							generator = getCharacterData(event, eventReader);
							break;
						}// end switch

					} else if (event.isEndElement()) {

						if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
							FeedMessage message = new FeedMessage();

							message.setTitle(title);
							message.setLink(link);
							message.setDescription(description);
							message.setPubDate(pubdate);
							message.setGuid(guid);
							message.setEnclosure(enclosure);

							feed.getItems().add(message);
							event = eventReader.nextEvent();
							continue;
						}
					}

				} // End checking next xml

			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(ResponseMessage.createResponse("Error reading RSS feed content"));
			}

			feedMap.put(s, feed);

		} // End url list iteration

		return ResponseEntity.ok(feedMap);

	}// readRssFeed()

	/***
	 * get input stream from url feed
	 * 
	 * @author Osanda Wedamulla
	 * @return InputStream
	 * @throws Exception
	 */
	private InputStream getStreamToRead(URL url) throws RuntimeException {

		if (url == null) {
			throw new RuntimeException();
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
	private String getCharacterData(XMLEvent event, XMLEventReader eventReader) throws XMLStreamException {
		String result = null;
		event = eventReader.nextEvent();

		StringBuffer sb = new StringBuffer();
		while (event.isCharacters()) {
			sb.append(event.asCharacters().getData());
			event = eventReader.nextEvent();
		}

		result = sb.toString();

		return result;
	}// getCharacterData()

	/***
	 * get element data in xml by element name
	 * 
	 * @author Osanda Wedamulla
	 * @param event
	 * @param eventReader
	 * @param element
	 * @return
	 * @throws XMLStreamException
	 */
	private String getElementData(XMLEvent event, XMLEventReader eventReader, String element)
			throws XMLStreamException {
		String result = "";

		if (event.getEventType() == 1) {

			Iterator<Attribute> attributes = event.asStartElement().getAttributes();

			while (attributes.hasNext()) {

				Attribute attribute = attributes.next();
				String value = attribute.getValue();
				String name = attribute.getName().toString();

				if (name.equalsIgnoreCase(element)) {
					result = value;
				}
			}
		}
		return result;
	}// getElementData()

	/***
	 * create a text file with rss feed and write to the response
	 * 
	 * @author Osanda Wedamulla
	 * @param request
	 * @param response
	 */
	@Override
	public void createTextFileRss(HttpServletRequest request, HttpServletResponse response) {

		ResponseEntity<Map<String, Feed>> readRssFeed = (ResponseEntity<Map<String, Feed>>) readRssFeed(null);
		
		File export = new File("data/rss/export.txt");
		
		StringBuffer sb = new StringBuffer();

		readRssFeed.getBody().forEach((key, value) -> {
			
			sb.append(value.getTitle() + "\n");
			sb.append(value.getLink() + "\n");
			sb.append(value.getDescription() + "\n");
			sb.append(value.getLastBuildDate() + "\n");
			sb.append(value.getDocs() + "\n");
			sb.append(value.getGenerator() + "\n");
			
			value.getItems().forEach(fm ->{
				
				sb.append(fm.getTitle() + "\n");
				sb.append(fm.getLink() + "\n");
				sb.append(fm.getDescription() + "\n");
				sb.append(fm.getPubDate() + "\n");
				sb.append(fm.getEnclosure() + "\n");
				sb.append(fm.getGuid() + "\n");
			});
			
		});
		
		
		try {
			OutputStream file = new FileOutputStream(export);
			file.write(sb.toString().getBytes());
			file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.writeFileToResponse(request, response, export);

	}// createTextFileRss()

	/***
	 * write file to the response
	 * 
	 * @author Osanda Wedamulla
	 * @param request
	 * @param response
	 * @param file
	 */
	private void writeFileToResponse(HttpServletRequest request, HttpServletResponse response, File file) {

		try {

			ServletContext context = request.getServletContext();
			FileInputStream inputStream = new FileInputStream(file);
			String mimeType = context.getMimeType(file.getName());
			if (mimeType == null) {
				mimeType = MediaType.APPLICATION_OCTET_STREAM.getType();
			}
			response.setContentType(mimeType);
			response.setContentLength((int) file.length());
			String headerValue = String.format("attachment; filename=\"%s\"", file.getName());
			response.setHeader("Content-Disposition", headerValue);
			OutputStream outStream = response.getOutputStream();
			byte[] buffer = new byte[1028];
			int bytesRead = -1;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, bytesRead);
			}
			inputStream.close();
			outStream.close();
		} catch (Exception e) {
			log.warn(e.getMessage());
		}

	}// writeFileToResponse()

}// class {} RssFeedParserService