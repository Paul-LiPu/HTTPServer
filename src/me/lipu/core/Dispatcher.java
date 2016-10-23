package me.lipu.core;

import java.io.IOException;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import me.lipu.util.CloseUtil;
import me.lipu.webapp.StaticServlet;

public class Dispatcher implements Runnable{
	private Socket client;
	private Request req;
	private Response rep;
	private int code;
	
	Dispatcher(Socket client){
		this.client=client;
		try {
			req =new Request(client.getInputStream());
			rep =new Response(client.getOutputStream());
		} catch (IOException e) {
			//e.printStackTrace();
			code =500;
			return ;
		}
	}
	
	@Override
	public void run() {
		try {
			Servlet serv = WebApp.getServlet(req.getUrl());
			if(null==serv){
				Path path = Paths.get(WebApp.getRootPath() + req.getUrl());
				if (path == null || !Files.exists(path)) {
					this.code = 404;
				}
				serv = new StaticServlet();
			}else{
				this.code = 200;
			}
			WebApp.getFilterChain(serv).doFilter(req, rep);
			rep.pushToClient(code); //推送到客户端
		}catch (Exception e) {
			e.printStackTrace();
			this.code=500;
			try {
				rep.pushToClient(500);
			} catch (IOException d) {
				d.printStackTrace();
			}
		}
		WebApp.getLogger().info("request for " + req.getUrl() + ", and response code : " + code);
		req.close();
		rep.close();		
		CloseUtil.closeSocket(client);
	}
	
}
