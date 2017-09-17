package com.sling.tv.core;

import java.util.List;

import javax.jcr.Node;
import javax.jcr.Value;

import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUsePojo;
import com.sling.tv.core.utils.SlingChannelUtil;


public class DynamicGridComponent extends WCMUsePojo {
	
	/** Default log. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

	private DynamicGridBean dynamicGridBean = null;
	
	private boolean rowOneTwoCol;
	
	private boolean rowOneThreeCol;
	
	private boolean rowTwoOneCol;
	
	private boolean rowTwoTwoCol;

	private boolean rowTwoThreeCol;

	
	//private StringBuilder m25Channels = new StringBuilder();
	@Override
	public void activate() throws Exception {
		log.info("##### INVOKED ACTIVATE of DynamicGridComponent");
		dynamicGridBean = new DynamicGridBean();
        Node currentNode = getResource().adaptTo(Node.class);
        if(currentNode.hasProperty("extraPagePath")) {
        	dynamicGridBean.setExtraPagePath(currentNode.getProperty("extraPagePath").getString());
        }
        if(currentNode.hasProperty("monthlyTitle")) {
        	dynamicGridBean.setMonthlyTitle(currentNode.getProperty("monthlyTitle").getString());
        }
        if(currentNode.hasProperty("sixMonthlyTitle")) {
        	dynamicGridBean.setYearlyTitle(currentNode.getProperty("sixMonthlyTitle").getString());
        	rowOneTwoCol = true;
        }
        if(currentNode.hasProperty("yearlyTitle")) {
        	dynamicGridBean.setYearlyTitle(currentNode.getProperty("yearlyTitle").getString());
        	rowOneThreeCol = true;
        }
        if(currentNode.hasProperty("monthlySubTitle")) {
        	dynamicGridBean.setMonthlySubTitle(currentNode.getProperty("monthlySubTitle").getString());
        }
        if(currentNode.hasProperty("sixMonthlySubtTitle")) {
        	dynamicGridBean.setMonthlySubTitle(currentNode.getProperty("sixMonthlySubtTitle").getString());
        }
        if(currentNode.hasProperty("yearlySubTitle")) {
        	dynamicGridBean.setYearlySubTitle(currentNode.getProperty("yearlySubTitle").getString());
        }
        if(currentNode.hasProperty("m25ChannelTitle")) {
        	dynamicGridBean.setM25ChannelTitle(currentNode.getProperty("m25ChannelTitle").getString());
        	rowTwoOneCol =  true;
        }
        if(currentNode.hasProperty("m25ChannelSubTitle")) {
        	dynamicGridBean.setM25ChannelSubTitle(currentNode.getProperty("m25ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("m25ChannelMoney")) {
        	dynamicGridBean.setM25ChannelMoney(currentNode.getProperty("m25ChannelMoney").getString());
        }
        if(currentNode.hasProperty("m30ChannelTitle")) {
        	dynamicGridBean.setM30ChannelTitle(currentNode.getProperty("m30ChannelTitle").getString());
        	rowTwoTwoCol = true;
        }
        if(currentNode.hasProperty("m30ChannelSubTitle")) {
        	dynamicGridBean.setM30ChannelSubTitle(currentNode.getProperty("m30ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("m30ChannelMoney")) {
        	dynamicGridBean.setM30ChannelMoney(currentNode.getProperty("m30ChannelMoney").getString());
        }
        if(currentNode.hasProperty("m60ChannelTitle")) {
        	dynamicGridBean.setM60ChannelTitle(currentNode.getProperty("m60ChannelTitle").getString());
        	rowTwoThreeCol = true;
        }
        if(currentNode.hasProperty("m60ChannelSubTitle")) {
        	dynamicGridBean.setM60ChannelSubTitle(currentNode.getProperty("m60ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("m60ChannelMoney")) {
        	dynamicGridBean.setM60ChannelMoney(currentNode.getProperty("m60ChannelMoney").getString());
        }
        
        
        
        if(currentNode.hasProperty("s25ChannelTitle")) {
        	dynamicGridBean.setS25ChannelTitle(currentNode.getProperty("s25ChannelTitle").getString());
        }
        if(currentNode.hasProperty("s25ChannelSubTitle")) {
        	dynamicGridBean.setS25ChannelSubTitle(currentNode.getProperty("s25ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("s25ChannelMoney")) {
        	dynamicGridBean.setS25ChannelMoney(currentNode.getProperty("s25ChannelMoney").getString());
        }
        if(currentNode.hasProperty("s30ChannelTitle")) {
        	dynamicGridBean.setS30ChannelTitle(currentNode.getProperty("s30ChannelTitle").getString());
        }
        if(currentNode.hasProperty("s30ChannelSubTitle")) {
        	dynamicGridBean.setS30ChannelSubTitle(currentNode.getProperty("s30ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("s30ChannelMoney")) {
        	dynamicGridBean.setS30ChannelMoney(currentNode.getProperty("s30ChannelMoney").getString());
        }
        if(currentNode.hasProperty("s60ChannelTitle")) {
        	dynamicGridBean.setS60ChannelTitle(currentNode.getProperty("s60ChannelTitle").getString());
        }
        if(currentNode.hasProperty("s60ChannelSubTitle")) {
        	dynamicGridBean.setS60ChannelSubTitle(currentNode.getProperty("s60ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("s60ChannelMoney")) {
        	dynamicGridBean.setS60ChannelMoney(currentNode.getProperty("s60ChannelMoney").getString());
        }
        
        
        
        if(currentNode.hasProperty("y25ChannelTitle")) {
        	dynamicGridBean.setY25ChannelTitle(currentNode.getProperty("y25ChannelTitle").getString());
        }
        if(currentNode.hasProperty("y25ChannelSubTitle")) {
        	dynamicGridBean.setY25ChannelSubTitle(currentNode.getProperty("y25ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("y25ChannelMoney")) {
        	dynamicGridBean.setY25ChannelMoney(currentNode.getProperty("y25ChannelMoney").getString());
        }
        if(currentNode.hasProperty("y30ChannelTitle")) {
        	dynamicGridBean.setY30ChannelTitle(currentNode.getProperty("y30ChannelTitle").getString());
        }
        if(currentNode.hasProperty("y30ChannelSubTitle")) {
        	log.info("y30ChannelSubTitle found "+currentNode.getProperty("y30ChannelSubTitle").getString());
        	dynamicGridBean.setY30ChannelSubTitle(currentNode.getProperty("y30ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("y30ChannelMoney")) {
        	dynamicGridBean.setY30ChannelMoney(currentNode.getProperty("y30ChannelMoney").getString());
        }
        if(currentNode.hasProperty("y60ChannelTitle")) {
        	dynamicGridBean.setY60ChannelTitle(currentNode.getProperty("y60ChannelTitle").getString());
        }
        if(currentNode.hasProperty("y60ChannelSubTitle")) {
        	dynamicGridBean.setY60ChannelSubTitle(currentNode.getProperty("y60ChannelSubTitle").getString());
        }
        if(currentNode.hasProperty("y60ChannelMoney")) {
        	dynamicGridBean.setY60ChannelMoney(currentNode.getProperty("y60ChannelMoney").getString());
        }
        
        if(currentNode.hasProperty("m25channels")) {
        	log.info("25 channels found");
        	StringBuilder m25Channels = new StringBuilder();
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
        	} else {
        		Value channelsJson = currentNode.getProperty("m25channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(m25Channels.toString().isEmpty()) {
        			m25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
        	dynamicGridBean.setM25ChannelIds(m25Channels.toString());
        }
        if(currentNode.hasProperty("m30channels")) {
        	log.info("30 channels found");
        	StringBuilder m30Channels = new StringBuilder();
        	if(currentNode.getProperty("m30channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("m30channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		
            		if(m30Channels.toString().isEmpty()) {
            			m30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			m30Channels.append(",");
            			m30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("m30channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(m30Channels.toString().isEmpty()) {
        			m30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
        	dynamicGridBean.setM30ChannelIds(m30Channels.toString());
        }
        if(currentNode.hasProperty("m60channels")) {
        	log.info("60 channels found");
        	StringBuilder m60Channels = new StringBuilder();
        	if(currentNode.getProperty("m60channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("m60channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		
            		if(m60Channels.toString().isEmpty()) {
            			m60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			m60Channels.append(",");
            			m60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("m60channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(m60Channels.toString().isEmpty()) {
        			m60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
        	dynamicGridBean.setM60ChannelIds(m60Channels.toString());
        }
        if(currentNode.hasProperty("s25channels")) {
        	log.info("S 25 channels found");
        	StringBuilder s25Channels = new StringBuilder();
        	if(currentNode.getProperty("s25channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("s25channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(s25Channels.toString().isEmpty()) {
            			s25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			s25Channels.append(",");
            			s25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("s25channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(s25Channels.toString().isEmpty()) {
        			s25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
        	dynamicGridBean.setS25ChannelIds(s25Channels.toString());
        }
        if(currentNode.hasProperty("s30channels")) {
        	log.info("S 30 channels found");
        	StringBuilder s30Channels = new StringBuilder();
        	if(currentNode.getProperty("s30channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("s30channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(s30Channels.toString().isEmpty()) {
            			s30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			s30Channels.append(",");
            			s30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("s30channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(s30Channels.toString().isEmpty()) {
        			s30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
        	dynamicGridBean.setS30ChannelIds(s30Channels.toString());
        }
        if(currentNode.hasProperty("s60channels")) {
        	log.info("S 60 channels found");
        	StringBuilder s60Channels = new StringBuilder();
        	if(currentNode.getProperty("s60channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("s60channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(s60Channels.toString().isEmpty()) {
            			s60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			s60Channels.append(",");
            			s60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("s60channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(s60Channels.toString().isEmpty()) {
        			s60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
        	dynamicGridBean.setS60ChannelIds(s60Channels.toString());
        }
        if(currentNode.hasProperty("y25channels")) {
        	log.info("Y 25 channels found");
        	StringBuilder y25Channels = new StringBuilder();
        	if(currentNode.getProperty("y25channels").isMultiple()) {
        		log.info("has comma");
        		Value[] channelsArr = currentNode.getProperty("y25channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(y25Channels.toString().isEmpty()) {
            			y25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			y25Channels.append(",");
            			y25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("y25channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(y25Channels.toString().isEmpty()) {
        			y25Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
    		dynamicGridBean.setY25ChannelIds(y25Channels.toString());
        }
        if(currentNode.hasProperty("y30channels")) {
        	log.info("Y 30 channels found");
        	StringBuilder y30Channels = new StringBuilder();
        	if(currentNode.getProperty("y30channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("y30channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(y30Channels.toString().isEmpty()) {
            			y30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			y30Channels.append(",");
            			y30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("y30channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(y30Channels.toString().isEmpty()) {
        			y30Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
    		dynamicGridBean.setY30ChannelIds(y30Channels.toString());
        }
        if(currentNode.hasProperty("y60channels")) {
        	log.info("Y 60 channels found");
        	StringBuilder y60Channels = new StringBuilder();
        	if(currentNode.getProperty("y60channels").isMultiple()) {
        		Value[] channelsArr = currentNode.getProperty("y60channels").getValues();
        		for (Value channel : channelsArr) {
            		JSONObject jsonObject = new JSONObject(channel.getString());
            		String channelName = jsonObject.getString("channelName");
            		if(y60Channels.toString().isEmpty()) {
            			y60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		} else {
            			y60Channels.append(",");
            			y60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
            		}
    			}
        	} else {
        		Value channelsJson = currentNode.getProperty("y60channels").getValue();
        		JSONObject jsonObject = new JSONObject(channelsJson.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(y60Channels.toString().isEmpty()) {
        			y60Channels.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
        	}
    		dynamicGridBean.setY60ChannelIds(y60Channels.toString());
        }
        
        List<Node> allChannels = SlingChannelUtil.getAllCHannels(getResourceResolver(), getCurrentPage().getPath(), "sling-tv/components/content/general/allchannels");
        if(!allChannels.isEmpty()) {
        	Node m60Channel = allChannels.get(0);
        	Value[] allChannelsProp = m60Channel.getProperty("allChannels").getValues();
        	StringBuilder allChannelStr = new StringBuilder();
        	for (Value channel : allChannelsProp) {
        		JSONObject jsonObject = new JSONObject(channel.getString());
        		String channelName = jsonObject.getString("channelName");
        		if(allChannelStr.toString().isEmpty()) {
        			allChannelStr.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		} else {
        			allChannelStr.append(",");
        			allChannelStr.append(channelName.toLowerCase().replaceAll("\\s+",""));
        		}
			}
        	dynamicGridBean.setAllChannels(allChannelStr.toString());
        }
        
        if(rowOneTwoCol) {
        	dynamicGridBean.setRowOneCol(2);
        }
        if(rowOneThreeCol) {
        	dynamicGridBean.setRowOneCol(3);
        }
        if(rowTwoOneCol) {
        	dynamicGridBean.setRowTwoCol(1);
        }
        if(rowTwoTwoCol) {
        	dynamicGridBean.setRowTwoCol(2);
        }
        if(rowTwoThreeCol) {
        	dynamicGridBean.setRowTwoCol(3);
        }
	}
	public DynamicGridBean getDynamicGridBean() {
		return dynamicGridBean;
	}
}