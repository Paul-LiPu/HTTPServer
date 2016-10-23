package me.lipu.webapp;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.lipu.core.Request;
import me.lipu.core.Response;
import me.lipu.core.Servlet;
import me.lipu.core.WebApp;

public class StaticServlet extends Servlet {
	@Override
	public void doGet(Request req,Response rep) throws Exception {
		String file = WebApp.getRootPath() + req.getUrl();
		if(WebApp.getCache().containsKey(file)) {
			String content = WebApp.getCache().get(file);
			rep.println(content);
			return;
		}
		Path path = Paths.get(file);
		path = Files.exists(path) ? path : Paths.get(WebApp.getRootPath() + "/404.html");
		
		file = path.toAbsolutePath().toString();
		if(WebApp.getCache().containsKey(file)) {
			String content = WebApp.getCache().get(file);
			rep.println(content);
			return;
		}
		
		StringBuilder builder = new StringBuilder();
		for (String line : Files.readAllLines(path)) {
			rep.println(line);
			builder.append(line + "\n");
		}
		WebApp.getCache().put(file, builder.toString());
	}
	
	@Override
	public void doPost(Request req,Response rep) throws Exception {
		
	}

}
