package com.example.chen.taco.parser;

import android.content.Context;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 解析environment_config.xml专用
 */
public class SaxParseHandler extends DefaultHandler {
	private String environment;
	private Map<String,String> messageRouteURL;
	private Map<String,String> venusURL;
	private Map<String,String> serverURL;
	private Map<String,String> fileServerURL;
	private Map<String,String> pmfURL;
	private Map<String,String> trackingId;
	private InputStream xmlStream;
    private String preKey = null;
    private String preTag = null;//作用是记录解析时的上一个节点名称
    private static SaxParseHandler instance = null;
    
    public synchronized static SaxParseHandler shareInstance(Context appContext, int rawResourceId) {
		if (instance == null) {
			instance = new SaxParseHandler(appContext, rawResourceId);
		}
		return instance;
	}

    public SaxParseHandler(Context appContext, int rawResourceId) {
		// TODO Auto-generated constructor stub
		xmlStream = appContext.getResources().openRawResource(rawResourceId);

        try {
		SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
			parser.parse(xmlStream, this);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startDocument()
	 */
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();
		messageRouteURL = new HashMap<String, String>();
		venusURL = new HashMap<String, String>();
		serverURL = new HashMap<String, String>();
		fileServerURL = new HashMap<String, String>();
		pmfURL = new HashMap<String, String>();
		trackingId = new HashMap<String, String>();
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endDocument()
	 */
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#startElement(java.lang.String, java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */
	@Override
	public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub
		super.startElement(uri, localName, qName, attributes);
		if ("MessageRouteURL".equals(qName)||"VenusURL".equals(qName)
				||"VenusSecurityKey".equals(qName)||"ServerURL".equals(qName)
				||"FileServerURL".equals(qName)||"TrackingId".equals(qName)
				||"PMFURL".equals(qName)) {
			preTag = qName;
		}
		preKey = qName;	
	}
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#endElement(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		preKey = null;  
	}
	
	/* (non-Javadoc)
	 * @see org.xml.sax.helpers.DefaultHandler#characters(char[], int, int)
	 */
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		if (preKey != null) {
	        String content = new String(ch,start,length);
			if ("Environment".equals(preKey)) {
				environment = content;
			}else if("DEV".equals(preKey)||"SIT".equals(preKey)||"PRE".equals(preKey)||"PRD".equals(preKey)) {
				if("MessageRouteURL".equals(preTag)) {
					messageRouteURL.put(preKey, content);
				}else if ("VenusURL".equals(preTag)) {
					venusURL.put(preKey, content);
				}else if ("ServerURL".equals(preTag)) {
					serverURL.put(preKey, content);
				}else if ("FileServerURL".equals(preTag)) {
					fileServerURL.put(preKey, content);
				}else if ("PMFURL".equals(preTag)) {
					pmfURL.put(preKey, content);
				}else if ("TrackingId".equals(preTag)) {
					trackingId.put(preKey, content);
				}
			}
		}
	}
	
	public String getMessageRouteURL() {
		return messageRouteURL.get(environment);
	}
	
	public String getVenusURL() {
		return venusURL.get(environment);
	}
	
	public String getServerURL() {
		return serverURL.get(environment);
	}
	
	public String getFileServerURL() {
		return fileServerURL.get(environment);
	}
	
	public String getPMFURL() {
		return pmfURL.get(environment);
	}
	
	public String getTrackingId() {
		return trackingId.get(environment);
	}
}