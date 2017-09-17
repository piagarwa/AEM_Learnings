package com.sling.tv.core.models;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.inherit.HierarchyNodeInheritanceValueMap;
import com.day.cq.commons.inherit.InheritanceValueMap;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.sling.tv.core.SlingTvConstants;

/**
 * This class is used to create CTA URL based on external , CTA and page properties parameter
 *
 * @author deou
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class CTAComponent {

    private static Logger LOG = LoggerFactory.getLogger(CTAComponent.class);

    private String ctaURL;

    private String offerDescription;

    public String getOfferDescription() {
        return offerDescription;
    }

    @Inject
    @Self
    private SlingHttpServletRequest request;

    @Inject
    private PageManager pageManager;

    @Inject
    private Page currentPage;

    @Inject
    @Optional
    @Via("resource")
    @Named("targetURL")
    private String targetUrl;

    @Inject
    @Optional
    @Named("classification")
    @Via("resource")
    private String ctaClassification;

    @Inject
    @Optional
    @Named(SlingTvConstants.CTA_OVERRIDE_OFFER)
    @Via("resource")
    private String overrideOffer;

    @Inject
    @Optional
    @Named("overrideCTAPath")
    @Via("resource")
    private String ctaPath;
    @Inject
    @Optional
    @Via("resource")
    private String cartFlow;

    @Inject
    @Optional
    @Via("resource")
    private String cartStep;

    @Inject
    @Optional
    @Via("resource")
    private String deviceType;

    @Inject
    @Optional
    @Via("resource")
    private String planId;

    @Inject
    @Optional
    @Via("resource")
    private String offerId;

    @Inject
    @Optional
    @Via("resource")
    private String packageId;

    @Inject
    @Optional
    @Via("resource")
    private String extra;

    @Inject
    @Optional
    @Via("resource")
    private String text;

    /** The hero text bean. */

    @PostConstruct
    public final void init() {

        final InheritanceValueMap pagePropertiesMap = new HierarchyNodeInheritanceValueMap(
                currentPage.getContentResource());
        final RequestParameterMap slingRequestParams = request.getRequestParameterMap();

        final String queryParams = getQueryParams(slingRequestParams, pagePropertiesMap);
        if (targetUrl != null) {
            if (targetUrl.contains("?")) {
                ctaURL = StringUtils.isNotEmpty(queryParams) ? targetUrl + "&" + queryParams : targetUrl;
            } else {
                ctaURL = StringUtils.isNotEmpty(queryParams) ? targetUrl + "?" + queryParams : targetUrl;
            }
        }

        if ("no".equalsIgnoreCase(overrideOffer)) {
            final Page offerPage = pageManager.getPage(ctaPath);
            final Resource offerResource = offerPage.getContentResource().getChild("par/rich_text");
            if (offerResource != null) {
                offerDescription = (String) offerResource.getValueMap().get(SlingTvConstants.CTA_OFFER_DESCRIPTION);
            }
        } else {
            offerDescription = text;
        }

        LOG.debug("CTA URL {}", ctaURL);

    }

    private String getQueryParams(final RequestParameterMap slingRequestParams, final ValueMap pagePropertiesMap) {

        final String pageClassification = (String) pagePropertiesMap.getOrDefault(SlingTvConstants.CTA_CLASSIFICATION,
                StringUtils.EMPTY);
        final String pageDevicePartner = (String) pagePropertiesMap.getOrDefault("devicePartner", StringUtils.EMPTY);
        final String pageSalesPartner = (String) pagePropertiesMap.getOrDefault("salesChannelPartner",
                StringUtils.EMPTY);

        final String classification = StringUtils.isNotEmpty(ctaClassification) ? ctaClassification
                : pageClassification;

        final String locale = currentPage.getLanguage(false).getLanguage();
        String queryParams = StringUtils.EMPTY;
        final Iterator slingRequestParamIterator = slingRequestParams.keySet().iterator();
        while (slingRequestParamIterator.hasNext()) {
            final String requestParam = (String) slingRequestParamIterator.next();
            final String requestParamValue = slingRequestParams.getValue(requestParam).getString();
            queryParams = addQueryParam(requestParam, requestParamValue, queryParams);
        }

        if (!queryParams.contains(SlingTvConstants.CTA_DEVICE_PARTNER)) {
            queryParams = addQueryParam(SlingTvConstants.CTA_DEVICE_PARTNER, pageDevicePartner, queryParams);
        }
        if (!queryParams.contains(SlingTvConstants.CTA_SALES_CHANNEL_PARTNER)) {
            queryParams = addQueryParam(SlingTvConstants.CTA_SALES_CHANNEL_PARTNER, pageSalesPartner, queryParams);
        }
        if (!queryParams.contains(SlingTvConstants.CTA_LOCALE)) {
            queryParams = addQueryParam(SlingTvConstants.CTA_LOCALE, locale, queryParams);
        }
        queryParams = addQueryParam(SlingTvConstants.CTA_CLASSIFICATION, classification, queryParams);
        queryParams = addQueryParam("flow", cartFlow, queryParams);
        queryParams = addQueryParam("step", cartStep, queryParams);
        queryParams = addQueryParam("device", deviceType, queryParams);
        queryParams = addQueryParam("plan", planId, queryParams);
        queryParams = addQueryParam("offer_id", offerId, queryParams);
        queryParams = addQueryParam("package", packageId, queryParams);
        queryParams = addQueryParam("extra", extra, queryParams);
        queryParams = queryParams.substring(0, queryParams.length() - 1);
        return queryParams;
    }

    private String addQueryParam(final String queryParamName, final String queryParamValue, final String queryParams) {
        if (StringUtils.isNotEmpty(queryParamValue)) {
            final StringBuilder queryParam = new StringBuilder(queryParams);
            queryParam.append(queryParamName);
            queryParam.append("=");
            queryParam.append(queryParamValue);
            queryParam.append("&");
            return queryParam.toString();
        }
        return queryParams;

    }

    public String getCtaURL() {
        return ctaURL;
    }

}
