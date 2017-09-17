package com.sling.tv.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Value;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;


public class ExtrasComponent extends WCMUsePojo {
	
	/** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private List<ExtrasBean> extrasBeans = null;
	
	private int subHeadingCounter = 1;
	@Override
	public void activate() throws Exception {
		log.info("##### INVOKED ACTIVATE of ExtrasComponent");
		extrasBeans = new ArrayList<ExtrasBean>();
        Node currentNode = getResource().adaptTo(Node.class);
        if(currentNode.hasProperty("extraSlides")) {
        	log.info("extraSlides found");
        	if(currentNode.getProperty("extraSlides").isMultiple()) {
        		Value[] extraChannels = currentNode.getProperty("extraSlides").getValues();
            	for (Value channel : extraChannels) {
            		ExtrasBean extraChannelBean = new ExtrasBean();
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		extraChannelBean.setTitle(jsonObject.getString("extraPackages"));
    				List<Map<String, String>> subheadings = getSubheadings(jsonObject);
    				extraChannelBean.setSubheading(subheadings);
    				extrasBeans.add(extraChannelBean);
    			}
        	} else {
        		log.info("extraSlides found single value");
        		ExtrasBean extraChannelBean = new ExtrasBean();
        		Value extraChannel = currentNode.getProperty("extraSlides").getValue();
        		JSONObject jsonObject = new JSONObject(extraChannel.getString());
        		extraChannelBean.setTitle(jsonObject.getString("extraPackages"));
        		List<Map<String, String>> subheadings = getSubheadings(jsonObject);
				extraChannelBean.setSubheading(subheadings);
				extrasBeans.add(extraChannelBean);
        	}
        }
	}
	
	/**
	 * @param jsonObject
	 * @return
	 */
	private List<Map<String, String>> getSubheadings(JSONObject jsonObject) {
		try {
			List<Map<String, String>> subHeadings = new ArrayList<Map<String, String>>();
			JSONArray array = jsonObject.getJSONArray("subPackages");
			for(int i=0; i<array.length(); i++) {
				Map<String, String> item = new HashMap<String, String>();
				String subHeading = array.getJSONObject(i).getString("subHeading");
				if(StringUtils.isNotBlank(subHeading)) {
					item.put("subHeading", subHeading);
					item.put("extraChannels", "extraChannel"+subHeadingCounter);
					subHeadings.add(item);
					subHeadingCounter++;
				}
			}
			return subHeadings;
		} catch(Exception ex) {
			log.error("Error in getSubheadings :::: ", ex);
		}
		return null;
	}

	public List<ExtrasBean> getExtrasBeans() {
		return extrasBeans;
	}
}

