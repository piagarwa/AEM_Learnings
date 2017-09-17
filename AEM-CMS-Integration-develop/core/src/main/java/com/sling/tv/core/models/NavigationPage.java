
package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Optional;

/**
 * Sling model for holding the Navigation Page properties.
 *
 * @author vhs
 *
 */
@Model(adaptables = Resource.class)
public class NavigationPage {

    @Inject
    @Named("jcr:title")
    private String title;

    @Inject
    @Named("subtitle")
    @Optional
    private String subtitle;

    public String getWindowSelection() {
        return windowSelection;
    }

    public void setWindowSelection(final String windowSelection) {
        this.windowSelection = windowSelection;
    }

    @Inject
    @Named("redirectTarget")
    @Optional
    private String link;

    private String windowSelection;

    @Inject
    @Optional
    @Named("imageFile")
    private String imageFile;

    private String hideInMobileNav;

    private String hideInDesktopNav;

    public String getHideInMobileNav() {
        return hideInMobileNav;
    }

    public void setHideInMobileNav(final String hideInMobileNav) {
        this.hideInMobileNav = hideInMobileNav;
    }

    private String path;

    private String hreflang;

    private String uniqueId;

    private boolean active;

    private int itemNumber;

    private List<NavigationPage> children = new ArrayList<>();

    public final String getTitle() {
        return title;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public final String getLink() {
        return link;
    }

    public final void setLink(final String link) {
        this.link = link;
    }

    public final String getUniqueId() {
        return uniqueId;
    }

    public final void setUniqueId(final String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public final boolean isActive() {
        return active;
    }

    public final void setActive(final boolean active) {
        this.active = active;
    }

    public final List<NavigationPage> getChildren() {
        return children;
    }

    public final void setChildren(final List<NavigationPage> children) {
        this.children = children;
    }

    public final int getItemNumber() {
        return itemNumber;
    }

    public final void setItemNumber(final int itemNumber) {
        this.itemNumber = itemNumber;
    }

    public final String getPath() {
        return path;
    }

    public final void setPath(final String path) {
        this.path = path;
    }

    public String getHreflang() {
        return hreflang;
    }

    public void setHreflang(final String hreflang) {
        this.hreflang = hreflang;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(final String imageFile) {
        this.imageFile = imageFile;
    }

    public String getHideInDesktopNav() {
        return hideInDesktopNav;
    }

    public void setHideInDesktopNav(final String hideInDesktopNav) {
        this.hideInDesktopNav = hideInDesktopNav;
    }

}
