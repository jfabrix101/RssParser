package jfabrix101.lib.rss;

/**
 * RssFeed Lib Project
 * 
 * jfabrix101
 */
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

@SuppressWarnings("all")
public class RssHandler extends DefaultHandler {

	private StringBuilder textBuilder = null;
	private RssFeed objectModel = null;

	private boolean debugMode = false;
	private PrintStream debugStream = System.out;
	private long executionTime = 0;
	
	private final String CDATA_BEGIN = "<![CDATA[";
	private final String CDATA_END = "]]>";
	
	private Stack<String> tagStack = new Stack<String>();
	
	public RssFeed getFeed() { 
		return objectModel;
	}
	
	/**
	 * Set the debugMode (default = false).
	 * If set to ON, the debug informations will be printed on System.out
	 */
	public void setDebugMode(boolean mode) {
		this.debugMode = mode;
	}
	
	/**
	 * Set the debugMode and the PrintStream where to print the 
	 * information.
	 * 
	 */
	public void setDebugMode(boolean mode, PrintStream debugStream) {
		this.debugMode = mode;
		this.debugStream = debugStream;
	}
	
	/**
	 * Return the execution time taken from parser process
	 */
	public long getExecutionTime() { 
		return executionTime; 
	}
	
	public void characters(char[] ch, int start, int length) throws SAXException {
		super.characters(ch, start, length);
		textBuilder.append(new String(ch, start, length));
	}
	
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		objectModel = new RssFeed();
		currentItem = null;
		executionTime = System.currentTimeMillis();
		if (debugMode) debugStream.println("< statDocument()");
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		executionTime = System.currentTimeMillis() - executionTime;
		if (debugMode) {
			debugStream.println("> endDocument()");
			debugStream.println("Execution time: " + executionTime + " millis");
		}
		
