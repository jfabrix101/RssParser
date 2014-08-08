RssParser
==

Java and Android RssParser
--

**RssReader** is a simple and light implementation of RSS Parser that can be used in java and android application without any external libraries.


To parse a feed simple use this code:

```java

	String url = "TYPE_YOUR_FEED_URL";
	RssParser parser = new RssParser();
	
	// optional
	parser.setDebugMode(true);
	
	RssFeed feed = parser.parse(new URL(url));
```	
	
If the feed is successfully parsed (not an RssParserException is thrown) then you can browse
your feed data. For example:

```java
	private String dumpFeed(RssFeed feed, boolean showItems) {
		StringBuilder sb = new StringBuilder();
		sb.append(feed + "\n");
		for (String cat : feed.getCategories()) {
			sb.append(" +-- Category: " + cat + "\n");
		}
		if (showItems) {
			for (RssItem item : feed.getItemList()) {
				sb.append(">> " + item + "\n");
			}
		}
		return sb.toString();
	}
```

Each RssItem contain the information related to the news. 
The RssItem is a simple POJO with information like pubDate (publication date), title, author, description and link.
This information can be null if not provided inside the feed.



