package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sling.tv.core.SlingTvConstants;
import com.sling.tv.core.beans.OfferDealsBean;
import com.sling.tv.core.services.OsgiConfigService;

@Model(adaptables = SlingHttpServletRequest.class)
public class OTAComponentModel {
	private static Logger LOG = LoggerFactory.getLogger(OTAComponentModel.class);
	
	@Inject
	OsgiConfigService osgiConfigService;

	@Inject
    protected SlingHttpServletRequest request;
	protected Resource resource;
	private String addressScrubURL;
	private String antennaServiceURL;
		
	@Inject
    @Optional
    @Via("resource")
    private List<String> offerDeals;

    public List<OfferDealsBean> offerDealsList;

    public List<String> getOfferDeals() {
        return offerDeals;
    }

    public List<OfferDealsBean> getOfferDealsList() {
        return offerDealsList;
    }
    
	@PostConstruct
	protected void init() {

		this.addressScrubURL = osgiConfigService.getSlingAddressScrubURL();
		this.antennaServiceURL = osgiConfigService.getSlingAntennaServiceURL();
		LOG.debug("AddressScrub URL {}", addressScrubURL);
		populateOfferDeals();
    }
	
	public String getAddressScrubURL() {
        return addressScrubURL;
    }
	
	public String getAntennaServiceURL() {
        return antennaServiceURL;
    }
	
	public void populateOfferDeals() {
        if (null != offerDeals && !offerDeals.isEmpty()) {
            offerDealsList = new ArrayList<>(offerDeals.size());
            for (final String offerDeal : offerDeals) {
                populateofferDeals(offerDeal);
            }
        }
    }

	public void populateofferDeals(final String offerDeal) {
        if (StringUtils.isNotEmpty(offerDeal)) {
            final OfferDealsBean offerDealBean = new OfferDealsBean();
            try {
                final JSONObject offerDealsJSON = new JSONObject(offerDeal);
                offerDealBean.setOfferImage(offerDealsJSON.has(SlingTvConstants.OTA_OFFER_IMAGE)
                        ? offerDealsJSON.getString(SlingTvConstants.OTA_OFFER_IMAGE) : StringUtils.EMPTY);
                offerDealBean.setOfferHeader(offerDealsJSON.has(SlingTvConstants.OTA_OFFER_HEADER)
                        ? offerDealsJSON.getString(SlingTvConstants.OTA_OFFER_HEADER) : StringUtils.EMPTY);
                offerDealBean.setOfferDescription(offerDealsJSON.has(SlingTvConstants.OTA_OFFER_DESCRIPTION)
                        ? offerDealsJSON.getString(SlingTvConstants.OTA_OFFER_DESCRIPTION) : StringUtils.EMPTY);
                offerDealBean.setOfferPrice(offerDealsJSON.has(SlingTvConstants.OTA_OFFER_PRICE)
                        ? offerDealsJSON.getString(SlingTvConstants.OTA_OFFER_PRICE) : StringUtils.EMPTY);
                offerDealBean.setOfferCaveat(offerDealsJSON.has(SlingTvConstants.OTA_OFFER_CAVEAT)
                        ? offerDealsJSON.getString(SlingTvConstants.OTA_OFFER_CAVEAT) : StringUtils.EMPTY);
                offerDealBean.setOfferRibbonText(offerDealsJSON.has(SlingTvConstants.OTA_OFFER_RIBBON_TEXT)
                        ? offerDealsJSON.getString(SlingTvConstants.OTA_OFFER_RIBBON_TEXT) : StringUtils.EMPTY);
                

                
                offerDealsList.add(offerDealBean);
                
            } catch (final JSONException e) {
                LOG.error("JSON Exception ", e);
            }

        }
    }
}
