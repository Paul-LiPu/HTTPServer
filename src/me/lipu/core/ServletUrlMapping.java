package me.lipu.core;

import java.util.ArrayList;
import java.util.List;

public class ServletUrlMapping {
	private String name;
	private List<String> urlPattern;
	
	public ServletUrlMapping(){
		urlPattern =new ArrayList<String>();
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getUrlPattern() {
		return urlPattern;
	}
	public void setUrlPattern(List<String> urlPattern) {
		this.urlPattern = urlPattern;
	}
}
