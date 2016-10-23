package me.lipu.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.ehcache.Cache;

public class ServletContext {
	private Cache<String, String> cache;
	private ArrayList<Filter> filterChain;
	private Logger logger;
	
	//url --> servlet class name
	private Map<String,String> mapping;
	private String logPath = ServletContext.class.getResource("").getPath().substring(1)+"../webapp";
	public String getLogPath() {
		return logPath;
	}


	private String rootPath = System.getProperty("user.dir") + "/Resources"; 
	//servlet class name --> servlet class
	private Map<String,String> servlet ;
	
	public ServletContext(){
		servlet =new HashMap<String,String>();
		mapping =new HashMap<String,String>();
		filterChain = new ArrayList<Filter>();
	}


	public Cache<String, String> getCache() {
		return cache;
	}


	public ArrayList<Filter> getFilterChain() {
		return filterChain;
	}
	
	
	public Logger getLogger() {
		return logger;
	}
	public Map<String, String> getMapping() {
		return mapping;
	}


	public String getRootPath() {
		return rootPath;
	}
	
	public Map<String, String> getServlet() {
		return servlet;
	}
	public void setCache(Cache<String, String> cache) {
		this.cache = cache;
	}
	public void setFilterChain(ArrayList<Filter> filterChain) {
		this.filterChain = filterChain;
	}
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}


	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}


	public void setServlet(Map<String, String> servlet) {
		this.servlet = servlet;
	}
}