		// If pubDate is null, then pubDate is the pubDate of first items (the latest)
		try {
			if (objectModel.getPubDate() == null) {
				objectModel.setPubDate(objectModel.getItemList().get(0).getPubDate());
			}
		} catch (Exception e) {}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) 
	throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		textBuilder = new StringBuilder(); // Reset Text inside a tag
		String tagName = localName + ":" + qName;
		int numAttr = attributes.getLength();
		Properties attrs = null;
		if (numAttr > 0) {
			attrs = new Properties();
			for(int i=0; i<numAttr; i++) {
				String name = attributes.getQName(i);
				String value = attributes.getValue(i);
				attrs.put(name, value);
			}
		}
		
		doStartTag(tagName, attrs);
	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException {
		super.endElement(uri, localName, qName);
		doEendElement(uri, localName, qName);
		tagStack.pop();
		textBuilder = new StringBuilder(); // Reset Text inside a tag
	}

	private String printStack() {
		StringBuilder sb = new StringBuilder("TagStack: ");
		for (int i=0; i<tagStack.size(); i++) {
			sb.append(" > " + tagStack.get(i));
		}
		return sb.toString();
	}
	
	// ------------------
	
	RssItem currentItem = null;

	
	public void doStartTag(String tagName, Properties attributes) {
		tagStack.add(tagName);
		if (debugMode) {
			debugStream.println("< Start Tag: " + tagName + " -- Attributes: " + attributes);
		}
		
		if (tagName.equals(":item") || tagName.equals(":entry")) {
			currentItem = new RssItem();
			currentItem.setPubDate(new Date().toLocaleString());  // Default is now
			return;
		}
		
	}
	
	public void doEendElement(String uri, String localName, String qName)
	throws SAXException {
		String tagName = localName + ":" + qName;
		if (debugMode) {
			debugStream.println("> EndTag " + tagName + " - InsideText: " + getText());
			debugStream.println(printStack() + " --> " + getText());
		}
		
		
		if (tagName.equals(":entry") 
				||	tagName.equals(":item")) {
			if (currentItem.getPubDate() == null) {
				currentItem.setPubDate(new Date().toLocaleString());
			}
			objectModel.getItemList().add(currentItem);
			currentItem = null;
			return;
		}
		
		if (currentItem == null) {
			if (tagName.equals(":title") ) { objectModel.setTitle(getText()); return;}
			if (tagName.equals(":language") ) { objectModel.setLanguage(getText()); return;}
			if (tagName.equals(":description") ) { objectModel.setDescription(getText()); return;}
			if (tagName.equals(":link") ) { objectModel.setLink(getText()); return;}
			if (tagName.equals(":pubDate")) { objectModel.setPubDate(getText()); return; }
			if (tagName.equals(":lastBuildDate")) { objectModel.setPubDate(getText()); return; }
			if (tagName.equals(":ttl")) { objectModel.setTTL(getText()); return; }
			if (tagName.equals(":url")) {
				String tags = printStack();
				if (tags.endsWith(":image > :url")) objectModel.setImageUrl(getText());
				return;
			}

			// Atom 
			if (tagName.equals(":logo")) { objectModel.setImageUrl(getText()); }
			if (tagName.equals(":subtitle")) { objectModel.setDescription(getText()); return; }
			if (tagName.equals(":updated")) { objectModel.setPubDate(getText()); return; }
			return;
		}
		
		
		if (tagName.equals(":title") ) { currentItem.setTitle(getText()); return; }
		if (tagName.equals(":author") || tagName.equals(":dc:creator")) { currentItem.setAuthor(getText()); return;}
		if (tagName.equals(":description") ) { currentItem.setDescription(getText()); return;}
		
		if (tagName.equals(":link") ) { currentItem.setLink(getText()); return;}
		
		if (tagName.equals(":origLink")) { // RssFeed by googleReader
			String txt = getText();
			if (!isEmptyString(txt)) currentItem.setLink(getText()); 
			return;
		}
		
		if (tagName.equals(":pubDate") || tagName.equals(":date") || tagName.equals("published")) {
			currentItem.setPubDate(getText());
//			String dateText = getText();
//			Date xDate = parseDate(dateText);
//			if (xDate == null) {
//				debug("***" + tagName + " - Unable to parse date format: " + dateText);
//			} else {
//				currentItem.setPubDate(xDate.getTime());
//			}
			return;
		}
		
		// Recupero testo HTML per varie tipoligie di FeedRss
		if (tagName.equals(":content:encoded") || tagName.equals(":content")
				|| tagName.equals(":encoded")) { 
			currentItem.setDescription(getText()); 
			return;
		}
	
		if (tagName.equals(":category")) {
			currentItem.getCategories().add(getText());
		}
	}
	
	private void debug(String msg) {
		if (debugMode) 	debugStream.println(msg);
	}
	
	
	protected String getText() {
		String text = textBuilder.toString();
		if (text.startsWith(CDATA_BEGIN) && text.endsWith(CDATA_END)) {
			text = text.substring(CDATA_BEGIN.length(), text.length() - CDATA_END.length());
		}
		
//		text = text.replaceAll("&quot;", "\"");
//		text = text.replaceAll("&#8217;", "'");
//		text = text.replaceAll("&#8220;", "\"");
//		text = text.replaceAll("&#8221;", "\"");
//		text = text.replaceAll("&#8222;", "\"");
//		text = text.replaceAll("&#8230;", "...");
//		text = text.replaceAll("&#8216;", "'");
//		text = text.replaceAll("&#8217;", "'");
//		text = text.replaceAll("&#8442;", " "); 
//		text = text.replaceAll("\n", "");
//		text = text.replaceAll("\t", "  ");
		return text.trim();
	}
	
	private boolean isEmptyString(String s) {
		if (s == null) return true;
		if (s.length() == 0) return true;
		if (s.trim().length() == 0) return true;
		return false;
	}
	
	// Possible date formats
	private final static SimpleDateFormat SIMPLE_DATE_FORMATS[] = {
		new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'"),
		new SimpleDateFormat("MM/dd/yyyy HH:mm:ss"),
		new SimpleDateFormat("MM/dd/yyyy - HH:mm"),
		new SimpleDateFormat("dd/MM/yyyy"),
		new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
		new SimpleDateFormat("dd/MM/yyyy - HH:mm"),
		new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss")
		
	};
	
	public static Date parseDate(String strDate) {
		Date res = null;
		
		try {
			res = new Date(strDate);
			if (res != null) return res;
		} catch (Exception e) {}
		
		
		for (int i=0; i<SIMPLE_DATE_FORMATS.length && res == null; i++) {
			try {
				SimpleDateFormat df = SIMPLE_DATE_FORMATS[i];
				res = df.parse(strDate);
			} catch (Exception e) {
				
			}
		}

		// Prova solo con la data escludendo l'ora
		if (res == null) {
			try {
				int tPos = strDate.indexOf("T");
				if (tPos != -1) {
					String s = strDate.substring(0, tPos);
					String formats[] = new String[] {"yyyy-MM-dd", "dd-MM-yyyy", "dd/MM/yyyy" };
					for (int i=0; i<formats.length && res == null; i++) {
						SimpleDateFormat df = new SimpleDateFormat(formats[i]);
						res = df.parse(strDate);
					}
				}
			} catch (Exception e) {}
		}
		
		return res;
	}
}
