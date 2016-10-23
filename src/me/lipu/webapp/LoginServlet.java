package me.lipu.webapp;

import me.lipu.core.Request;
import me.lipu.core.Response;
import me.lipu.core.Servlet;

public class LoginServlet extends Servlet {
	@Override
	public void doGet(Request req,Response rep) throws Exception {
		String name = req.getParameter("uname");
		String pwd =req.getParameter("pwd");
		if(login(name,pwd)){
			rep.println("µÇÂ¼³É¹¦");
		}else{
			rep.println("µÇÂ¼Ê§°Ü");
		}
	}
	
	public boolean login(String name,String pwd){
		return name.equals("test") && pwd.equals("test");
	}
	
	
	@Override
	public void doPost(Request req,Response rep) throws Exception {
		
	}

}
