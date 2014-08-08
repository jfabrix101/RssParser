package jfabrix101.lib.rss;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public final class RssParser {

	private final RssHandler handler = new RssHandler();
	private XMLReader xmlReader = null;
	
	public RssParser() throws RssParserException {
		try {
	      
		  // Since SAXParserFactory implementations are not guaranteed to be
	      // thread-safe, a new local object is instantiated.
	      final SAXParserFactory factory = SAXParserFactory.newInstance();
	
	      // Support Android 1.6 (see Issue 1)
	      factory.setFeature("http://xml.org/sax/features/namespaces", false);
	      factory.setFeature("http://xml.org/sax/features/namespace-prefixes", true);
	
	      final SAXParser parser = factory.newSAXParser();
	      xmlReader = parser.getXMLReader();
	      xmlReader.setContentHandler(handler);
	      
	    } catch (ParserConfigurationException e) {
	      throw new RssParserException("ParserConfigurationException: " + e.getMessage(), e);
	    } catch (SAXException e) {
	    	throw new RssParserException("SAXException: " + e.getMessage(), e);
	    }
	}

	public RssFeed parse(URL url) throws RssParserException {
		try {
			RssFeed feed = parse(url.openStream());
			feed.setLink(url.toString());
			return feed;
		} catch (IOException e) {
	    	throw new RssParserException("IOException reading stream: " + e.getMessage(), e);
	    }
	}
	
	public RssFeed parse(InputStream in) throws RssParserException {
		// SAX automatically detects the correct character encoding 
		// from the stream. See also http://www.w3.org/TR/REC-xml/#sec-guessing
	    final InputSource source = new InputSource(in);
	    
	    try {
	    	xmlReader.parse(source);
	    } catch (SAXException e) {
	    	throw new RssParserException("SAXException reading stream: " + e.getMessage(), e);
	    } catch (IOException e) {
	    	throw new RssParserException("IOException reading stream: " + e.getMessage(), e);
	    }

	    return handler.getFeed();
	}
	
	
	public RssFeed parse(String source) throws RssParserException {
		// SAX automatically detects the correct character encoding 
		// from the stream. See also http://www.w3.org/TR/REC-xml/#sec-guessing
	    final InputSource src = new InputSource(new StringReader(source));
	    
	    try {
	    	xmlReader.parse(src);
	    } catch (SAXException e) {
	    	throw new RssParserException("SAXException reading stream: " + e.getMessage(), e);
	    } catch (IOException e) {
	    	throw new RssParserException("IOException reading stream: " + e.getMessage(), e);
	    }

	    return handler.getFeed();
	}
	
	public long getExecutionTime() { return handler.getExecutionTime(); }
	
	/**
	 * Set the debugMode (default = false).
	 * If set to ON, the debug informations will be printed on System.out
	 */
	public void setDebugMode(boolean mode) {
		handler.setDebugMode(mode);
	}
	
	/**
	 * Set the debugMode and the PrintStream where to print the 
	 * information.
	 * 
	 */
	public void setDebugMode(boolean mode, PrintStream debugStream) {
		handler.setDebugMode(mode, debugStream);
	}
}
