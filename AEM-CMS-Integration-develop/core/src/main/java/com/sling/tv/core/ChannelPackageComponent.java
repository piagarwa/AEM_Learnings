package com.sling.tv.core;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.Value;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.sling.tv.core.utils.SlingChannelUtil;


public class ChannelPackageComponent extends WCMUsePojo {
	
	/** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private ChannelPackageBean channelPackageBean = null;
	
	private StringBuilder m25Channels = new StringBuilder();
	@Override
	public void activate() throws Exception {
		log.info("##### INVOKED ACTIVATE of ChannelPackageComponent");
		channelPackageBean = new ChannelPackageBean();
        Node currentNode = getResource().adaptTo(Node.class);
        if(currentNode.hasProperty("monthlyTitle")) {
        	channelPackageBean.setMonthlyTitle(currentNode.getProperty("monthlyTitle").getString());
        }
        if(currentNode.hasProperty("yearlyTitle")) {
        	channelPackageBean.setYearlyTitle(currentNode.getProperty("yearlyTitle").getString());
        }
        if(currentNode.hasProperty("monthlySubTitle")) {
        	channelPackageBean.setMonthlySubTitle(currentNode.getProperty("monthlySubTitle").getString());
        }
        if(currentNode.hasProperty("yearlySubTitle")) {
        	channelPackageBean.setYearlySubTitle(currentNode.getProperty("yearlySubTitle").getString());
        }
        if(currentNode.hasProperty("m25ChannelTitle")) {
        	channelPackageBean.setM25ChannelTitle(currentNode.getProperty("m25ChannelTitle").getString());
        }
        if(currentNode.hasProperty("m25ChannelSubtitle")) {
        	channelPackageBean.setM25ChannelSubtitle(currentNode.getProperty("m25ChannelSubtitle").getString());
        }
        if(currentNode.hasProperty("m25ChannelMoney")) {
        	channelPackageBean.setM25ChannelMoney(currentNode.getProperty("m25ChannelMoney").getString());
        }
        if(currentNode.hasProperty("m30ChannelTitle")) {
        	channelPackageBean.setM30ChannelTitle(currentNode.getProperty("m30ChannelTitle").getString());
        }
        if(currentNode.hasProperty("m30ChannelSubtitle")) {
        	channelPackageBean.setM30ChannelSubtitle(currentNode.getProperty("m30ChannelSubtitle").getString());
        }
        if(currentNode.hasProperty("m30ChannelMoney")) {
        	channelPackageBean.setM30ChannelMoney(currentNode.getProperty("m30ChannelMoney").getString());
        }
        if(currentNode.hasProperty("m60ChannelTitle")) {
        	channelPackageBean.setM60ChannelTitle(currentNode.getProperty("m60ChannelTitle").getString());
        }
        if(currentNode.hasProperty("m60ChannelSubtitle")) {
        	channelPackageBean.setM60ChannelSubtitle(currentNode.getProperty("m60ChannelSubtitle").getString());
        }
        if(currentNode.hasProperty("m60ChannelMoney")) {
        	channelPackageBean.setM60ChannelMoney(currentNode.getProperty("m60ChannelMoney").getString());
        }
        if(currentNode.hasProperty("25ChannelTitle")) {
        	channelPackageBean.setY25ChannelTitle(currentNode.getProperty("25ChannelTitle").getString());
        }
        if(currentNode.hasProperty("25ChannelSubtitle")) {
        	channelPackageBean.setY25ChannelSubtitle(currentNode.getProperty("25ChannelSubtitle").getString());
        }
        if(currentNode.hasProperty("25ChannelMoney")) {
        	channelPackageBean.setY25ChannelMoney(currentNode.getProperty("25ChannelMoney").getString());
        }
        if(currentNode.hasProperty("30ChannelTitle")) {
        	channelPackageBean.setY30ChannelTitle(currentNode.getProperty("30ChannelTitle").getString());
        }
        if(currentNode.hasProperty("30ChannelSubtitle")) {
        	channelPackageBean.setY30ChannelSubtitle(currentNode.getProperty("30ChannelSubtitle").getString());
        }
        if(currentNode.hasProperty("30ChannelMoney")) {
        	channelPackageBean.setY30ChannelMoney(currentNode.getProperty("30ChannelMoney").getString());
        }
        if(currentNode.hasProperty("60ChannelTitle")) {
        	channelPackageBean.setY60ChannelTitle(currentNode.getProperty("60ChannelTitle").getString());
        }
        if(currentNode.hasProperty("60ChannelSubtitle")) {
        	channelPackageBean.setY60ChannelSubtitle(currentNode.getProperty("60ChannelSubtitle").getString());
        }
        if(currentNode.hasProperty("60ChannelMoney")) {
        	channelPackageBean.setY60ChannelMoney(currentNode.getProperty("60ChannelMoney").getString());
        }
        
        if(currentNode.hasProperty("m25channels")) {
        	log.info("25 channels found");
        	if(currentNode.getProperty("m25channels").isMultiple()) {
        		log.info("has comma");
        		Value[] channelsArr = currentNode.getProperty("m25channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(m25Channels.toString().isEmpty()) {
            			m25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			m25Channels.append(",");
            			m25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        		channelPackageBean.setM25ChannelIds(m25Channels.toString());
        	} 
        }
        
        if(currentNode.hasProperty("m30channels")) {
        	log.info("30 channels found");
        	if(currentNode.getProperty("m30channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("m30channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		m25Channels.append(",");
            		m25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
    			}
        		log.info("Setting 30 channels :::: "+m25Channels.toString());
        		channelPackageBean.setM30ChannelIds(m25Channels.toString());
        	} 
        }
        List<Node> m60Channels = SlingChannelUtil.getAllCHannels(getResourceResolver(), getCurrentPage().getPath(), "sling-tv/components/content/general/allchannels");
        if(!m60Channels.isEmpty()) {
        	Node m60Channel = m60Channels.get(0);
        	Value[] allChannels = m60Channel.getProperty("allChannels").getValues();
        	StringBuilder allChannelStr = new StringBuilder();
        	for (Value channel : allChannels) {
        		JSONObject jsonObject = new JSONObject(channel.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(allChannelStr.toString().isEmpty()) {
        			allChannelStr.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		} else {
        			allChannelStr.append(",");
        			allChannelStr.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
			}
    		channelPackageBean.setM60ChannelIds(allChannelStr.toString());
        }
	}
	
	/**
	 * @return
	 */
	public ChannelPackageBean getChannelPackageBean() {
		return channelPackageBean;
	}
}
