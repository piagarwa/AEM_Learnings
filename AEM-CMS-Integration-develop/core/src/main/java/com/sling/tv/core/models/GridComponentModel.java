
package com.sling.tv.core.models;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;

import com.day.cq.wcm.api.Page;

/**
 *
 * This Sling Model will populate the Channel Grid.
 *
 * @author vhs
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class GridComponentModel {

    @Inject
    private Page currentPage;

    @Inject
    @Optional
    @Via("resource")
    private String navLink;

    @Inject
    @Optional
    @Via("resource")
    private String slingOrange;

    @Inject
    @Optional
    @Via("resource")
    private String slingBlue;
    
    @Inject
    @Optional
    @Via("resource")
    private String slingGreen;
    
    private ChannelPage channelList;

    /**
     * This method gets the Footer Navigation values from the navigation pages.
     *
     */
    @PostConstruct
    public final void init() {

        if (StringUtils.isNotEmpty(navLink)) {
            channelList = buildChannelGrid(navLink);
        }

    }

    private ChannelPage buildChannelGrid(final String navigationStructurePath) {
    	ChannelPage channelPage = null;

        if (StringUtils.isNotEmpty(navigationStructurePath)) {
            final Page page = currentPage.getPageManager().getContainingPage(navigationStructurePath);
            if (page != null && page.getContentResource() != null) {
            	channelPage = page.getContentResource().adaptTo(ChannelPage.class);
                final Iterator<Page> leftChildren = page.listChildren();
                buildChannelTree(channelPage, leftChildren);
            }
        }
        return channelPage;
    }

    /**
     * @param page
     * @param children
     *
     *            Method to recursively populate the navigation tree of pages.
     */
    private void buildChannelTree(final ChannelPage page, final Iterator<Page> children) {
        while (children.hasNext()) {
            final Page child = children.next();
            if (child != null && child.getContentResource() != null) {
                final ChannelPage subPage = child.getContentResource().adaptTo(ChannelPage.class);
                if (subPage != null) {
                    buildChannelTree(subPage, child.listChildren());
                    page.getChildren().add(subPage);
                }
            }
        }
    }

    /**
     * @return
     */
    public final String getNavLink() {
        return navLink;
    }

    /**
     * @return
     */
    public final ChannelPage getChannelList() {
        return channelList;
    }

	/**
	 * @return
	 */
	public String getSlingOrange() {
		return slingOrange;
	}

	/**
	 * @return
	 */
	public String getSlingBlue() {
		return slingBlue;
	}

	/**
	 * @return
	 */
	public String getSlingGreen() {
		return slingGreen;
	}
    
    
}
