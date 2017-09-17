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
import com.sling.tv.core.beans.OfferDealsBean;

/**
 *
 * @author deou
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class OffersDealsModel {
    private static Logger LOG = LoggerFactory.getLogger(OffersDealsModel.class);
    @Inject
    @Optional
    @Via("resource")
    private List<String> offerDeals;

    private List<OfferDealsBean> offerDealsList;

    public List<String> getOfferDeals() {
        return offerDeals;
    }

    public List<OfferDealsBean> getOfferDealsList() {
        return offerDealsList;
    }

    @PostConstruct
    public final void init() {

        populateOfferDeals();
    }

    private void populateOfferDeals() {
        if (null != offerDeals && !offerDeals.isEmpty()) {
            offerDealsList = new ArrayList<>(offerDeals.size());
            for (final String offerDeal : offerDeals) {
                populateofferDeals(offerDeal);
            }
        }
    }

    private void populateofferDeals(final String offerDeal) {
        if (StringUtils.isNotEmpty(offerDeal)) {
            final OfferDealsBean offerDealBean = new OfferDealsBean();
            try {
                final JSONObject offerDealsJSON = new JSONObject(offerDeal);
                offerDealBean.setOfferFlagImage(offerDealsJSON.has(SlingTvConstants.OFFER_FLAG_IMAGE)
                        ? offerDealsJSON.getString(SlingTvConstants.OFFER_FLAG_IMAGE) : StringUtils.EMPTY);
                offerDealBean.setOfferFlagImageAlt(offerDealsJSON.has(SlingTvConstants.OFFER_FLAG_IMAGE_ALT)
                        ? offerDealsJSON.getString(SlingTvConstants.OFFER_FLAG_IMAGE_ALT) : StringUtils.EMPTY);
                offerDealBean.setOfferFlag(offerDealsJSON.has(SlingTvConstants.OFFER_FLAG)
                        ? offerDealsJSON.getString(SlingTvConstants.OFFER_FLAG) : StringUtils.EMPTY);
                offerDealBean.setDeviceTitleImage(offerDealsJSON.has(SlingTvConstants.DEVICE_TITLE_IMAGE)
                        ? offerDealsJSON.getString(SlingTvConstants.DEVICE_TITLE_IMAGE) : StringUtils.EMPTY);
                offerDealBean.setDeviceTitleImageAlt(offerDealsJSON.has(SlingTvConstants.DEVICE_TITLE_IMAGE_ALT)
                        ? offerDealsJSON.getString(SlingTvConstants.DEVICE_TITLE_IMAGE_ALT) : StringUtils.EMPTY);
                offerDealBean.setDeviceImage(offerDealsJSON.has(SlingTvConstants.DEVICE_IMAGE)
                        ? offerDealsJSON.getString(SlingTvConstants.DEVICE_IMAGE) : StringUtils.EMPTY);
                offerDealBean.setDeviceImageAlt(offerDealsJSON.has(SlingTvConstants.DEVICE_IMAGE_ALT)
                        ? offerDealsJSON.getString(SlingTvConstants.DEVICE_IMAGE_ALT) : StringUtils.EMPTY);

                if (offerDealsJSON.has(SlingTvConstants.DEVICE_TITLE_IMAGE)) {
                    offerDealsList.add(offerDealBean);
                }
            } catch (final JSONException e) {
                LOG.error("JSON Exception ", e);
            }

        }
    }

}
