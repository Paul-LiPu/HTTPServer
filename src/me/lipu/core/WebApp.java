package me.lipu.core;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;
import net.contentobjects.jnotify.JNotifyListener;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;

public class WebApp {
	private static ServletContext contxt;
	static{
		try {
			//获取解析工厂
			SAXParserFactory factory = SAXParserFactory.newInstance();
			//获取解析器
			SAXParser sax = factory.newSAXParser();
			//指定xml+处理器
			WebHandler web = new WebHandler();
			sax.parse(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("me/lipu/webapp/web.xml"), web);
			
			
			//将list 转成Map
			contxt =new ServletContext();			
			Map<String,String> servlet =contxt.getServlet();
			
			//servlet-name  servlet-class 
			for(ServletClassMapping entity:web.getEntityList()){
				servlet.put(entity.getName(), entity.getClz());
			}
			
			//url-pattern servlet-name			
			Map<String,String> mapping =contxt.getMapping();
			for(ServletUrlMapping mapp:web.getMappingList()){
				List<String> urls =mapp.getUrlPattern();
				for(String url:urls ){
					mapping.put(url, mapp.getName());
				}
			}
			
			// add filter chain to contxt
			ArrayList<Filter> filterChain = contxt.getFilterChain();
			for (FilterClassMapping fm : web.getFilterList()) {
				String name = fm.getClz();
				contxt.getFilterChain().add((Filter)Class.forName(name).newInstance());
			}
			
			// add Logger to Context
            try (InputStream is  = WebApp.class.getResourceAsStream("./logger.properties")) {
                LogManager.getLogManager().readConfiguration(is);
                Logger logger = Logger.getLogger("Logger");
                logger.setUseParentHandlers(false);
//                String str = WebApp.class.getResource("").getPath();
//                str = str.substring(1)+"../webapp/log/server%g.log";
                String str = contxt.getLogPath() + "/log/server%g.log";
                Handler handler = new FileHandler(str
                		, 104857600, 3, true);
                handler.setFormatter(new SimpleFormatter());
                handler.setLevel(Level.INFO);
                logger.addHandler(handler);
                handler = new ConsoleHandler();
                handler.setFormatter(new SimpleFormatter());
                handler.setLevel(Level.INFO);
                logger.addHandler(handler);
                contxt.setLogger(logger);
            } catch (Exception e) {
            	System.out.println("input properties file is error.\n" + e.toString());
            }
            
            // initiate ehcache
            CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().withCache("myCache",
                    CacheConfigurationBuilder.newCacheConfigurationBuilder(String.class, String.class, ResourcePoolsBuilder.heap(10))) 
                    .build();;
            cacheManager.init();
            Cache<String, String> myCache =
            	    cacheManager.getCache("myCache", String.class, String.class);
            contxt.setCache(myCache);
            
            // 监控根文件夹下的文件
            addNotify(contxt.getRootPath());
		} catch (Exception e) {
			
		}
	}
	
	private static void addNotify(String rootPath) throws JNotifyException {
	    //监控文件夹
	    JNotify.addWatch(rootPath,    
	            JNotify.FILE_DELETED  |    
	            JNotify.FILE_MODIFIED |    
	            JNotify.FILE_RENAMED,    
	            true,  new JNotifyListener(){   
	        
	        @Override  
	        public void fileCreated(int wd,   
	                String rootPath, String name) {   
	        }   
	        
	        @Override  
	        public void fileDeleted(int wd,   
	                String rootPath, String name) {   
	            contxt.getCache().remove(rootPath + name);
	        }   
	        
	        @Override  
	        public void fileModified(int wd,   
	                String rootPath, String name) {
	        	WebApp.getLogger().info(rootPath + "\\" + name + " has been modified.");
	        	Path path = Paths.get(rootPath, name);
	        	WebApp.getCache().remove(path.toString());
	        	WebApp.getLogger().info(rootPath + "\\" + name + " : cache deleted.");
	        	
	    		StringBuilder builder = new StringBuilder();
	    		try {
	    			cacheStatic(rootPath, name);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		WebApp.getCache().put(rootPath + name, builder.toString());
	    		WebApp.getLogger().info(rootPath + "\\" + name + " : cache refreshed.");
	        }   
	        
	        @Override  
	        public void fileRenamed(int wd,   
	                String rootPath, String oldName,   
	                String newName) {   
	        	WebApp.getCache().remove(rootPath+oldName);   
	            try {
					cacheStatic(rootPath, newName);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}   
	        }   
	    });  
	}
	
	private static void cacheStatic(String root, String file) throws IOException {
		Path path = Paths.get(root, file);
		StringBuilder builder = new StringBuilder();
		for (String line : Files.readAllLines(path)) {
			builder.append(line + "\n");
		}
		WebApp.getCache().put(file, builder.toString());
	}
	
	public static Servlet getServlet(String url) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		if((null==url)||(url=url.trim()).equals("")){
			return null;
		}
		//根据字符串(完整路径)创建对象
		
		//return contxt.getServlet().get(contxt.getMapping().get(url));
		String name=contxt.getServlet().get(contxt.getMapping().get(url));
		if (name == null) {
			return null;
		}
		return (Servlet)Class.forName(name).newInstance();//确保空构造存在
	}
	
	public static FilterChain getFilterChain(Servlet servlet) {
		return new FilterChain(contxt.getFilterChain(), servlet);
	}
	
	public static Logger getLogger() {
		return contxt.getLogger();
	}
	
	public static Cache<String, String> getCache() {
		return contxt.getCache();
	}
	
	public static String getRootPath() {
		return contxt.getRootPath();
	}
	
//	public static Servlet getStaticServlet() {
//		
//	}
}
