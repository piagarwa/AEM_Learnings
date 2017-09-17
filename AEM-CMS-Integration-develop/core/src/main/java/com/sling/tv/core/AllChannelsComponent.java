package com.sling.tv.core;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Value;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;

public class AllChannelsComponent extends WCMUsePojo {
	
	/** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

	List<ChannelBean> allChannels = null;
	@Override
	public void activate() throws Exception {
		log.info("##### INVOKED ACTIVATE");
		allChannels = new ArrayList<ChannelBean>();
        Node currentNode = getResource().adaptTo(Node.class);
        if(currentNode.hasProperty("allChannels")) {
        	if(currentNode.getProperty("allChannels").isMultiple()) {
        		Value[] allChannel = currentNode.getProperty("allChannels").getValues();
            	for (Value channel : allChannel) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
    				allChannels.add(convertToChannelBean(jsonObject));
    			}
        	} else {
        		Value allChannel = currentNode.getProperty("allChannels").getValue();
        		JSONObject jsonObject = new JSONObject(allChannel.getString());
				allChannels.add(convertToChannelBean(jsonObject));
        	}
        }
	}
	
	/**
	 * @param jsonObject
	 * @return
	 */
	private ChannelBean convertToChannelBean(JSONObject jsonObject) {
		try {
			ChannelBean channelBean = new ChannelBean();
			String channelName =jsonObject.getString("channelName");
			channelBean.setName(jsonObject.getString("channelName"));
			channelBean.setLogo(jsonObject.getString("channelLogo"));
			channelBean.setChannelId(channelName.toLowerCase().replaceAll("\\s+",""));
			return channelBean;
		} catch(Exception ex) {
			log.error("Error in convertToChannelBean ::: ", ex);
		}
		return null;
	}
	
	public List<ChannelBean> getAllChannels() {
		log.info("Getting channels :::: "+allChannels.size());
		return allChannels;
	}
}