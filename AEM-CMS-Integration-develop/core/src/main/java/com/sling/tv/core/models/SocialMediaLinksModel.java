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
import com.sling.tv.core.beans.SocialMediaBean;

/**
 * This model class will be used to display social links
 *
 * @author deou
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class SocialMediaLinksModel {

    private static Logger LOG = LoggerFactory.getLogger(SocialMediaLinksModel.class);
    @Inject
    @Optional
    @Via("resource")
    private List<String> socialMediaLinks;

    private List<SocialMediaBean> socialMediaList;

    public List<SocialMediaBean> getSocialMediaList() {
        return socialMediaList;
    }

    @PostConstruct
    public final void init() {

        // Populate the social media section
        populateSocialMediaLinks();
    }

    private void populateSocialMediaLinks() {
        if (null != socialMediaLinks && !socialMediaLinks.isEmpty()) {
            socialMediaList = new ArrayList<>(socialMediaLinks.size());
            for (final String socialMediaItem : socialMediaLinks) {
                populateSocialMediaBean(socialMediaItem);
            }
        }
    }

    private void populateSocialMediaBean(final String socialMediaItem) {
        if (StringUtils.isNotEmpty(socialMediaItem)) {

            final SocialMediaBean socialMediaBean = new SocialMediaBean();
            try {
                final JSONObject socialMediaJSON = new JSONObject(socialMediaItem);
                socialMediaBean.setIcon(socialMediaJSON.has(SlingTvConstants.SOCIAL_MEDIA_ICON)
                        ? socialMediaJSON.getString(SlingTvConstants.SOCIAL_MEDIA_ICON) : StringUtils.EMPTY);
                socialMediaBean.setLink(socialMediaJSON.has(SlingTvConstants.SOCIAL_MEDIA_LINK)
                        ? socialMediaJSON.getString(SlingTvConstants.SOCIAL_MEDIA_LINK) : StringUtils.EMPTY);
                socialMediaBean.setWindowOption(socialMediaJSON.has(SlingTvConstants.SOCIAL_MEDIA_LINK_SELECTION)
                        ? socialMediaJSON.getString(SlingTvConstants.SOCIAL_MEDIA_LINK_SELECTION) : StringUtils.EMPTY);

                if (socialMediaJSON.has(SlingTvConstants.SOCIAL_MEDIA_ICON)) {
                    socialMediaList.add(socialMediaBean);
                }
            } catch (final JSONException e) {
                LOG.error("JSON Exception ", e);
            }

        }
    }
}
