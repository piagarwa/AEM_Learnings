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
import com.sling.tv.core.beans.MultiFieldBean;

/**
 * @author vhs
 * 
 *         Model class to populate multi-field properties.
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class MultiFieldModel {

	@Inject
	@Optional
	@Via("resource")
	private List<String> imageFieldSet;

	private List<MultiFieldBean> multiFieldBeans;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(MultiFieldModel.class);

	@PostConstruct
	public final void init() {
		// Populate multifield
		populateMultiFields();
	}

	private void populateMultiFields() {
		if (null != imageFieldSet && !imageFieldSet.isEmpty()) {
			multiFieldBeans = new ArrayList<>();
			// Iterate over field items in multifield set and storing them into
			// multiFieldBean.
			for (String multiField : imageFieldSet) {
				if (StringUtils.isNotEmpty(multiField)) {
					pupulateMultiFieldBean(multiField);
				}
			}
		}
	}

	private void pupulateMultiFieldBean(String alertItem) {
		if (StringUtils.isNotEmpty(alertItem)) {
			MultiFieldBean multiFieldBean = new MultiFieldBean();
			try {
				JSONObject holderBean = new JSONObject(alertItem);

				multiFieldBean
						.setTitle(holderBean.has(SlingTvConstants.TITLE) ? holderBean
								.getString(SlingTvConstants.TITLE)
								: StringUtils.EMPTY);
				multiFieldBean.setDescription(holderBean
						.has(SlingTvConstants.DESC) ? holderBean
						.getString(SlingTvConstants.DESC) : StringUtils.EMPTY);
				multiFieldBean
						.setLink(holderBean.has(SlingTvConstants.LINK) ? holderBean
								.getString(SlingTvConstants.LINK)
								: StringUtils.EMPTY);
				multiFieldBean
				.setAlt(holderBean.has(SlingTvConstants.ALT) ? holderBean
						.getString(SlingTvConstants.ALT)
						: StringUtils.EMPTY);

				if (holderBean.has(SlingTvConstants.TITLE)
						|| holderBean.has(SlingTvConstants.DESC)
						|| holderBean.has(SlingTvConstants.LINK)) {
					multiFieldBeans.add(multiFieldBean);
				}
			} catch (JSONException e) {
				LOGGER.error("Error while fetching multi-field object: {}", e);
			}
		}
	}

	
	/**
	 * @param multiFieldBeans
	 */
	public void setMultiFieldBeans(List<MultiFieldBean> multiFieldBeans) {
		this.multiFieldBeans = multiFieldBeans;
	}

	/**
	 * @return
	 */
	public final List<MultiFieldBean> getMultiFieldBeans() {
		return multiFieldBeans;
	}

}
