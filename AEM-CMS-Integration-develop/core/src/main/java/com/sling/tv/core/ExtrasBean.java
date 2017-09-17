package com.sling.tv.core;

import java.util.List;
import java.util.Map;


public class ExtrasBean {
	
	private String title;    
	   
    private List<Map<String, String>> subheading;
    
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Map<String, String>> getSubheading() {
		return subheading;
	}

	public void setSubheading(List<Map<String, String>> subheading) {
		this.subheading = subheading;
	}
}
