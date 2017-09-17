package com.sling.tv.core.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;
import javax.jcr.query.QueryResult;

import org.apache.commons.collections.IteratorUtils;
import org.apache.sling.api.resource.ResourceResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.SearchResult;

public class SlingChannelUtil {
	
	/** Default log. */
    protected final static Logger log = LoggerFactory.getLogger(SlingChannelUtil.class);

	/**
	* @param resolver
	* @return
	*/
	@SuppressWarnings("unchecked")
	public static List<Node> getAllCHannels(ResourceResolver resolver, String contentPath, String componentPath) {
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("path", contentPath);
			map.put("type", "nt:unstructured");
			map.put("1_property", "sling:resourceType");
			map.put("1_property.value", componentPath);
			map.put("p.hits", "selective");
			map.put("p.limit", "-1");
			QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
			Session session = resolver.adaptTo(Session.class);
			com.day.cq.search.Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
			SearchResult result = query.getResult();
			return IteratorUtils.toList(result.getNodes());
		} catch(Exception ex) {
			log.error("Error in getAllCHannels in SlingChannelUtil ::: ", ex);
		}
		return null;
	}
	
	/**
	* @param resolver
	* @return
	*/
	@SuppressWarnings("unchecked")
	public static List<Node> getExtras(ResourceResolver resolver, String contentPath, String componentPath, String extrasId) {
		try {
			log.info("Entering getExtras of SlingChannelUtil");
			/*Map<String, String> map = new HashMap<String, String>();
			map.put("path", contentPath);
			map.put("type", "nt:unstructured");
			map.put("1_property", "sling:resourceType");
			map.put("1_property.value", componentPath);
			map.put("2_property", "extrasId");
			map.put("2_property.value", extrasId);
			map.put("p.hits", "selective");
			map.put("p.limit", "-1");
			QueryBuilder queryBuilder = resolver.adaptTo(QueryBuilder.class);
			Session session = resolver.adaptTo(Session.class);
			Query query = queryBuilder.createQuery(PredicateGroup.create(map), session);
			SearchResult result = query.getResult();
			return IteratorUtils.toList(result.getNodes());*/
			String compPath = "\"" + componentPath + "\"";
			String extraId = "\"" + extrasId + "\"";
			String queryStr = "SELECT * FROM [nt:base] AS s WHERE ISDESCENDANTNODE(["+contentPath+"]) and s.[sling:resourceType]="+compPath+" and s.[extrasId]="+extraId+"";
			QueryManager qm = resolver.adaptTo(Session.class).getWorkspace().getQueryManager();
			//QueryManager qm = resolver.adaptTo(QueryManager.class);
			Query query = qm.createQuery(queryStr, "JCR-SQL2");
			QueryResult resultNodes = query.execute();
			return IteratorUtils.toList(resultNodes.getNodes());
		} catch(Exception ex) {
			log.error("Error in getExtras in SlingChannelUtil ::: ", ex);
		}
		return null;
	}
}

