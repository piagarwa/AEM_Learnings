package com.sling.tv.core.beans;

/**
 * @author vhs
 * 
 *         Bean to hold the emergency message properties.
 *
 */
public class MultiFieldBean {

	private String title;
	private String description;
	private String link;
	private String alt;

	public final String getTitle() {
		return title;
	}

	public final void setTitle(String title) {
		this.title = title;
	}

	public final String getDescription() {
		return description;
	}

	public final void setDescription(String description) {
		this.description = description;
	}

	public final String getLink() {
		return link;
	}

	public final void setLink(String link) {
		this.link = link;
	}

	public final String getAlt() {
		return alt;
	}

	public final void setAlt(String alt) {
		this.alt = alt;
	}
	
	
}
