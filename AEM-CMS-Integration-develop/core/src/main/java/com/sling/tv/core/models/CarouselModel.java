package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

@Model(adaptables = Resource.class)
public class CarouselModel {

	@Inject
	@Named("imagePaths")
	@Optional
	private String[] imagePaths;
	
	@Inject
	private ResourceResolver resourceResolver;

	private List<String> paths;
	

	@PostConstruct
	protected void init() throws Exception {
		
		this.paths = new ArrayList<String>();
		if (imagePaths != null && imagePaths.length > 0) {
			//Arrays.stream(imagePaths).forEach(link -> {
			for(String link:imagePaths){
				Resource pathResource = resourceResolver.getResource(link);
				if (pathResource != null) {
					paths.add(link);
				}
			}
		}
	}

	public List<String> getPaths() {
		return paths;
	}
}
