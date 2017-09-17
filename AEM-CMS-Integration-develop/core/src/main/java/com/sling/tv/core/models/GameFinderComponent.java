package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jcr.Node;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sling.tv.core.beans.MatchInfo;


@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GameFinderComponent {

    private static Logger LOG = LoggerFactory.getLogger(GameFinderComponent.class);
    
    @Inject
    @Self
    private GameFinderComponentModel gameFinderComponentModel;
    
        
    @Inject
    @Self
    private Node currentNode;
    
	private List<Map<String,String>> gameTypes;

    
    public List<Map<String, String>> getGameTypes() {
		return gameTypes;
	}

	public void setGameTypes(List<Map<String, String>> gameTypes) {
		this.gameTypes = gameTypes;
	}

    
    
    private List<MatchInfo> matchList;
    
	public List<MatchInfo> getMatchList() {
		return matchList;
	}

	public void setMatchList(List<MatchInfo> matchList) {
		this.matchList = matchList;
	}

	@PostConstruct
    public final void init() {
		LOG.info("Entering Init of gamefinder component");
    	try {
    		getMatchTypes();   		
    	} catch(Exception ex) {
    		LOG.error("Error in init :::: ", ex);
    	}
    }
	
    private void getMatchTypes() {
		LOG.info("Entering getmatchTypes of gamefinder component");

		try {
			String[] gameTypeArray = gameFinderComponentModel.getAddSports();
			
			if (gameTypeArray != null) {
				List<Map<String,String>> gameList = new ArrayList<Map<String,String>>();
				for (String game : gameTypeArray) {
					JSONObject jsonObject = new JSONObject(game);
					gameList.add(convertToMap(jsonObject));
				}
				LOG.info("total matches are " +gameList.size());

				setGameTypes(gameList);
			}
		} catch (Exception e) {
		}					
	}
    private Map<String, String> convertToMap(JSONObject jsonObject) {
		try {
				Map<String, String> logoInfo = new HashMap<String, String>();
				logoInfo.put("sportName", jsonObject.getString("sportName"));
				logoInfo.put("keyName", jsonObject.getString("keyName"));
				logoInfo.put("checked", jsonObject.getString("checked"));
				logoInfo.put("showHide", jsonObject.getString("showHide"));
				return logoInfo;
			} catch(Exception ex) {
			LOG.error("Error in convertToMap Gamefinder ::: ", ex);
			}
			return null;
		}
}
