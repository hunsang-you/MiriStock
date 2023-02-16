package com.udteam.miristock.util;

import com.udteam.miristock.dto.NewsResponseDto;
import com.udteam.miristock.dto.NewsMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

@Slf4j
public class RSSFeedParser {
    static final String TITLE = "title";
    static final String LINK = "link";
    static final String DESCRIPTION = "description";
    static final String ITEM = "item";
    static final String PUB_DATE = "pubDate";
    static final String LASTBUILDTIME ="lastBuildDate";

    final URL url;

    public RSSFeedParser(String feedUrl) {
        try {
            this.url = new URL(feedUrl);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public NewsResponseDto readFeed() {
        NewsResponseDto newsResponseDto = null;
        try {
            boolean isFeedHeader = true;
            // Set header values intial to the empty string
            String title = "";
            String link = null;
            String description = null;
            String pubdate = null;
            String lastBuildDate = null;

            // First create a new XMLInputFactory
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            // Setup a new eventReader
            InputStream in = read();
            XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
            // read the XML document
            int newsCount = 0;
            while (eventReader.hasNext()) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isStartElement()) {
                    String localPart = event.asStartElement().getName().getLocalPart();
//                    System.out.println(localPart);
                    switch (localPart) {
                        case ITEM:
                            if (isFeedHeader) {
                                isFeedHeader = false;
                                newsResponseDto = new NewsResponseDto(title, link, description, lastBuildDate);
                            }
                            event = eventReader.nextEvent();

                        case TITLE:
                            title = getCharacterData(event, eventReader);
                            break;
                        case LINK:
                            link = getCharacterData(event, eventReader);
                            break;
                        case PUB_DATE:
                            pubdate = getCharacterData(event, eventReader);
                            break;
                        case LASTBUILDTIME:
                            lastBuildDate = getCharacterData(event, eventReader);
                            break;
                        case DESCRIPTION:
                            description = getCharacterData(event, eventReader);
                            break;
                    }
                } else if (event.isEndElement()) {
                    if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
                        NewsMessage message = new NewsMessage();
                        message.setTitle(title);
                        message.setLink(link);
                        message.setPubDate(pubdate);
                        assert newsResponseDto != null;
                        newsResponseDto.getMessages().add(message);
                        event = eventReader.nextEvent();
                        newsCount++;
                        if(newsCount >=35) {
                            return newsResponseDto;
                        }
                        continue;
                    }
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return newsResponseDto;
    }

    private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
            throws XMLStreamException {
        String result = "";
        event = eventReader.nextEvent();
        if (event instanceof Characters) {
            result = event.asCharacters().getData();
        }
        return result;
    }

    private InputStream read() {
        try {
            return url.openStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
