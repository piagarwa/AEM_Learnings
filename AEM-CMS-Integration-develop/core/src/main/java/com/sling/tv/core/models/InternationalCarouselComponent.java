package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class InternationalCarouselComponent {

	private static Logger LOG = LoggerFactory
			.getLogger(InternationalCarouselComponent.class);

	@Inject
	@Self
	private InternationalCarouselModel internationalCarouselModel;

	private List<Integer> imagesCount;

	private List<Map<String, String>> carouselImages;

	public List<Map<String, String>> getCarouselImages() {
		return carouselImages;
	}

	public void setCarouselImages(List<Map<String, String>> carouselImages) {
		this.carouselImages = carouselImages;
	}

	@PostConstruct
	public final void init() {
		try {
			calculateImagesCount();
		} catch (Exception ex) {
			LOG.error("Error in init :::: ", ex);
		}
	}

	/**
     * 
     */
	private void calculateImagesCount() {
		try {
			LOG.info("Entering calculateImagesCount ...");
			String[] imagesArray = internationalCarouselModel.getImages();
			List<Map<String, String>> carouselImagesList = null;
			if (imagesArray != null) {
				List<Integer> imagesCount = new ArrayList<Integer>();
				carouselImagesList = new ArrayList<Map<String, String>>();
				for (int i = 0; i < imagesArray.length; i++) {
					imagesCount.add(i);
					JSONObject jsonObject = new JSONObject(imagesArray[i]);
					carouselImagesList.add(convertToMap(jsonObject, i));
				}
				LOG.info("Total carousel images ::: " + imagesCount.size());
				setImagesCount(imagesCount);
				LOG.info("Total carouselImagesList ::: "
						+ carouselImagesList.size());
				setCarouselImages(carouselImagesList);
			}
		} catch (Exception ex) {
			LOG.error("Error in calculateImagesCount ::: ", ex);
		}
	}

	/**
	 * @param jsonObject
	 * @return
	 */
	private Map<String, String> convertToMap(JSONObject jsonObject, int counter) {
		try {
			Map<String, String> logoInfo = new HashMap<String, String>();
			logoInfo.put("carousalImage", jsonObject.getString("carousalImage"));
			logoInfo.put("altText", jsonObject.getString("altName"));
			logoInfo.put("path", jsonObject.getString("imageURL"));
            logoInfo.put("id", "img"+counter);

			if (counter == 0) {
				logoInfo.put("first", "true");
			}
			return logoInfo;
		} catch (Exception ex) {
			LOG.error("Error in convertToExtraChannelBean ::: ", ex);
		}
		return null;
	}

	public List<Integer> getImagesCount() {
		return imagesCount;
	}

	public void setImagesCount(List<Integer> imagesCount) {
		this.imagesCount = imagesCount;
	}
}
