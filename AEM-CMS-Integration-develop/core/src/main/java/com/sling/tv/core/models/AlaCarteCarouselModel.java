package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sling.tv.core.SlingTvConstants;
import com.sling.tv.core.beans.AlaCarteCarouselBean;

/**
 * This class is used to create Image List containing Image Path and Color Bar
 *
 * @author deou
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class AlaCarteCarouselModel {

    private static Logger LOG = LoggerFactory.getLogger(AlaCarteCarouselModel.class);
    @Inject
    @Optional
    @Via("resource")
    private List<String> alaCarteCarouselImages;

    private List<AlaCarteCarouselBean> alaCarteCarouselList;

    @PostConstruct
    public final void init() {

        populateCarouselImages();
    }

    private void populateCarouselImages() {
        if (null != alaCarteCarouselImages && !alaCarteCarouselImages.isEmpty()) {
            alaCarteCarouselList = new ArrayList<>(alaCarteCarouselImages.size());
            for (final String carouselImage : alaCarteCarouselImages) {
                populateTVCarousel(carouselImage);
            }
        }
    }

    private void populateTVCarousel(final String carouselImage) {
        if (StringUtils.isNotEmpty(carouselImage)) {
            final AlaCarteCarouselBean carouselBean = new AlaCarteCarouselBean();
            try {
                final JSONObject carouselImageJSON = new JSONObject(carouselImage);
                carouselBean.setImagePath(carouselImageJSON.has(SlingTvConstants.CAROUSEL_IMAGE_PATH)
                        ? carouselImageJSON.getString(SlingTvConstants.CAROUSEL_IMAGE_PATH) : StringUtils.EMPTY);
                carouselBean.setBarColor(carouselImageJSON.has(SlingTvConstants.CAROUSEL_IMAGE_COLOR_BAR)
                        ? carouselImageJSON.getString(SlingTvConstants.CAROUSEL_IMAGE_COLOR_BAR) : StringUtils.EMPTY);
                carouselBean.setAlt(carouselImageJSON.has(SlingTvConstants.ALT)
                        ? carouselImageJSON.getString(SlingTvConstants.ALT) : StringUtils.EMPTY);

                if (carouselImageJSON.has(SlingTvConstants.CAROUSEL_IMAGE_COLOR_BAR)) {
                    alaCarteCarouselList.add(carouselBean);
                }
            } catch (final JSONException e) {
                LOG.error("JSON Exception ", e);
            }

        }
    }

    public List<AlaCarteCarouselBean> getAlaCarteCarouselList() {
        return alaCarteCarouselList;
    }

}
