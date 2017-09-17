package com.sling.tv.core.models;

import java.util.ArrayList;
import java.util.List;

import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;

import com.adobe.cq.sightly.WCMUsePojo;
import com.sling.tv.core.beans.Tab;

public class CategoryTabs extends WCMUsePojo {

	public List<Tab> tabs;
	int count = 0;

	@Override
	public void activate() throws Exception {

		String currentItem;

		tabs = new ArrayList<Tab>();
		Node currentNode = this.getResource().adaptTo(Node.class);

		if (currentNode.hasProperty(currentItem = "tabs")) {
			this.setTabs(currentNode, currentItem,
					this.tabs);
		}

	}

	private void setTabs(Node currentNode, String currentItem,
						 List<Tab> tabs) throws RepositoryException, JSONException {

		Property currentProperty = currentNode.getProperty(currentItem);
		Value[] values = currentProperty.isMultiple() ? currentProperty
				.getValues() : new Value[] { currentProperty.getValue() };

		for (int i = 0; i < values.length; ++i) {
			count++;
			JSONObject jObj = new JSONObject(values[i].getString());
			Tab tab = new Tab();
			tab.setTabTitle(jObj.getString("tabTitle"));
			tab.setTabIcon(jObj.getString("tabIcon"));
			this.tabs.add(tab);
		}
	}
}
