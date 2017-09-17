
package com.sling.tv.core.models;

import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.xss.XSSAPI;

import com.day.cq.wcm.api.Page;

/**
 *
 * This Sling Model will populate the Footer links.
 *
 * @author deou
 *
 */
@Model(adaptables = SlingHttpServletRequest.class)
public class NavigationComponentModel {

    @Inject
    private Page currentPage;

    @Inject
    private XSSAPI xssapi;

    @Inject
    @Optional
    @Via("resource")
    private String footerNavPath;

    @Inject
    @Optional
    @Via("resource")
    private String headerNavPath;

    private NavigationPage header;

    private NavigationPage footer;

    /**
     * This method gets the Footer Navigation values from the navigation pages.
     *
     */
    @PostConstruct
    public final void init() {

        if (StringUtils.isNotEmpty(headerNavPath)) {
            setHeader(buildLinks(headerNavPath));

        }

        if (StringUtils.isNotEmpty(footerNavPath)) {
            setFooter(buildLinks(footerNavPath));
        }
    }

    private NavigationPage buildLinks(final String navigationStructurePath) {
        NavigationPage navigationPage = null;

        if (StringUtils.isNotEmpty(navigationStructurePath)) {
            final Page page = currentPage.getPageManager().getContainingPage(navigationStructurePath);
            if (page != null && page.getContentResource() != null) {
                navigationPage = page.getContentResource().adaptTo(NavigationPage.class);
                final Iterator<Page> leftChildren = page.listChildren();
                buildNavigationTree(navigationPage, leftChildren);
            }
        }

        return navigationPage;
    }

    /**
     * @param page
     * @param children
     *
     *            Method to recursively populate the navigation tree of pages.
     */
    private void buildNavigationTree(final NavigationPage page, final Iterator<Page> children) {
        while (children.hasNext()) {
            final Page child = children.next();
            if (child != null && child.getContentResource() != null) {
                final NavigationPage subPage = child.getContentResource().adaptTo(NavigationPage.class);
                if (subPage != null) {
                    subPage.setUniqueId(xssapi.encodeForHTMLAttr(child.getName()));
                    subPage.setWindowSelection((String) child.getProperties().get("windowSelection"));
                    subPage.setHideInMobileNav((String) child.getProperties().get("hideInMobileNav"));
                    subPage.setHideInDesktopNav((String) child.getProperties().get("hideInDesktopNav"));
                    buildNavigationTree(subPage, child.listChildren());

                    page.getChildren().add(subPage);
                }
            }
        }
    }

    public NavigationPage getHeader() {
        return header;
    }

    public void setHeader(final NavigationPage header) {
        this.header = header;
    }

    public NavigationPage getFooter() {
        return footer;
    }

    public void setFooter(final NavigationPage footer) {
        this.footer = footer;
    }

}
