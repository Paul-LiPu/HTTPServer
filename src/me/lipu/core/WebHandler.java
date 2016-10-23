package me.lipu.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class WebHandler extends DefaultHandler{
	private List<ServletClassMapping> entityList;
	private List<ServletUrlMapping> mappingList;
	private List<FilterClassMapping> filterList;
	private ServletClassMapping entity;
	private ServletUrlMapping mapping;
	private FilterClassMapping filter;
	private String beginTag ;	
	private int flag; 
	
	
 	@Override
	public void startDocument() throws SAXException {
		//文档解析开始
 		entityList =new ArrayList<ServletClassMapping>() ;
 		mappingList =new ArrayList<ServletUrlMapping>() ;
 		filterList = new ArrayList<FilterClassMapping>();
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		//开始元素
		if(null!=qName){
			beginTag=qName;
			
			if(qName.equals("servlet")){
				flag=1;
				entity=new ServletClassMapping();
			}else if(qName.equals("servlet-mapping")){
				flag=2;
				mapping=new ServletUrlMapping();
			} else if (qName.equals("filter")) {
				flag=3;
				filter=new FilterClassMapping();
			}
			
		}
		
	}
	
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		//处理内容
		if(null!=beginTag){
			String str =new String(ch,start,length);
			if(flag == 2){
				if(beginTag.equals("servlet-name")){
					mapping.setName(str);
				}else if(beginTag.equals("url-pattern")){
					mapping.getUrlPattern().add(str);
				}
			}else if (flag == 1){
				if(beginTag.equals("servlet-name")){
					entity.setName(str);					
				}else if(beginTag.equals("servlet-class")){
					entity.setClz(str);
				}
			} else if (flag == 3){
				if(beginTag.equals("filter-name")){
					filter.setName(str);					
				}else if(beginTag.equals("filter-class")){
					filter.setClz(str);
				}
			}
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		//结束元素
		if(null!=qName){
			
			if(qName.equals("servlet")){
				entityList.add(entity);
			}else if(qName.equals("servlet-mapping")){
				mappingList.add(mapping);
			}else if(qName.equals("filter")) {
				filterList.add(filter);
			}
			
		}
		beginTag=null;
	}

	
	@Override
	public void endDocument() throws SAXException {
		//文档解析结束
	}

	public List<ServletClassMapping> getEntityList() {
		return entityList;
	}
	public void setEntityList(List<ServletClassMapping> entityList) {
		this.entityList = entityList;
	}
	public List<ServletUrlMapping> getMappingList() {
		return mappingList;
	}
	public void setMappingList(List<ServletUrlMapping> mappingList) {
		this.mappingList = mappingList;
	}
	public ServletClassMapping getEntity() {
		return entity;
	}
	public void setEntity(ServletClassMapping entity) {
		this.entity = entity;
	}
	public ServletUrlMapping getMapping() {
		return mapping;
	}
	public void setMapping(ServletUrlMapping mapping) {
		this.mapping = mapping;
	}
	public List<FilterClassMapping> getFilterList() {
		return filterList;
	}
	public void setFilterList(List<FilterClassMapping> filterList) {
		this.filterList = filterList;
	}
	
	

}
