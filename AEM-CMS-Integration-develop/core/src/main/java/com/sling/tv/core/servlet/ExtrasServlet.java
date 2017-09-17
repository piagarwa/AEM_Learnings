package com.sling.tv.core.servlet;

import java.io.IOException;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Value;
import javax.servlet.ServletException;

import org.apache.commons.collections.IteratorUtils;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sling.tv.core.utils.SlingChannelUtil;

@SlingServlet(paths = "/bin/ExtrasServlet", methods = "GET")
public class ExtrasServlet extends SlingSafeMethodsServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
		try {
			String extrasId = request.getParameter("extrasId");
			String extraPagePath = request.getParameter("extraPagePath");
			List<Node> extrasNode = SlingChannelUtil.getExtras(request.getResourceResolver(), extraPagePath, "sling-tv/components/content/general/extras", extrasId);
	        if(!extrasNode.isEmpty()) {
	        	log.info("Extras Node is :::: "+extrasNode.get(0).getPath());
	        	Node extraNode = extrasNode.get(0);
	        	JSONObject extras = getExtrasJson(extraNode, extrasId);
	        	response.getWriter().write(extras.toString());
	        }
		} catch(Exception ex) {
			log.error("Error in doGet of ExtrasServlet :::", ex);
		}
	}

	/**
	 * @param extraNode
	 * @param extrasId
	 * @return
	 */
	private JSONObject getExtrasJson(Node extraNode, String extrasId) {
		try {
			JSONObject extrasJson = new JSONObject();
			extrasJson.put("extrasId", extrasId);
			if(extraNode.hasProperty("heading")) {
                extrasJson.put("heading", extraNode.getProperty("heading").getString());
            }
            if(extraNode.hasProperty("subHeading")) {
                extrasJson.put("subHeading", extraNode.getProperty("subHeading").getString());
            }
			JSONArray extras = new JSONArray();
			if(extraNode.hasProperty("extraSlides")) {
				int slideCounter = 0;
				if(extraNode.getProperty("extraSlides").isMultiple()) {
					Value[] extraSlides = extraNode.getProperty("extraSlides").getValues();
					for (Value slide : extraSlides) {
						JSONObject inSlideJson = new JSONObject(slide.getString());
						JSONObject outSlideJson = new JSONObject();
						outSlideJson.put("title", inSlideJson.getString("extraPackages"));
						JSONArray inSubHeadingsArr = inSlideJson.getJSONArray("subPackages");
						JSONArray outSubHeadingsArr = new JSONArray();
						for (int i = 0; i < inSubHeadingsArr.length(); i++) {
							JSONObject inSubHeading = inSubHeadingsArr.getJSONObject(i);
							JSONObject outSubHeading = new JSONObject();
							outSubHeading.put("title", inSubHeading.getString("subHeading"));
							Node extraChannel = (Node)IteratorUtils.toList(extraNode.getNodes()).get(slideCounter);
							if(extraChannel.hasProperty("allExtraLogos")) {
								JSONArray logoArr = new JSONArray();
								if(extraChannel.getProperty("allExtraLogos").isMultiple()) {
									Value[] inLogos = extraChannel.getProperty("allExtraLogos").getValues();
									for(Value inLogo : inLogos) {
										JSONObject inLogoJson = new JSONObject(inLogo.getString());
										JSONObject outLogoJson = new JSONObject();
										outLogoJson.put("path", inLogoJson.getString("extraChannelLogo"));
										outLogoJson.put("altText", inLogoJson.getString("logoAltText"));
										logoArr.put(outLogoJson);
									}
								} else {
									Value inLogos = extraChannel.getProperty("allExtraLogos").getValue();
									JSONObject inLogoJson = new JSONObject(inLogos.getString());
									JSONObject outLogoJson = new JSONObject();
									outLogoJson.put("path", inLogoJson.getString("extraChannelLogo"));
									outLogoJson.put("altText", inLogoJson.getString("logoAltText"));
									logoArr.put(outLogoJson);
								}
								outSubHeading.put("logos", logoArr);
							}
							if(extraChannel.hasProperty("allExtraChannels")) {
								JSONArray chNameArr = new JSONArray();
								if(extraChannel.getProperty("allExtraChannels").isMultiple()) {
									Value[] inChNames = extraChannel.getProperty("allExtraChannels").getValues();
									for(Value inChName : inChNames) {
										JSONObject inChNameJson = new JSONObject(inChName.getString());
										JSONObject outChNameJson = new JSONObject();
										outChNameJson.put("channelName", inChNameJson.getString("extraChannelName"));
										chNameArr.put(outChNameJson);
									}
								} else {
									Value inChName = extraChannel.getProperty("allExtraChannels").getValue();
									JSONObject inChNameJson = new JSONObject(inChName.getString());
									JSONObject outChNameJson = new JSONObject();
									outChNameJson.put("channelName", inChNameJson.getString("extraChannelName"));
									chNameArr.put(outChNameJson);
								}
								outSubHeading.put("channelNames", chNameArr);
							}
							outSubHeadingsArr.put(outSubHeading);
							slideCounter++;
						}
						outSlideJson.put("subheadings", outSubHeadingsArr);
						extras.put(outSlideJson);
					}
				} else {
					Value slide = extraNode.getProperty("extraSlides").getValue();
					JSONObject inSlideJson = new JSONObject(slide.getString());
					JSONObject outSlideJson = new JSONObject();
					outSlideJson.put("title", inSlideJson.getString("extraPackages"));
					JSONArray inSubHeadingsArr = inSlideJson.getJSONArray("subPackages");
					JSONArray outSubHeadingsArr = new JSONArray();
					for (int i = 0; i < inSubHeadingsArr.length(); i++) {
						JSONObject inSubHeading = inSubHeadingsArr.getJSONObject(i);
						JSONObject outSubHeading = new JSONObject();
						outSubHeading.put("title", inSubHeading.getString("subHeading"));
						Node extraChannel = (Node)IteratorUtils.toList(extraNode.getNodes()).get(slideCounter);
						if(extraChannel.hasProperty("allExtraLogos")) {
							JSONArray logoArr = new JSONArray();
							if(extraChannel.getProperty("allExtraLogos").isMultiple()) {
								Value[] inLogos = extraChannel.getProperty("allExtraLogos").getValues();
								for(Value inLogo : inLogos) {
									JSONObject inLogoJson = new JSONObject(inLogo.getString());
									JSONObject outLogoJson = new JSONObject();
									outLogoJson.put("path", inLogoJson.getString("extraChannelLogo"));
									outLogoJson.put("altText", inLogoJson.getString("logoAltText"));
									logoArr.put(outLogoJson);
								}
							} else {
								Value inLogos = extraChannel.getProperty("allExtraLogos").getValue();
								JSONObject inLogoJson = new JSONObject(inLogos.getString());
								JSONObject outLogoJson = new JSONObject();
								outLogoJson.put("path", inLogoJson.getString("extraChannelLogo"));
								outLogoJson.put("altText", inLogoJson.getString("logoAltText"));
								logoArr.put(outLogoJson);
							}
							outSubHeading.put("logos", logoArr);
						}
						if(extraChannel.hasProperty("allExtraChannels")) {
							JSONArray chNameArr = new JSONArray();
							if(extraChannel.getProperty("allExtraChannels").isMultiple()) {
								Value[] inChNames = extraChannel.getProperty("allExtraChannels").getValues();
								for(Value inChName : inChNames) {
									JSONObject inChNameJson = new JSONObject(inChName.getString());
									JSONObject outChNameJson = new JSONObject();
									outChNameJson.put("channelName", inChNameJson.getString("extraChannelName"));
									chNameArr.put(outChNameJson);
								}
							} else {
								Value inChName = extraChannel.getProperty("allExtraChannels").getValue();
								JSONObject inChNameJson = new JSONObject(inChName.getString());
								JSONObject outChNameJson = new JSONObject();
								outChNameJson.put("channelName", inChNameJson.getString("extraChannelName"));
								chNameArr.put(outChNameJson);
							}
							outSubHeading.put("channelNames", chNameArr);
						}
						outSubHeadingsArr.put(outSubHeading);
						slideCounter++;
					}
					outSlideJson.put("subheadings", outSubHeadingsArr);
					extras.put(outSlideJson);
				}
			}
			extrasJson.put("extras", extras);
			return extrasJson;
		} catch(Exception ex) {
			log.error("Error in getExtrasJson of ExtrasServlet :::", ex);
		}
		return null;
	}
}