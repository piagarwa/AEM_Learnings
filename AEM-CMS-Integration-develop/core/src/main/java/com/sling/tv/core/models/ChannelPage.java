package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * Sling model for holding the Channel Page properties.
 *
 * @author vhs
 *
 */
@Model(adaptables = Resource.class)
public class ChannelPage {

	@Inject
	@Named("jcr:title")
	private String title;

	@Inject
	@Named("description")
	@Optional
	private String description;

	@Inject
	@Optional
	@Named("imageFile")
	private String imageFile;

	@Inject
	@Named("alt")
	@Optional
	private String alt;

	@Inject
	@Named("slingBlue")
	@Optional
	private String slingBlue;

	@Inject
	@Named("slingOrange")
	@Optional
	private String slingOrange;

	@Inject
	@Named("slingGreen")
	@Optional
	private String slingGreen;

	private List<ChannelPage> children = new ArrayList<>();

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

	public final String getImageFile() {
		return imageFile;
	}

	public final void setImageFile(String imageFile) {
		this.imageFile = imageFile;
	}

	public final String getAlt() {
		return alt;
	}

	public final void setAlt(String alt) {
		this.alt = alt;
	}

	public final String getSlingBlue() {
		return slingBlue;
	}

	public final void setSlingBlue(String slingBlue) {
		this.slingBlue = slingBlue;
	}

	public final String getSlingOrange() {
		return slingOrange;
	}

	public final void setSlingOrange(String slingOrange) {
		this.slingOrange = slingOrange;
	}

	public final String getSlingGreen() {
		return slingGreen;
	}

	public final void setSlingGreen(String slingGreen) {
		this.slingGreen = slingGreen;
	}

	public final List<ChannelPage> getChildren() {
		return children;
	}

	public final void setChildren(List<ChannelPage> children) {
		this.children = children;
	}

}
