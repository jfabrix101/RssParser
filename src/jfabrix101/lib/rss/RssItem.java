package jfabrix101.lib.rss;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * RssFeed Lib Project
 * 
 * jfabrix101
 * 
 * Single item of feed
 */
public class RssItem implements Serializable {

	private String pubDate = null;
	private String title;
	private String author;
	private String description;
	private String link;
	
	private List<String> categories = new ArrayList<String>();
	
	
	public String getPubDate() { 	return pubDate; }
	public void setPubDate(String pubDate) { this.pubDate = pubDate; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getAuthor() { return author; }
	public void setAuthor(String author) { this.author = author; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }
	public List<String> getCategories() { return categories; }
	public void setCategories(List<String> categories) { this.categories = categories; }
	
	
	@Override
	public String toString() {
		return "RssItem [pubDate=" + pubDate + ", title=" + title + ", author="
				+ author + ", description=" + description + ", link=" + link
				+ "]";
	}
	
	
}
