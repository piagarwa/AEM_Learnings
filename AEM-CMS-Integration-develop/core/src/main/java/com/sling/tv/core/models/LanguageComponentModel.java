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
import com.sling.tv.core.beans.LanguageBean;

/**
 * This class is used to map language page properties to Language Bean resource
 * 
 * @author deou
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class LanguageComponentModel {
    private static Logger LOG = LoggerFactory.getLogger(SocialMediaLinksModel.class);

    @Inject
    @Optional
    @Via("resource")
    private List<String> languages;

    private List<LanguageBean> languageList;

    @PostConstruct
    public final void init() {

        populateLanguages();
    }

    private void populateLanguages() {
        if (null != languages && !languages.isEmpty()) {
            languageList = new ArrayList<>(languages.size());
            for (final String language : languages) {
                populateLanguageBean(language);
            }
        }
    }

    private void populateLanguageBean(final String language) {
        if (StringUtils.isNotEmpty(language)) {

            final LanguageBean languageBean = new LanguageBean();
            try {
                final JSONObject languageJSON = new JSONObject(language);
                languageBean.setLangTitle(languageJSON.has(SlingTvConstants.LANGUAGE_TITLE)
                        ? languageJSON.getString(SlingTvConstants.LANGUAGE_TITLE) : StringUtils.EMPTY);
                languageBean.setLangLink(languageJSON.has(SlingTvConstants.LANGUAGE_LINK)
                        ? languageJSON.getString(SlingTvConstants.LANGUAGE_LINK) : StringUtils.EMPTY);

                if (languageJSON.has(SlingTvConstants.LANGUAGE_TITLE)) {
                    languageList.add(languageBean);
                }
            } catch (final JSONException e) {
                LOG.error("JSON Exception ", e);
            }

        }
    }

    public List<LanguageBean> getLanguageList() {
        return languageList;
    }

    public void setLanguageList(final List<LanguageBean> languageList) {
        this.languageList = languageList;
    }
}
