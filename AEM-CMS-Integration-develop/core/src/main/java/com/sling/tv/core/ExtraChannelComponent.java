package com.sling.tv.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Value;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

public class ExtraChannelComponent extends WCMUsePojo {
/** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

private ExtraChannelBean extraChannelBean = null;
@Override
public void activate() throws Exception {
log.info("##### INVOKED ACTIVATE of ExtrasChannelComponent");
extraChannelBean = new ExtraChannelBean();
log.info("current resource ::::"+getResource().getPath());
        Node currentNode = getResource().adaptTo(Node.class);
        if(currentNode.hasProperty("allExtraLogos")) {
        log.info("ExtraChannel component configured!!!");
        List<Map<String, String>> logos = new ArrayList<Map<String, String>>();
        if(currentNode.getProperty("allExtraLogos").isMultiple()) {
        Value[] allExtras = currentNode.getProperty("allExtraLogos").getValues();
            for (Value extraItem : allExtras) {
            JSONObject jsonObject = new JSONObject(extraItem.getString());
            logos.add(convertToMap(jsonObject));
    }
            extraChannelBean.setExtraChannelLogos(logos);
        } else {
        Value logoInRepository = currentNode.getProperty("allExtraLogos").getValue();
        JSONObject jsonObject = new JSONObject(logoInRepository.getString());
        logos.add(convertToMap(jsonObject));
            extraChannelBean.setExtraChannelLogos(logos);
        }
    }
        if(currentNode.getProperty("allExtraChannels").isMultiple()) {
        List<String> names = new ArrayList<String>();
        Value[] allExtras = currentNode.getProperty("allExtraChannels").getValues();
            for (Value extraItem : allExtras) {
            JSONObject jsonObject = new JSONObject(extraItem.getString());
            names.add(convertToExtraChannelBean(jsonObject, "extraChannelName"));
    }
            extraChannelBean.setExtraChannelNames(names);
        } else {
        List<String> names = new ArrayList<String>();
        Value nameInRepository = currentNode.getProperty("allExtraChannels").getValue();
        JSONObject jsonObject = new JSONObject(nameInRepository.getString());
        names.add(convertToExtraChannelBean(jsonObject, "extraChannelName"));
            extraChannelBean.setExtraChannelNames(names);
        }
       
}
/**
* @param jsonObject
* @return
*/
		private Map<String, String> convertToMap(JSONObject jsonObject) {
		try {
				Map<String, String> logoInfo = new HashMap<String, String>();
				logoInfo.put("logo", jsonObject.getString("extraChannelLogo"));
				logoInfo.put("altText", jsonObject.getString("logoAltText"));
				return logoInfo;
			} catch(Exception ex) {
			log.error("Error in convertToExtraChannelBean ::: ", ex);
			}
			return null;
		}
		/**
		* @param jsonObject
		* @return
		*/
		private String convertToExtraChannelBean(JSONObject jsonObject, String property) {
		try {
		return jsonObject.getString(property);
		} catch(Exception ex) {
		log.error("Error in convertToExtraChannelBean ::: ", ex);
		}
		return null;
		}
		
		/**
		* @return
		*/
		public ExtraChannelBean getExtraChannelBean() {
		return extraChannelBean;
		}
		}
