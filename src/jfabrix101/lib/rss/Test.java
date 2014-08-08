package jfabrix101.lib.rss;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.net.URL;

public class Test {

	
	public static void main(String[] args)  throws Exception {
		test2(args);
	}
	
	public static void test3(String[] args) throws Exception {
		RssParser parser = new RssParser();
		
		parser.setDebugMode(true, new PrintStream(new File("output.txt")));
		
		File folder = new File("/home/jfabrix101/Desktop/test-feeds");
		RssFeed feed = parser.parse(new FileInputStream(new File(folder, "politica.xml")));
		System.out.println(parser.getExecutionTime() + "> " + dumpFeed(feed, true));
	}
	
	
	public static void test2(String[] args) throws Exception {
		String url = "http://feeds.feedburner.com/Androidiani?format=xml";
		
		url = "http://feeds.feedburner.com/worldpressphoto/RuUA";
		RssParser parser = new RssParser();
		parser.setDebugMode(true);
		
		RssFeed feed = parser.parse(new URL(url));
		System.out.println(parser.getExecutionTime() + "> " + dumpFeed(feed, true));
		
	}
	
	/**
	 * @param args
	 */
	public static void test1(String[] args) throws Exception {
		
		RssParser parser = new RssParser();
		parser.setDebugMode(false);
		
		File folder = new File("/home/jfabrix101/Desktop/test-feeds");
		String files[] = folder.list();
		for (String f : files)  {
			if (f.equals("script.sh")) continue;
			if (f.equals("output.txt")) continue;
			try {
				File xml = new File(folder, f);
				RssFeed feed = parser.parse(new FileInputStream(xml));
				
//				System.out.println(parser.getExecutionTime() + "> " + feed);
//				for (String cat : feed.getCategories()) {
//					System.out.println("Category: " + cat);
//				}
				for (RssItem item : feed.getItemList()) {
					if (item.getAuthor() == null) {
						System.out.println("Author missing: " + f);
						break;
					}
					
//					System.out.println(">> " + item);//
				}
			} catch (Exception e) {
				System.err.println("Exception reading file : " + f);
				e.printStackTrace();
				System.exit(1);
			}
		}
		
	}

	
	private static String dumpFeed(RssFeed feed, boolean showItems) {
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
}
