package jfabrix101.lib.rss;

/**
 * RssFeed Lib Project
 * 
 * jfabrix101
 */
import java.util.ArrayList;
import java.util.List;

public class RssFeed {

	private List<String> xmlns = new ArrayList<String>();
	
	private List<RssItem> itemList = new ArrayList<RssItem>();
	
	private String title;
	private String link;
	private String description;
	private String language;
	private String pubDate;
	private String imageUrl;
	private String ttl;
	private List<String> categories = new ArrayList<String>();
	
	// ---- Stupid GETTER and SETTER 
	
	public List<RssItem> getItemList() { return itemList; }

	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getLink() { return link; }
	public void setLink(String link) { this.link = link; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public String getLanguage() { return language; }
	public void setLanguage(String language) { this.language = language; }
	public String getPubDate() { return pubDate; }
	public void setPubDate(String pubDate) { this.pubDate = pubDate; }
	public String getImageUrl() { return imageUrl; }
	public void setImageUrl(String imageUrl) {	this.imageUrl = imageUrl;	}
	public void setItemList(List<RssItem> itemList) { this.itemList = itemList; }
	public String getTTL() { return ttl; }
	public void setTTL(String ttl) { this.ttl = ttl; }
	public List<String> getCategories() { return categories; }
	public void setCategories(List<String> categories) { this.categories = categories; }
	
	public List<String> getXmlns() { return xmlns; }
	
	
	@Override
	public String toString() {
		return "RssFeed [title=" + title + ", link=" + link + 
				", description=" + description + ", language=" + language + 
				", pubDate=" + pubDate + ", imageUrl=" + imageUrl + 
				", TTL=" + ttl + 
				"]";
	}
	
	
}
