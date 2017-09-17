package com.sling.tv.core.models;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.wcm.api.Page;

/**
 *
 * This Sling Model is for data layer.
 *
 * @author vhs
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class AnalyticsModel {

	public static final int LOCALE_LEVEL = 3;
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyticsModel.class);

    @Inject
    private Page currentPage;

    private String dataLayer;

    @PostConstruct
    private void init() {
        // populate the product info and page info
        try {
            final JSONObject dataLayerObj = new JSONObject();
            dataLayerObj.put("pageInfo", createPageInfo());
            dataLayerObj.put("category", createCategory());
            dataLayer = dataLayerObj.toString(2);
            LOGGER.debug("dataLayer ::: " + dataLayer);

        } catch (final JSONException e) {
            LOGGER.error("failed to create data layer.", e);
            dataLayer = "{ 'error': 'failed to create data layer''}";
        }

    }

    private JSONObject createPageInfo() throws JSONException {
        final JSONObject pageInfo = new JSONObject();
        Page depthPage = currentPage.getAbsoluteParent(LOCALE_LEVEL-1);
        pageInfo.put("pageName", depthPage.getName()+":"+currentPage.getName());
        
        return pageInfo;
    }

    private JSONObject createCategory() throws JSONException {
        final JSONObject category = new JSONObject();
        String firstLevel = getPageNameByDepth(LOCALE_LEVEL);
        
        category.put("PageType", "");
        category.put("primaryCategory", firstLevel);
        category.put("subCategory1","");
        
        StringBuilder subCategory = new StringBuilder();
        String secondLevel = getPageNameByDepth(LOCALE_LEVEL+1);

        if(secondLevel != null){
        	subCategory.append(firstLevel);
        	subCategory.append(":");
        	subCategory.append(secondLevel);
            category.put("subCategory1", subCategory.toString());
        }
        return category;
        
    }
    
    private String getPageNameByDepth(int depth){
    	String depthPageName = null;
    	Page depthPage = currentPage.getAbsoluteParent(depth);
        if (depthPage != null) {
            depthPageName = depthPage.getName();
        }
        return depthPageName;
    }
    
    public final String getDataLayer() {
        return dataLayer;
    }

}
