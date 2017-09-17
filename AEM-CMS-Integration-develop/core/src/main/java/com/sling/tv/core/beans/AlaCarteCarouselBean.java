package com.sling.tv.core.beans;

/**
 * This class is POJO class to encapsulate ImagePath and BarColor options
 * 
 * @author deou
 *
 */
public class AlaCarteCarouselBean {

	private String barColor;
	private String imagePath;
	private String alt;

	public final String getBarColor() {
		return barColor;
	}

	public final void setBarColor(final String barColor) {
		this.barColor = barColor;
	}

	public final String getImagePath() {
		return imagePath;
	}

	public final void setImagePath(final String imagePath) {
		this.imagePath = imagePath;
	}

	public final String getAlt() {
		return alt;
	}

	public final void setAlt(String alt) {
		this.alt = alt;
	}

}
