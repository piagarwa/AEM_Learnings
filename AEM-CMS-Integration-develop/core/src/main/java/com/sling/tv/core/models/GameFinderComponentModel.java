package com.sling.tv.core.models;

import javax.inject.Inject;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;



@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class GameFinderComponentModel {

    @Inject
    private String[] matchInfo;
    
    @Inject
    private String[] addSports;
    
    public String[] getAddSports() {
		return addSports;
	}

	@Inject
    private String filePath;

	public String[] getMatchInfo() {
		return matchInfo;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
